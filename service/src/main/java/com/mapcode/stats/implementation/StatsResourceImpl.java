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

package com.mapcode.stats.implementation;

import akka.dispatch.Futures;
import com.mapcode.stats.StatsResource;
import com.mapcode.stats.dto.VersionDTO;
import com.tomtom.speedtools.apivalidation.exceptions.ApiInvalidFormatException;
import com.tomtom.speedtools.rest.ResourceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

/**
 * This class implements the REST API that handles mapcode conversions.
 */
public class StatsResourceImpl implements StatsResource {
    private static final Logger LOG = LoggerFactory.getLogger(StatsResourceImpl.class);

    private final ResourceProcessor processor;

    /**
     * The constructor is called by Google Guice at start-up time and gets a processor injected
     * to executed web requests on.
     *
     * @param processor        Processor to process web requests on.
     * @param metricsCollector Metric collector.
     */
    @Inject
    public StatsResourceImpl(@Nonnull final ResourceProcessor processor) {
        assert processor != null;
        this.processor = processor;
    }

    @Override
    public void getCountTotalRequests(@Nonnull final AsyncResponse response) throws ApiInvalidFormatException {
        assert response != null;

        processor.process("getCountTotalRequests", LOG, response, () -> {
            LOG.info("getCountTotalRequests");
            final VersionDTO result = new VersionDTO("getCountTotalRequests");

            // Validate the DTO before returning it, to make sure it's valid (internal consistency check).
            result.validate();
            response.resume(Response.ok(result).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }
}
