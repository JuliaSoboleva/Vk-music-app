package com.soboleva.vkmusicapp.utils.eventBusMessages;

public class MessageInternetStateChangedEvent {
    private final boolean state;

    public MessageInternetStateChangedEvent(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }
}
