package com.xytsz.xytsz.activity;




import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.bean.Deal;
import com.xytsz.xytsz.bean.DealType;
import com.xytsz.xytsz.bean.DiseaseType;
import com.xytsz.xytsz.bean.FacilityName;
import com.xytsz.xytsz.bean.FacilitySpecifications;
import com.xytsz.xytsz.bean.FacilityType;
import com.xytsz.xytsz.bean.Road;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.fragment.HelpFragment;
import com.xytsz.xytsz.fragment.HomeFragment;
import com.xytsz.xytsz.adapter.MainAdapter;
import com.xytsz.xytsz.fragment.MeFragment;
import com.xytsz.xytsz.fragment.MoreFragment;

import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.ui.NoScrollViewpager;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.fragment.SettingFragment;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/3.
 *
 * 主页
 */
public class HomeActivity extends AppCompatActivity {

    private RadioGroup mRadiogroup;
    private NoScrollViewpager mViewpager;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 没有登陆的时候，先登陆
         */
        String loginId = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);

        SDKInitializer.initialize(getApplicationContext());
        if (loginId == null || TextUtils.isEmpty(loginId)){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        setContentView(R.layout.activity_home);
        /**
         * 最后去掉注释
         */

        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_purchase:

                //市政采购平台
                IntentUtil.startActivity(this,PurchaseActivity.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        mRadiogroup = (RadioGroup) findViewById(R.id.homeactivity_rg_radiogroup);
        mViewpager = (NoScrollViewpager) findViewById(R.id.homeactivity_vp);
        //默认显示home界面
        mRadiogroup.check(R.id.homeactivity_rbtn_home);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new HelpFragment());
        fragments.add(new MoreFragment());
        fragments.add(new MeFragment());
        fragments.add(new SettingFragment());
        //把fragment填充到viewpager

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            //当界面切换完成的时候
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = (BaseFragment) fragments.get(position);
                //加载的时候可能会出错
                fragment.initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.homeactivity_rbtn_home:
                        mViewpager.setCurrentItem(0,false);
                        break;
                    case R.id.homeactivity_rbtn_help:
                        mViewpager.setCurrentItem(1,false);
                        break;
                    case R.id.homeactivity_rbtn_more:
                        mViewpager.setCurrentItem(2,false);
                        break;
                    case R.id.homeactivity_rbtn_me:
                        mViewpager.setCurrentItem(3,false);
                        break;
                    case R.id.homeactivity_rbtn_setting:
                        mViewpager.setCurrentItem(4,false);
                        break;

                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)

        {

            // 创建退出对话框

            AlertDialog.Builder isExit = new AlertDialog.Builder(this);

            // 设置对话框标题

            isExit.setTitle("系统提示");

            // 设置对话框消息

            isExit.setMessage("确定要退出吗");

            // 添加选择按钮并注册监听

            isExit.setPositiveButton("确定", listener);

            isExit.setNegativeButton("取消", listener);

            // 显示对话框

            isExit.show();


        }

        return false;
    }


    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()

    {

        public void onClick(DialogInterface dialog, int which)

        {

            switch (which)

            {

                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

                    finish();

                    break;

                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框

                    break;

                default:

                    break;

            }

        }

    };
    private String getJson(String method, String soap_action) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(soap_action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }



    @Override
    protected void onStart() {
        super.onStart();


    }
}
