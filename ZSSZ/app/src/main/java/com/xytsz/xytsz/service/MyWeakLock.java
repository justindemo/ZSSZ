package com.xytsz.xytsz.service;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by admin on 2018/7/9.
 * </p>
 */
public class MyWeakLock {

    private PowerManager.WakeLock mWakeLock;
    private Context mContext;

    public MyWeakLock(Context context) {
        this.mContext = context;
    }

    public void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"weakLock");
            mWakeLock.setReferenceCounted(false);
            if (mWakeLock != null) {
                mWakeLock.acquire();
                Log.e("weakLock", "get powermanager wakelock!");
            }
        }
    }

    public void acquireKeyboradWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"weakLock");
        if (mWakeLock != null) {
            mWakeLock.acquire();
            Log.e("weakLock", "get powermanager wakelock!");
        }
    }




    public void releaseWakeLock() {
        if (mWakeLock != null) {
//            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();

        }
    }
}
