package com.epocal.reader.type.Legacy;

/**
 * Created by rzhuang on Aug 8 2018.
 */

public class GeneralConfig {

    public void setReadyTimer(byte readyTimer) {
        this.mReadyTimer = readyTimer;
    }

    public void setHandleTurningTimer(byte handleTurningTimer) {
        this.mHandleTurningTimer = handleTurningTimer;
    }

    public void setCardRemovingTimer(byte cardRemovingTimer) {
        this.mCardRemovingTimer = cardRemovingTimer;
    }

    public byte getReadyTimer() {
        return mReadyTimer;
    }

    public byte getHandleTurningTimer() {
        return mHandleTurningTimer;
    }

    public byte getCardRemovingTimer() {
        return mCardRemovingTimer;
    }

    private byte mReadyTimer;
    private byte mHandleTurningTimer;
    private byte mCardRemovingTimer;
}