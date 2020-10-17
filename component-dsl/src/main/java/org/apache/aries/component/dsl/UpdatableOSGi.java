/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package org.apache.aries.component.dsl;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @author Carlos Sierra Andrés
 */
public interface UpdatableOSGi<T> extends OSGi<T> {

    public <S> OSGi<S> flatMap(BiFunction<T, UpdateSelector<S>, OSGi<S>> fun);

    public interface UpdateSelector<T> {

    }

    public default UpdatableOSGi<T> filter(Predicate<T> predicate) {
        return flatMap()
    }


}
