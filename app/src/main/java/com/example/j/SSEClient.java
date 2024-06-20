package com.example.j;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.content.SharedPreferences;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Executors;

public class SSEClient {
    private String token;
    private String id;
    private SSEHandler sseHandlers = null;
    private EventSource eventSourceSse = null;

    public SSEClient(String token, String id ) {
       this.token = token;
       this.id = id;
    }
    public void initSse(SSEHandler sseHandler, String Token ,  ErrorCallback errorCallback) {
        this.sseHandlers = sseHandler;
        EventHandler eventHandler = new DefaultEventHandler(sseHandler);



      String baseUrlTemplate = "http://192.168.1.180:3001/stream2?token=%s&id=%s";
      String baseUrl = String.format(baseUrlTemplate, token, id);
        String PATH = "/q3/events"; // Replace with the SSE endpoint path
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                eventSourceSse = new EventSource.Builder(eventHandler, URI.create(baseUrl + PATH))
                        .connectTimeout(Duration.ofSeconds(3))
                        .backoffResetThreshold(Duration.ofSeconds(3))
                        .build();
                eventSourceSse.start();
            }
        } catch (Exception e) {
            errorCallback.onError(e);
        }
    }

    private class DefaultEventHandler implements EventHandler {

        private final SSEHandler sseHandler;

        DefaultEventHandler(SSEHandler sseHandler) {
            this.sseHandler = sseHandler;
        }

        @Override
        public void onOpen() {
            sseHandler.onSSEConnectionOpened();
        }

        @Override
        public void onClosed() {
            sseHandler.onSSEConnectionClosed();
        }

        @Override
        public void onMessage(String event, MessageEvent messageEvent) {
            sseHandler.onSSEEventReceived(event, messageEvent);
        }

        @Override
        public void onError(Throwable t) {
            sseHandler.onSSEError(t);
        }

        @Override
        public void onComment(String comment) {
            Log.i("SSE_CONNECTION", comment);
        }
    }

    public void disconnect() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                if (eventSourceSse != null) {
                    eventSourceSse.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public interface ErrorCallback {
        void onError(Throwable t);
    }
}