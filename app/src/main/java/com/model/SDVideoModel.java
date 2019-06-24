package com.model;

/**
 * Created by gyl1 on 3/30/17.
 */

public class SDVideoModel {
    public String sdVideo; // 视频的路径   /sd/20170802/80003016_20170802_144821.mp4
    public String url;
    public boolean checked;
    public boolean viewed;
    public SDVideoModel()
    {
        checked=false;
        viewed = false;
    }
    public String getsdVideoUrl(DevModel model){
        //sdVideo = /sd/20171113/80003029_20171113_105745.mp4
        return "http://"+model.ip+":"+model.httpport+""+sdVideo;
    }
    public String getSdVideoName(){
        if (sdVideo.contains("aaaa")){
            return "aaaa.mp4";
        }
        else{
            String[] array = sdVideo.split("/");
            return array[array.length-1];
        }

    }
    public boolean isSDVideo(){
        return sdVideo.contains("mp4");
    }
}
