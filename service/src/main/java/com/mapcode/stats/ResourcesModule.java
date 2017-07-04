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
import com.mapcode.stats.implementation.StatsResourceImpl;
import com.mapcode.stats.implementation.RootResourceImpl;

import javax.annotation.Nonnull;
import javax.inject.Singleton;


/**
 * This class defines the deployment configuration for Google Guice.
 * <p>
 * The deployment module "bootstraps" the whole Guice injection process.
 * <p>
 * It bootstraps the Guice injection and specifies the property files to be read. It also needs to bind the tracer, so
 * they can be used early on in the app. Finally, it can bind a "startup check" (example provided) as an eager
 * singleton, so the system won't start unless a set of basic preconditions are fulfilled.
 * <p>
 * The "speedtools.default.properties" is required, but its values may be overridden in other property files.
 */
public class ResourcesModule implements Module {

    @Override
    public void configure(@Nonnull final Binder binder) {
        assert binder != null;

        // Bind APIs to their implementation.
        binder.bind(RootResource.class).to(RootResourceImpl.class).in(Singleton.class);
        binder.bind(StatsResource.class).to(StatsResourceImpl.class).in(Singleton.class);
    }
}
