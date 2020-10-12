/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package org.apache.aries.component.dsl.internal;

import org.apache.aries.component.dsl.OSGi;
import org.apache.aries.component.dsl.OSGiResult;
import org.apache.aries.component.dsl.OSGiRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public class DistributeOSGiImpl<T, S> extends OSGiImpl<S> {

    @SafeVarargs
    public DistributeOSGiImpl(
        OSGiRunnable<T> operation, Function<OSGi<T>, OSGi<S>>... funs) {

        super((bundleContext, publisher) -> {
            Pad<T, S>[] pads = new Pad[funs.length];

            for (int i = 0; i < funs.length; i++) {
                pads[i] = new Pad<>(bundleContext, funs[i], publisher);
            }

            OSGiResult result = operation.run(
                bundleContext,
                t -> {
                    List<OSGiResult> terminators = new ArrayList<>(funs.length);

                    int i = 0;

                    try {
                        for (; i < funs.length; i++) {
                            terminators.add(pads[i].publish(t));
                        }
                    }
                    catch (Exception e) {
                        cleanUp(terminators);

                        throw e;
                    }

                    return new OSGiResultImpl(
                        () -> cleanUp(terminators),
                        () -> terminators.forEach(OSGiResult::update)
                    );
                });

            return new OSGiResultImpl(
                () -> {
                    result.close();

                    for (Pad<T, S> pad : pads) {
                        try {
                            pad.close();
                        } catch (Exception e) {
                        }
                    }
                },
                () -> {
                    for (Pad<T, S> pad : pads) {
                        try {
                            pad.update();
                        } catch (Exception e) {
                        }
                    }

                    result.update();
                }
            );
        });
    }

    private static void cleanUp(List<OSGiResult> terminators) {
        ListIterator<OSGiResult> iterator =
            terminators.listIterator(terminators.size());

        while (iterator.hasPrevious()) {
            try {
                iterator.previous().run();
            }
            catch (Exception e) {
            }
        }
    }

}
