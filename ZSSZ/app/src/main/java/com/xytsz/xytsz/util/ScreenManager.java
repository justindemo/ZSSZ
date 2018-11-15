package com.xytsz.xytsz.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xytsz.xytsz.activity.HomeActivity;
import com.xytsz.xytsz.activity.LiveActivity;
import com.xytsz.xytsz.service.LocationService;

import java.lang.ref.WeakReference;

/**
 * Created by admin on 2018/7/4.
 * </p>
 */
public class ScreenManager {

    private Context mContext;

    private WeakReference<Activity> mActivityWref;

    public static ScreenManager gDefualt;

    public static ScreenManager getInstance(Context pContext) {
        if (gDefualt == null) {
            gDefualt = new ScreenManager(pContext.getApplicationContext());
        }
        return gDefualt;
    }
    private ScreenManager(Context pContext) {
        this.mContext = pContext;
    }

    public void setActivity(Activity pActivity) {
        mActivityWref = new WeakReference<Activity>(pActivity);
    }

    public void startActivity() {
//        isStartActivity();
        LiveActivity.actionToLiveActivity(mContext);
    }

    public void finishActivity() {
        //结束掉LiveActivity
        if (mActivityWref != null) {
            Activity activity = mActivityWref.get();
            if (activity != null) {
//
                activity.finish();
            }
        }
    }


}
