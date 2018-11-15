package com.xytsz.xytsz.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/20.
 *
 */
public class Unit implements Serializable {
    private int vectorID;
    private String info;

    public int getVectorID() {
        return vectorID;
    }

    public void setVectorID(int vectorID) {
        this.vectorID = vectorID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
