package com.xytsz.xytsz.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.MyApplication;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.SendRoadAdapter;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.PersonList;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/23.
 * 派发二级页面
 */
public class SendRoadActivity extends AppCompatActivity {

    private static final int ISSEND = 1000002;
    private static final int ISPERSONLIST = 111002;
    private static final int ISSENDPERSON = 1000003;
    private ListView mlv;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ISSEND:
                    String ispass = (String) msg.obj;
                    if (ispass.equals("true")){
                        ToastUtil.shortToast(getApplicationContext(),"下派成功");
                        goHome();
                    }
                    break;

                case ISSENDPERSON :
                    String userName = (String) msg.obj;
                    NotificationManager nm = (NotificationManager)getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                    Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification noti = new NotificationCompat.Builder(getApplicationContext())
                            .setTicker("任务已派发给："+userName )
                            .setContentTitle(userName)
                            .setContentText("已收到新的派发任务")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(getContentIntent())
                            //自动隐藏
                            .setAutoCancel(true)
                            .setSound(ringUri)
                            .build();
                    //id =0 =  用来定义取消的id
                    nm.notify(0, noti);
                    break;

                case ISPERSONLIST:
                    personlist = (List<PersonList.PersonListBean>) msg.obj;
                    servicePerson = new String[personlist.size()];
                    for (int i = 0; i < servicePerson.length; i++) {
                        servicePerson[i] = personlist.get(i).getName();
                    }

                    break;
            }
        }
    };

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(getApplicationContext(),2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private List<List<ImageUrl>> imageUrlReportLists = new ArrayList<>();


    private  String[] servicePerson ;
    private List<PersonList.PersonListBean> personlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendroad);
        mlv = (ListView) findViewById(R.id.lv_sendroad);
        initData();


    }


    private void initData() {

        ToastUtil.shortToast(getApplicationContext(), "正在加载数据..");


        //点击的时候请求参数
        new Thread(){
            @Override
            public void run() {

                try {
                    String allPersonList = getAllPersonList();
                    PersonList personList = JsonUtil.jsonToBean(allPersonList, PersonList.class);
                    //人员数量
                    List<PersonList.PersonListBean> list = personList.getPersonList();

                    Message message = Message.obtain();
                    message.what = ISPERSONLIST;
                    message.obj = list;
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
                    String reviewData = SendActivity.getReviewData(GlobalContanstant.GETSEND);


                    if (reviewData != null) {

                        Review review = JsonUtil.jsonToBean(reviewData, Review.class);
                        Intent intent = getIntent();
                        int position = intent.getIntExtra("position", 0);
                        Review.ReviewRoad reviewRoad = review.getReviewRoadList().get(position);
                        List<Review.ReviewRoad.ReviewRoadDetail> list = reviewRoad.getList();
                        //遍历list
                        for (Review.ReviewRoad.ReviewRoadDetail detail :list){
                            String taskNumber = detail.getTaskNumber();
                            /**
                             * 获取到图片的URl
                             */
                            String json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                            if(json != null) {
                                List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlReportLists.add(imageUrlList);

                            }

                        }



                        final SendRoadAdapter sendRoadAdapter = new SendRoadAdapter(handler,reviewRoad, imageUrlReportLists,servicePerson,personlist);
                        if (sendRoadAdapter != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mlv.setAdapter(sendRoadAdapter);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();




    }






    private String  getAllPersonList() throws Exception {
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

    private void goHome() {
        Intent intent = new Intent(SendRoadActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
