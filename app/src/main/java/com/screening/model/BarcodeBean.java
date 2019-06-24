package com.screening.model;

import java.io.Serializable;

public class BarcodeBean implements Serializable {
    private String identification;//标识

    private String digit;//位数

    private boolean state;//状态，false未启用

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
