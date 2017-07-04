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

import com.google.gson.Gson;
import com.mapcode.stats.dto.VersionDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class ApiOthersTest {
    private static final Logger LOG = LoggerFactory.getLogger(ApiOthersTest.class);

    private LocalTestServer server;

    @Before
    public void startServer() {
        server = new LocalTestServer("1.0", 8081);
        server.start();
    }

    @After
    public void stopServer() {
        server.stop();
    }

    @Test
    public void getHelp() {
        LOG.info("getHelp");
        final Response r = new ResteasyClientBuilder().build().
                target(server.url("/stats")).
                request().
                get();
        Assert.assertNotNull(r);
        final int status = r.getStatus();
        LOG.info("status = {}", status);
        Assert.assertEquals(200, status);
        Assert.assertEquals("<html>", r.readEntity(String.class).substring(0, 6));
    }

    @Test
    public void checkVersionJson() {
        LOG.info("checkVersionJson");
        final Response response = new ResteasyClientBuilder().build().
                target(server.url("/stats/version")).
                request().
                accept(MediaType.APPLICATION_JSON).get();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatus());
        final String s = response.readEntity(String.class);
        Assert.assertEquals("{\"version\":\"1.0\"}",
                s);
        final VersionDTO x = new Gson().fromJson(s, VersionDTO.class);
        Assert.assertNotNull(x);
        Assert.assertEquals("1.0", x.getVersion());
    }
}
