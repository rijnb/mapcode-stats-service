/*
 * Copyright (C) 2016-2019, Stichting Mapcode Foundation (http://www.mapcode.com)
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

package com.mapcode.stats.api;

import org.joda.time.DateTime;

/**
 * This utility class contains constants used in the Web services API.
 */
public final class ApiConstants {

    /**
     * General HTTP timeout for @Suspend() annotations.
     */
    public static final int SUSPEND_TIMEOUT = 30000;

    /**
     * General limits for count and offset.
     */
    public static final int API_COUNT_MAX = Integer.MAX_VALUE;
    public static final int API_OFFSET_MAX = Integer.MAX_VALUE;

    /**
     * Ranges for binder values.
     */
    public static final int API_VERSION_LEN_MIN = 1;
    public static final int API_VERSION_LEN_MAX = 250;

    public static final DateTime API_DATE_MAX = new DateTime(2200, 1, 1, 0, 0);
    public static final DateTime API_DATE_MIN = new DateTime(1900, 1, 1, 0, 0);

    public static final double API_LAT_MAX = 90.0;
    public static final double API_LAT_MIN = -90.0;

    // Prevent instantiation.
    private ApiConstants() {
        super();
        assert false;
    }
}
