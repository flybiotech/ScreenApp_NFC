package com.screening.model;

/**
 * Copyright 2019 bejson.com
 */

public class Rets {

    private long screeningId;
    private int code;
    private String message;

    public void setScreeningId(long screeningId) {
        this.screeningId = screeningId;
    }

    public long getScreeningId() {
        return screeningId;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}