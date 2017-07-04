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

import com.mapcode.stats.RootResource;
import com.mapcode.stats.dto.VersionDTO;
import com.tomtom.speedtools.maven.MavenProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

/**
 * This class implements the REST API that deals with the root resource for the Mapcode REST API.
 * This includes methods to get HTML help, the service version and server status.
 */
public class RootResourceImpl implements RootResource {
    private static final Logger LOG = LoggerFactory.getLogger(RootResourceImpl.class);

    @Nonnull
    private static final String HELP_TEXT = "" +

            "The REST API Methods\n" +
            "--------------------\n\n" +

            "All REST services are able to return both JSON and XML. Use the HTTP\n" +
            "'Accept:' header to specify the expected format: application/json or application/xml\n" +
            "If the 'Accept:' header is omitted, JSON is assumed.\n\n" +

            "GET /stats         Returns this help page.\n" +
            "GET /stats/version Returns the software version.\n" +
            "GET /stats/status  Returns 200 if the service OK.\n";

    private final MavenProperties mavenProperties;

    @Inject
    public RootResourceImpl(@Nonnull final MavenProperties mavenProperties) {
        assert mavenProperties != null;

        // Store the injected values.
        this.mavenProperties = mavenProperties;
    }

    @Override
    @Nonnull
    public String getHelpHTML() {
        LOG.info("getHelpHTML: show help page", mavenProperties.getPomVersion());
        return "<html><pre>\n" +
                "MAPCODE STATS API (" + mavenProperties.getPomVersion() + ")\n" +
                "-----------------\n\n" +
                HELP_TEXT + "</pre></html>\n";
    }

    @Override
    public void getVersion(@Suspended @Nonnull final AsyncResponse response) {
        assert response != null;

        // No input validation required. Just return version number.
        final String pomVersion = mavenProperties.getPomVersion();
        LOG.info("getVersion: POM version={}", pomVersion);

        // Create the response binder and validate it (returned objects must also be validated!).
        // Validation errors are automatically caught as exceptions and returned by the framework.
        final VersionDTO result = new VersionDTO(pomVersion); // Create a binder.
        result.validate();                                    // You must validate it before using it.

        // Build the response and return it.
        response.resume(Response.ok(result).build());
    }

    @Override
    public void getVersionXml(@Suspended @Nonnull final AsyncResponse response) {
        getVersion(response);
    }

    @Override
    public void getVersionJson(@Suspended @Nonnull final AsyncResponse response) {
        getVersion(response);
    }

    @Override
    public void getStatus(@Suspended @Nonnull final AsyncResponse response) {
        assert response != null;
        LOG.info("getStatus: get status");
        response.resume(Response.ok().build());
    }

    @Override
    public void getStatusXml(@Suspended @Nonnull final AsyncResponse response) {
        getStatus(response);
    }

    @Override
    public void getStatusJson(@Suspended @Nonnull final AsyncResponse response) {
        getStatus(response);
    }
}
