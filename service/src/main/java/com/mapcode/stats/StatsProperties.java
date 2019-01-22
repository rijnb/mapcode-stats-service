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

package com.mapcode.stats;

import com.tomtom.speedtools.guice.HasProperties;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

public class StatsProperties implements HasProperties {

    @Nonnull
    private final String apiKey;

    @Inject
    public StatsProperties(
            @Named("Mapcode.stats.apiKey") @Nonnull final String apiKey) {
        assert apiKey != null;
        this.apiKey = apiKey.trim();
    }

    @Nonnull
    public String getApiKey() {
        return apiKey;
    }
}

