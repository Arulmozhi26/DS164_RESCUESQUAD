package com.example.try2;

public class Member {

    String Title;
    String Desc;
    String Latlong;
    String url ;
    String time_stamp;
    String Track;

    public Member() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getLatlong() {
        return Latlong;
    }

    public void setLatlong(String latlong) {
        Latlong = latlong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

}
