/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softwarefactory.keycloak.providers.events.http;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;

/**
 *
 * @author a.bashir
 */
public class HTTPEventListenerThread extends Thread {

    public AdminEvent getAdminEvent() {
        return adminEvent;
    }

    public void setAdminEvent(AdminEvent adminEvent) {
        this.adminEvent = adminEvent;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public int getDebounceTime() {
        return debounceTime;
    }

    public void setDebounceTime(int debounceTime) {
        this.debounceTime = debounceTime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
   
    private String serverUri;
    private int debounceTime;
    private Event event;
    private AdminEvent adminEvent;
    private String eventType;
    private String token;

    public HTTPEventListenerThread() {

    }

    public HTTPEventListenerThread(String serverUri, String debounceTime, Event event) {
        this.event = event;
        this.serverUri = serverUri;
        this.debounceTime = Integer.parseInt(debounceTime);
    }

    public HTTPEventListenerThread(String serverUri, String debounceTime, AdminEvent event) {
        this.adminEvent = event;
        this.serverUri = serverUri;
        this.debounceTime = Integer.parseInt(debounceTime);
    }

    @Override
    public void run() {
        {
            System.out.println("going to sleep for : " +this.debounceTime +" milliseconds");
            //  HTTPEventListenerThread.runningThread = true;
            try {
                Thread.sleep(this.debounceTime);
                try {
                    System.out.println("calling web service");
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(this.serverUri))
                            .timeout(Duration.ofMinutes(1))
                            .header("Content-Type", "application/plain")
                            .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJXSmE4aEFiWmhTMlBvTDZaeDUxZ3pPNF9ZSml6MHpuYnVGaDFzWnF1TXR3In0.eyJleHAiOjE2NzQ4NDIyNTEsImlhdCI6MTY3NDgxODg1MSwiYXV0aF90aW1lIjoxNjc0ODE4ODUwLCJqdGkiOiJhZWY2NzVlYi02M2I1LTQ3ODEtOTQ0MC03NTUzYTI2ZThjMzkiLCJpc3MiOiJodHRwczovL2lkcC1kZXYuZGlnaXh0LmFlL2F1dGgvcmVhbG1zL0RpZ2l4dCIsImF1ZCI6WyJyZWFsbS1tYW5hZ2VtZW50IiwicXVlcnlib29rIiwidHJpbm8iLCJzdXBlcnNldCIsIm9wZW5zZWFyY2giLCJncmFmYW5hIiwiYWlyZmxvdyIsIm1pbmlvIiwiYWNjb3VudCJdLCJzdWIiOiIyYzhlYmUwZC1mNGFhLTRhNjktOGI1MS00OWM4NmQxMjczNjIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJkaWdpeHQtY2xpZW50Iiwibm9uY2UiOiJkYTM3M2NiZC05MGRiLTQ1ODctOGQ4OC0wN2M1NGMxMWUzODQiLCJzZXNzaW9uX3N0YXRlIjoiZmYzMDE4ZWMtNTkxMi00OTk1LTgzZDMtMjgyZDVkZDZmZWM2IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWRpZ2l4dCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidW1hX3Byb3RlY3Rpb24iLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwicXVlcnlib29rIjp7InJvbGVzIjpbImFkbWluIl19LCJkaWdpeHQtY2xpZW50Ijp7InJvbGVzIjpbImRpZ2l4dF9hZG1pbiJdfSwidHJpbm8iOnsicm9sZXMiOlsiYWRtaW4iXX0sInN1cGVyc2V0Ijp7InJvbGVzIjpbIkFkbWluIl19LCJvcGVuc2VhcmNoIjp7InJvbGVzIjpbImluZGV4X21hbmFnZW1lbnRfZnVsbF9hY2Nlc3MiLCJyZWFkYWxsX2FuZF9tb25pdG9yIiwib2JzZXJ2YWJpbGl0eV9mdWxsX2FjY2VzcyIsInJlcG9ydHNfZnVsbF9hY2Nlc3MiLCJvd25faW5kZXgiLCJraWJhbmFfcmVhZF9vbmx5IiwidW1hX3Byb3RlY3Rpb24iLCJhbGVydGluZ19mdWxsX2FjY2VzcyIsImFkbWluIiwiYWxsX2FjY2VzcyIsImFub21hbHlfZnVsbF9hY2Nlc3MiXX0sImdyYWZhbmEiOnsicm9sZXMiOlsiQWRtaW4iXX0sImFpcmZsb3ciOnsicm9sZXMiOlsiYWlyZmxvd19hZG1pbiJdfSwibWluaW8iOnsicm9sZXMiOlsiY29uc29sZUFkbWluIiwid3JpdGVvbmx5IiwiZGlhZ25vc3RpY3MiLCJhZG1pbiIsInJlYWR3cml0ZSJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgdXNlciBlbWFpbCBwcm9maWxlIiwic2lkIjoiZmYzMDE4ZWMtNTkxMi00OTk1LTgzZDMtMjgyZDVkZDZmZWM2IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJyb2xlIjoiQWRtaW4iLCJuYW1lIjoiRGlnaXh0IEFkbWluIiwiZ3JvdXBzIjpbIi9iaWdkYXRhLWFkbWluIl0sInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6IkRpZ2l4dCIsImZhbWlseV9uYW1lIjoiQWRtaW4iLCJ1c2VyIjp7ImVtYWlsIjoiYWRtaW5Ac2FhbC5haSIsInVzZXJuYW1lIjoiYWRtaW4ifSwiZW1haWwiOiJhZG1pbkBzYWFsLmFpIiwicG9saWN5IjpbImRpZ2l4dF9hZG1pbiJdfQ.JcTH-jikLKbJG6IK4HIodSG7gPoS5k19ncY1RmqJBmLryuw7LjmdgwEIY8aIE3dKMslHTE_J0C3qMMrVQS6wXxjMxlP8v3hmn6laxIDEc6Twq_AhBTRgB0P-CsB5Nm8VtH5dwPT3L1Rk-D6BPon5BSnDHS6j4_cUTAvOF2ghNmCBEeHeGGQwnJCXqYEufVcg9zTFRspQk625reMRv9yJiLLA8Pey3EgTgQlqodSGLxNC90irgJ5rsFdQ5QrGczOD431lir45wlDJal9fHWEiClNT280UuMU6dnJSx3G2L9NJxS6ZhXgp4harzqbW8e7WaRiU1RdIk7zNBzs4A6YekQ")
                            .GET()
                            .build();
                    java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder().build();
                    java.net.http.HttpResponse<String> response
                            = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                    System.out.println("response" + response.body());
                } catch (Exception e) {
                    // ?
                    System.out.println("UH OH!! " + e.toString());
                    e.printStackTrace();
                    return;
                }
                System.out.println("exiting");
            } catch (InterruptedException ex) {
                Logger.getLogger(HTTPEventListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            //     HTTPEventListenerThread.runningThread = false;
        }
//        else {
//            System.out.println("another thread already running... quiting this thread");
//        }
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

}
