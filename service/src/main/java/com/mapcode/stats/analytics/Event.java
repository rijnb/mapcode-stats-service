/*
 * Copyright (C) 2016-2017, Stichting Mapcode Foundation (http://www.mapcode.com)
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

package com.mapcode.stats.analytics;

import com.tomtom.speedtools.geometry.GeoPoint;
import com.tomtom.speedtools.time.UTCTime;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Event {
    private static final String CLIENT_IOS = "ios";
    private static final String CLIENT_ANDROID = "android";

    // Definition of a single event.
    private final long time;
    private final EventType eventType;
    private final GeoPoint point;
    private final ClientType clientType;

    public enum EventType {
        LAT_LON_TO_MAPCODE,
        MAPCODE_TO_LATLON,
        INCORRECT_MAPCODE
    }

    public enum ClientType {
        WEB,
        IOS,
        ANDROID;

        public static ClientType fromString(@Nullable final String client) {
            if (client == null) {
                return WEB;
            }
            switch (client) {
                case CLIENT_IOS:
                    return IOS;
                case CLIENT_ANDROID:
                    return ANDROID;
                default:
                    return WEB;
            }
        }
    }

    Event(@Nonnull  final DateTime time,
          @Nonnull final EventType eventType,
          @Nonnull final GeoPoint point,
          @Nonnull final ClientType clientType) {
        this.time = time.getMillis();
        this.eventType = eventType;
        this.point = point;
        this.clientType = clientType;
    }

    @Nonnull
    public DateTime getTime() {
        return UTCTime.from(new DateTime(time));
    }

    @Nonnull
    public EventType getEventType() {
        return eventType;
    }

    @Nonnull
    public GeoPoint getPoint() {
        return point;
    }

    @Nonnull
    public ClientType getClientType() {
        return clientType;
    }
}
