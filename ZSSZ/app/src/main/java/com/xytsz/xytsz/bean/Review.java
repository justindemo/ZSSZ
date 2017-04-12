package com.xytsz.xytsz.bean;


import java.io.Serializable;
import java.util.List;


/**
 * Created by admin on 2017/3/14.
 */
public class Review implements Serializable {
    private List<ReviewRoad> reviewRoadList;

    public List<ReviewRoad> getReviewRoadList() {
        return reviewRoadList;
    }

    public void setReviewRoadList(List<ReviewRoad> reviewRoadList) {
        this.reviewRoadList = reviewRoadList;
    }


    public static class ReviewRoad implements Serializable {
        /**
         * streetId : 1
         * list : [{"DI_ID":1,"TaskNumber":"20170316164153","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"20170316164153","Channel":1,"PhaseIndication":1,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":1,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.343208","Latitude":"39.810088","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":2,"TaskNumber":"123035256","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"123035256","Channel":1,"PhaseIndication":1,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":1,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"133.33333","Latitude":"26.33333","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":35,"TaskNumber":"20170320104229","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"","Channel":0,"PhaseIndication":0,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":0,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.343215","Latitude":"39.81007","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":36,"TaskNumber":"20170320104351","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"","Channel":0,"PhaseIndication":0,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":0,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.34317","Latitude":"39.810115","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":37,"TaskNumber":"20170320105621","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"","Channel":0,"PhaseIndication":0,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":0,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.343192","Latitude":"39.810077","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":38,"TaskNumber":"20170320155115","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"","Channel":0,"PhaseIndication":0,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":0,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.343405","Latitude":"39.809799","node":0,"Dirty":true,"Key":"","SubEntities":[]},{"DI_ID":39,"TaskNumber":"20170320170003","DisposalLevel_ID":1,"FacilityType_ID":1,"DiseaseType_ID":1,"FacilityName_ID":1,"FacilitySpecifications_ID":1,"StreetAddress_ID":1,"AddressDescription":"","DiseaseDescription":"","Channel":0,"PhaseIndication":0,"UploadTime":"2017/3/15 23:14:15","Upload_Person_ID":0,"Issued_Person_ID":0,"IssuedTime":null,"RequirementsComplete_Person_ID":0,"RequirementsCompleteTime":null,"ActualCompletion_Person_ID":0,"ActualCompletionTime":null,"ActualCompletionInfo":"","Department_ID":1,"Longitude":"116.343408","Latitude":"39.809809","node":0,"Dirty":true,"Key":"","SubEntities":[]}]
         */

        private int streetId;
        private List<ReviewRoadDetail> list;

        public int getStreetId() {
            return streetId;
        }

        public void setStreetId(int streetId) {
            this.streetId = streetId;
        }

        public List<ReviewRoadDetail> getList() {
            return list;
        }

        public void setList(List<ReviewRoadDetail> list) {
            this.list = list;
        }

        public static class ReviewRoadDetail implements Serializable {
            /**
             * DI_ID : 1
             * TaskNumber : 20170316164153
             * //处置等级
             * DisposalLevel_ID : 1
             * FacilityType_ID : 1
             * DiseaseType_ID : 1
             * FacilityName_ID : 1
             * FacilitySpecifications_ID : 1
             * StreetAddress_ID : 1
             * AddressDescription :
             * DiseaseDescription : 20170316164153
             * Channel : 1
             * PhaseIndication : 1
             * UploadTime : 2017/3/15 23:14:15
             * Upload_Person_ID : 1
             * //审核人员
             * Issued_Person_ID : 0
             * //审核时间
             * IssuedTime : null
             * //要去完成人
             *
             * RequirementsComplete_Person_ID : 0
             * RequirementsCompleteTime : null
             * ActualCompletion_Person_ID : 0
             * ActualCompletionTime : null
             * //维修情况
             * ActualCompletionInfo :
             * Department_ID : 1
             * Longitude : 116.343208
             * Latitude : 39.810088
             * node : 0
             * Dirty : true
             * Key :
             * SubEntities : []
             */

            private int DI_ID;
            private int Level;
            private String TaskNumber;
            private int DisposalLevel_ID;
            private int FacilityType_ID;
            private int DiseaseType_ID;
            private int FacilityName_ID;
            private int FacilitySpecifications_ID;
            private int StreetAddress_ID;
            private String AddressDescription;
            private String DiseaseDescription;
            private int Channel;
            private int PhaseIndication;
            private String UploadTime;
            private int Upload_Person_ID;
            private int Issued_Person_ID;
            private Object IssuedTime;
            private int RequirementsComplete_Person_ID;
            private Object RequirementsCompleteTime;
            private int ActualCompletion_Person_ID;
            private Object ActualCompletionTime;
            private String ActualCompletionInfo;
            private int Department_ID;
            private String Longitude;
            private String Latitude;
            private Object postedTime;
            private Object DealedTime;
            private int node;
            private boolean Dirty;
            private String Key;
            private String sendPerson;
            private String requestTime;
            private String bitmapUrl;
            private boolean isCheck;
            private boolean isShow;


            private List<?> SubEntities;



