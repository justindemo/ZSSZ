package com.xytsz.xytsz.activity;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.widget.LinearLayout;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.SpUtils;

/**
 * Created by admin on 2017/1/17.
 * splash页面
 */
public class SplashAcitvity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_splash);

        LinearLayout mroot = (LinearLayout) findViewById(R.id.ll_splash_root);
        //动画效果参数直接定义
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(1000);

        mroot.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean isfirst = SpUtils.getBoolean(SplashAcitvity.this, GlobalContanstant.ISFIRSTENTER,true);
                String loginId = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);
                if (isfirst){
                    IntentUtil.startActivity(SplashAcitvity.this,GuideActivity.class);
                }else if (loginId == null || loginId.isEmpty()){
                    IntentUtil.startActivity(SplashAcitvity.this,MainActivity.class);

                }else {
                    IntentUtil.startActivity(SplashAcitvity.this,HomeActivity.class);
                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
