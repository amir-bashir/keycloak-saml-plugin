/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.softwarefactory.keycloak.providers.events.http;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.HashSet;
import java.util.Set;
import org.keycloak.KeycloakSecurityContext;

/**
 * @author <a href="mailto:jessy.lenne@stadline.com">Jessy Lennee</a>
 */
public class HTTPEventListenerProviderFactory implements EventListenerProviderFactory {

    private Set<EventType> excludedEvents;
    private Set<OperationType> excludedAdminOperations;
    private String serverUri;
    private String debounceTime;
    private String password;
    private String topic;
    private String SERVER_URI = "SERVER_URI";
    private String USER_NAME = "USERNAME";
    private String PASSWORD = "PASSWORD";
    private String DEBOUNCE_TIME = "DEBOUNCE_TIME";
    private String SYNC_CREATE_EVENT = "SYNC_CREATE_EVENT";
    private String syncCreateEvent;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new HTTPEventListenerProvider(excludedEvents, excludedAdminOperations, serverUri, debounceTime, syncCreateEvent, topic, session);
    }

    @Override
    public void init(Config.Scope config) {
        String[] excludes = config.getArray("exclude-events");
        if (excludes != null) {
            excludedEvents = new HashSet<>();
            for (String e : excludes) {
                excludedEvents.add(EventType.valueOf(e));
            }
        }

        String[] excludesOperations = config.getArray("excludesOperations");
        if (excludesOperations != null) {
            excludedAdminOperations = new HashSet<>();
            for (String e : excludesOperations) {
                excludedAdminOperations.add(OperationType.valueOf(e));
            }
        }

        serverUri = config.get("serverUri", System.getenv(SERVER_URI));
        debounceTime = config.get("debounceTime", System.getenv(DEBOUNCE_TIME));
        syncCreateEvent = config.get("captureCreateEvent", System.getenv(SYNC_CREATE_EVENT));
        topic = config.get("topic", "keycloak/events");

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "amir-http";
    }

}
