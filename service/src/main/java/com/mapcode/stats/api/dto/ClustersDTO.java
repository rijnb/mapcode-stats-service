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

package com.mapcode.stats.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tomtom.speedtools.apivalidation.ApiDTO;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "clusters")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ClustersDTO extends ApiDTO {

    @JsonProperty("total")
    @XmlElement(name = "total")
    @Nonnull
    private int total;

    @JsonProperty("clusters")
    @JsonUnwrapped
    @XmlElement(name = "cluster")
    @Nonnull
    private ClusterListDTO clusters;

    @Override
    public void validate() {
        validator().start();
        validator().checkInteger(true, "total", total, 0, clusters.size());
        validator().checkNotNullAndValidateAll(false, "clusters", clusters);
        validator().done();
    }

    public ClustersDTO(
            final int total,
            @Nonnull final ClusterListDTO clusters) {
        this.total = total;
        this.clusters = clusters;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private ClustersDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getTotal() {
        beforeGet();
        return total;
    }

    public void setTotal(final int total) {
        beforeSet();
        this.total = total;
    }

    @Nonnull
    public ClusterListDTO getClusters() {
        beforeGet();
        return clusters;
    }

    public void setClusters(@Nonnull final ClusterListDTO clusters) {
        beforeSet();
        this.clusters = clusters;
    }
}