            public String getBitmapUrl() {
                return bitmapUrl;
            }

            public void setBitmapUrl(String bitmapUrl) {
                this.bitmapUrl = bitmapUrl;
            }

            public int getDI_ID() {
                return DI_ID;
            }

            public void setDI_ID(int DI_ID) {
                this.DI_ID = DI_ID;
            }

            public String getTaskNumber() {
                return TaskNumber;
            }

            public void setTaskNumber(String TaskNumber) {
                this.TaskNumber = TaskNumber;
            }

            public int getDisposalLevel_ID() {
                return DisposalLevel_ID;
            }

            public void setDisposalLevel_ID(int DisposalLevel_ID) {
                this.DisposalLevel_ID = DisposalLevel_ID;
            }

            public int getFacilityType_ID() {
                return FacilityType_ID;
            }

            public void setFacilityType_ID(int FacilityType_ID) {
                this.FacilityType_ID = FacilityType_ID;
            }

            public int getDiseaseType_ID() {
                return DiseaseType_ID;
            }

            public void setDiseaseType_ID(int DiseaseType_ID) {
                this.DiseaseType_ID = DiseaseType_ID;
            }

            public int getFacilityName_ID() {
                return FacilityName_ID;
            }

            public void setFacilityName_ID(int FacilityName_ID) {
                this.FacilityName_ID = FacilityName_ID;
            }

            public int getFacilitySpecifications_ID() {
                return FacilitySpecifications_ID;
            }

            public void setFacilitySpecifications_ID(int FacilitySpecifications_ID) {
                this.FacilitySpecifications_ID = FacilitySpecifications_ID;
            }

            public int getStreetAddress_ID() {
                return StreetAddress_ID;
            }

            public void setStreetAddress_ID(int StreetAddress_ID) {
                this.StreetAddress_ID = StreetAddress_ID;
            }

            public String getAddressDescription() {
                return AddressDescription;
            }

            public void setAddressDescription(String AddressDescription) {
                this.AddressDescription = AddressDescription;
            }

            public String getDiseaseDescription() {
                return DiseaseDescription;
            }

            public void setDiseaseDescription(String DiseaseDescription) {
                this.DiseaseDescription = DiseaseDescription;
            }

            public int getChannel() {
                return Channel;
            }

            public void setChannel(int Channel) {
                this.Channel = Channel;
            }

            public int getPhaseIndication() {
                return PhaseIndication;
            }

            public void setPhaseIndication(int PhaseIndication) {
                this.PhaseIndication = PhaseIndication;
            }

            public String getUploadTime() {
                return UploadTime;
            }

            public void setUploadTime(String UploadTime) {
                this.UploadTime = UploadTime;
            }

            public int getUpload_Person_ID() {
                return Upload_Person_ID;
            }

            public void setUpload_Person_ID(int Upload_Person_ID) {
                this.Upload_Person_ID = Upload_Person_ID;
            }

            public int getIssued_Person_ID() {
                return Issued_Person_ID;
            }

            public void setIssued_Person_ID(int Issued_Person_ID) {
                this.Issued_Person_ID = Issued_Person_ID;
            }

            public Object getIssuedTime() {
                return IssuedTime;
            }

            public void setIssuedTime(Object IssuedTime) {
                this.IssuedTime = IssuedTime;
            }

            public int getRequirementsComplete_Person_ID() {
                return RequirementsComplete_Person_ID;
            }

            public void setRequirementsComplete_Person_ID(int RequirementsComplete_Person_ID) {
                this.RequirementsComplete_Person_ID = RequirementsComplete_Person_ID;
            }

            public String getRequirementsCompleteTime() {
                return RequirementsCompleteTime.toString();
            }

            public void setRequirementsCompleteTime(Object RequirementsCompleteTime) {
                this.RequirementsCompleteTime = RequirementsCompleteTime;
            }

            public int getActualCompletion_Person_ID() {
                return ActualCompletion_Person_ID;
            }

            public void setActualCompletion_Person_ID(int ActualCompletion_Person_ID) {
                this.ActualCompletion_Person_ID = ActualCompletion_Person_ID;
            }

            public String getActualCompletionTime() {
                return ActualCompletionTime.toString();
            }

            public void setActualCompletionTime(Object ActualCompletionTime) {
                this.ActualCompletionTime = ActualCompletionTime;
            }

            public String getActualCompletionInfo() {
                return ActualCompletionInfo;
            }

            public void setActualCompletionInfo(String ActualCompletionInfo) {
                this.ActualCompletionInfo = ActualCompletionInfo;
            }

            public int getDepartment_ID() {
                return Department_ID;
            }

            public void setDepartment_ID(int Department_ID) {
                this.Department_ID = Department_ID;
            }

            public double getLongitude() {
                return Double.parseDouble(Longitude);

            }

            public void setLongitude(String Longitude) {
                this.Longitude = Longitude;
            }

            public double getLatitude() {
                return Double.parseDouble(Latitude);

            }

            public void setLatitude(String Latitude) {
                this.Latitude = Latitude;
            }

            public int getNode() {
                return node;
            }

            public void setNode(int node) {
                this.node = node;
            }

