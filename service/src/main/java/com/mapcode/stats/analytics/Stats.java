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

import akka.actor.ActorSystem;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Stats {
    @Nonnull
    private static final Logger LOG = LoggerFactory.getLogger(Stats.class);

    private static final String CLIENT_IOS = "ios";
    private static final String CLIENT_ANDROID = "android";

    @Nonnull
    final ActorSystem actorSystem;

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

    // Definition of a single event:
    class Event {
        final long time;
        final int eventType;
        final double latDeg;
        final double lonDeg;
        final int clientType;

        Event(final DateTime time,
              final EventType eventType,
              final double latDeg,
              final double lonDeg,
              final ClientType clientType) {
            this.time = time.getMillis();
            this.eventType = eventType.ordinal();
            this.latDeg = latDeg;
            this.lonDeg = lonDeg;
            this.clientType = clientType.ordinal();
        }
    }

    // Collection of events (limited in size).
    private static final int MAX_EVENTS = 1000 * 1000;
    final private ArrayList<Event> events = new ArrayList<>(MAX_EVENTS);

    public Stats() {
        // Schedule processing.
        this.actorSystem = ActorSystem.create("stats-processor");
        scheduleNext();
    }

    private void processOne() {
        LOG.debug("showClusters...");
        showClusters(10);
        scheduleNext();
    }

    private void scheduleNext() {
        actorSystem.scheduler().scheduleOnce(FiniteDuration.apply(1, TimeUnit.SECONDS), () -> {
            processOne();
        }, actorSystem.dispatcher());
    }

    public void addEvent(
            @Nonnull final DateTime now,
            @Nonnull final EventType eventType,
            final double latDeg,
            final double lonDeg,
            @Nonnull final ClientType clientType) {
        LOG.trace("addEvent: eventType={}, latDeg={}, lonDeg={}, clientType={}", eventType, latDeg, lonDeg, clientType);
        synchronized (events) {
            // Add event.
            final Event event = new Event(now, eventType, latDeg, lonDeg, clientType);
            events.add(event);

            // Cap collection.
            while (events.size() >= MAX_EVENTS) {
                events.remove(events.size() - MAX_EVENTS);
            }
        }
    }

    public void showClusters(final int nrClusters) {
        LOG.debug("getClusters: nrClusters={}, events={}", nrClusters, events.size());
        synchronized (events) {
            if (events.isEmpty()) {
                return;
            }
            final Dataset dataset = new DefaultDataset();
            events.stream().forEach((x) -> {
                final DenseInstance instance = new DenseInstance(new double[]{x.latDeg, x.lonDeg});
                dataset.add(instance);
            });
            final Clusterer clusterer = new KMeans(nrClusters);
            final Dataset[] clusters = clusterer.cluster(dataset);
            for (int i = 0; i < clusters.length; ++i) {
                final Dataset cluster = clusters[i];
                LOG.debug("cluster {}: size={}", i, cluster.size());
            }
        }
    }
}
