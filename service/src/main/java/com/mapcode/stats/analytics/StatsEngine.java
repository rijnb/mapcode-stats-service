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
import com.tomtom.speedtools.geometry.GeoArea;
import com.tomtom.speedtools.geometry.GeoPoint;
import com.tomtom.speedtools.geometry.GeoRectangle;
import com.tomtom.speedtools.time.UTCTime;
import com.tomtom.speedtools.utils.MathUtils;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class StatsEngine {
    @Nonnull
    private static final Logger LOG = LoggerFactory.getLogger(StatsEngine.class);

    public static final int NR_CLUSTERS_MIN = 1;
    public static final int NR_CLUSTERS_MAX = 50;
    public static final int NR_ITERATIONS_MIN = 5;
    public static final int NR_ITERATIONS_MAX = 25;

    // Collection of events (limited in size).
    private static final int MAX_EVENTS = 1000 * 1000;

    @Nonnull
    private final CircularFifoQueue<Event> events = new CircularFifoQueue<>(MAX_EVENTS);
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
        final Event event = new Event(time, eventType, point, clientType);

        // Lock events collection while adding/removing.
        synchronized (events) {
            events.add(event);
            InternalStats.statsTotalEvents.incrementAndGet();
            InternalStats.statsCachedEvents.set(events.size());
            InternalStats.statsOldestEvent.set(events.peek().getTime().getMillis());
            InternalStats.statsNewestEvent.set(event.getTime().getMillis());
            final DateTime now = UTCTime.now();
            if (last.plusSeconds(10).isBefore(now)) {
                last = now;
                LOG.debug("addEvent: total events={} (of which {} cached)",
                        InternalStats.statsTotalEvents.get(), events.size());
            }
        }
    }

    @Nonnull
    public Set<Cluster> getClustersForArea(@Nonnull final GeoArea area, final int nrClusters, final int nrIterationsOrDefault) {
        LOG.info("getClusters: boundingBox={}, nrClusters={}, nrIterations={}, total events={}",
                area, nrClusters, nrIterationsOrDefault, events.size());

        // Destination.
        final Dataset filteredDataset = new DefaultDataset();

        // Lock events collection while copying.
        synchronized (events) {
            events.stream().
                    filter(event -> area.contains(event.getPoint())).
                    forEach(event -> {
                        final DenseInstance instance = new DenseInstance(new double[]{event.getPoint().getLat(), event.getPoint().getLon()});
                        filteredDataset.add(instance);
                    });
        }

        final Set<Cluster> clusters = new HashSet<>();
        final int nrEvents = filteredDataset.size();
        final int nrIterations = (nrIterationsOrDefault != 0) ?
                nrIterationsOrDefault :
                MathUtils.limitTo(
                        NR_ITERATIONS_MAX - (int) (
                                ((((double) nrEvents) / MAX_EVENTS) * (NR_ITERATIONS_MAX - NR_ITERATIONS_MIN)) *
                                        ((nrClusters / NR_CLUSTERS_MAX) + 1.0)
                        ),
                        NR_ITERATIONS_MIN, NR_ITERATIONS_MAX);

        LOG.debug("getClusters: filtered events, creating {} clusters from {} events in {} iterations...", nrClusters, nrEvents, nrIterations);
        // Need to check if dataset is empty or not.
        if (!filteredDataset.isEmpty()) {

            // Divide data set into a number of clusters.
            final Clusterer clusterer = new KMeans(nrClusters, nrIterations);
            final Dataset[] datasets = clusterer.cluster(filteredDataset);

            for (final Dataset dataset : datasets) {
                final int count = dataset.size();
                GeoRectangle boundingBox = null;
                for (int i = 0; i < dataset.size(); ++i) {
                    final Instance instance = dataset.instance(i);
                    final GeoPoint point = new GeoPoint(instance.value(0), instance.value(1));
                    if (boundingBox == null) {
                        boundingBox = new GeoRectangle(point, point);
                    } else {
                        boundingBox = boundingBox.grow(point);
                    }
                }
                assert boundingBox != null;
                final Cluster cluster = new Cluster(count, boundingBox);
                clusters.add(cluster);
            }
        }

        LOG.debug("getClusters: {} clusters found in {} iterations", clusters.size(), nrIterations);
        clusters.stream().forEach(cluster -> {
            LOG.debug("getClusters: | {}, {}", cluster.getCount(), cluster.getBoundingBox());
        });
        return clusters;
    }
}
