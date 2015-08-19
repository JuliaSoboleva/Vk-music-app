package com.soboleva.vkmusicloader.utils.eventBusMessages;

public class MessageInternetStateChangedEvent {
    private final boolean state;

    public MessageInternetStateChangedEvent(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
