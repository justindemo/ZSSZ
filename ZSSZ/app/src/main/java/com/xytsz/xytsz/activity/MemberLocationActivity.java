package com.xytsz.xytsz.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.PersonLocation;

import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

/**
 * Created by admin on 2017/1/16.
 * 人员定位
 */
public class MemberLocationActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {

    private static final int PERSONLIST = 1111;
    private TextureMapView mMp;
    private BaiduMap baiduMap;
    private View pop;
    private TextView title;
    private MarkerOptions option;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PERSONLIST:
                    personList = (List<PersonLocation.PersonListBean>) msg.obj;

                    if (personList.size() == 0) {
                        ToastUtil.shortToast(getApplicationContext(), "没有巡查人员");
                    } else {
                        initPop();
                        draw();
                    }

                    break;
            }
        }
    };
    private List<PersonLocation.PersonListBean> personList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberloaction);
        ToastUtil.shortToast(getApplicationContext(), "正在定位...");
        initView();
        initData();
    }

    private void initView() {
        mMp = (TextureMapView) findViewById(R.id.mapview);
        baiduMap = mMp.getMap();

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pop.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });



    }

    private void initData() {
        //设置默认显示的地方  就是定位的地址
        new Thread() {
            @Override
            public void run() {
                try {
                    String json = getAllPersonLoaction();
                    if (json != null) {
                        //解析
                        PersonLocation personLocation = JsonUtil.jsonToBean(json, PersonLocation.class);
                        List<PersonLocation.PersonListBean> personList = personLocation.getPersonList();
                        Message message = Message.obtain();
                        message.what = PERSONLIST;
                        message.obj = personList;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private String getAllPersonLoaction() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getpersonLocation);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getPersonlocation_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    //画出 三种不同的颜色

    private void draw() {
        mMp.showScaleControl(false);
        //请求服务器  获取每个人的地址  long 经度

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_twinkle);

        for (int i = 0; i < personList.size(); i++) {
            option = new MarkerOptions();
            double latitude = personList.get(i).getLatitude();
            double longitude = personList.get(i).getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            //经纬度
            option.position(latLng).icon(bitmap);
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
            baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            baiduMap.addOverlay(option);
        }


        // 把事件传递给Marker覆盖物
         baiduMap.setOnMarkerClickListener(this);

    }

    private void initPop() {
        pop = View.inflate(this, R.layout.pop, null);
        title = (TextView) pop.findViewById(R.id.title);
        pop.setVisibility(View.INVISIBLE);

        double latitude = personList.get(0).getLatitude();
        double longitude = personList.get(0).getLongitude();
        LatLng latLng = new LatLng(latitude,longitude);


        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(latLng)// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .build();
        mMp.addView(pop, param);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMp.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMp.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMp.onDestroy();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        pop.setVisibility(View.VISIBLE);
        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(marker.getPosition())// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-10)
                .build();
        mMp.updateViewLayout(pop, param);

        return true;
    }

}
