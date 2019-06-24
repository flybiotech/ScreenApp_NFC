package com.screening.model;

/**
 * Copyright 2019 bejson.com
 */

import java.util.List;

public class Data {

    private int status;
    private int total;
    private int sucCount;
    private int errCount;
    private List<Rets> rets;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setSucCount(int sucCount) {
        this.sucCount = sucCount;
    }

    public int getSucCount() {
        return sucCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setRets(List<Rets> rets) {
        this.rets = rets;
    }

    public List<Rets> getRets() {
        return rets;
    }

}
