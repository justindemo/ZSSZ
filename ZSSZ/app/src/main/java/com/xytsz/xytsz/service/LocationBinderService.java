package com.xytsz.xytsz.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.aidl.ProcessService;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.ApkUtils;
import com.xytsz.xytsz.util.LogUtils;
import com.xytsz.xytsz.util.ScreenBroadcastListener;
import com.xytsz.xytsz.util.ScreenManager;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by admin on 2018/7/12.
 * </p>
 */
public class LocationBinderService extends Service {

    private MyBinder binder;
    private ServiceConnection conn;
    private static final int ISLOAD = 33001;
    private LocationClient locationClient;
    public MyListener myListener = new MyListener();
    private String longitude;
    private String latitude;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case ISLOAD:
                    String isload = (String) msg.obj;
                    if (!isload.equals("true")){
                        ToastUtil.shortToast(getApplicationContext(),"上报位置信息失败，请检查网络");
                    }

                    if (myWeakLock != null){
                        myWeakLock.releaseWakeLock();
                    }
                    break;
                case 1:
                    roll();
                    if (latitude != null && longitude != null){
                        if (!latitude.isEmpty() && !longitude.isEmpty()){
                            UploadLocation();
                        }
                    }

                    break;

            }
        }
    };

    private MyWeakLock myWeakLock;

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            /**
             * 此处执行任务
             * */

            myWeakLock = new MyWeakLock(LocationBinderService.this);
            //唤醒屏幕
            if (myWeakLock != null){
                myWeakLock.acquireKeyboradWakeLock();
            }
            handler.sendEmptyMessage(1);
            handler.postDelayed(this, 10*1000);//延迟10秒,再次执行task本身,实现了循环的效果

        }
    };

    private int personID;
    private static final int NOTIFICATION_ID = 0x11;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        init();

        //API 18以下，直接发送Notification并将其置为前台
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            //API 18以上，发送Notification并将其置为前台后，启动InnerService
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(NOTIFICATION_ID, builder.build());
            startService(new Intent(this, InnerService.class));
        }

    }


    public class InnerService extends  Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        @Override
        public void onCreate() {
            super.onCreate();

            //发送与KeepLiveService中ID相同的Notification，然后将其取消并取消自己的前台显示
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(NOTIFICATION_ID, builder.build());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopForeground(true);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFICATION_ID);
                    stopSelf();
                }
            },100);



        }
    }


    private void init() {
        if (conn == null) {
            conn = new MyConnection();
        }
        binder = new MyBinder();
    }

    class MyBinder extends ProcessService.Stub{

        @Override
        public String getName() throws RemoteException {
            return "绑定";
        }
    }
    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName nme) {

            LocationBinderService.this.startService(new Intent(LocationBinderService.this,
                    LocationService.class));
            LocationBinderService.this.bindService(new Intent(LocationBinderService.this,
                    LocationService.class), conn, Context.BIND_IMPORTANT);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //如果locationservice 没有运行
        if (ApkUtils.isServiceRunning(getApplicationContext(),
                "com.xytsz.xytsz.service.LocationService")){
            handler.postDelayed(task,10000);

            final ScreenManager screenManager = ScreenManager.getInstance(LocationBinderService.this);
            ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
            listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
                @Override
                public void onScreenOn() {

                    screenManager.finishActivity();
                }
                @Override
                public void onScreenOff() {

                    screenManager.startActivity();
                }


            });
        }

        return START_STICKY;
    }


    private void  roll(){
        locat();
    }

    private void locat() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(10000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);
        locationClient.start();
    }



    private class MyListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //经度
            longitude = bdLocation.getLongitude() +"";
            //维度
            latitude = bdLocation.getLatitude()+"";



        }
    }

    private void UploadLocation() {
        //发送数据到服务器
        new Thread(){
            @Override
            public void run() {

                try {
                    LogUtils.init(LocationBinderService.this);
                    Log.d("locate","locate:"+"start");
                    LogUtils.d("locate","start");
                    String isLoad = toUpLoadLocation(personID, latitude, longitude);
                    LogUtils.d("locate",isLoad);
                    Message message = Message.obtain();
                    message.what = ISLOAD;
                    message.obj = isLoad;
                    handler.sendMessage(message);

                } catch (Exception e) {

                }

            }
        }.start();
    }

    private String toUpLoadLocation(int personID, String latitude, String longitude) throws  Exception{

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace,NetUrl.uploadLocationmethodName);
        soapObject.addProperty("personId",personID);
        soapObject.addProperty("latitude",latitude);
        soapObject.addProperty("longitude",longitude);

        SoapSerializationEnvelope envelope  =new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.bodyOut = soapObject;
        envelope.dotNet= true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.toUploadlocation_SOAP_ACTION,envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        Log.d("locate","result:"+result);
        return result;
    }

}
