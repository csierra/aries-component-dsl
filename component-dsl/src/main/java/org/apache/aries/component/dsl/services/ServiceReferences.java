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

package org.apache.aries.component.dsl.services;

import org.apache.aries.component.dsl.CachingServiceReference;
import org.apache.aries.component.dsl.OSGi;
import org.apache.aries.component.dsl.internal.ServiceReferenceOSGi;

public interface ServiceReferences {

    static <T> OSGi<CachingServiceReference<T>> withUpdate(
        Class<T> clazz) {

        return new ServiceReferenceOSGi<>(clazz, null);
    }

    static OSGi<CachingServiceReference<Object>> withUpdate(
        String filterString) {

        return new ServiceReferenceOSGi<>(null, filterString);
    }

    static <T> OSGi<CachingServiceReference<T>> withUpdate(
        Class<T> clazz, String filterString) {

        return new ServiceReferenceOSGi<>(clazz, filterString);
    }

}
