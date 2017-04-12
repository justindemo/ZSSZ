package com.xytsz.xytsz.bean;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 * 设施规格
 */
public class FacilitySpecifications {

    /**
     * m_bDirty : true
     * m_objKey :
     * subEntites : []
     * _facilityname_id : 1
     * _facilityspecifications_name : 200*400*60
     * _fs_id : 1
     * _ison : 1
     * _updatetime : /Date(1491965491853+0800)/
     */

    private boolean m_bDirty;
    private String m_objKey;
    private int _facilityname_id;
    private String _facilityspecifications_name;
    private int _fs_id;
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

    public String get_facilityspecifications_name() {
        return _facilityspecifications_name;
    }

    public void set_facilityspecifications_name(String _facilityspecifications_name) {
        this._facilityspecifications_name = _facilityspecifications_name;
    }

    public int get_fs_id() {
        return _fs_id;
    }

    public void set_fs_id(int _fs_id) {
        this._fs_id = _fs_id;
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
