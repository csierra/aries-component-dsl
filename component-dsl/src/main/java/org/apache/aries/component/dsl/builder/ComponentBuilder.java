/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.aries.component.dsl.builder;

import org.apache.aries.component.dsl.OSGi;
import org.apache.aries.component.dsl.consumer.TriConsumer;
import org.apache.aries.component.dsl.function.*;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.aries.component.dsl.OSGi.*;

/**
 * @author Carlos Sierra Andrés
 */
public class ComponentBuilder<T> {

    private final OSGi<T> initializer;
    private final List<Function<OSGi<T>, OSGi<T>>> mandatory;
    private final List<Function<OSGi<T>, OSGi<Void>>> optional;
    private final Consumer<T> postConstruct;
    private final Consumer<T> preDestroy;

    private ComponentBuilder(OSGi<T> initializer) {
        this(
            initializer, Collections.emptyList(),
            Collections.emptyList(), __ -> {}, __ -> {}
        );
    }

    private ComponentBuilder(
        OSGi<T> initializer, List<Function<OSGi<T>, OSGi<T>>> mandatory,
        List<Function<OSGi<T>, OSGi<Void>>> optional,
        Consumer<T> postConstruct,
        Consumer<T> preDestroy) {

        this.initializer = initializer;
        this.mandatory = mandatory;
        this.optional = optional;
        this.postConstruct = postConstruct;
        this.preDestroy = preDestroy;
    }

    public <S> ComponentBuilder<T> optionalDependency(OSGi<S> s, BiConsumer<T, S> setter) {
        ArrayList<Function<OSGi<T>, OSGi<Void>>> list = new ArrayList<>(optional);
        list.add(o ->
            ignore(
                combine(TS::new, o, s).effects(
                    ts -> setter.accept(ts.t, ts.s),
                    ts -> setter.accept(ts.t, null))
            )
        );

        return new ComponentBuilder<>(initializer, mandatory, list, postConstruct, preDestroy);
    }

    public <S> ComponentBuilder<T> optionalDependency(OSGi<S> s, BiConsumer<T, S> setter, BiConsumer<T, S> unsetter) {
        ArrayList<Function<OSGi<T>, OSGi<Void>>> list = new ArrayList<>(optional);
        list.add(o ->
            ignore(
                combine(TS::new, o, s).effects(
                    ts -> setter.accept(ts.t, ts.s),
                    ts -> unsetter.accept(ts.t, ts.s))
            )
        );

        return new ComponentBuilder<>(initializer, mandatory, list, postConstruct, preDestroy);
    }

    public <S> ComponentBuilder<T> optionalDependency(
        OSGi<ServiceReference<S>> sr,
        TriConsumer<T, Map<String, Object>, S> setter,
        TriConsumer<T, Map<String, Object>, S> unsetter) {

        ArrayList<Function<OSGi<T>, OSGi<Void>>> list = new ArrayList<>(optional);
        list.add(o ->
            ignore(
                combine(TS::new, o, sr).flatMap(ts ->
                just(properties(ts.s)).flatMap(properties ->
                service(ts.s).effects(
                    s -> setter.accept(ts.t, properties, s),
                    s -> unsetter.accept(ts.t, properties, s)
                )))
            )
        );
        return new ComponentBuilder<>(
            initializer, mandatory, list, postConstruct, preDestroy);
    }

    public <S> ComponentBuilder<T> mandatoryDependency(OSGi<S> s, BiConsumer<T, S> setter) {
        ArrayList<Function<OSGi<T>, OSGi<T>>> list = new ArrayList<>(mandatory);
        list.add(o ->
            combine(TS::new, o, s).effects(
                ts -> setter.accept(ts.t, ts.s),
                ts -> setter.accept(ts.t, null)
            ).then(
                o
            )
        );

        return new ComponentBuilder<>(initializer, list, optional, postConstruct, preDestroy);
    }

    public <S> ComponentBuilder<T> mandatoryDependency(OSGi<S> s, BiConsumer<T, S> setter, BiConsumer<T, S> unsetter) {
        ArrayList<Function<OSGi<T>, OSGi<T>>> list = new ArrayList<>(mandatory);
        list.add(o ->
            combine(TS::new, o, s).effects(
                ts -> setter.accept(ts.t, ts.s),
                ts -> unsetter.accept(ts.t, ts.s)
            ).then(
                o
            )
        );

        return new ComponentBuilder<>(initializer, list, optional, postConstruct, preDestroy);
    }

