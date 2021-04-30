package com.etz.fraudeagleeyemanager.constant;

public enum SuspicionLevel {
    DO_NOTHING(1), HOLD_TRANSACTION_FOR_CONFIRMATION(2),
    BLOCK_TRANSACTION(3);
    private final int value;

    private SuspicionLevel(int value) {
        this.value = value;
    }


    public int getLevelCode() {
        return this.value;
    }
}
