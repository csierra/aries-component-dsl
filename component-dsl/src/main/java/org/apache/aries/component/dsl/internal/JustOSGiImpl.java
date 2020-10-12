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


package org.apache.aries.component.dsl.internal;


import org.apache.aries.component.dsl.OSGiResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
public class JustOSGiImpl<T> extends OSGiImpl<T> {

	public JustOSGiImpl(Collection<T> t) {
		this(() -> t);
	}

	public JustOSGiImpl(Supplier<Collection<T>> supplier) {
		super((bundleContext, op) -> {

			Collection<T> collection = supplier.get();
			ArrayList<OSGiResult> results = new ArrayList<>(collection.size());

			try {
				for (T t : collection) {
					results.add(op.publish(t));
				}
			}
			catch (Exception e) {
				cleanUp(results);

				throw e;
			}

			return new OSGiResultImpl(
				() -> cleanUp(results),
				() -> results.forEach(OSGiResult::update)
			);
		});
	}

	private static void cleanUp(ArrayList<OSGiResult> results) {
		ListIterator<OSGiResult> iterator =
			results.listIterator(results.size());

		while (iterator.hasPrevious()) {
			try {
				iterator.previous().run();
			}
			catch (Exception e) {
			}
		}
	}

	public JustOSGiImpl(T t) {
		this(() -> Collections.singletonList(t));
	}

}
