package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.PostAdapter;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;


import java.util.List;

/**
 * Created by admin on 2017/2/22.
 * 报验界面
 */
public class PostActivity extends AppCompatActivity {

    private ListView mlv;
    private int personID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mlv = (ListView) findViewById(R.id.lv_post);
        //获取当前登陆人的ID;   sp 获取
        personID = SpUtils.getInt(getApplicationContext(),GlobalContanstant.PERSONID);


        initData();
    }

    private void initData() {

        ToastUtil.shortToast(getApplicationContext(), "正在加载数据...");

        new Thread() {
            @Override
            public void run() {

                try {
                    String sendData = DealActivity.getDealData(GlobalContanstant.GETPOST,personID);
                    if (sendData != null) {
                        Review review = JsonUtil.jsonToBean(sendData, Review.class);
                        List<Review.ReviewRoad> list = review.getReviewRoadList();

                        int postSum = 0;
                        for (Review.ReviewRoad reviewRoad : list){
                            postSum += reviewRoad.getList().size();
                        }
                        SpUtils.saveInt(getApplicationContext(),GlobalContanstant.POSTSUM,postSum);


                        final PostAdapter adapter = new PostAdapter(list);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter != null) {
                                    mlv.setAdapter(adapter);
                                } else {
                                    ToastUtil.shortToast(getApplicationContext(), "没有下派的数据，请稍后重试");

                                }
                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PostActivity.this, PostRoadActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}
