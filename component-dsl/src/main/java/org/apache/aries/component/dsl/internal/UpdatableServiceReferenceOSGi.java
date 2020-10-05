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
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public class UpdatableServiceReferenceOSGi<T, S>
	extends OSGiImpl<StateTuple<T, S>> {

	public UpdatableServiceReferenceOSGi(
		String filterString, Class<T> clazz,
		Refresher<? super CachingServiceReference<T>> refresher,
		Function<CachingServiceReference<T>, OSGi<S>> stateFunction,
		BiConsumer<CachingServiceReference<T>, S> onUpdate) {

		super((bundleContext, op) -> {
			ServiceTracker<T, ?>
				serviceTracker = new ServiceTracker<>(
					bundleContext,
					buildFilter(bundleContext, filterString, clazz),
					new DefaultServiceTrackerCustomizer<T, S>(
						bundleContext, op, refresher, stateFunction, onUpdate));

			serviceTracker.open();

			return new OSGiResultImpl(serviceTracker::close);
		});
	}

	private static class DefaultServiceTrackerCustomizer<T, S>
		implements ServiceTrackerCustomizer<T, Tracked<T, S>> {

		public DefaultServiceTrackerCustomizer(
			BundleContext bundleContext, Publisher<? super StateTuple<T, S>> addedSource,
			Refresher<? super CachingServiceReference<T>> refresher,
			Function<CachingServiceReference<T>, OSGi<S>> stateFunction,
			BiConsumer<CachingServiceReference<T>, S> onUpdate) {

			_bundleContext = bundleContext;
			_addedSource = addedSource;
			_refresher = refresher;
			_stateFunction = stateFunction;
			_onUpdate = onUpdate;
		}



		@Override
		public Tracked<T, S> addingService(ServiceReference<T> reference) {
			CachingServiceReference<T> cachingServiceReference =
				new CachingServiceReference<>(reference);

			final AtomicReference<S> atomicReference = new AtomicReference<>(
				(S)NULL_OBJECT);

			return new Tracked<>(
				cachingServiceReference,
				_doRun(cachingServiceReference, atomicReference),
				atomicReference);
		}

		private OSGiResult _doRun(
			CachingServiceReference<T> cachingServiceReference,
			AtomicReference<S> atomicReference) {

			return _stateFunction.apply(
				cachingServiceReference
			).effects(
				atomicReference::set, __ -> atomicReference.set((S)NULL_OBJECT)
			).map(s ->
				_addedSource.apply(new StateTuple<>(cachingServiceReference, s))
			).effects(
				__ -> {}, OSGiResult::close
			).run(
				_bundleContext
			);
		}

		@Override
		public void modifiedService(
			ServiceReference<T> reference, Tracked<T, S> tracked) {

			if (_refresher.test(tracked.cachingServiceReference)) {
				_runUpdate(reference, tracked);
			}
			else {
				tracked.cachingServiceReference = new CachingServiceReference<>(
					reference);

				S s = tracked.atomicReference.get();

				if (s != NULL_OBJECT) {
					_onUpdate.accept(tracked.cachingServiceReference, s);
				}
				else {
					_runUpdate(reference, tracked);
				}
			}
		}

		private void _runUpdate(ServiceReference<T> reference, Tracked<T, S> tracked) {
			UpdateSupport.runUpdate(() -> {
				tracked.runnable.run();
				tracked.cachingServiceReference = new CachingServiceReference<>(
					reference);
				tracked.runnable =
					_doRun(tracked.cachingServiceReference, tracked.atomicReference);
			});
		}

		@Override
		public void removedService(
			ServiceReference<T> reference, Tracked<T, S> tracked) {

			tracked.runnable.run();
			tracked.atomicReference.set((S)NULL_OBJECT);
		}

		private BundleContext _bundleContext;
		private final Publisher<? super StateTuple<T, S>> _addedSource;
		private Refresher<? super CachingServiceReference<T>> _refresher;
		private final Function<CachingServiceReference<T>, OSGi<S>> _stateFunction;
		private final BiConsumer<CachingServiceReference<T>, S> _onUpdate;

	}

	private static class Tracked<T, S> {

		public Tracked(
			CachingServiceReference<T> cachingServiceReference, Runnable runnable,
			AtomicReference<S> atomicReference) {

			this.cachingServiceReference = cachingServiceReference;
			this.runnable = runnable;
			this.atomicReference = atomicReference;
		}

		volatile CachingServiceReference<T> cachingServiceReference;
		volatile Runnable runnable;
		volatile AtomicReference<S> atomicReference;

	}

	private static final Object NULL_OBJECT = new Object();
}