            public boolean isDirty() {
                return Dirty;
            }

            public void setDirty(boolean Dirty) {
                this.Dirty = Dirty;
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

            public String getSendPerson() {
                return sendPerson;
            }

            public void setSendPerson(String sendPerson) {
                this.sendPerson = sendPerson;
            }

            public String getRequestTime() {
                return requestTime;
            }

            public void setRequestTime(String requestTime) {
                this.requestTime = requestTime;
            }

            @Override
            public String toString() {
                return "ReviewRoadDetail{" +
                        "DI_ID=" + DI_ID +
                        ", TaskNumber='" + TaskNumber + '\'' +
                        ", DisposalLevel_ID=" + DisposalLevel_ID +
                        ", FacilityType_ID=" + FacilityType_ID +
                        ", DiseaseType_ID=" + DiseaseType_ID +
                        ", FacilityName_ID=" + FacilityName_ID +
                        ", FacilitySpecifications_ID=" + FacilitySpecifications_ID +
                        ", StreetAddress_ID=" + StreetAddress_ID +
                        ", AddressDescription='" + AddressDescription + '\'' +
                        ", DiseaseDescription='" + DiseaseDescription + '\'' +
                        ", Channel=" + Channel +
                        ", PhaseIndication=" + PhaseIndication +
                        ", UploadTime='" + UploadTime + '\'' +
                        ", Upload_Person_ID=" + Upload_Person_ID +
                        ", Issued_Person_ID=" + Issued_Person_ID +
                        ", IssuedTime=" + IssuedTime +
                        ", RequirementsComplete_Person_ID=" + RequirementsComplete_Person_ID +
                        ", RequirementsCompleteTime=" + RequirementsCompleteTime +
                        ", ActualCompletion_Person_ID=" + ActualCompletion_Person_ID +
                        ", ActualCompletionTime=" + ActualCompletionTime +
                        ", ActualCompletionInfo='" + ActualCompletionInfo + '\'' +
                        ", Department_ID=" + Department_ID +
                        ", Longitude='" + Longitude + '\'' +
                        ", Latitude='" + Latitude + '\'' +
                        ", node=" + node +
                        ", Dirty=" + Dirty +
                        ", Key='" + Key + '\'' +
                        ", sendPerson='" + sendPerson + '\'' +
                        ", requestTime='" + requestTime + '\'' +
                        ", SubEntities=" + SubEntities +
                        ", bitmapUrl='" + bitmapUrl + '\'' +
                        '}';
            }


            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public boolean isShow() {
                return isShow;
            }

            public void setShow(boolean show) {
                isShow = show;
            }

            public int getLevel() {
                return Level;
            }

            public void setLevel(int level) {
                Level = level;
            }

            public String getPostedTime() {
                return postedTime.toString();
            }

            public void setPostedTime(Object postedTime) {
                this.postedTime = postedTime;
            }

            public String getDealedTime() {
                return DealedTime.toString();
            }

            public void setDealedTime(Object dealedTime) {
                DealedTime = dealedTime;
            }
        }
    }
    //道路名称+道路信息
   /* private List<ReviewRoad> reviewRoads = new ArrayList<>();

    public List<ReviewRoad> getReviewRoads() {
        return reviewRoads;
    }

    public void setReviewRoads(List<ReviewRoad> reviewRoads) {
        this.reviewRoads = reviewRoads;
    }

    public class ReviewRoad{
        private List<ReviewRoadDetail> reviewRoadDetails = new ArrayList<>();

        public List<ReviewRoadDetail> getReviewRoadDetails() {
            return reviewRoadDetails;
        }

        public void setReviewRoadDetails(List<ReviewRoadDetail> reviewRoadDetails) {
            this.reviewRoadDetails = reviewRoadDetails;
        }

        //道路名称
        private int reviewRoadname_id;

        public int getReviewRoadname_id() {
            return reviewRoadname_id;
        }

        public void setReviewRoadname_id(int reviewRoadname_id) {
            this.reviewRoadname_id = reviewRoadname_id;
        }
    }
    //道路信息
    public class ReviewRoadDetail{
        //实际完成人
        public int actualcompletion_person_id;
        //实际完成信息
        public String actualcompletioninfo;
        //地址描述
        public String addressdescription;
        //渠道
        public int channel;
        //部门
        public int department_id;
        //
        public int di_id;
        //病害描述
        public String diseasedescription;
        //病害类型
        public int diseasetype_id;
        //病害名称  一级
        public int disposallevel_id;
        //设施名称
        public int facilityname_id;
        //设施规格
        public int facilityspecifications_id;
        //设施类型
        public int facilitytype_id;
        //病害人
        public int issued_person_id;
        //经纬度
        public String latitude;
        public String longitude;

        public int node;
        //病害处置状态
        public int phaseindication;
        //要求完成人
        public int requirementscomplete_person_id;
        //街道名
        public int streetaddress_id;
        //任务名
        public String tasknumber;
        //上报人
        public int upload_person_id;
        //上报时间
        public String uploadtime;

        public boolean m_bDirty;
        public String m_objKey;
    }*/



}
