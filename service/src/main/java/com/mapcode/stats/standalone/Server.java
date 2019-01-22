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

package com.mapcode.stats.standalone;

import com.google.inject.Inject;
import com.mapcode.stats.StatsProperties;
import com.mapcode.stats.analytics.StatsEngine;
import com.mapcode.stats.api.implementation.RootResourceImpl;
import com.mapcode.stats.api.implementation.StatsResourceImpl;
import com.tomtom.speedtools.maven.MavenProperties;
import com.tomtom.speedtools.rest.Reactor;
import com.tomtom.speedtools.rest.ResourceProcessor;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.interceptors.CacheControlFeature;
import org.jboss.resteasy.plugins.providers.*;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.jaxb.*;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class Server {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private final Reactor reactor;
    private final StatsProperties statsProperties;
    private final StatsEngine statsEngine;
    private final MavenProperties mavenProperties;
    private boolean started = false;
    private final TJWSEmbeddedJaxrsServer server;

    @Inject
    public Server(
            @Nonnull final Reactor reactor,
            @Nonnull final StatsProperties statsProperties,
            @Nonnull final StatsEngine statsEngine,
            @Nonnull final MavenProperties mavenProperties) {
        this.reactor = reactor;
        this.statsProperties = statsProperties;
        this.statsEngine = statsEngine;
        this.mavenProperties = mavenProperties;
        server = new TJWSEmbeddedJaxrsServer();
    }

    public synchronized void startServer(final int port) {
        stopServer();
        server.setPort(port);

        /**
         * Create a simple ResourceProcessor, required for implementation of REST service using the
         * SpeedTools framework.
         */
        LOG.debug("Server: create resource processor...");
        final ResourceProcessor resourceProcessor = new ResourceProcessor(reactor);

        LOG.debug("Server: add resources...");
        final ResteasyDeployment deployment = server.getDeployment();
        final List<Object> resources = deployment.getResources();

        // Add root resource.
        resources.add(new RootResourceImpl(mavenProperties));

        // Add mapcode resource.
        resources.add(new StatsResourceImpl(statsProperties, statsEngine, resourceProcessor));

        LOG.debug("Server: start server...");
        server.start();

        LOG.debug("Server: register providers...");
        final Dispatcher dispatcher = deployment.getDispatcher();
        final ResteasyProviderFactory providerFactory = dispatcher.getProviderFactory();

        /**
         * Register providers for JSON and XML. This list was obtained by looking at which providers
         * were registered when the application would be running its unit tests, which works as well.
         */
        providerFactory.registerProvider(JAXBXmlSeeAlsoProvider.class, true);
        providerFactory.registerProvider(JAXBXmlRootElementProvider.class, true);
        providerFactory.registerProvider(JAXBElementProvider.class, true);
        providerFactory.registerProvider(JAXBXmlTypeProvider.class, true);
        providerFactory.registerProvider(CollectionProvider.class, true);
        providerFactory.registerProvider(MapProvider.class, true);
        providerFactory.registerProvider(XmlJAXBContextFinder.class, true);
        providerFactory.registerProvider(DataSourceProvider.class, true);
        providerFactory.registerProvider(DocumentProvider.class, true);
        providerFactory.registerProvider(DefaultTextPlain.class, true);
        providerFactory.registerProvider(StringTextStar.class, true);
        providerFactory.registerProvider(SourceProvider.class, true);
        providerFactory.registerProvider(InputStreamProvider.class, true);
        providerFactory.registerProvider(ReaderProvider.class, true);
        providerFactory.registerProvider(ByteArrayProvider.class, true);
        providerFactory.registerProvider(FormUrlEncodedProvider.class, true);
        providerFactory.registerProvider(JaxrsFormProvider.class, true);
        providerFactory.registerProvider(FileProvider.class, true);
        providerFactory.registerProvider(FileRangeWriter.class, true);
        providerFactory.registerProvider(StreamingOutputProvider.class, true);
        providerFactory.registerProvider(IIOImageProvider.class, true);
        providerFactory.registerProvider(CacheControlFeature.class, true);
        providerFactory.registerProvider(ResteasyJackson2Provider.class, true);

        started = true;
        LOG.debug("Server: server is ready");
    }

    public synchronized void stopServer() {
        LOG.debug("Server: stop server, started={}", started);
        if (started) {
            server.stop();
            started = false;
        }
    }
}