    public <S> ComponentBuilder<T> mandatoryDependency(
            OSGi<ServiceReference<S>> sr,
            TriConsumer<T, Map<String, Object>, S> setter,
            TriConsumer<T, Map<String, Object>, S> unsetter) {

        ArrayList<Function<OSGi<T>, OSGi<T>>> list = new ArrayList<>(mandatory);
        list.add(o ->
            combine(TS::new, o, sr).flatMap(ts ->
            just(properties(ts.s)).flatMap(properties ->
            service(ts.s).effects(
                s -> setter.accept(ts.t, properties, s),
                s -> unsetter.accept(ts.t, properties, s)))).
            then(
                o
            )
        );

        return new ComponentBuilder<>(initializer, list, optional, postConstruct, preDestroy);
    }

    public ComponentBuilder<T> postConstruct(Consumer<T> postConstruct) {
        return new ComponentBuilder<>(
            initializer,
            mandatory,
            optional,
            this.postConstruct.andThen(postConstruct),
            preDestroy
        );
    }

    public ComponentBuilder<T> predestroy(Consumer<T> preDestroy) {
        return new ComponentBuilder<>(
            initializer,
            mandatory,
            optional,
            postConstruct,
            this.preDestroy.andThen(preDestroy)
        );
    }

    private static class TS<T,S> {
        T t;
        S s;

        TS(T t, S s) {
            this.t = t;
            this.s = s;
        }
    }

    public OSGi<ServiceRegistration<?>> register(Map<String, ?> properties, Class<?> ... interfaces) {
        return asOSGi().flatMap(t ->
            OSGi.register(
                Arrays.stream(interfaces).map(Class::getName).toArray(String[]::new),
                t,
                properties));
    }

    public OSGi<T> asOSGi() {
        OSGi<T> program = this.initializer;

        for (Function<OSGi<T>, OSGi<T>> m : mandatory) {
            program = program.flatMap(p -> m.apply(just(p)));
        }

        for (Function<OSGi<T>, OSGi<Void>> o : optional) {
            program = program.distribute(
                Function.identity(),
                p -> o.apply(p).then(nothing())
            );
        }

        program = program.effects(this.postConstruct, this.preDestroy);

        return program;
    }

    private static Map<String, Object> properties(ServiceReference<?> serviceReference) {
        Map<String, Object> map = new HashMap<>();

        for (String propertyKey : serviceReference.getPropertyKeys()) {
            map.put(propertyKey, serviceReference.getProperty(propertyKey));
        }

        return map;
    }

    public static <RES> ComponentBuilder<RES> constructor(Supplier<RES> fun) {
        return new ComponentBuilder<>(just(fun));
    }
    
    public static <A, RES> ComponentBuilder<RES> constructor(Function<A, RES> fun, OSGi<A> a) {
        return new ComponentBuilder<>(a.applyTo(just(fun)));
    }

    public static <A, B, RES> ComponentBuilder<RES> constructor(Function2<A, B, RES> fun, OSGi<A> a, OSGi<B> b) {
        return new ComponentBuilder<>(b.applyTo(a.applyTo(just(fun.curried()))));
    }

    public static <A, B, C, RES> ComponentBuilder<RES> constructor(Function3<A, B, C, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c) {
        return new ComponentBuilder<>(c.applyTo(combine((A aa, B bb) -> fun.curried().apply(aa).apply(bb), a, b)));
    }

    public static <A, B, C, D, RES> ComponentBuilder<RES> constructor(Function4<A, B, C, D, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d) {
        return new ComponentBuilder<>(d.applyTo(combine((A aa, B bb, C cc) -> fun.curried().apply(aa).apply(bb).apply(cc), a, b, c)));
    }

