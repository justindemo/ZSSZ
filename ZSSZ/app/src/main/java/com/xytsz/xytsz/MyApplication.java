package com.xytsz.xytsz;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.bean.Deal;
import com.xytsz.xytsz.bean.DealType;
import com.xytsz.xytsz.bean.DiseaseType;
import com.xytsz.xytsz.bean.FacilityName;
import com.xytsz.xytsz.bean.FacilitySpecifications;
import com.xytsz.xytsz.bean.FacilityType;
import com.xytsz.xytsz.bean.PersonList;
import com.xytsz.xytsz.bean.Road;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2017/1/11.
 */
public class MyApplication extends Application {

    private static final int ISMEMBER = 55551;
    private List<String> personNameList = new ArrayList<>();
    private List<String> personIDList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ISMEMBER:
                    List<PersonList.PersonListBean> list = (List<PersonList.PersonListBean>) msg.obj;
                    for (PersonList.PersonListBean detail : list) {
                        personNameList.add(detail.getName());
                        personIDList.add(detail.getId() + "");
                    }
                    SpUtils.putStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST, personNameList);
                    SpUtils.putStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST, personIDList);
                    break;
            }
        }
    };
    private Vibrator mVibrator;


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化SDK
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        //获取所有人的列表 并保存

        new Thread() {
            @Override
            public void run() {

                try {
                    String personJson = getAllPersonList();
                    PersonList personList = JsonUtil.jsonToBean(personJson, PersonList.class);
                    List<PersonList.PersonListBean> memberList = personList.getPersonList();
                    Message message = Message.obtain();
                    message.obj = memberList;
                    message.what = ISMEMBER;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


        new Thread() {
            @Override
            public void run() {

                try {
                    String dealTypejson = getJson(NetUrl.dealtypemethodName, NetUrl.dealtype_SOAP_ACTION);
                    String fatypejson = getJson(NetUrl.fatypemethodName, NetUrl.fatype_SOAP_ACTION);
                    String pbTypejson = getJson(NetUrl.problemmethodName, NetUrl.pbtype_SOAP_ACTION);
                    String faNamejson = getJson(NetUrl.faNamemethodName, NetUrl.faname_SOAP_ACTION);
                    String faSizejson = getJson(NetUrl.faSizemethodName, NetUrl.fasize_SOAP_ACTION);
                    String streetjson = getJson(NetUrl.streetmethodName, NetUrl.street_SOAP_ACTION);


                    List<DealType> dealtypeList = JsonUtil.jsonToBean(dealTypejson, new TypeToken<List<DealType>>() {
                    }.getType());
                    List<FacilityType> fatypeList = JsonUtil.jsonToBean(fatypejson, new TypeToken<List<FacilityType>>() {
                    }.getType());
                    List<DiseaseType> pbtypeList = JsonUtil.jsonToBean(pbTypejson, new TypeToken<List<DiseaseType>>() {
                    }.getType());
                    List<FacilityName> faNameList = JsonUtil.jsonToBean(faNamejson, new TypeToken<List<FacilityName>>() {
                    }.getType());
                    List<FacilitySpecifications> faSizeList = JsonUtil.jsonToBean(faSizejson, new TypeToken<List<FacilitySpecifications>>() {
                    }.getType());
                    List<Road> streetList = JsonUtil.jsonToBean(streetjson, new TypeToken<List<Road>>() {
                    }.getType());


                    if (dealtypeList.size() != 0 && fatypeList.size() != 0 && pbtypeList.size() != 0 && faNameList.size() != 0 && faSizeList.size() != 0 && streetList.size() != 0) {
                        //开始添加数据
                        ArrayList<String> dealtype = new ArrayList<>();
                        for (int i = 0; i < dealtypeList.size(); i++) {
                            dealtype.add(dealtypeList.get(i).getDealTypeName());
                        }


                        Deal.dealType.addAll(dealtype);

                        String[] roads = new String[streetList.size()];
                        for (int i = 0; i < streetList.size(); i++) {
                            roads[i] = streetList.get(i).getStreetName();
                        }

                        Deal.roadS = roads;

                        //设施类型
                        //zhangyi
                        for (int i = 0; i < dealtypeList.size(); i++) {
                            ArrayList<String> strings = new ArrayList<String>();
                            for (int j = 0; j < fatypeList.size(); j++) {
                                if (dealtypeList.get(i).getID() == fatypeList.get(j).getDealTypeID()) {
                                    strings.add(fatypeList.get(j).getFacilityTypeName());
                                }
                            }
                            Deal.facilityTypes.add(strings);

                        }

                        for (int i = 0; i < fatypeList.size(); i++) {
                            Deal.selectFatype.add(fatypeList.get(i).getFacilityTypeName());
                        }

                        for (int i = 0; i < pbtypeList.size(); i++) {
                            Deal.selectPbtype.add(pbtypeList.get(i).getDiseaseType_Name());
                        }

                        for (int i = 0; i < faNameList.size(); i++) {
                            Deal.selectFaNametype.add(faNameList.get(i).getFacilityName_Name());
                        }

                        for (int i = 0; i < faSizeList.size(); i++) {
                            Deal.selectFaSizetype.add(faSizeList.get(i).getFacilitySpecifications_Name());
                        }

                        //病害类型

                        for (int i = 0; i < dealtypeList.size(); i++) {
                            ArrayList<ArrayList<String>> problemtypes = new ArrayList<ArrayList<String>>();

                            for (int j = 0; j < fatypeList.size(); j++) {
                                if (dealtypeList.get(i).getID() == fatypeList.get(j).getDealTypeID()) {

                                    ArrayList<String> list = new ArrayList<String>();
                                    for (int k = 0; k < pbtypeList.size(); k++) {

                                        if (fatypeList.get(j).getFacilityTypeID() == pbtypeList.get(k).getFacilityType_ID()) {

                                            list.add(pbtypeList.get(k).getDiseaseType_Name());
                                        }
                                    }

                                    problemtypes.add(list);
                                }
                            }
                            Deal.problemTypes.add(problemtypes);
                        }

                        //设施名称

                        for (int i = 0; i < dealtypeList.size(); i++) {
                            ArrayList<ArrayList<String>> facilitynames = new ArrayList<ArrayList<String>>();
                            for (int j = 0; j < fatypeList.size(); j++) {
                                if (dealtypeList.get(i).getID() == fatypeList.get(j).getDealTypeID()) {
                                    ArrayList<String> faNames = new ArrayList<String>();
                                    for (int k = 0; k < faNameList.size(); k++) {
                                        if (fatypeList.get(j).getFacilityTypeID() == faNameList.get(k).getFacilityType_ID()) {

                                            faNames.add(faNameList.get(k).getFacilityName_Name());
                                        }
                                    }
                                    facilitynames.add(faNames);
                                }
                            }
                            Deal.facilityNames.add(facilitynames);
                        }


                        // 设施规格
                        for (int i = 0; i < dealtypeList.size(); i++) {
                            ArrayList<ArrayList<ArrayList<String>>> facilitysizes = new ArrayList<ArrayList<ArrayList<String>>>();
                            for (int j = 0; j < fatypeList.size(); j++) {

                                if (dealtypeList.get(i).getID() == fatypeList.get(j).getDealTypeID()) {
                                    ArrayList<ArrayList<String>> faSizes = new ArrayList<ArrayList<String>>();

                                    for (int k = 0; k < faNameList.size(); k++) {
                                        if (fatypeList.get(j).getFacilityTypeID() == faNameList.get(k).getFacilityType_ID()) {
                                            ArrayList<String> faSize = new ArrayList<String>();
                                            for (int g = 0; g < faSizeList.size(); g++) {
                                                if (faNameList.get(k).getFacilityName_ID() == faSizeList.get(g).getFacilityName_ID()) {

                                                    faSize.add(faSizeList.get(g).getFacilitySpecifications_Name());

                                                }
                                            }
                                        faSizes.add(faSize);
                                        }
                                    }
                                    facilitysizes.add(faSizes);
                                }
                            }
                            Deal.facilitySizes.add(facilitysizes);
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();


    }

    private String getJson(String method, String soap_action) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(soap_action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }


    private String getAllPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPersonList_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;


    }

    public static String getAllImagUrl(String taskNumber, int phaseIndication) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);
        soapObject.addProperty("PhaseIndication", phaseIndication);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getAllImageURL_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


}
