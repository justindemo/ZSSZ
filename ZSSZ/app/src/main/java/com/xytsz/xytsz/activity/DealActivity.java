package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.DealAdapter;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 * 处置界面
 * 处置界面是根据下派 来获取的需要处置的信息
 */
public class DealActivity extends AppCompatActivity {

    private ListView mLv;
    private int personID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);




        personID = SpUtils.getInt(getApplicationContext(),GlobalContanstant.PERSONID);

        initView();

        initData();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.Lv_deal);
    }

    private void initData() {

        ToastUtil.shortToast(getApplicationContext(), "正在加载数据...");

        new Thread() {
            @Override
            public void run() {
                try {

                    String dealData = getDealData(GlobalContanstant.GETDEAL,personID);
                    if (dealData != null) {

                        Review review = JsonUtil.jsonToBean(dealData, Review.class);
                        List<Review.ReviewRoad> list = review.getReviewRoadList();

                        int dealSum = 0;
                        for (Review.ReviewRoad reviewRoad : list){
                            dealSum += reviewRoad.getList().size();
                        }


                        SpUtils.saveInt(getApplicationContext(),GlobalContanstant.DEALSUM,dealSum);


                        final DealAdapter adapter = new DealAdapter(list);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter != null) {
                                    mLv.setAdapter(adapter);
                                } else {
                                    ToastUtil.shortToast(getApplicationContext(), "没有已下派的数据，请稍后重试");
                                    return;
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DealActivity.this, MakerActivty.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    public static String getDealData(int phaseIndication,int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getManagementList);
        soapObject.addProperty("PhaseIndication", phaseIndication);
        soapObject.addProperty("personId", personID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getManagementList_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        Log.i("json", json);
        return json;
    }



}
