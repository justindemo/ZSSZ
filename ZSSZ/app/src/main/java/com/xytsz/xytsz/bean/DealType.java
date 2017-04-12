package com.xytsz.xytsz.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 * 处置类型
 */
public class DealType {


    /**
     * DealType_Name : 道路巡察
     * Dirty : true
     * ID : 1
     * Key :
     * SubEntities : []
     */

    private String DealType_Name;
    private boolean Dirty;
    private int ID;
    private String Key;
    private List<?> SubEntities;

    public String getDealType_Name() {
        return DealType_Name;
    }

    public void setDealType_Name(String DealType_Name) {
        this.DealType_Name = DealType_Name;
    }

    public boolean isDirty() {
        return Dirty;
    }

    public void setDirty(boolean Dirty) {
        this.Dirty = Dirty;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public List<?> getSubEntities() {
        return SubEntities;
    }

    public void setSubEntities(List<?> SubEntities) {
        this.SubEntities = SubEntities;
    }
}
