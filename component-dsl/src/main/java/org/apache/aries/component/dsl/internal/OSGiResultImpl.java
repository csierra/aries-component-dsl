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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Carlos Sierra Andrés
 */
public class OSGiResultImpl implements OSGiResult {

	public OSGiResultImpl(OSGiResult result) {
		this.onClose = result::close;
		this.onUpdate = result::update;
	}

	public OSGiResultImpl(Runnable onClose, Runnable onUpdate) {
		this.onClose = onClose;
		this.onUpdate = onUpdate;
	}

	@Override
	public void close() {
		if (_closed.compareAndSet(false, true)) {
			onClose.run();
		}
	}

	@Override
	public void update() {
		if (!_closed.get()) {
			onUpdate.run();
		}
	}

	private final Runnable onClose;
	private final Runnable onUpdate;
	private final AtomicBoolean _closed = new AtomicBoolean();

}
