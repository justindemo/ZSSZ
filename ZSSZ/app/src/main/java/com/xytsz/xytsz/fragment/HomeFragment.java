package com.xytsz.xytsz.fragment;


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
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.CheckActivity;
import com.xytsz.xytsz.activity.DealActivity;
import com.xytsz.xytsz.activity.PostActivity;
import com.xytsz.xytsz.activity.ReportActivity;
import com.xytsz.xytsz.activity.ReviewActivity;
import com.xytsz.xytsz.activity.SendActivity;
import com.xytsz.xytsz.base.BaseFragment;

import com.xytsz.xytsz.global.GlobalContanstant;

import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

/**
 * Created by admin on 2017/1/4.
 */
public class HomeFragment extends BaseFragment {


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

        /*int reviewNumber = SpUtils.getInt(getContext(), GlobalContanstant.REVIEWSUM);
        int sendNumber = SpUtils.getInt(getContext(), GlobalContanstant.SENDSUM);
        int dealNumber = SpUtils.getInt(getContext(), GlobalContanstant.DEALSUM);
        int postNumber = SpUtils.getInt(getContext(), GlobalContanstant.POSTSUM);
        int checkNumber = SpUtils.getInt(getContext(), GlobalContanstant.CHECKSUM);


        if (reviewNumber == 0 ){
            mtvreviewNumber.setVisibility(View.INVISIBLE);
        }else {
            mtvreviewNumber.setVisibility(View.VISIBLE);
            mtvreviewNumber.setText(reviewNumber+"");
        }

        if (sendNumber == 0 ){

            mtvsendNumber.setVisibility(View.INVISIBLE);
            }else {
            mtvsendNumber.setVisibility(View.VISIBLE);

            mtvsendNumber.setText(sendNumber+"");
        }

        if (dealNumber == 0 ){
            mtvdealNumber.setVisibility(View.INVISIBLE);
        }else {
            mtvdealNumber.setVisibility(View.VISIBLE);
            mtvdealNumber.setText(dealNumber+"");
        }

        if (postNumber == 0 ){
            mtvuncheckNumber.setVisibility(View.INVISIBLE);
        }else {
            mtvuncheckNumber.setVisibility(View.VISIBLE);
            mtvuncheckNumber.setText(postNumber+"");
        }

        if (checkNumber == 0 ){
            mtvcheckNumber.setVisibility(View.INVISIBLE);
        }else {
            mtvcheckNumber.setVisibility(View.VISIBLE);
            mtvcheckNumber.setText(checkNumber+"");
        }*/


        //是否显示缩放按钮
        mapview.showZoomControls(false);
        //是否显示地图标尺
        mapview.showScaleControl(false);
        map = mapview.getMap();
        locat();
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        //1,纬度double latitude  2，经度longitude  116.347005,39.812991
        mllCheck.setOnClickListener(listener);
        mllReport.setOnClickListener(listener);
        mllUncheck.setOnClickListener(listener);
        mllDeal.setOnClickListener(listener);
        mllSend.setOnClickListener(listener);
        mllReview.setOnClickListener(listener);

    }

    private void locat() {
        locationClient = new LocationClient(getContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3600 * 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);

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
    public void onStart() {
        super.onStart();
        locationClient.start();
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
        locationClient.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }


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
                e.printStackTrace();

            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_home_report:

                    IntentUtil.startActivity(getContext(), ReportActivity.class);

                    break;
                case R.id.ll_home_review:
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), ReviewActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有审核的权限");
                    }
                    //IntentUtil.startActivity(getContext(), ReviewActivity.class);

                    break;
                case R.id.ll_home_send:
                    //下派
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), SendActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有下派的权限");
                    }
                    //IntentUtil.startActivity(getContext(), SendActivity.class);
                    break;
                case R.id.ll_home_deal:
                    //处置 1，2
                    if (role == 1 || role == 2) {
                        IntentUtil.startActivity(getContext(), DealActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有处置的权限");
                    }
                    //IntentUtil.startActivity(getContext(), DealActivity.class);
                    break;
                case R.id.ll_home_uncheck:
                    //报验
                    if (role == 1 || role == 2) {
                        IntentUtil.startActivity(getContext(), PostActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有报验的权限");
                    }
                    //IntentUtil.startActivity(getContext(), PostActivity.class);
                    break;
                case R.id.ll_home_check:
                    //验收
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), CheckActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有验收的权限");
                    }
                    //IntentUtil.startActivity(getContext(), CheckActivity.class);
                    break;
            }
        }
    }


}
