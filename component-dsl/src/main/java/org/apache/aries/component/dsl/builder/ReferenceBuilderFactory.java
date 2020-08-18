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

import org.apache.aries.component.dsl.CachingServiceReference;
import org.apache.aries.component.dsl.OSGi;

import java.util.Dictionary;
import java.util.function.Function;

import static org.apache.aries.component.dsl.OSGi.*;

/**
 * @author Carlos Sierra Andrés
 */
public
interface ReferenceBuilderFactory {
    ReferenceBuilderFactory DEFAULT_REFERENCE_BUILDER_FACTORY =
        (name, configuration, referenceFilterContributor) -> new ReferenceBuilder() {

            @Override
            public <S> OSGi<S> createReference(
                Class<S> serviceClass,
                Function<OSGi<CachingServiceReference<S>>, OSGi<CachingServiceReference<S>>> function) {

                return coalesce(
                    service(
                        function.apply(
                            (OSGi<CachingServiceReference<S>>) (OSGi) serviceReferences(
                                referenceFilterContributor.contribute("(objectClass=" + serviceClass.getName() + ")")))
                    ),
                    service(function.apply(serviceReferences(serviceClass)))
                );
            }
        };

    ReferenceBuilder create(
        String name, Dictionary<String, ?> configuration,
        ReferenceFilterContributor referenceFilterContributor);
}
