package com.example.j;
import android.util.Log;
import androidx.lifecycle.ViewModel;

import com.launchdarkly.eventsource.MessageEvent;
public class YourViewModel extends ViewModel implements SSEHandler {

    private final SSEClient sseClient;

    public YourViewModel(SSEClient sseClient, String Token) {
        this.sseClient = sseClient;
        this.sseClient.initSse(this, Token,Throwable::printStackTrace);
    }

    @Override
    public void onSSEConnectionOpened() {
        // Handle SSE connection opened event
    }
    @Override
    public void onSSEConnectionClosed() {
        // Handle SSE connection closed event
    }

    @Override
    public void onSSEEventReceived(String event, MessageEvent messageEvent) {
        Log.e("hello", "reveived succes");
    }
    @Override
    public void onSSEError(Throwable t) {
    }
    public void disconnect() {
        sseClient.disconnect();
    }
}