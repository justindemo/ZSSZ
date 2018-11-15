package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.PersonLocationList;
import com.xytsz.xytsz.net.NetUrl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/2.
 * <p>
 * 人员位置信息
 */
public class PersonLocationInfoActivity extends AppCompatActivity {

    @Bind(R.id.person_loca)
    TextView personLoca;
    @Bind(R.id.person_track)
    TextView personTrack;
    @Bind(R.id.car_mapview)
    MapView carMapview;
    @Bind(R.id.carinfo_progressbar)
    ProgressBar carinfoProgressbar;
    @Bind(R.id.person_state)
    TextView personState;
    @Bind(R.id.person_location_message)
    TextView personLocationMessage;
    private PersonLocationList personLocationList;
    private LatLng latLng;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personlocationinfo);
        ButterKnife.bind(this);

        if (getIntent() != null){
            personLocationList = (PersonLocationList) getIntent().getSerializableExtra("personInfo");
        }
        initActionBar();
        initData();

    }

    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(personLocationList.getPersonName());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initData() {
        latLng = new LatLng(personLocationList.getLatitude(), personLocationList.getLongitude());
        carMapview.showScaleControl(false);
        carMapview.showZoomControls(true);

        BaiduMap map = carMapview.getMap();
        map.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        map.setTrafficEnabled(true);

        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);

        MarkerOptions options = new MarkerOptions();
        options.icon(descriptor).position(latLng);
        map.addOverlay(options);


        personLocationMessage.setText(personLocationList.getLocationInfo());

    }




    @OnClick({R.id.person_loca, R.id.person_track})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_track:
                Intent intent = new Intent(PersonLocationInfoActivity.this, PersonTrackActivty.class);
                intent.putExtra("personId", personLocationList.getPersonId());
                intent.putExtra("personName", personLocationList.getPersonName());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        carMapview.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        carMapview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        carMapview.onDestroy();
        super.onDestroy();

    }


}
