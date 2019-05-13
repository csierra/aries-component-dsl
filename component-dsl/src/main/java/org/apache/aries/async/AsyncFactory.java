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

    }

    private final PromiseFactory[] _promiseFactories;

    private int _size;

    private PromiseFactory getPromiseFactory(Object object) {
        return _promiseFactories[Math.abs(object.hashCode() % _size)];
    }

    public <T> Async<T> wrap(T t) {
        return new AsyncImpl<>(getPromiseFactory(t), t);
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        Date date = new Date();

        AsyncFactory asyncFactory = new AsyncFactory(8);

        Async<Date> dateAsync = asyncFactory.wrap(date);

        Promise<String> stringPromise = dateAsync.invoke(Date::toString);

        System.out.println(stringPromise.getValue());

    }

}

