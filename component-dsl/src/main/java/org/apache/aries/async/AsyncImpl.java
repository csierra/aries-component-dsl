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

package org.apache.aries.async;

import org.apache.aries.component.dsl.function.Function2;
import org.apache.aries.component.dsl.function.Function3;
import org.apache.aries.component.dsl.function.Function4;
import org.apache.aries.component.dsl.function.Function5;
import org.apache.aries.component.dsl.function.Function6;
import org.apache.aries.component.dsl.function.Function7;
import org.apache.aries.component.dsl.function.Function8;
import org.apache.aries.component.dsl.function.Function9;
import org.apache.aries.component.dsl.function.Function10;
import org.apache.aries.component.dsl.function.Function11;
import org.apache.aries.component.dsl.function.Function12;
import org.apache.aries.component.dsl.function.Function13;
import org.apache.aries.component.dsl.function.Function14;
import org.apache.aries.component.dsl.function.Function15;
import org.apache.aries.component.dsl.function.Function16;
import org.apache.aries.component.dsl.function.Function17;
import org.apache.aries.component.dsl.function.Function18;
import org.apache.aries.component.dsl.function.Function19;
import org.apache.aries.component.dsl.function.Function20;
import org.apache.aries.component.dsl.function.Function21;
import org.apache.aries.component.dsl.function.Function22;
import org.apache.aries.component.dsl.function.Function23;
import org.apache.aries.component.dsl.function.Function24;
import org.apache.aries.component.dsl.function.Function25;
import org.apache.aries.component.dsl.function.Function26;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public class AsyncImpl<T> implements Async<T> {

    public AsyncImpl(PromiseFactory promiseFactory, T t) {
        _promiseFactory = promiseFactory;
        _t = t;
    }

    private PromiseFactory _promiseFactory;
    private T _t;

    private T getT() {
        return _t;
    }

    private PromiseFactory getPromiseFactory() {
        return _promiseFactory;
    }

    public <RESULT> Promise<RESULT> invoke(Function<T, RESULT> fun) {
        return getPromiseFactory().submit(() -> fun.apply(getT()));
    }

    public <RESULT, AA> Promise<RESULT> invoke(Function2<T, AA, RESULT> fun, AA aa) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa));
    }

    public <RESULT, AA> Promise<RESULT> invoke(Function2<T, AA, RESULT> fun, Promise<AA> aa) {
        return aa.map(aaaa -> fun.apply(getT(), aaaa));
    }

    public <RESULT, AA, AB> Promise<RESULT> invoke(Function3<T, AA, AB, RESULT> fun, AA aa, AB ab) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab));
    }

    public <RESULT, AA, AB> Promise<RESULT> invoke(Function3<T, AA, AB, RESULT> fun, Promise<AA> aa, Promise<AB> ab) {
        return aa.flatMap(aaaa -> ab.map(abab -> fun.apply(getT(), aaaa, abab)));
    }

    public <RESULT, AA, AB, AC> Promise<RESULT> invoke(Function4<T, AA, AB, AC, RESULT> fun, AA aa, AB ab, AC ac) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac));
    }

    public <RESULT, AA, AB, AC> Promise<RESULT> invoke(Function4<T, AA, AB, AC, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.map(acac -> fun.apply(getT(), aaaa, abab, acac))));
    }

    public <RESULT, AA, AB, AC, AD> Promise<RESULT> invoke(Function5<T, AA, AB, AC, AD, RESULT> fun, AA aa, AB ab, AC ac, AD ad) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad));
    }

    public <RESULT, AA, AB, AC, AD> Promise<RESULT> invoke(Function5<T, AA, AB, AC, AD, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.map(adad -> fun.apply(getT(), aaaa, abab, acac, adad)))));
    }

    public <RESULT, AA, AB, AC, AD, AE> Promise<RESULT> invoke(Function6<T, AA, AB, AC, AD, AE, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae));
    }

    public <RESULT, AA, AB, AC, AD, AE> Promise<RESULT> invoke(Function6<T, AA, AB, AC, AD, AE, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.map(aeae -> fun.apply(getT(), aaaa, abab, acac, adad, aeae))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF> Promise<RESULT> invoke(Function7<T, AA, AB, AC, AD, AE, AF, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF> Promise<RESULT> invoke(Function7<T, AA, AB, AC, AD, AE, AF, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.map(afaf -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf)))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG> Promise<RESULT> invoke(Function8<T, AA, AB, AC, AD, AE, AF, AG, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG> Promise<RESULT> invoke(Function8<T, AA, AB, AC, AD, AE, AF, AG, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.map(agag -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH> Promise<RESULT> invoke(Function9<T, AA, AB, AC, AD, AE, AF, AG, AH, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH> Promise<RESULT> invoke(Function9<T, AA, AB, AC, AD, AE, AF, AG, AH, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.map(ahah -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah)))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI> Promise<RESULT> invoke(Function10<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI> Promise<RESULT> invoke(Function10<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.map(aiai -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ> Promise<RESULT> invoke(Function11<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ> Promise<RESULT> invoke(Function11<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.map(ajaj -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj)))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK> Promise<RESULT> invoke(Function12<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK> Promise<RESULT> invoke(Function12<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.map(akak -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL> Promise<RESULT> invoke(Function13<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL> Promise<RESULT> invoke(Function13<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.map(alal -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal)))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM> Promise<RESULT> invoke(Function14<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM> Promise<RESULT> invoke(Function14<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.map(amam -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN> Promise<RESULT> invoke(Function15<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN> Promise<RESULT> invoke(Function15<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.map(anan -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan)))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO> Promise<RESULT> invoke(Function16<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO> Promise<RESULT> invoke(Function16<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.map(aoao -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP> Promise<RESULT> invoke(Function17<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP> Promise<RESULT> invoke(Function17<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.map(apap -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap)))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ> Promise<RESULT> invoke(Function18<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ> Promise<RESULT> invoke(Function18<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.map(aqaq -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR> Promise<RESULT> invoke(Function19<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR> Promise<RESULT> invoke(Function19<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.map(arar -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar)))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS> Promise<RESULT> invoke(Function20<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS> Promise<RESULT> invoke(Function20<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.map(asas -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas))))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT> Promise<RESULT> invoke(Function21<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as, at));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT> Promise<RESULT> invoke(Function21<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.map(atat -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas, atat)))))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU> Promise<RESULT> invoke(Function22<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as, at, au));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU> Promise<RESULT> invoke(Function22<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.flatMap(atat -> au.map(auau -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas, atat, auau))))))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV> Promise<RESULT> invoke(Function23<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as, at, au, av));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV> Promise<RESULT> invoke(Function23<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.flatMap(atat -> au.flatMap(auau -> av.map(avav -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas, atat, auau, avav)))))))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW> Promise<RESULT> invoke(Function24<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av, AW aw) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as, at, au, av, aw));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW> Promise<RESULT> invoke(Function24<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av, Promise<AW> aw) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.flatMap(atat -> au.flatMap(auau -> av.flatMap(avav -> aw.map(awaw -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas, atat, auau, avav, awaw))))))))))))))))))))))));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX> Promise<RESULT> invoke(Function25<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av, AW aw, AX ax) {
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa, ab, ac, ad, ae, af, ag, ah, ai, aj, ak, al, am, an, ao, ap, aq, ar, as, at, au, av, aw, ax));
    }

    public <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX> Promise<RESULT> invoke(Function25<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av, Promise<AW> aw, Promise<AX> ax) {
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.flatMap(atat -> au.flatMap(auau -> av.flatMap(avav -> aw.flatMap(awaw -> ax.map(axax -> fun.apply(getT(), aaaa, abab, acac, adad, aeae, afaf, agag, ahah, aiai, ajaj, akak, alal, amam, anan, aoao, apap, aqaq, arar, asas, atat, auau, avav, awaw, axax)))))))))))))))))))))))));
    }

    public <RESULT,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA> Promise<RESULT> invoke(Function26<T, AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA, RESULT> fun, AA aa,AB ab,AC ac,AD ad,AE ae,AF af,AG ag,AH ah,AI ai,AJ aj,AK ak,AL al,AM am,AN an,AO ao,AP ap,AQ aq,AR ar,AS as,AT at,AU au,AV av,AW aw,AX ax,BA ba){
        return getPromiseFactory().submit(() -> fun.apply(getT(), aa,ab,ac,ad,ae,af,ag,ah,ai,aj,ak,al,am,an,ao,ap,aq,ar,as,at,au,av,aw,ax,ba));
    }

    public <RESULT,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA> Promise<RESULT> invoke(Function26<T, AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA, RESULT> fun, Promise<AA> aa,Promise<AB> ab,Promise<AC> ac,Promise<AD> ad,Promise<AE> ae,Promise<AF> af,Promise<AG> ag,Promise<AH> ah,Promise<AI> ai,Promise<AJ> aj,Promise<AK> ak,Promise<AL> al,Promise<AM> am,Promise<AN> an,Promise<AO> ao,Promise<AP> ap,Promise<AQ> aq,Promise<AR> ar,Promise<AS> as,Promise<AT> at,Promise<AU> au,Promise<AV> av,Promise<AW> aw,Promise<AX> ax,Promise<BA> ba){
        return aa.flatMap(aaaa -> ab.flatMap(abab -> ac.flatMap(acac -> ad.flatMap(adad -> ae.flatMap(aeae -> af.flatMap(afaf -> ag.flatMap(agag -> ah.flatMap(ahah -> ai.flatMap(aiai -> aj.flatMap(ajaj -> ak.flatMap(akak -> al.flatMap(alal -> am.flatMap(amam -> an.flatMap(anan -> ao.flatMap(aoao -> ap.flatMap(apap -> aq.flatMap(aqaq -> ar.flatMap(arar -> as.flatMap(asas -> at.flatMap(atat -> au.flatMap(auau -> av.flatMap(avav -> aw.flatMap(awaw -> ax.flatMap(axax -> ba.map(baba -> fun.apply(getT(), aaaa,abab,acac,adad,aeae,afaf,agag,ahah,aiai,ajaj,akak,alal,amam,anan,aoao,apap,aqaq,arar,asas,atat,auau,avav,awaw,axax,baba))))))))))))))))))))))))));
    }

}
