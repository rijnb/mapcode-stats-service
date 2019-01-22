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

package com.mapcode.services.implementation;

import com.mapcode.Territory;
import com.tomtom.speedtools.tracer.Traceable;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface defines a Tracer interface for mapcode service events.
 */
public class MapcodeResourceImpl {

    /**
     * This interface defines a Tracer interface for mapcode service events.
     */
    public interface Tracer extends Traceable {

        // A request to translate a lat/lon to a mapcode is made.
        void eventLatLonToMapcode(double latDeg, double lonDeg, @Nullable Territory territory,
                                  int precision, @Nullable String type, @Nullable String alphabet,
                                  @Nullable String include);

        void eventLatLonToMapcode(double latDeg, double lonDeg, @Nullable Territory territory,
                                  int precision, @Nullable String type, @Nullable String alphabet,
                                  @Nullable String include, @Nonnull DateTime now);

        void eventLatLonToMapcode(double latDeg, double lonDeg, @Nullable Territory territory,
                                  int precision, @Nullable String type, @Nullable String alphabet,
                                  @Nullable String include, @Nonnull DateTime now, @Nullable String client);

        // A request to translate a mapcode to a lat/lon is made.
        void eventMapcodeToLatLon(@Nonnull String code, @Nullable Territory territory);

        void eventMapcodeToLatLon(@Nonnull String code, @Nullable Territory territory, @Nonnull DateTime now);

        void eventMapcodeToLatLon(@Nonnull String code, @Nullable Territory territory, @Nonnull DateTime now, @Nullable String client);
    }
}
