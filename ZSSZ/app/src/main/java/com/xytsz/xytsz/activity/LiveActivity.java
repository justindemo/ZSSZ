package com.xytsz.xytsz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.service.LocationService;
import com.xytsz.xytsz.util.ApkUtils;
import com.xytsz.xytsz.util.ScreenManager;

/**
 * Created by admin on 2018/7/4.
 * </p>
 */
public class LiveActivity extends AppCompatActivity {


    public static final String TAG = LiveActivity.class.getSimpleName();

    public static void actionToLiveActivity(Context pContext) {
        Intent intent = new Intent(pContext, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        //宽高设计为1个像素
        attributes.width = 1;
        attributes.height = 1;
        //起始坐标
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);

        ScreenManager.getInstance(this).setActivity(this);
        //判断流量是否关闭。
        //判断服务是否还在。不在继续打开。
      /*  if (ApkUtils.isServiceRunning(getApplicationContext(),"com.xytsz.xytsz.service.LocationService")){
            Intent intent = new Intent(LiveActivity.this, LocationService.class);
            startService(intent);
        }*/

    }
}
