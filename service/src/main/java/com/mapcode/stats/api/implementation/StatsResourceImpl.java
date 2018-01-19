/*
 * Copyright (C) 2016-2018, Stichting Mapcode Foundation (http://www.mapcode.com)
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

package com.mapcode.stats.api.implementation;

import akka.dispatch.Futures;
import com.mapcode.stats.StatsProperties;
import com.mapcode.stats.analytics.StatsEngine;
import com.mapcode.stats.api.StatsResource;
import com.tomtom.speedtools.apivalidation.exceptions.ApiException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiIntegerOutOfRangeException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiUnauthorizedException;
import com.tomtom.speedtools.rest.ResourceProcessor;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class implements the REST API that handles mapcode conversions.
 */
public class StatsResourceImpl implements StatsResource {
    private static final Logger LOG = LoggerFactory.getLogger(StatsResourceImpl.class);

    private final StatsProperties statsProperties;
    private final StatsEngine statsEngine;
    private final ResourceProcessor processor;

    @Inject
    public StatsResourceImpl(
            @Nonnull final StatsProperties statsProperties,
            @Nonnull final StatsEngine statsEngine,
            @Nonnull final ResourceProcessor processor) {
        assert statsProperties != null;
        assert statsEngine != null;
        assert processor != null;
        this.statsProperties = statsProperties;
        this.statsEngine = statsEngine;
        this.processor = processor;
    }

    @Override
    public void getMapcodeRequestsForWorld(
            @Nonnull final String apiKey,
            final int offset,
            final int count,
            @Nonnull final AsyncResponse response) throws ApiException {
        assert response != null;

        processor.process("getMapcodeRequestsForWorld", LOG, response, () -> {

            // Check API key.
            if (!statsProperties.getApiKey().equals(apiKey)) {
                throw new ApiUnauthorizedException("Invalid API key");
            }

            // Check value of count.
            if (count < 0) {
                throw new ApiIntegerOutOfRangeException(PARAM_COUNT, count, 0, Integer.MAX_VALUE);
            }
            assert count >= 0;

            // Copy events.
            final List<Feature> features = statsEngine.getEvents(count, offset);
            final FeatureCollection featureCollection = new FeatureCollection();
            featureCollection.setFeatures(features);
            response.resume(Response.ok(featureCollection).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }
}
