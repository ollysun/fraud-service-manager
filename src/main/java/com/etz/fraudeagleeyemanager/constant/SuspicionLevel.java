package com.etz.fraudeagleeyemanager.constant;

public enum SuspicionLevel {
    NOTHING(1), HOLD(2),
    BLOCK(3);
    private final int value;

    private SuspicionLevel(int value) {
        this.value = value;
    }


    public int getLevelCode() {
        return this.value;
    }
}
