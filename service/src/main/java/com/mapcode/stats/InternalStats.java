/*
 * Copyright (C) 2016-2018, Stichting Mapcode Foundation (http://www.mapcode.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2017, TomTom International BV. All rights reserved.
 */

package com.mapcode.stats;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class InternalStats {

    // Some internal stats.
    public static final AtomicInteger statsCachedEvents = new AtomicInteger(0);
    public static final AtomicInteger statsTotalEvents = new AtomicInteger(0);
    public static final AtomicLong statsOldestEvent = new AtomicLong(0);
    public static final AtomicLong statsNewestEvent = new AtomicLong(0);

    private InternalStats() {
        // Do not instantiate.
    }
}
