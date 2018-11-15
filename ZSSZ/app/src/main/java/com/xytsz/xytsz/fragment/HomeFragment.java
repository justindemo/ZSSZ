package com.xytsz.xytsz.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.CheckActivity;
import com.xytsz.xytsz.activity.DealActivity;
import com.xytsz.xytsz.activity.MainActivity;
import com.xytsz.xytsz.activity.PostActivity;
import com.xytsz.xytsz.activity.ReportActivity;
import com.xytsz.xytsz.activity.ReviewActivity;
import com.xytsz.xytsz.activity.SendActivity;
import com.xytsz.xytsz.base.BaseFragment;

import com.xytsz.xytsz.bean.Deal;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.bean.Road;
import com.xytsz.xytsz.global.GlobalContanstant;

import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.PermissionUtils;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by admin on 2017/1/4.
 *
 */
public class HomeFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int FAIL = 404;
    private static final int MANAGER = 111120;
    private static final int SIMPLER = 111122;
    private static final int REVIEWER = 11000;
    private static final int DEALER = 22000;
    private TextureMapView mapview;
    private BaiduMap map;
    private View mllReport;
    private View mllReview;
    private View mllDeal;
    private View mllSend;
    private View mllUncheck;
    private View mllCheck;
    private TextView mtvdealNumber;
    private TextView mtvreviewNumber;
    private TextView mtvsendNumber;
    private TextView mtvuncheckNumber;
    private TextView mtvcheckNumber;
    private LocationClient locationClient;
    private double longitude;
    private double latitude;
    public BDLocationListener myListener = new MyListener();
    private View.OnClickListener listener = new MyListener();
    private int role;
    private static final int ISLOAD = 33301;

    private PermissionUtils.PermissionGrant mPermissionGrant
            = new PermissionUtils.PermissionGrant() {

        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                //case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    locat();
                    break;
            }
        }
    };
    private int personId;

    private List<Review.ReviewRoad> reviewList;
    private List<Review.ReviewRoad> sendList;
    private List<Review.ReviewRoad> checkList;

    private int reviewNumber;
    private int sendNumber;
    private int checkNumber;
    private int dealNumber;

    private List<Integer> manageNumbers = new ArrayList<>();


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        mapview = (TextureMapView) view.findViewById(R.id.home_mv);
        mllReport = view.findViewById(R.id.ll_home_report);
        mllReview = view.findViewById(R.id.ll_home_review);
        mllDeal = view.findViewById(R.id.ll_home_deal);
        mllSend = view.findViewById(R.id.ll_home_send);
        mllUncheck = view.findViewById(R.id.ll_home_uncheck);
        mllCheck = view.findViewById(R.id.ll_home_check);
        mtvdealNumber = (TextView) view.findViewById(R.id.tv_home_deal_number);
        mtvreviewNumber = (TextView) view.findViewById(R.id.tv_home_review_number);
        mtvsendNumber = (TextView) view.findViewById(R.id.tv_home_send_number);
        mtvuncheckNumber = (TextView) view.findViewById(R.id.tv_home_unchecek_number);
        mtvcheckNumber = (TextView) view.findViewById(R.id.tv_home_check_number);
        return view;
    }

    @Override
    public void initData() {
        //获取当前登陆人的ID
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);

        //是否显示缩放按钮
        mapview.showZoomControls(false);
        //是否显示地图标尺
        mapview.showScaleControl(false);
        map = mapview.getMap();

        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);

        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        //1,纬度double latitude  2，经度longitude  116.347005,39.812991
        mllCheck.setOnClickListener(listener);
        mllReport.setOnClickListener(listener);
        mllUncheck.setOnClickListener(listener);
        mllDeal.setOnClickListener(listener);
        mllSend.setOnClickListener(listener);
        mllReview.setOnClickListener(listener);


        //如果是临时的不更新
        if (role != 10){

            getData();
        }

    }



    private void getData() {
        reviewNumber = 0;
        sendNumber = 0;
        checkNumber = 0;
        dealNumber = 0;
        manageNumbers.clear();

        new Thread() {
            @Override
            public void run() {
                try {
                    // 做判断 根据权限。
                    //上报没有
                    // 如果是管理者，
                    // 1.是否是下派者领导。
                    //2.显示下边三行
                    // 如果是处置者就显示第一行，
                    // 如果是下派领导，显示下拍和验收  先不做处理

                    String reviewData = getManageData(GlobalContanstant.GETREVIEW);
                    String sendData = getManageData(GlobalContanstant.GETSEND);
                    String checkData = getManageData(GlobalContanstant.GETCHECK);
                    Review review = JsonUtil.jsonToBean(reviewData, Review.class);
                    Review send = JsonUtil.jsonToBean(sendData, Review.class);
                    Review check = JsonUtil.jsonToBean(checkData, Review.class);

                    reviewList = review.getReviewRoadList();
                    sendList = send.getReviewRoadList();
                    checkList = check.getReviewRoadList();

                    for (Review.ReviewRoad reviewRoad : reviewList) {
                        reviewNumber += reviewRoad.getList().size();
                    }
                    manageNumbers.add(reviewNumber);
                    for (Review.ReviewRoad reviewRoad : sendList) {
                        sendNumber += reviewRoad.getList().size();
                    }
                    manageNumbers.add(sendNumber);
                    for (Review.ReviewRoad reviewRoad : checkList) {
                        checkNumber += reviewRoad.getList().size();
                    }
                    manageNumbers.add(checkNumber);


                    if (role == 1 ) {
                        Message message = Message.obtain();
                        message.what = MANAGER;
                        message.obj = manageNumbers;
                        handler.sendMessage(message);

                    }

                    if (role == 2 ) {
                        Message message = Message.obtain();
                        message.what = REVIEWER;
                        message.obj = manageNumbers;
                        handler.sendMessage(message);
                    }


                    if (role == 3 ) {
                        Message message = Message.obtain();
                        message.what = DEALER;
                        message.obj = manageNumbers;
                        handler.sendMessage(message);
                    }


                    if (role == 1 || role == 3 || role == 5) {
                        String simpleData = getSimpleData(GlobalContanstant.GETDEAL, personId);
                        Review post = JsonUtil.jsonToBean(simpleData, Review.class);
                        List<Review.ReviewRoad> reviewRoadList = post.getReviewRoadList();
                        for (Review.ReviewRoad reviewRoad : reviewRoadList) {
                            dealNumber += reviewRoad.getList().size();
                        }

                        Message message = Message.obtain();
                        message.what = SIMPLER;
                        message.obj = dealNumber;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getSimpleData(int state, int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getManagementList);
        soapObject.addProperty("PhaseIndication", state);
        soapObject.addProperty("personId", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getManagementList_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;

    }

    //根据personId 去获取
    private String getManageData(int state) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskList);
        soapObject.addProperty("PhaseIndication", state);
        soapObject.addProperty("personid", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }

    private void locat() {
        if (getActivity()!= null) {
            locationClient = new LocationClient(getActivity().getApplicationContext());
            locationClient.registerLocationListener(myListener);

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
            int span = 5 * 1000;
            option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
            option.setOpenGps(true);// 可选，默认false,设置是否使用gps
            option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
            locationClient.setLocOption(option);

            locationClient.start();
        }
    }

    private void markMe(LatLng latlng) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        MarkerOptions option = new MarkerOptions();
        //经纬度
        option.position(latlng).icon(bitmap);
        map.clear();
        map.addOverlay(option);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(getActivity(),requestCode,permissions,grantResults,mPermissionGrant);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationClient != null) {
            locationClient.start();
        }
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
        if (role != 10){
            getData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
        if(locationClient != null){
            locationClient.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        if(locationClient != null){
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case MANAGER:
                    mtvreviewNumber.setVisibility(View.VISIBLE);
                    mtvsendNumber.setVisibility(View.VISIBLE);
                    mtvcheckNumber.setVisibility(View.VISIBLE);

                    List<Integer> list = (List<Integer>) msg.obj;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) == 0) {
                            if (i == 0) {
                                mtvreviewNumber.setVisibility(View.GONE);
                            } else if (i == 1) {
                                mtvsendNumber.setVisibility(View.GONE);
                            } else if (i == 2) {
                                mtvcheckNumber.setVisibility(View.GONE);
                            }
                        } else {
                            mtvreviewNumber.setText(String.valueOf(list.get(0)));
                            mtvsendNumber.setText(String.valueOf(list.get(1)));
                            mtvcheckNumber.setText(String.valueOf(list.get(2)));
                        }
                    }

                    reviewNumber = 0;
                    sendNumber = 0;
                    checkNumber = 0;
                    break;
                case REVIEWER:
                    mtvreviewNumber.setVisibility(View.VISIBLE);
                    List<Integer> reviewlist = (List<Integer>) msg.obj;
                    for (int i = 0; i < reviewlist.size(); i++) {
                        if (reviewlist.get(i) == 0) {
                            if (i == 0) {
                                mtvreviewNumber.setVisibility(View.GONE);
                            }
                        } else {
                            mtvreviewNumber.setText(String.valueOf(reviewlist.get(0)));

                        }
                    }
                    reviewNumber = 0;
                    sendNumber = 0;
                    checkNumber = 0;
                    break;
                case DEALER:
                    mtvsendNumber.setVisibility(View.VISIBLE);
                    mtvcheckNumber.setVisibility(View.VISIBLE);

                    List<Integer> deallist = (List<Integer>) msg.obj;
                    for (int i = 0; i < deallist.size(); i++) {
                        if (deallist.get(i) == 0) {
                            if (i == 1) {
                                mtvsendNumber.setVisibility(View.GONE);
                            } else if (i == 2) {
                                mtvcheckNumber.setVisibility(View.GONE);
                            }
                        } else {
                            mtvsendNumber.setText(String.valueOf(deallist.get(1)));
                            mtvcheckNumber.setText(String.valueOf(deallist.get(2)));
                        }
                    }

                    reviewNumber = 0;
                    sendNumber = 0;
                    checkNumber = 0;
                    break;
                case SIMPLER:
                    mtvuncheckNumber.setVisibility(View.VISIBLE);
                    mtvdealNumber.setVisibility(View.VISIBLE);
                    int number = (int) msg.obj;
                    if (number == 0) {
                        mtvdealNumber.setVisibility(View.GONE);
                        mtvuncheckNumber.setVisibility(View.GONE);
                    } else {
                        mtvdealNumber.setText(String.valueOf(number));
                        mtvuncheckNumber.setText(String.valueOf(number));
                    }
                    dealNumber = 0;
                    break;

                case ISLOAD:
                    String isload = (String) msg.obj;
                    if (!isload.equals("true")){
                        ToastUtil.shortToast(getContext(),"上报位置信息失败，请检查网络");
                    }
                    break;

                case FAIL:
//                    ToastUtil.shortToast(getContext(), "数据未加载");
                    break;
            }
        }
    };


    private class MyListener implements BDLocationListener, View.OnClickListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取到经度
            longitude = bdLocation.getLongitude();
            //获取到纬度
            latitude = bdLocation.getLatitude();
            //经纬度  填的是纬度，经度
            LatLng latlng = new LatLng(latitude, longitude);
            try {
                if (map != null) {
                    map.setMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
                    markMe(latlng);
                } else {
                    ToastUtil.shortToast(getContext(), "无法定位，请检查网络");
                }
            } catch (Exception e) {


            }



        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_home_report:
                    IntentUtil.startActivity(getContext(), ReportActivity.class);
                    break;
                case R.id.ll_home_review:

                    if (role == 1||role == 2 ) {
                        IntentUtil.startActivity(getContext(), ReviewActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有审核的权限");
                    }

                    break;
                case R.id.ll_home_send:
                    //下派
                    if (role == 1 ||role == 3) {
                        IntentUtil.startActivity(getContext(), SendActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有下派的权限");
                    }

                    break;
                case R.id.ll_home_deal:
                    //处置 1，2
                    if (role == 1 || role == 3 ||role == 5 ) {
                        IntentUtil.startActivity(getContext(), DealActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有处置的权限");
                    }

                    break;
                case R.id.ll_home_uncheck:
                    //报验
                    if (role == 1 || role == 3 ||role == 5) {
                        IntentUtil.startActivity(getContext(), PostActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有报验的权限");
                    }

                    break;
                case R.id.ll_home_check:
                    //验收
                    if (role == 1||role ==3) {
                        IntentUtil.startActivity(getContext(), CheckActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有验收的权限");
                    }

                    break;
            }
        }
    }


}
