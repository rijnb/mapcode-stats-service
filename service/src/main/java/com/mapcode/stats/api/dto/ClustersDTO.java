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

package com.mapcode.stats.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tomtom.speedtools.apivalidation.ApiDTO;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "clusters")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ClustersDTO extends ApiDTO {

    @JsonProperty("totalNrClusters")
    @XmlElement(name = "totalNrClusters")
    @Nonnull
    private int totalNrClusters;

    @JsonProperty("totalNrEvents")
    @XmlElement(name = "totalNrEvents")
    @Nonnull
    private int totalNrEvents;

    @JsonProperty("clusters")
    @JsonUnwrapped
    @XmlElement(name = "cluster")
    @Nullable
    private ClusterListDTO clusters;

    @XmlElement(name = "southWest")
    @Nonnull
    private PointDTO southWest;

    @XmlElement(name = "northEast")
    @Nonnull
    private PointDTO northEast;

    @Override
    public void validate() {
        validator().start();
        final int calcTotalNrClusters = (clusters == null) ? 0 : clusters.size();
        final Integer calcTotalNrEvents = (clusters == null) ? 0 : clusters.stream().
                map(ClusterDTO::getNrEvents).
                reduce(0, Integer::sum);
        validator().checkInteger(true, "totalNrClusters", totalNrClusters, calcTotalNrClusters, calcTotalNrClusters);
        validator().checkInteger(true, "totalNrEvents", totalNrEvents, calcTotalNrEvents, calcTotalNrEvents);
        validator().checkNotNullAndValidateAll(false, "clusters", clusters);
        validator().checkNotNullAndValidate(true, "southWest", southWest);
        validator().checkNotNullAndValidate(true, "northEast", northEast);
        validator().done();
    }

    public ClustersDTO(
            final int totalNrClusters,
            final int totalNrEvents,
            @Nullable final ClusterListDTO clusters,
            @Nonnull final PointDTO southWest,
            @Nonnull final PointDTO northEast) {
        this.totalNrClusters = totalNrClusters;
        this.totalNrEvents = totalNrEvents;
        this.clusters = clusters;
        this.southWest = southWest;
        this.northEast = northEast;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private ClustersDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getTotalNrClusters() {
        beforeGet();
        return totalNrClusters;
    }

    public void setTotalNrClusters(final int totalNrClusters) {
        beforeSet();
        this.totalNrClusters = totalNrClusters;
    }

    public int getTotalNrEvents() {
        beforeGet();
        return totalNrEvents;
    }

    public void setTotalNrEvents(final int totalNrEvents) {
        beforeSet();
        this.totalNrEvents = totalNrEvents;
    }

    @Nullable
    public ClusterListDTO getClusters() {
        beforeGet();
        return clusters;
    }

    public void setClusters(@Nullable final ClusterListDTO clusters) {
        beforeSet();
        this.clusters = clusters;
    }

    @Nonnull
    public PointDTO getSouthWest() {
        beforeGet();
        return southWest;
    }

    public void setSouthWest(@Nonnull final PointDTO southWest) {
        beforeSet();
        this.southWest = southWest;
    }

    @Nonnull
    public PointDTO getNorthEast() {
        beforeGet();
        return northEast;
    }

    public void setNorthEast(@Nonnull final PointDTO northEast) {
        beforeSet();
        this.northEast = northEast;
    }
}
