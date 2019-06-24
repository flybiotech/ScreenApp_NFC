package com.screening.model;

import org.litepal.crud.LitePalSupport;

/**
 * 医生的编号
 */
public class DoctorId extends LitePalSupport {

    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
