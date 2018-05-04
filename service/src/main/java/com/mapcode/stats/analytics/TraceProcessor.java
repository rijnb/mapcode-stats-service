/**
 * Copyright (C) 2017, TomTom International BV (http://www.tomtom.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mapcode.stats.analytics;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import com.tomtom.speedtools.time.UTCTime;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceStream;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class TraceProcessor {
    private static final FiniteDuration INITIAL_DELAY = FiniteDuration.apply(2, TimeUnit.SECONDS);
    private static final FiniteDuration SCHEDULE_DELAY = FiniteDuration.apply(500, TimeUnit.MILLISECONDS);
    private static final int NR_DAYS_TO_REWIND_AT_START = 21;

    @Nonnull
    private final ActorSystem actorSystem;

    @Nonnull
    private final MongoDBTraceStream events;

    @Inject
    public TraceProcessor(
            @Nonnull final ActorSystem actorSystem,
            @Nonnull final MongoDBTraceStream events,
            @Nonnull final MapcodeResourceTraceHandler mapcodeToLatLonTraceHandler) {
        this.events = events;

        // Schedule processing.
        this.actorSystem = actorSystem;

        // Register event handlers.
        this.events.addTraceHandler(mapcodeToLatLonTraceHandler);

        // Start event processor.
        events.moveTo(UTCTime.now().minusDays(NR_DAYS_TO_REWIND_AT_START));
        scheduleNextPlaybackToEnd(INITIAL_DELAY);
    }

    private void playbackToEndOnce() {
        // Playback a number of events (not necessarily all events to end).
        events.playbackToEnd();
        scheduleNextPlaybackToEnd(SCHEDULE_DELAY);
    }

    private void scheduleNextPlaybackToEnd(@Nonnull final FiniteDuration duration) {
        actorSystem.scheduler().scheduleOnce(duration, this::playbackToEndOnce, actorSystem.dispatcher());
    }
}
