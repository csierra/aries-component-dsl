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

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Carlos Sierra Andrés
 */
public class UpdateSupport {

    private static final ThreadLocal<Deque<Deque<Runnable>>>
        deferredPublishersStack = ThreadLocal.withInitial(LinkedList::new);
    private static final ThreadLocal<Deque<Deque<Runnable>>>
        deferredTerminatorsStack = ThreadLocal.withInitial(LinkedList::new);
    private static final ThreadLocal<Boolean> isUpdate =
        ThreadLocal.withInitial(() -> Boolean.FALSE);

    public static boolean isUpdate() {
        return isUpdate.get();
    }

    public static void deferPublication(Runnable runnable) {
        if (isUpdate.get()) {
            deferredPublishersStack.get().peekLast().addLast(runnable);
        }
        else {
            runnable.run();
        }
    }

    public static void deferTermination(Runnable runnable) {
        if (isUpdate.get()) {
            deferredTerminatorsStack.get().peekLast().addLast(runnable);
        }
        else {
            runnable.run();
        }
    }

    public static void runUpdate(Runnable runnable) {
        UpdateSupport.<Void>runInUpdate(() -> {runnable.run(); return null;});
    }

    public static boolean sendUpdate(OSGiResult osgiResult) {
        return runInUpdate(osgiResult::update);
    }

    public static <R> R runInUpdate(Supplier<R> supplier) {
        isUpdate.set(true);

        Deque<Deque<Runnable>> deferredPublishers =
            deferredPublishersStack.get();
        Deque<Deque<Runnable>> deferredTerminators =
            deferredTerminatorsStack.get();

        deferredPublishers.addLast(new LinkedList<>());
        deferredTerminators.addLast(new LinkedList<>());

        try {
            return supplier.get();
        }
        finally {
            isUpdate.set(false);

            Deque<Runnable> terminators =
                deferredTerminatorsStack.get().removeLast();

            for (Runnable terminator : terminators) {
                terminator.run();
            }

            Deque<Runnable> publishers =
                deferredPublishersStack.get().removeLast();

            for (Runnable publisher : publishers) {
                publisher.run();
            }

            isUpdate.set(!deferredTerminators.isEmpty());
        }
    }

}
