package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.AudioUrl;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.SoundUtil;
import com.xytsz.xytsz.util.SpUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/6/16.\
 *
 */
public class SendRoadDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_send_detail_name)
    TextView tvSendDetailName;
    @Bind(R.id.tv_send_detail_reporter)
    TextView tvSendDetailReporter;
    @Bind(R.id.tv_send_detail_diseasename)
    TextView tvSendDetailDiseasename;
    @Bind(R.id.tv_send_detail_grade)
    TextView tvSendDetailGrade;
    @Bind(R.id.tv_send_detail_dealtype)
    TextView tvSendDetailDealtype;
    @Bind(R.id.tv_send_detail_fatype)
    TextView tvSendDetailFatype;
    @Bind(R.id.tv_send_detail_pbtype)
    TextView tvSendDetailPbtype;
    @Bind(R.id.tv_send_detail_reporteplace)
    TextView tvSendDetailReporteplace;
    @Bind(R.id.tv_send_detail_faname)
    TextView tvSendDetailFaname;

    @Bind(R.id.tv_send_detail_reportetime)
    TextView tvSendDetailReportetime;
    @Bind(R.id.tv_send_detail_address)
    TextView tvSendDetailAddress;
    @Bind(R.id.iv_send_detail_photo1)
    ImageView ivSendDetailPhoto1;
    @Bind(R.id.iv_send_detail_photo2)
    ImageView ivSendDetailPhoto2;
    @Bind(R.id.iv_send_detail_photo3)
    ImageView ivSendDetailPhoto3;
    @Bind(R.id.ll_iv)
    LinearLayout llIv;
    @Bind(R.id.bt_send_detail_back)
    Button btSendDetailBack;
    @Bind(R.id.tv_send_detail_loca)
    TextView tvSendDetailLoca;
    @Bind(R.id.tv_send_problem_audio)
    TextView tvSendProblemAudio;
    @Bind(R.id.tv_send_detail_diseasedes)
    TextView tvSendDetailDiseasedes;

    private Review.ReviewRoad.ReviewRoadDetail detail;
    private List<ImageUrl> imageUrls;
    private int id;
    private AudioUrl audioUrl;
    private SoundUtil soundUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            detail = (Review.ReviewRoad.ReviewRoadDetail) getIntent().getSerializableExtra("detail");

            imageUrls = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrls");
            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");

        }
        setContentView(R.layout.activity_sendroaddetail);
        ButterKnife.bind(this);
        initAcitionbar();

        initData();
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.problem_detail);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void initData() {

        String upload_person_id = detail.getUpload_Person_ID() + "";

        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
        List<String> personNamelist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST);
        List<String> personIDlist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST);

        for (int i = 0; i < personIDlist.size(); i++) {
            if (upload_person_id.equals(personIDlist.get(i))) {
                id = i;
            }
        }

        String userName = personNamelist.get(id);
        tvSendDetailReporter.setText(userName);
        int disposalLevel_id = detail.getDisposalLevel_ID() - 1;
        int level = detail.getLevel();
        tvSendDetailDiseasename.setText(Data.pbname[level]);
        tvSendDetailGrade.setText(Data.grades[disposalLevel_id]);

        tvSendDetailFatype.setText(detail.getFacilityType_Name());
        tvSendDetailDealtype.setText(detail.getDealType_Name());

        tvSendDetailPbtype.setText(detail.getDiseaseType_Name());


        tvSendDetailReporteplace.setText(detail.getStreetAddress_Name());


        tvSendDetailFaname.setText(detail.getFacilityName_Name());


        String uploadTime = detail.getUploadTime();
        tvSendDetailReportetime.setText(uploadTime);
        tvSendDetailAddress.setText(detail.getAddressDescription());

        tvSendDetailDiseasedes.setText(detail.getDiseaseDescription());
        //点击返回的时候
        btSendDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (imageUrls.size() != 0) {
            if (imageUrls.size() == 1) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                ivSendDetailPhoto2.setVisibility(View.INVISIBLE);
                ivSendDetailPhoto2.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 2) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 3) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                Glide.with(getApplicationContext()).load(imageUrls.get(2).getImgurl()).into(ivSendDetailPhoto3);
            }
        } else {
            ivSendDetailPhoto1.setVisibility(View.VISIBLE);
            ivSendDetailPhoto2.setVisibility(View.INVISIBLE);
            ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(ivSendDetailPhoto1);
        }


        ivSendDetailPhoto1.setOnClickListener(this);
        ivSendDetailPhoto2.setOnClickListener(this);
        ivSendDetailPhoto3.setOnClickListener(this);


        if (detail.getAddressDescription().isEmpty()) {
            if (audioUrl != null) {
                if (!audioUrl.getAudiourl().equals("false")) {
                    tvSendDetailAddress.setVisibility(View.GONE);
                    tvSendProblemAudio.setVisibility(View.VISIBLE);
                    soundUtil = new SoundUtil();

                    if (!audioUrl.getTime().isEmpty()) {
                        tvSendProblemAudio.setText(audioUrl.getTime());

                        tvSendProblemAudio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


                                soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                    @Override
                                    public void onFinish() {
                                        tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                                soundUtil.play(audioUrl.getAudiourl());
                            }
                        });
                    }
                }else {
                    tvSendDetailAddress.setVisibility(View.VISIBLE);
                    tvSendProblemAudio.setVisibility(View.GONE);
                }
            } else {
                tvSendDetailAddress.setVisibility(View.VISIBLE);
                tvSendProblemAudio.setVisibility(View.GONE);
            }

        } else {
            tvSendDetailAddress.setVisibility(View.VISIBLE);
            tvSendProblemAudio.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SendRoadDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrls);
        startActivity(intent);
    }

    @OnClick(R.id.tv_send_detail_loca)
    public void onViewClicked() {
        Intent intent = new Intent(SendRoadDetailActivity.this, PositionActivity.class);
        intent.putExtra("detail", detail);
        startActivity(intent);
    }
}
