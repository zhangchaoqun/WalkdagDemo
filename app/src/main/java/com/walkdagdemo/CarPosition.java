package com.walkdagdemo;

/**
 * Created by 小车的位置及小车id on 2016/7/7.
 */
public class CarPosition {

    String cId;//小车ID

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcLong() {
        return cLong;
    }

    public void setcLong(String cLong) {
        this.cLong = cLong;
    }

    public String getcLat() {
        return cLat;
    }

    public void setcLat(String cLat) {
        this.cLat = cLat;
    }

    public CarPosition(String cId, String cLong, String cLat) {

        this.cId = cId;
        this.cLong = cLong;
        this.cLat = cLat;
    }

    String cLong;//经度
    String cLat;//纬度
}
