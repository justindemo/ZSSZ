package com.xytsz.xytsz.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.xytsz.xytsz.util.ApkUtils;

/**
 * Created by admin on 2018/7/12.
 * </p>
 */

@SuppressLint("NewApi")
public class JobHandlerService extends JobService{

    private JobScheduler mJobScheduler;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("locate","job"+"start");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (ApkUtils.isServiceRunning(this,"com.xytsz.xytsz.service.LocationService") ||
                ApkUtils.isServiceRunning(this,"com.xytsz.xytsz.service.LocationBinderService"))
        {
            startService(new Intent(this, LocationService.class));
            startService(new Intent(this, LocationBinderService.class));
        }

        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++,
                    new ComponentName(getPackageName(), JobHandlerService.class.getName()));

            builder.setPeriodic(10000); //每隔5秒运行一次
            builder.setRequiresCharging(true);
            builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
            builder.setRequiresDeviceIdle(true);

            if (mJobScheduler.schedule(builder.build()) <= 0) {
                //If something goes wrong
                System.out.println("工作失败");
            } else {
                System.out.println("工作成功");
            }
        }
        return START_STICKY;
    }
}
