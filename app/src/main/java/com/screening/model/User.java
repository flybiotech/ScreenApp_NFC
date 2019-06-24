package com.screening.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by dell on 2018/4/24.
 * user 表示登陆的人员，
 */

public class User extends LitePalSupport implements Parcelable {

    private String name;
    private String passWord;

    /**
     * 员工登记的信息是  state = staff
     * state 表示用户的类型，state = admin  state =normal 表示登陆的用户  state=staff 表示筛查人员的信息
     */
    private String state;

    private String doctorId;

    public User() {

    }

    private User(Builder builder) {
        this.name = builder.name;
        this.passWord = builder.passWord;
        this.state = builder.state;
        this.doctorId = builder.doctorId;
    }


    protected User(Parcel in) {
        name = in.readString();
        passWord = in.readString();
        state = in.readString();
        doctorId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(passWord);
        dest.writeString(state);
        dest.writeString(doctorId);
    }


    public static class Builder {

        private String name;
        private String passWord;
        private String state;
        private String doctorId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder pass(String pass) {
            this.passWord = pass;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder doctor(String doctorId) {
            this.doctorId = doctorId;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }


}
