package com.xytsz.xytsz.activity;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.MyApplication;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.RoadAdapter;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;

import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/11.
 * 审核页面的二级道路页面
 */
public class RoadActivity extends AppCompatActivity {

    private static final int ISPASS = 100001;
    private static final int ISFAIL = 100002;

    private ListView mLv;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ISPASS:
                    String ispass = (String) msg.obj;
                    if (ispass.equals("true")){
                        ToastUtil.shortToast(getApplicationContext(),"审核通过");
                        gohome();
                    }
                    break;
                case ISFAIL:
                    String isfail = (String) msg.obj;
                    if (isfail.equals("true")){
                        ToastUtil.shortToast(getApplicationContext(),"未通过审核");
                    }
                    break;


            }
        }
    };
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);
        initView();
        initData();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.road_lv);

    }
    //从服务器获取当前道路的信息  所有

    private void initData() {

        ToastUtil.shortToast(getApplicationContext(), "正在加载数据..");
        //获取的数据当作 list传入构造中   ***应该传的是bean
        new Thread() {
            @Override
            public void run() {

                try {

                    String serviceData = ReviewActivity.getServiceData(GlobalContanstant.GETREVIEW);


                    if (serviceData != null) {

                        Review review = JsonUtil.jsonToBean(serviceData, Review.class);


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
                                //String list = new JSONObject(json).getJSONArray("").toString();
                                List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlLists.add(imageUrlList);
                            }

                        }

                        final RoadAdapter roadAdapter = new RoadAdapter(handler,reviewRoad,imageUrlLists);
                        if (roadAdapter != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLv.setAdapter(roadAdapter);
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




    private void gohome() {
        Intent intent = new Intent(RoadActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}