package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.MyApplication;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.PostRoadAdapter;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/22.
 * 报验二级菜单
 */
public class PostRoadActivity extends AppCompatActivity {

    private ListView mlv;
    private int personID;
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroad);
        mlv = (ListView) findViewById(R.id.lv_postRoad);


        //sp 获取当前登陆人的ID
        personID = SpUtils.getInt(getApplicationContext(),GlobalContanstant.PERSONID);

        initData();

    }

    private void initData() {


        new Thread() {
            @Override
            public void run() {

                try {
                    String sendData = DealActivity.getDealData(GlobalContanstant.GETPOST,personID);

                    if (sendData != null) {

                        Review review = JsonUtil.jsonToBean(sendData, Review.class);
                        Intent intent = getIntent();
                        int position = intent.getIntExtra("position", 0);
                        List<Review.ReviewRoad> reviewRoadList = review.getReviewRoadList();
                        Review.ReviewRoad reviewRoad = reviewRoadList.get(position);
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
                        final PostRoadAdapter adapter = new PostRoadAdapter(review.getReviewRoadList().get(position),imageUrlLists);
                        //主线程更新UI
                        if (adapter != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mlv.setAdapter(adapter);

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
}
