package com.xytsz.xytsz.bean;

import android.graphics.Bitmap;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by admin on 2017/2/15.
 */
public class Deal {

    public ArrayList<String> dealRoadNames = new ArrayList<>();
    public ArrayList<Integer> dealRoadNum = new ArrayList<>();
    public List<String> unreadnum;
    public List<DealRoadDetail> dealRoadDetail = new ArrayList<>();
    public int phaseId;

    public class DealRoadDetail {
        public String reporter;
        public Bitmap reporterIcon;
        //处理状态
        public String dealStatu;
        public String reporteTime;
        public double latitude;
        public double longitude;
        public String sendTime;
        public String sender;
        public List<Bitmap> reporteIcon = new ArrayList<>();
        public String requestTime;
        public String diseaseName;
        //处置等级
        public String grades;
        public String fatype;
        public String pbtype;
        //上报地点
        public String reporteplace;
        public String faname;
        public String faSize;

    }
}
