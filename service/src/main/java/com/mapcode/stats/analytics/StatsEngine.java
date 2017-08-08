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

/*
 * Copyright (C) 2017, TomTom International BV. All rights reserved.
 */

package com.mapcode.stats.analytics;

import com.mapcode.stats.InternalStats;
import com.mapcode.stats.analytics.Event.ClientType;
import com.mapcode.stats.analytics.Event.EventType;
import com.tomtom.speedtools.geometry.GeoPoint;
import com.tomtom.speedtools.time.UTCTime;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.geojson.Feature;
import org.geojson.Point;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AccessToStaticFieldLockedOnInstance")
public class StatsEngine {
    @Nonnull
    private static final Logger LOG = LoggerFactory.getLogger(StatsEngine.class);

    // Collection of events (limited in size).
    private static final int MAX_EVENTS = 2 * 1000 * 1000;

    @Nonnull
    private final CircularFifoQueue<GeoPoint> points = new CircularFifoQueue<>(MAX_EVENTS);
    private DateTime last = UTCTime.now();

    public void addEvent(
            @Nonnull final DateTime time,
            @Nonnull final EventType eventType,
            final double latDeg,
            final double lonDeg,
            @Nonnull final ClientType clientType) {
        LOG.trace("addEvent: eventType={}, latDeg={}, lonDeg={}, clientType={}", eventType, latDeg, lonDeg, clientType);

        // Create event.
        final GeoPoint point = new GeoPoint(latDeg, lonDeg);

        // Lock events collection while adding/removing.
        synchronized (points) {
            InternalStats.statsTotalEvents.incrementAndGet();
            InternalStats.statsCachedEvents.set(points.size());
            if (points.isEmpty()) {
                InternalStats.statsOldestEvent.set(time.getMillis());
            }
            points.add(point);
            InternalStats.statsNewestEvent.set(time.getMillis());
            final DateTime now = UTCTime.now();
            if (last.plusSeconds(10).isBefore(now)) {
                last = now;
                LOG.debug("addEvent: total events={} (of which {} cached)",
                        InternalStats.statsTotalEvents.get(), points.size());
            }
        }
    }

    @Nonnull
    public List<Feature> getEvents(final int count, final int offset) {

        final List<Feature> result = new ArrayList<>(count);
        synchronized (points) {
            // Calculate sub-list to use.
            final int fromIndex = (offset < 0) ? Math.max(0, points.size() + offset) : Math.min(points.size(), offset);
            final int toIndex = Math.min(points.size(), fromIndex + count);
            LOG.debug("getEvents: from={}, to={}", fromIndex, toIndex);

            int i = 0;
            for (final GeoPoint point : points) {
                if (i >= fromIndex) {
                    final Feature feature = new Feature();
                    feature.setGeometry(new Point(point.getLon(), point.getLat()));
                    result.add(feature);
                }
                ++i;
                if (i >= toIndex) {
                    break;
                }
            }
        }
        LOG.debug("getEvents: result={}", result.size());
        return result;
    }
}
