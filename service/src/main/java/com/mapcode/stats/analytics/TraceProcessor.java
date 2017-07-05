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

import com.google.inject.Inject;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class TraceProcessor {
    @Nonnull
    private static final Logger LOG = LoggerFactory.getLogger(TraceProcessor.class);

    @Nonnull
    private final MongoDBTraceStream events;

    @Inject
    public TraceProcessor(
            @Nonnull final MongoDBTraceStream events,
            @Nonnull final MapcodeResourceTraceHandler mapcodeToLatLonTraceHandler) {
        this.events = events;

        // Register event handlers.
        this.events.addTraceHandler(mapcodeToLatLonTraceHandler);
    }

    public void process() {
        LOG.info("process: start processing events...");
        events.playbackToEnd();
    }
}
