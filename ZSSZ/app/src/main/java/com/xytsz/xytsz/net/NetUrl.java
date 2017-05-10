package com.xytsz.xytsz.net;

/**
 * Created by admin on 2017/2/13.
 *
 * 网络层
 */
public class NetUrl {


    public static final String SERVERURL = "http://dx.xytgps.com:10010/SZWEBSERVICE/newxytszServices.asmx";

    /**
     *命名空间
     */
    public static final String nameSpace = "http://xytsz.org/";
    /**
     * 方法名
     */
    public static final String dealtypemethodName = "getAllSZDealType";
    public static final String gradesmethodName = "getAllSZDisposalLeve";

    public static final String departmethodName = "getAllSZDepatment";
    public static final String fatypemethodName = "getAllSZFacilityType";
    public static final String problemmethodName = "getAllSZDiseaseType";
    public static final String faNamemethodName = "getAllSZFacilityName";
    public static final String faSizemethodName = "getAllSZFacilitySpecifications";
    public static final String loginmethodName = "toLogin";
    public static final String registmethodName = "toRegister";
    public static final String noticeMethodName = "getAllSZNotice";
    public static final String streetmethodName = "getAllSZStreet";
    public static final String photomethodName = "toReportImg";
    public static final String reportmethodName = "toReport";
    public static final String getTaskList = "GetTaskList";
    //获取当前人员要处置的信息
    public static final String getManagementList = "GetManagementList";
    /*
    * 去派工*/
    public static final String sendmethodName = "ToDispatching";
    /*
    * 审核*/
    public static final String reviewmethodName = "ToExamine";

    /*
    * 报验*/
    public static final String postmethodName = "ToManagement";
    /*
    * 验收*/
    public static final String checkmethodName = "ToInspection";
    /*
    * 上传位置信息到服务器*/
    public static final String uploadLocationmethodName = "toUploadLocation";

    /*
    * 获取人员位置信息*/
    public static final String getpersonLocation = "getAllPersonLocation";

    /**
     * 我的界面的
     */
    public static final String getTaskCountOfReport ="GetTaskCountOfReport";
    public static final String getTaskCountOfDeal ="GetTaskCountOfDeal";
    /**
     * 获取所有病害列表 用于病害定位
     */
    public static final String getAllTaskMethodName = "GetAllTask";
    /**
     * 获取所有的图片Url
     */
    public static final String getAllImageURLmethodName = "getAllImgUrl";
    /*
    * 方法*/
    public static final  String grade_SOAP_ACTION = "http://xytsz.org/getAllSZDisposalLeve";
    public static final  String depart_SOAP_ACTION = "http://xytsz.org/getAllSZDepatment";
    public static final  String fatype_SOAP_ACTION = "http://xytsz.org/getAllSZFacilityType";
    public static final  String pbtype_SOAP_ACTION = "http://xytsz.org/getAllSZDiseaseType";
    public static final  String faname_SOAP_ACTION = "http://xytsz.org/getAllSZFacilityName";
    public static final  String fasize_SOAP_ACTION = "http://xytsz.org/getAllSZFacilitySpecifications";
    public static final  String login_SOAP_ACTION = "http://xytsz.org/toLogin";
    public static final  String notice_SOAP_ACTION = "http://xytsz.org/getAllSZNotice";
    public static final  String street_SOAP_ACTION = "http://xytsz.org/getAllSZStreet";
    public static final  String photo_SOAP_ACTION = "http://xytsz.org/toReportImg";
    public static final  String report_SOAP_ACTION = "http://xytsz.org/toReport";
    public static final  String getTasklist_SOAP_ACTION = "http://xytsz.org/GetTaskList";
    public static final  String toExamine_SOAP_ACTION = "http://xytsz.org/ToExamine";
    public static final  String toDispatching_SOAP_ACTION = "http://xytsz.org/ToDispatching";
    public static final  String toManagement_SOAP_ACTION = "http://xytsz.org/ToManagement";
    public static final  String toInspection_SOAP_ACTION = "http://xytsz.org/ToInspection";
    public static final  String getPersonlocation_SOAP_ACTION = "http://xytsz.org/getAllPersonLocation";
    public static final  String toUploadlocation_SOAP_ACTION = "http://xytsz.org/toUploadLocation";


    public static final String getManagementList_SOAP_ACTION = "http://xytsz.org/GetManagementList";

    public static final String getTaskCountOfReport_SOAP_ACTION = "http://xytsz.org/GetTaskCountOfReport";
    public static final String getTaskCountOfDeal_SOAP_ACTION = "http://xytsz.org/GetTaskCountOfDeal";

    public static final String getAllImageURL_SOAP_ACTION = "http://xytsz.org/getAllImgUrl";

    public static final String getAllTask_SOAP_ACTION = "http://xytsz.org/GetAllTask";
    public static final String getPostImageURLmethodName = "getPostImgUrl";
    public static final String getPostImageURL_SOAP_ACTION ="http://xytsz.org/getPostImgUrl" ;
    public static final String getPreImageURLmethodName = "getPreImgUrl";
    public static final String getRngImageURLmethodName = "getRngImgUrl";
    public static final String getPreImageURLSoap_Action = "http://xytsz.org/getPreImgUrl";
    public static final String getRngImageURLSoap_Action = "http://xytsz.org/getRngImgUrl";
    public static final String getPersonList ="getPersonList";

    public static final String getPersonList_SOAP_ACTION ="http://xytsz.org/getPersonList";
    public static final String getVersionInfoMethodName = "getVersionInfo";
    public static final String getVersionInfo_SOAP_ACTION = "http://xytsz.org/getVersionInfo";
    public static final String dealtype_SOAP_ACTION = "http://xytsz.org/getAllSZDealType";
}
