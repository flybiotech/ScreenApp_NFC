package com.screening.model;

/**
 * Created by zhangbin on 2018/4/25.
 * 这个类为信息列表展示数据的对象，包含所有展示的字段
 */

public class Addmessage {
    private String pId, name, phone, idCard;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
