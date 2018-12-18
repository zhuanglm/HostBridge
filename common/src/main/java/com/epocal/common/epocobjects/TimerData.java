package com.epocal.common.epocobjects;

public class TimerData {
    public Object string_params;
    public int percentage_remaining;

    public TimerData(Object params, int percentage) {
        this.string_params = params;
        this.percentage_remaining = percentage;
    }
}
