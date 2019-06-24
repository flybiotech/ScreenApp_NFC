package com.model;

import android.os.Parcel;
import android.os.Parcelable;



public class DevModel implements Parcelable {

    public enum EnumOnlineState {
        Unkown,Online,Offline
    }


    public String sn;
    public String ip;
    public String dataport;
    public String httpport;
    public String name;
    public String usr;
    public String pwd;
    public int resolute;
    public EnumOnlineState online;

    public DevModel()
    {
        usr = "admin";
        pwd = "admin";
        resolute = 0;
        ip="";
        online = EnumOnlineState.Unkown;

    }

    protected DevModel(Parcel in) {
        sn = in.readString();
        ip = in.readString();
        dataport = in.readString();
        httpport = in.readString();
        name = in.readString();
        usr = in.readString();
        pwd = in.readString();
        resolute = in.readInt();
    }

    public static final Creator<DevModel> CREATOR = new Creator<DevModel>() {
        @Override
        public DevModel createFromParcel(Parcel in) {
            return new DevModel(in);
        }

        @Override
        public DevModel[] newArray(int size) {
            return new DevModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sn);
        dest.writeString(ip);
        dest.writeString(dataport);
        dest.writeString(httpport);
        dest.writeString(name);
        dest.writeString(usr);
        dest.writeString(pwd);
        dest.writeInt(resolute);
    }
}
