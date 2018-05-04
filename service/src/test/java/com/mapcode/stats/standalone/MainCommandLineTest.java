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

package com.mapcode.stats.standalone;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings({"rawtypes", "ProhibitedExceptionDeclared", "unchecked"})
public class MainCommandLineTest {
    private static final Logger LOG = LoggerFactory.getLogger(MainCommandLineTest.class);

    private static final int SERVER_PORT = 8081;

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void testHelp() {
        LOG.info("testHelp");

        // Initialize Mockito.
        MockitoAnnotations.initMocks(this);
        MainCommandLine.execute("--help");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void testUnknownArgument() {
        LOG.info("testUnknownArgument");

        // Initialize Mockito.
        MockitoAnnotations.initMocks(this);
        MainCommandLine.execute("--help", "unknown");
        MainCommandLine.execute("--unknown");
        MainCommandLine.execute("--port");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void testServer() {
        LOG.info("testServer");

        // Initialize Mockito.
        MockitoAnnotations.initMocks(this);
        MainCommandLine.execute("--silent", "--debug", "--port", "8081");

        // Execute a REST API call.
        checkVersion();
        MainCommandLine.stop();
    }

    public void checkVersion() {
        LOG.info("checkVersion");
        final String expectedJson = "{\"version\":";
        final Response response = new ResteasyClientBuilder().build().
                target(localUrl("/stats/version")).
                request().
                accept(MediaType.APPLICATION_JSON_TYPE).get();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(response.readEntity(String.class).startsWith(expectedJson));
    }

    @Nonnull
    private static String localUrl(@Nonnull final String url) {
        return "http://localhost:" + SERVER_PORT + url;
    }
}
