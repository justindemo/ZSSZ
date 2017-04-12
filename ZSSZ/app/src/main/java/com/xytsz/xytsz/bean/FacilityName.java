package com.xytsz.xytsz.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 * 设施名称
 */
public class FacilityName {


    /**
     * m_bDirty : true
     * m_objKey :
     * subEntites : []
     * _facilityname_id : 1
     * _facilityname_name : 高粱红花岗岩砖
     * _facilitytype_id : 1
     * _ison : 1
     * _updatetime : /Date(1491907068207+0800)/
     */

    private boolean m_bDirty;
    private String m_objKey;
    private int _facilityname_id;
    private String _facilityname_name;
    private int _facilitytype_id;
    private int _ison;
    private String _updatetime;
    private List<?> subEntites;

    public boolean isM_bDirty() {
        return m_bDirty;
    }

    public void setM_bDirty(boolean m_bDirty) {
        this.m_bDirty = m_bDirty;
    }

    public String getM_objKey() {
        return m_objKey;
    }

    public void setM_objKey(String m_objKey) {
        this.m_objKey = m_objKey;
    }

    public int get_facilityname_id() {
        return _facilityname_id;
    }

    public void set_facilityname_id(int _facilityname_id) {
        this._facilityname_id = _facilityname_id;
    }

    public String get_facilityname_name() {
        return _facilityname_name;
    }

    public void set_facilityname_name(String _facilityname_name) {
        this._facilityname_name = _facilityname_name;
    }

    public int get_facilitytype_id() {
        return _facilitytype_id;
    }

    public void set_facilitytype_id(int _facilitytype_id) {
        this._facilitytype_id = _facilitytype_id;
    }

    public int get_ison() {
        return _ison;
    }

    public void set_ison(int _ison) {
        this._ison = _ison;
    }

    public String get_updatetime() {
        return _updatetime;
    }

    public void set_updatetime(String _updatetime) {
        this._updatetime = _updatetime;
    }

    public List<?> getSubEntites() {
        return subEntites;
    }

    public void setSubEntites(List<?> subEntites) {
        this.subEntites = subEntites;
    }
}
