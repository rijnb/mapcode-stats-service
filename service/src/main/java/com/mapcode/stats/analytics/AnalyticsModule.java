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

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tomtom.speedtools.tracer.TracerFactory;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceProperties;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceStream;

import javax.annotation.Nonnull;
import javax.inject.Singleton;


public class AnalyticsModule implements Module {

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Set tracers.
        TracerFactory.setEnabled(true);
        binder.bind(MongoDBTraceStream.class);
        binder.bind(MongoDBTraceProperties.class).in(Singleton.class);

        // Trace handlers.
        binder.bind(MapcodeResourceTraceHandler.class).in(Singleton.class);

        // Bind analytics engine.
        binder.bind(StatsEngine.class).asEagerSingleton();
        binder.bind(TraceProcessor.class).asEagerSingleton();
    }
}
