package com.xytsz.xytsz;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.xytsz.xytsz.bean.PersonList;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;

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
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ISMEMBER:
                    List<PersonList.PersonListBean> list = (List<PersonList.PersonListBean>) msg.obj;
                    for (PersonList.PersonListBean detail : list){
                        personNameList.add(detail.getName());
                        personIDList.add(detail.getId()+"");
                    }
                    SpUtils.putStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST,personNameList);
                    SpUtils.putStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST,personIDList);
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
        mVibrator = (Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
        //获取所有人的列表 并保存

        new Thread(){
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
        String result =  object.getProperty(0).toString();
        return result;
    }
}
