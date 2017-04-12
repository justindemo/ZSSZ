package com.xytsz.xytsz.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 *
 * 设施类型
 */
public class FacilityType {

    /**
     * m_bDirty : true
     * m_objKey :
     * subEntites : []
     * _dealtype_id : 1
     * _facilitytype_id : 1
     * _facilitytype_name : 步道
     * _ison : 0
     * _updatetime : /Date(1491965581570+0800)/
     */

    private boolean m_bDirty;
    private String m_objKey;
    private int _dealtype_id;
    private int _facilitytype_id;
    private String _facilitytype_name;
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

    public int get_dealtype_id() {
        return _dealtype_id;
    }

    public void set_dealtype_id(int _dealtype_id) {
        this._dealtype_id = _dealtype_id;
    }

    public int get_facilitytype_id() {
        return _facilitytype_id;
    }

    public void set_facilitytype_id(int _facilitytype_id) {
        this._facilitytype_id = _facilitytype_id;
    }

    public String get_facilitytype_name() {
        return _facilitytype_name;
    }

    public void set_facilitytype_name(String _facilitytype_name) {
        this._facilitytype_name = _facilitytype_name;
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
