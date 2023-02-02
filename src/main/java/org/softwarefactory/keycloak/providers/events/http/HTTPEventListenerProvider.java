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

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;

import java.util.Map;
import java.util.Set;

import okhttp3.*;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.models.KeycloakSession;

/**
 * @author <a href="mailto:jessy.lenne@stadline.com">Jessy Lenne</a>
 */
public class HTTPEventListenerProvider implements EventListenerProvider {

    private Set<EventType> excludedEvents;
    private Set<OperationType> excludedAdminOperations;
    private final String serverUri;
    private String debounceTime="15000";
    public static final String publisherId = "keycloak";
    public String TOPIC;
    private static final HTTPEventListenerThread callingThread = new HTTPEventListenerThread();
    private String syncCreateEvent="no";
    private String token;
    private KeycloakSession session;

    public HTTPEventListenerProvider(Set<EventType> excludedEvents, Set<OperationType> excludedAdminOperations, String serverUri, String debounceTime, String syncCreateEvent, String token, KeycloakSession session) {
        this.excludedEvents = excludedEvents;
        this.excludedAdminOperations = excludedAdminOperations;
        this.serverUri = serverUri;
        this.debounceTime = debounceTime;
        this.syncCreateEvent = syncCreateEvent;
        this.token = token;
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        
        System.out.println("event type : " + event.getType().toString());
        
        if (excludedEvents != null && excludedEvents.contains(event.getType())) {
            return;
        } else {
            System.out.println("checking event type");
            if (event.getType().equals(EventType.REGISTER)) {
                System.out.println("calling login thread");
                callThread("login");
            }
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
//        System.out.println("in onEvent(AdminEvent)");
        if (this.syncCreateEvent != null && this.syncCreateEvent.equalsIgnoreCase("yes")) {
            // Ignore excluded operations
            if (excludedAdminOperations != null && excludedAdminOperations.contains(event.getOperationType())) {
                return;
            } else {
                if (event.getOperationType().equals(OperationType.CREATE)) {
                    
                    callThread("admin");
                }
            }
        }
    }

    synchronized private void callThread(String eventType) {
        if (callingThread.getState().toString().equalsIgnoreCase("new") || callingThread.getState().toString().equalsIgnoreCase("terminated")) {
            callingThread.setServerUri(serverUri);
            callingThread.setDebounceTime(Integer.parseInt(debounceTime));
            //callingThread.setAdminEvent(event);
            callingThread.setEventType(eventType);
            callingThread.start();
        } else {
            System.out.println("another thread already running. not invoking new thread");
        }
    }

    private String toString(Event event) {
        StringBuilder sb = new StringBuilder();

        sb.append("{'type': '");
        sb.append(event.getType());
        sb.append("', 'realmId': '");
        sb.append(event.getRealmId());
        sb.append("', 'clientId': '");
        sb.append(event.getClientId());
        sb.append("', 'userId': '");
        sb.append(event.getUserId());
        sb.append("', 'ipAddress': '");
        sb.append(event.getIpAddress());
        sb.append("'");

        if (event.getError() != null) {
            sb.append(", 'error': '");
            sb.append(event.getError());
            sb.append("'");
        }
        sb.append(", 'details': {");
        if (event.getDetails() != null) {
            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
                sb.append("'");
                sb.append(e.getKey());
                sb.append("': '");
                sb.append(e.getValue());
                sb.append("', ");
            }
        }
        sb.append("}}");

        return sb.toString();
    }

    private String toString(AdminEvent adminEvent) {
        StringBuilder sb = new StringBuilder();

        sb.append("{'type': '");
        sb.append(adminEvent.getOperationType());
        sb.append("', 'realmId': '");
        sb.append(adminEvent.getAuthDetails().getRealmId());
        sb.append("', 'clientId': '");
        sb.append(adminEvent.getAuthDetails().getClientId());
        sb.append("', 'userId': '");
        sb.append(adminEvent.getAuthDetails().getUserId());
        sb.append("', 'ipAddress': '");
        sb.append(adminEvent.getAuthDetails().getIpAddress());
        sb.append("', 'resourcePath': '");
        sb.append(adminEvent.getResourcePath());
        sb.append("'");

        if (adminEvent.getError() != null) {
            sb.append(", 'error': '");
            sb.append(adminEvent.getError());
            sb.append("'");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void close() {
    }

}
