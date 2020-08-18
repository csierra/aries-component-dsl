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

import java.util.Dictionary;

/**
 * @author Carlos Sierra Andrés
 */
public
interface ReferenceFilterContributorFactory {

    ReferenceFilterContributorFactory DEFAULT_REFERENCE_FILTER_CONTRIBUTOR_FACTORY =
        (name, configuration, referenceBuilders) -> filterString -> {

        for (ReferenceFilterContributor referenceBuilder : referenceBuilders) {
            filterString = referenceBuilder.contribute(filterString);
        }

        return filterString;
    };

    ReferenceFilterContributor create(
        String name, Dictionary<String, ?> configuration,
        ReferenceFilterContributor... referenceBuilders);
}
