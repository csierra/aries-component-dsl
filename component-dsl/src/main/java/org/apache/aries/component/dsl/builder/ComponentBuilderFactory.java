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
import org.apache.aries.component.dsl.function.Function2;

import java.util.Deque;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.function.Function;

import static org.apache.aries.component.dsl.OSGi.*;
import static org.apache.aries.component.dsl.OSGi.just;
import static org.apache.aries.component.dsl.Utils.highest;

/**
 * @author Carlos Sierra Andrés
 */
public class ComponentBuilderFactory {

    public static <T> OSGi<T> component(
        Class<T> clazz, Function2<Dictionary<String, ?>, ReferenceBuilder, ComponentBuilder<T>> consumer) {

        String name = clazz.getName();

        return component(
            coalesce(
                all(configurations(name), configuration(name)),
                just(new Hashtable<>())
            ),
            name,
            consumer);
    }

    public static <T> OSGi<T> component(
        String name, Function2<Dictionary<String, ?>, ReferenceBuilder, ComponentBuilder<T>> consumer) {

        return component(
            coalesce(
                all(configurations(name), configuration(name)),
                just(new Hashtable<>())
            ),
            name,
            consumer);
    }

    public static <T> OSGi<T> component(
        OSGi<Dictionary<String, ?>> configurationOSGi, String name,
        Function2<Dictionary<String, ?>, ReferenceBuilder, ComponentBuilder<T>> consumer) {

        return configurationOSGi.flatMap(configuration ->
            combine(
                consumer, just(configuration), createReferenceBuilder(name, configuration))
        ).flatMap(
            ComponentBuilder::asOSGi
        );
    }

    public static OSGi<ReferenceBuilder> createReferenceBuilder(String name, Dictionary<String, ?> configuration) {
        return
            coalesce(
                service(
                    highest(
                        serviceReferences(
                            ReferenceFilterContributorFactory.class, "(target.component=" + name + ")"))),
                service(highest(serviceReferences(ReferenceFilterContributorFactory.class))),
                just(ReferenceFilterContributorFactory.DEFAULT_REFERENCE_FILTER_CONTRIBUTOR_FACTORY)).map(
                    referenceFilterContributorFactory ->
                        referenceFilterContributorFactory.create(
                            name, configuration, ReferenceFilterContributorThreadLocalStack.get())
                ).
                effects(
                    ReferenceFilterContributorThreadLocalStack::push,
                    __ -> ReferenceFilterContributorThreadLocalStack.pop()
                ).flatMap(referenceFilterContributor ->
            coalesce(
                service(
                highest(
                    serviceReferences(
                        ReferenceBuilderFactory.class, "(target.component=" + name + ")"))),
            service(highest(serviceReferences(ReferenceBuilderFactory.class))),
            just(ReferenceBuilderFactory.DEFAULT_REFERENCE_BUILDER_FACTORY)
        ).map(
            referenceBuilderFactory ->
                referenceBuilderFactory.create(name, configuration, referenceFilterContributor)
        ));
    }

    private static class ReferenceFilterContributorThreadLocalStack {
        private static final ThreadLocal<Deque<ReferenceFilterContributor>> _referenceBuilders =
            ThreadLocal.withInitial(LinkedList::new);

        public static void push(ReferenceFilterContributor referenceFilterContributor) {
            _referenceBuilders.get().push(referenceFilterContributor);
        }

        public static void pop() {
            _referenceBuilders.get().pop();
        }

        public static ReferenceFilterContributor[] get() {
            return _referenceBuilders.get().toArray(new ReferenceFilterContributor[0]);
        }
    }

}
