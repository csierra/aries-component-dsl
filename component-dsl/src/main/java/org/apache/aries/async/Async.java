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
import org.apache.aries.component.dsl.function.Function2;
import org.apache.aries.component.dsl.function.Function20;
import org.apache.aries.component.dsl.function.Function21;
import org.apache.aries.component.dsl.function.Function22;
import org.apache.aries.component.dsl.function.Function23;
import org.apache.aries.component.dsl.function.Function24;
import org.apache.aries.component.dsl.function.Function25;
import org.apache.aries.component.dsl.function.Function26;
import org.apache.aries.component.dsl.function.Function3;
import org.apache.aries.component.dsl.function.Function4;
import org.apache.aries.component.dsl.function.Function5;
import org.apache.aries.component.dsl.function.Function6;
import org.apache.aries.component.dsl.function.Function7;
import org.apache.aries.component.dsl.function.Function8;
import org.apache.aries.component.dsl.function.Function9;
import org.osgi.util.promise.Promise;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Async<T> {

    <RESULT> Promise<RESULT> invoke(Function<T, RESULT> fun);

    <RESULT, AA> Promise<RESULT> invoke(Function2<T, AA, RESULT> fun, AA aa);

    <RESULT, AA> Promise<RESULT> invoke(Function2<T, AA, RESULT> fun, Promise<AA> aa);

    <RESULT, AA, AB> Promise<RESULT> invoke(Function3<T, AA, AB, RESULT> fun, AA aa, AB ab);

    <RESULT, AA, AB> Promise<RESULT> invoke(Function3<T, AA, AB, RESULT> fun, Promise<AA> aa, Promise<AB> ab);

    <RESULT, AA, AB, AC> Promise<RESULT> invoke(Function4<T, AA, AB, AC, RESULT> fun, AA aa, AB ab, AC ac);

    <RESULT, AA, AB, AC> Promise<RESULT> invoke(Function4<T, AA, AB, AC, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac);

    <RESULT, AA, AB, AC, AD> Promise<RESULT> invoke(Function5<T, AA, AB, AC, AD, RESULT> fun, AA aa, AB ab, AC ac, AD ad);

    <RESULT, AA, AB, AC, AD> Promise<RESULT> invoke(Function5<T, AA, AB, AC, AD, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad);

    <RESULT, AA, AB, AC, AD, AE> Promise<RESULT> invoke(Function6<T, AA, AB, AC, AD, AE, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae);

    <RESULT, AA, AB, AC, AD, AE> Promise<RESULT> invoke(Function6<T, AA, AB, AC, AD, AE, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae);

    <RESULT, AA, AB, AC, AD, AE, AF> Promise<RESULT> invoke(Function7<T, AA, AB, AC, AD, AE, AF, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af);

    <RESULT, AA, AB, AC, AD, AE, AF> Promise<RESULT> invoke(Function7<T, AA, AB, AC, AD, AE, AF, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af);

    <RESULT, AA, AB, AC, AD, AE, AF, AG> Promise<RESULT> invoke(Function8<T, AA, AB, AC, AD, AE, AF, AG, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag);

    <RESULT, AA, AB, AC, AD, AE, AF, AG> Promise<RESULT> invoke(Function8<T, AA, AB, AC, AD, AE, AF, AG, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH> Promise<RESULT> invoke(Function9<T, AA, AB, AC, AD, AE, AF, AG, AH, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH> Promise<RESULT> invoke(Function9<T, AA, AB, AC, AD, AE, AF, AG, AH, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI> Promise<RESULT> invoke(Function10<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI> Promise<RESULT> invoke(Function10<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ> Promise<RESULT> invoke(Function11<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ> Promise<RESULT> invoke(Function11<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK> Promise<RESULT> invoke(Function12<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK> Promise<RESULT> invoke(Function12<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL> Promise<RESULT> invoke(Function13<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL> Promise<RESULT> invoke(Function13<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM> Promise<RESULT> invoke(Function14<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM> Promise<RESULT> invoke(Function14<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN> Promise<RESULT> invoke(Function15<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN> Promise<RESULT> invoke(Function15<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO> Promise<RESULT> invoke(Function16<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO> Promise<RESULT> invoke(Function16<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP> Promise<RESULT> invoke(Function17<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP> Promise<RESULT> invoke(Function17<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ> Promise<RESULT> invoke(Function18<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ> Promise<RESULT> invoke(Function18<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR> Promise<RESULT> invoke(Function19<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR> Promise<RESULT> invoke(Function19<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS> Promise<RESULT> invoke(Function20<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS> Promise<RESULT> invoke(Function20<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT> Promise<RESULT> invoke(Function21<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT> Promise<RESULT> invoke(Function21<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU> Promise<RESULT> invoke(Function22<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU> Promise<RESULT> invoke(Function22<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV> Promise<RESULT> invoke(Function23<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV> Promise<RESULT> invoke(Function23<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW> Promise<RESULT> invoke(Function24<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av, AW aw);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW> Promise<RESULT> invoke(Function24<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av, Promise<AW> aw);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX> Promise<RESULT> invoke(Function25<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av, AW aw, AX ax);

    <RESULT, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX> Promise<RESULT> invoke(Function25<T, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO, AP, AQ, AR, AS, AT, AU, AV, AW, AX, RESULT> fun, Promise<AA> aa, Promise<AB> ab, Promise<AC> ac, Promise<AD> ad, Promise<AE> ae, Promise<AF> af, Promise<AG> ag, Promise<AH> ah, Promise<AI> ai, Promise<AJ> aj, Promise<AK> ak, Promise<AL> al, Promise<AM> am, Promise<AN> an, Promise<AO> ao, Promise<AP> ap, Promise<AQ> aq, Promise<AR> ar, Promise<AS> as, Promise<AT> at, Promise<AU> au, Promise<AV> av, Promise<AW> aw, Promise<AX> ax);

    <RESULT,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA> Promise<RESULT> invoke(Function26<T, AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA, RESULT> fun, AA aa, AB ab, AC ac, AD ad, AE ae, AF af, AG ag, AH ah, AI ai, AJ aj, AK ak, AL al, AM am, AN an, AO ao, AP ap, AQ aq, AR ar, AS as, AT at, AU au, AV av, AW aw, AX ax, BA ba);

    <RESULT,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA> Promise<RESULT> invoke(Function26<T, AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,BA, RESULT> fun, Promise<AA> aa,Promise<AB> ab,Promise<AC> ac,Promise<AD> ad,Promise<AE> ae,Promise<AF> af,Promise<AG> ag,Promise<AH> ah,Promise<AI> ai,Promise<AJ> aj,Promise<AK> ak,Promise<AL> al,Promise<AM> am,Promise<AN> an,Promise<AO> ao,Promise<AP> ap,Promise<AQ> aq,Promise<AR> ar,Promise<AS> as,Promise<AT> at,Promise<AU> au,Promise<AV> av,Promise<AW> aw,Promise<AX> ax,Promise<BA> ba);
}
