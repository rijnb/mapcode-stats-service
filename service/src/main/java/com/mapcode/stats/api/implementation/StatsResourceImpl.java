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

package com.mapcode.stats.api.implementation;

import akka.dispatch.Futures;
import com.mapcode.stats.analytics.Cluster;
import com.mapcode.stats.analytics.StatsEngine;
import com.mapcode.stats.api.ApiConstants;
import com.mapcode.stats.api.StatsResource;
import com.mapcode.stats.api.dto.ClusterDTO;
import com.mapcode.stats.api.dto.ClusterListDTO;
import com.mapcode.stats.api.dto.ClustersDTO;
import com.mapcode.stats.api.dto.PointDTO;
import com.tomtom.speedtools.apivalidation.exceptions.ApiException;
import com.tomtom.speedtools.apivalidation.exceptions.ApiIntegerOutOfRangeException;
import com.tomtom.speedtools.geometry.GeoPoint;
import com.tomtom.speedtools.geometry.GeoRectangle;
import com.tomtom.speedtools.rest.ResourceProcessor;
import com.tomtom.speedtools.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class implements the REST API that handles mapcode conversions.
 */
public class StatsResourceImpl implements StatsResource {
    private static final Logger LOG = LoggerFactory.getLogger(StatsResourceImpl.class);

    private final StatsEngine statsEngine;
    private final ResourceProcessor processor;

    /**
     * The constructor is called by Google Guice at start-up time and gets a processor injected
     * to executed web requests on.
     *
     * @param processor        Processor to process web requests on.
     * @param metricsCollector Metric collector.
     */
    @Inject
    public StatsResourceImpl(
            @Nonnull final StatsEngine statsEngine,
            @Nonnull final ResourceProcessor processor) {
        assert statsEngine != null;
        assert processor != null;
        this.statsEngine = statsEngine;
        this.processor = processor;
    }

    @Override
    public void getClusters(
            final double paramLatSW,
            final double paramLonSW,
            final double paramLatNE,
            final double paramLonNE,
            final int paramNrClusters,
            final @Nonnull AsyncResponse response) throws ApiException {
        assert response != null;

        processor.process("getClustersForArea", LOG, response, () -> {

            // Check input parameter.
            if (!MathUtils.isBetween(paramNrClusters, ApiConstants.API_NR_CLUSTERS_MIN, ApiConstants.API_NR_CLUSTERS_MAX)) {
                throw new ApiIntegerOutOfRangeException(PARAM_NR_CLUSTERS, paramNrClusters, ApiConstants.API_NR_CLUSTERS_MIN, ApiConstants.API_NR_CLUSTERS_MAX);
            }
            final GeoPoint sw = new GeoPoint(paramLatSW, paramLonSW);
            final GeoPoint ne = new GeoPoint(paramLatNE, paramLonNE);
            final GeoRectangle area = new GeoRectangle(sw, ne);

            LOG.info("getClustersForArea: nrCluster={}, area={}", paramNrClusters, area);
            final Set<Cluster> clustersForArea = statsEngine.getClustersForArea(area, paramNrClusters);

            final List<ClusterDTO> list = new ArrayList<>();
            for (final Cluster cluster : clustersForArea) {
                final GeoRectangle boundingBox = cluster.getBoundingBox();
                final PointDTO southWest = new PointDTO(boundingBox.getSouthWest().getLat(), boundingBox.getSouthWest().getLon());
                final PointDTO northEast = new PointDTO(boundingBox.getNorthEast().getLat(), boundingBox.getNorthEast().getLon());
                list.add(new ClusterDTO(cluster.getCount(), southWest, northEast));
            }
            final ClusterListDTO clusters = new ClusterListDTO(list);
            final ClustersDTO result = new ClustersDTO(clusters.size(), clusters);

            // Validate the DTO before returning it, to make sure it's valid (internal consistency check).
            result.validate();
            response.resume(Response.ok(result).build());

            // The response is already set within this method body.
            return Futures.successful(null);
        });
    }
}
