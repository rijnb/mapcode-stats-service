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

package com.mapcode.stats;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.mapcode.stats.analytics.AnalyticsEngine;
import com.mapcode.stats.analytics.MapcodeResourceTraceHandler;
import com.mapcode.stats.analytics.Stats;
import com.mapcode.stats.analytics.TraceProcessor;
import com.mapcode.stats.implementation.RootResourceImpl;
import com.mapcode.stats.implementation.StatsResourceImpl;
import com.tomtom.speedtools.tracer.TracerFactory;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceProperties;
import com.tomtom.speedtools.tracer.mongo.MongoDBTraceStream;

import javax.annotation.Nonnull;
import javax.inject.Singleton;


public class ResourcesModule implements Module {

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Bind APIs to their implementation.
        binder.bind(RootResource.class).to(RootResourceImpl.class).in(Singleton.class);
        binder.bind(StatsResource.class).to(StatsResourceImpl.class).in(Singleton.class);

        // Set tracers.
        TracerFactory.setEnabled(true);
        binder.bind(MongoDBTraceStream.class);
        binder.bind(MongoDBTraceProperties.class).in(Singleton.class);

        // Trace handlers.
        binder.bind(MapcodeResourceTraceHandler.class).in(Singleton.class);

        // Bind analytics engine.
        binder.bind(AnalyticsEngine.class).in(Singleton.class);
        binder.bind(Stats.class).asEagerSingleton();
        binder.bind(TraceProcessor.class).asEagerSingleton();
    }
}
