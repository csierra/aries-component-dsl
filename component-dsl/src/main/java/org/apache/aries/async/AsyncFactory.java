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

import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @author Carlos Sierra Andrés
 */
public class AsyncFactory {

    public AsyncFactory(int size) {
        _size = size;
        _promiseFactories = new PromiseFactory[size];

        for (int i = 0; i < _promiseFactories.length; i++) {
            _promiseFactories[i] = new PromiseFactory(
                Executors.newSingleThreadExecutor());
        }

        _references = new ConcurrentHashMap<>();
        _referenceQueue = new ReferenceQueue<>();
        _referencesToKeys = new ConcurrentHashMap<>();
    }

    private final PromiseFactory[] _promiseFactories;
    private final ReferenceQueue<Object> _referenceQueue;
    private final ConcurrentHashMap<WeakKey, WeakReference<Async>> _references;
    private final ConcurrentHashMap<WeakReference<?>, WeakKey>
        _referencesToKeys;

    private int _size;

    private PromiseFactory getPromiseFactory(Object object) {
        return _promiseFactories[Math.abs(object.hashCode() % _size)];
    }

    public <T> Async<T> wrap(T t) {
        Reference<?> ref;

        while ((ref = _referenceQueue.poll()) != null) {
            _references.remove(_referencesToKeys.remove(ref));
        }

        WeakKey weakKey = new WeakKey(t, null);
        WeakReference<Async> weakReference = _references.get(weakKey);

        if (weakReference!= null) {
            Async async = weakReference.get();

            if (async != null) {
                return async;
            }
        }

        WeakKey newWeakKey = new WeakKey(t, _referenceQueue);

        WeakReference<Async> asyncWeakReference =
            _references.computeIfAbsent(
                newWeakKey,
                key -> {
                    key.setUsed();

                    WeakReference<Async> weakRef = new WeakReference<>(
                        new AsyncImpl<>(getPromiseFactory(t), t));

                    _referencesToKeys.put(weakRef, key);

                    return weakRef;
                });

        newWeakKey.clearIfNotUsed();

        return asyncWeakReference.get();
    }

    private class WeakKey {

        public void clearIfNotUsed() {
            if (!_used) {
                _weakReference.clear();
            }
        }

        public void setUsed() {
            _used = true;
        }

        private final int _hashCode;
        private boolean _used;
        private WeakReference<Object> _weakReference;

        public WeakKey(Object object, ReferenceQueue<Object> referenceQueue) {
            _hashCode = object.hashCode();
            _weakReference = new WeakReference<>(object, referenceQueue);
        }

        @Override
        public int hashCode() {
            return _hashCode;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }

            Object object = _weakReference.get();

            if (object == null) {
                return false;
            }

            return object == ((WeakKey)other)._weakReference.get();
        }

    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        Date date = new Date();

        AsyncFactory asyncFactory = new AsyncFactory(8);

        Async<Date> dateAsync = asyncFactory.wrap(date);
        Async<Date> dateAsync1 = asyncFactory.wrap(date);

        System.out.println(dateAsync == dateAsync1); //This should be true

        Async<Date> dateAsync2 = asyncFactory.wrap((Date)date.clone());

        System.out.println(dateAsync == dateAsync2); //This should be false

        Promise<String> stringPromise = dateAsync.invoke(Date::toString);

        System.out.println(stringPromise.getValue());

        dateAsync = null;
        dateAsync1 = null;
        dateAsync2 = null;
        date = null;

        System.gc();

        Async<Date> dateAsync3 = asyncFactory.wrap(new Date());

        System.out.println(dateAsync3.invoke(Date::toString).getValue());
    }

}

