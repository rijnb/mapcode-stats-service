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

package com.mapcode.stats.api;

import com.tomtom.speedtools.apivalidation.exceptions.ApiException;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
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
    static final String PARAM_LAT_SW = "latSW";
    static final String PARAM_LAT_NE = "latNE";
    static final String PARAM_LON_SW = "lonSW";
    static final String PARAM_LON_NE = "lonNE";
    static final String PARAM_NR_CLUSTERS = "nrClusters";

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("clusters/{" + PARAM_LAT_SW + "},{" + PARAM_LON_SW + "},{" + PARAM_LAT_NE + "},{" + PARAM_LON_NE + "}/{" + PARAM_NR_CLUSTERS + '}')
    void getClusters(
            @PathParam(PARAM_LAT_SW) double paramLatSW,
            @PathParam(PARAM_LON_SW) double paramLonSW,
            @PathParam(PARAM_LAT_NE) double paramLatNE,
            @PathParam(PARAM_LON_NE) double paramLonNE,
            @PathParam(PARAM_NR_CLUSTERS) int paramNrClusters,
            @Suspended @Nonnull AsyncResponse response) throws ApiException;
}
