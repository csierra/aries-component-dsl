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

import org.apache.aries.component.dsl.*;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * @author Carlos Sierra Andrés
 */
public class ServiceReferenceOSGi<T>
	extends OSGiImpl<CachingServiceReference<T>> implements UpdatableOSGi<T> {

	public ServiceReferenceOSGi(
		String filterString, Class<T> clazz) {

		this(filterString, clazz, CachingServiceReference::isDirty);
	}

	public ServiceReferenceOSGi(
		String filterString, Class<T> clazz,
		Refresher<? super CachingServiceReference<T>> refresher) {

		super((bundleContext, op) -> {
			ServiceTracker<T, Tracked<T>>
				serviceTracker = new ServiceTracker<>(
					bundleContext,
					buildFilter(bundleContext, filterString, clazz),
					new DefaultServiceTrackerCustomizer<>(op, refresher));

			serviceTracker.open();

			return new OSGiResultImpl(
				serviceTracker::close,
				() -> Arrays.stream(serviceTracker.getServices()).map(
					Tracked.class::cast
				).forEach(
					t -> t.result.update()
				));
		});
	}

	@Override
	public <S> OSGi<S> flatMap(BiFunction<T, UpdateSelector<S>, OSGi<S>> fun) {
		return null;
	}

	private static class DefaultServiceTrackerCustomizer<T>
		implements ServiceTrackerCustomizer<T, Tracked<T>> {

		public DefaultServiceTrackerCustomizer(
			Publisher<? super CachingServiceReference<T>> addedSource,
			Refresher<? super CachingServiceReference<T>> refresher) {

			_addedSource = addedSource;
			_refresher = refresher;
		}

		@Override
		public Tracked<T> addingService(ServiceReference<T> reference) {
			CachingServiceReference<T> cachingServiceReference =
				new CachingServiceReference<>(reference);

			return new Tracked<>(
				cachingServiceReference,
				_addedSource.apply(cachingServiceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<T> reference, Tracked<T> tracked) {

			if (_refresher.test(tracked.cachingServiceReference)) {
				UpdateSupport.runUpdate(() -> {
					tracked.result.run();
					tracked.cachingServiceReference = new CachingServiceReference<>(
						reference);
					tracked.result =
						_addedSource.apply(tracked.cachingServiceReference);
				});
			}
			else {
				tracked.result.update();
			}
		}

		@Override
		public void removedService(
			ServiceReference<T> reference, Tracked<T> tracked) {

			tracked.result.run();
		}

		private final Publisher<? super CachingServiceReference<T>> _addedSource;
		private Refresher<? super CachingServiceReference<T>> _refresher;

	}

	private static class Tracked<T> {

		public Tracked(
			CachingServiceReference<T> cachingServiceReference,
			OSGiResult result) {

			this.cachingServiceReference = cachingServiceReference;
			this.result = result;
		}

		volatile CachingServiceReference<T> cachingServiceReference;
		volatile OSGiResult result;

	}
}
