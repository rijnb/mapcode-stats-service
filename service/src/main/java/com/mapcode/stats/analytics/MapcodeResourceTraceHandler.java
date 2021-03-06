/*
 * Copyright (C) 2016-2021, Stichting Mapcode Foundation (http://www.mapcode.com)
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

import com.google.inject.Inject;
import com.mapcode.*;
import com.mapcode.services.implementation.MapcodeResourceImpl.Tracer;
import com.mapcode.stats.analytics.Event.EventType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mapcode.stats.analytics.Event.ClientType.fromString;

public final class MapcodeResourceTraceHandler implements Tracer {
    @Nonnull
    private static final Logger LOG = LoggerFactory.getLogger(MapcodeResourceTraceHandler.class);

    private final static DateTime DEFAULT_DATETIME = new DateTime(0L);

    private final StatsEngine statsEngine;

    // Constructor.
    @Inject
    public MapcodeResourceTraceHandler(@Nonnull final StatsEngine statsEngine) {
        super();
        this.statsEngine = statsEngine;
    }

    @Override
    public void eventLatLonToMapcode(final double latDeg, final double lonDeg, @Nullable final Territory territory,
                                     final int precision, @Nullable final String type, @Nullable final String alphabet,
                                     @Nullable final String include) {
        eventLatLonToMapcode(latDeg, lonDeg, territory, precision, type, alphabet, include, DEFAULT_DATETIME);
    }

    @Override
    public void eventLatLonToMapcode(final double latDeg, final double lonDeg, @Nullable final Territory territory,
                                     final int precision, @Nullable final String type, @Nullable final String alphabet,
                                     @Nullable final String include, @Nonnull final DateTime now) {
        eventLatLonToMapcode(latDeg, lonDeg, territory, precision, type, alphabet, include, now, null);
    }

    @Override
    public void eventLatLonToMapcode(final double latDeg, final double lonDeg, @Nullable final Territory territory,
                                     final int precision, @Nullable final String type, @Nullable final String alphabet,
                                     @Nullable final String include, @Nonnull final DateTime now,
                                     @Nullable final String client) {
        LOG.trace("eventLatLonToMapcode: latDeg={}, londeg={}, territory={}, type={}, alphabet={}, include={}, now={}, client={}",
                latDeg, lonDeg, territory, type, alphabet, include, now, client);
        statsEngine.addEvent(now, EventType.LAT_LON_TO_MAPCODE, latDeg, lonDeg, fromString(client));
    }

    @Override
    public void eventMapcodeToLatLon(@Nonnull final String code, @Nullable final Territory territory) {
        eventMapcodeToLatLon(code, territory, DEFAULT_DATETIME);
    }

    @Override
    public void eventMapcodeToLatLon(@Nonnull final String code, @Nullable final Territory territory, @Nonnull final DateTime now) {
        eventMapcodeToLatLon(code, territory, now, null);
    }

    @Override
    public void eventMapcodeToLatLon(@Nonnull final String code, @Nullable final Territory territory, @Nonnull final DateTime now,
                                     @Nullable final String client) {
        LOG.trace("eventMapcodeToLatLon: mapcode={}, territory={}, now={}, client={}", code, territory, now, client);
        final Point point;
        try {
            point = (territory == null) ? MapcodeCodec.decode(code) : MapcodeCodec.decode(code, territory);
            final Mapcode mapcodeLong = MapcodeCodec.encodeToInternational(point.getLatDeg(), point.getLonDeg());
            final Mapcode mapcodeShort = (territory == null) ?
                    MapcodeCodec.encodeToInternational(point.getLatDeg(), point.getLonDeg()) :
                    MapcodeCodec.encodeToShortest(point.getLatDeg(), point.getLonDeg(), territory);
            LOG.trace("eventMapcodeToLatLon: latDeg={}, londeg={}, mapcode={}|{}, territory={}, now={}, client={}",
                    point.getLatDeg(), point.getLonDeg(), mapcodeShort, mapcodeLong, territory, now, client);
            statsEngine.addEvent(now, EventType.MAPCODE_TO_LATLON, point.getLatDeg(), point.getLonDeg(), fromString(client));
        } catch (final UnknownMapcodeException ignored) {
            LOG.info("eventMapcodeToLatLon: cannot decode mapcode={}, territory={}", code, territory);
            statsEngine.addEvent(now, EventType.INCORRECT_MAPCODE, 0.0, 0.0, fromString(client));
        }
    }
}
