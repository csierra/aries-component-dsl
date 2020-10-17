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

import java.util.function.Consumer;

/**
 * @author Carlos Sierra Andrés
 */
public interface UpdateQuery {
    public static UpdateQuery onUpdate(From ... froms) {

    }

    public interface From {
        public static <T> From from(UpdatableOSGi.UpdateSelector<T> selector, Consumer<T> consumer) {

        }
    }
}