    public static <A, B, C, D, E, RES> ComponentBuilder<RES> constructor(Function5<A, B, C, D, E, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e) {
        return new ComponentBuilder<>(e.applyTo(combine((A aa, B bb, C cc, D dd) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd), a, b, c, d)));
    }

    public static <A, B, C, D, E, F, RES> ComponentBuilder<RES> constructor(Function6<A, B, C, D, E, F, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f) {
        return new ComponentBuilder<>(f.applyTo(combine((A aa, B bb, C cc, D dd, E ee) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee), a, b, c, d, e)));
    }

    public static <A, B, C, D, E, F, G, RES> ComponentBuilder<RES> constructor(Function7<A, B, C, D, E, F, G, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g) {
        return new ComponentBuilder<>(g.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff), a, b, c, d, e, f)));
    }

    public static <A, B, C, D, E, F, G, H, RES> ComponentBuilder<RES> constructor(Function8<A, B, C, D, E, F, G, H, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h) {
        return new ComponentBuilder<>(h.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg), a, b, c, d, e, f, g)));
    }

    public static <A, B, C, D, E, F, G, H, I, RES> ComponentBuilder<RES> constructor(Function9<A, B, C, D, E, F, G, H, I, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i) {
        return new ComponentBuilder<>(i.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh), a, b, c, d, e, f, g, h)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, RES> ComponentBuilder<RES> constructor(Function10<A, B, C, D, E, F, G, H, I, J, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j) {
        return new ComponentBuilder<>(j.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii), a, b, c, d, e, f, g, h, i)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, RES> ComponentBuilder<RES> constructor(Function11<A, B, C, D, E, F, G, H, I, J, K, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k) {
        return new ComponentBuilder<>(k.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj), a, b, c, d, e, f, g, h, i, j)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, RES> ComponentBuilder<RES> constructor(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l) {
        return new ComponentBuilder<>(l.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk), a, b, c, d, e, f, g, h, i, j, k)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RES> ComponentBuilder<RES> constructor(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m) {
        return new ComponentBuilder<>(m.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll), a, b, c, d, e, f, g, h, i, j, k, l)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> ComponentBuilder<RES> constructor(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n) {
        return new ComponentBuilder<>(n.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm), a, b, c, d, e, f, g, h, i, j, k, l, m)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> ComponentBuilder<RES> constructor(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o) {
        return new ComponentBuilder<>(o.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn), a, b, c, d, e, f, g, h, i, j, k, l, m, n)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> ComponentBuilder<RES> constructor(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p) {
        return new ComponentBuilder<>(p.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> ComponentBuilder<RES> constructor(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q) {
        return new ComponentBuilder<>(q.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> ComponentBuilder<RES> constructor(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r) {
        return new ComponentBuilder<>(r.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> ComponentBuilder<RES> constructor(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s) {
        return new ComponentBuilder<>(s.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> ComponentBuilder<RES> constructor(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t) {
        return new ComponentBuilder<>(t.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> ComponentBuilder<RES> constructor(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u) {
        return new ComponentBuilder<>(u.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> ComponentBuilder<RES> constructor(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u, OSGi<V> v) {
        return new ComponentBuilder<>(v.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> ComponentBuilder<RES> constructor(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u, OSGi<V> v, OSGi<W> w) {
        return new ComponentBuilder<>(w.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RES> ComponentBuilder<RES> constructor(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u, OSGi<V> v, OSGi<W> w, OSGi<X> x) {
        return new ComponentBuilder<>(x.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RES> ComponentBuilder<RES> constructor(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u, OSGi<V> v, OSGi<W> w, OSGi<X> x, OSGi<Y> y) {
        return new ComponentBuilder<>(y.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x)));
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> ComponentBuilder<RES> constructor(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RES> fun, OSGi<A> a, OSGi<B> b, OSGi<C> c, OSGi<D> d, OSGi<E> e, OSGi<F> f, OSGi<G> g, OSGi<H> h, OSGi<I> i, OSGi<J> j, OSGi<K> k, OSGi<L> l, OSGi<M> m, OSGi<N> n, OSGi<O> o, OSGi<P> p, OSGi<Q> q, OSGi<R> r, OSGi<S> s, OSGi<T> t, OSGi<U> u, OSGi<V> v, OSGi<W> w, OSGi<X> x, OSGi<Y> y, OSGi<Z> z) {
        return new ComponentBuilder<>(z.applyTo(combine((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx, Y yy) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx).apply(yy), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y)));
    }
}
