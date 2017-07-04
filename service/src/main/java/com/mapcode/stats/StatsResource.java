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

import com.tomtom.speedtools.apivalidation.exceptions.ApiException;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 * This class handle the Mapcode REST API, which includes conversions to and from mapcodes.
 */
@Path("/stats")
public interface StatsResource {

    /**
     * Strings used as path or url parameters.
     */
    static final String PARAM_COUNT = "count";
    static final String PARAM_OFFSET = "offset";

    static final String DEFAULT_OFFSET = "0";
    static final String DEFAULT_COUNT = "1000";

    /**
     * Get total number of calls.
     *
     * @param response Response.
     * @throws ApiException API exception, translated into HTTP status code.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("stats/total")
    void getCountTotalRequests(@Suspended @Nonnull AsyncResponse response) throws ApiException;
}
