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
import com.tomtom.speedtools.apivalidation.ApiDTO;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "mapcode")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ClusterDTO extends ApiDTO {

    @XmlElement(name = "nrEvents")
    @Nonnull
    private int nrEvents;

    @XmlElement(name = "southWest")
    @Nonnull
    private PointDTO southWest;

    @XmlElement(name = "northEast")
    @Nonnull
    private PointDTO northEast;

    @Override
    public void validate() {
        validator().start();
        validator().checkInteger(true, "nrEvents", nrEvents, 0, Integer.MAX_VALUE);
        validator().done();
    }

    public ClusterDTO(
            @Nonnull final int nrEvents,
            @Nonnull final PointDTO southWest,
            @Nonnull final PointDTO northEast) {
        this.nrEvents = nrEvents;
        this.southWest = southWest;
        this.northEast = northEast;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private ClusterDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getNrEvents() {
        beforeGet();
        return nrEvents;
    }

    public void setNrEvents(final int nrEvents) {
        beforeSet();
        this.nrEvents = nrEvents;
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

