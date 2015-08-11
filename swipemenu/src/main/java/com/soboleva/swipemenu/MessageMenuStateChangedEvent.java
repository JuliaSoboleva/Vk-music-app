package com.soboleva.swipemenu;

public class MessageMenuStateChangedEvent {
    private final boolean state;


    public MessageMenuStateChangedEvent(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
