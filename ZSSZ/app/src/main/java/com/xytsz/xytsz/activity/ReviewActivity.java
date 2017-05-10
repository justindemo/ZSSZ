package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.ReviewAdapter;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;


/**
 * Created by admin on 2017/1/11.
 * 审核页面
 */
public class ReviewActivity extends AppCompatActivity{

    private ListView mLv;

    private String json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        initView();
        //获取的是所有人员的上报信息 这里显示的就是所有的道路
        initData();


    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.review_lv);
    }

    private void initData() {


        ToastUtil.shortToast(getApplicationContext(), "正在加载数据..");

        GetTaskAsyn getTaskAsyn = new GetTaskAsyn();
        getTaskAsyn.execute(GlobalContanstant.GETREVIEW);



    }

    public static String getServiceData(int phaseIndication)throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace,NetUrl.getTaskList);
        soapObject.addProperty("PhaseIndication",phaseIndication);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION,envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        Log.i("json",json);
        return json;
    }


    class GetTaskAsyn extends AsyncTask<Integer,Integer,String>{


        @Override
        protected String doInBackground(Integer... params) {
            try {
                 json = getServiceData(params[0]);



                Log.i("result",json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            //解析json
            if (json != null) {
                //转化成bean
                Review review = JsonUtil.jsonToBean(json, Review.class);


                List<Review.ReviewRoad> list = review.getReviewRoadList();


                ReviewAdapter reviewAdapter = new ReviewAdapter(list);

                int reviewSum = 0;
                for (Review.ReviewRoad reviewRoad : list){
                    reviewSum += reviewRoad.getList().size();
                }
                SpUtils.saveInt(getApplicationContext(),GlobalContanstant.REVIEWSUM,reviewSum);

                if (reviewAdapter != null) {
                    mLv.setAdapter(reviewAdapter);
                }

                mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ReviewActivity.this, RoadActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });
            }else {
                ToastUtil.shortToast(getApplicationContext(), "未上报数据");

            }
        }
    }



}
