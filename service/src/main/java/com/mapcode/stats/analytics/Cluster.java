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

import com.tomtom.speedtools.geometry.GeoRectangle;

import javax.annotation.Nonnull;

public class Cluster {
    private final int count;
    @Nonnull
    private final GeoRectangle boundingBox;

    Cluster(final int count, @Nonnull final GeoRectangle boundingBox) {
        this.boundingBox = boundingBox;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Nonnull
    public GeoRectangle getBoundingBox() {
        return boundingBox;
    }
}
