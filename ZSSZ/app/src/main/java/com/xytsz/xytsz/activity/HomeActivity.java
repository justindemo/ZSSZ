package com.xytsz.xytsz.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.bean.Deal;
import com.xytsz.xytsz.bean.DealType;
import com.xytsz.xytsz.bean.DiseaseType;
import com.xytsz.xytsz.bean.FacilityName;
import com.xytsz.xytsz.bean.FacilitySpecifications;
import com.xytsz.xytsz.bean.FacilityType;
import com.xytsz.xytsz.bean.Road;
import com.xytsz.xytsz.bean.UpdateStatus;
import com.xytsz.xytsz.bean.VersionInfo;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.fragment.HomeFragment;
import com.xytsz.xytsz.adapter.MainAdapter;
import com.xytsz.xytsz.fragment.MeFragment;
import com.xytsz.xytsz.fragment.MoreFragment;


import com.xytsz.xytsz.service.JobHandlerService;
import com.xytsz.xytsz.service.LocationBinderService;
import com.xytsz.xytsz.service.LocationService;
import com.xytsz.xytsz.ui.NoScrollViewpager;
import com.xytsz.xytsz.R;


import com.xytsz.xytsz.util.ApkUtils;
import com.xytsz.xytsz.util.HomeListener;
import com.xytsz.xytsz.util.PermissionUtils;
import com.xytsz.xytsz.util.ScreenBroadcastListener;
import com.xytsz.xytsz.util.ScreenManager;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;
import com.xytsz.xytsz.util.UpdateVersionUtil;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.microedition.khronos.opengles.GL;


/**
 * Created by admin on 2017/1/3.
 * <p>
 * 主页
 */
public class HomeActivity extends AppCompatActivity {

    private RadioGroup mRadiogroup;
    private NoScrollViewpager mViewpager;
    private ArrayList<Fragment> fragments;
    private static final int DATA_REPORT = 1155552;
    private static final int VERSIONINFO = 1144211;
    private static final int DATA_SUCCESS = 1166666;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DATA_SUCCESS:
                    mViewpager.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    rl_notonlie.setVisibility(View.GONE);
                    break;
                case DATA_REPORT:
                    mprogressbar.setVisibility(View.GONE);
                    rl_notonlie.setVisibility(View.VISIBLE);
                    break;

                case VERSIONINFO:
                    String info = (String) msg.obj;
                    if (info != null) {
                        //检查更新
                        UpdateVersionUtil.localCheckedVersion(HomeActivity.this, new UpdateVersionUtil.UpdateListener() {

                            @Override
                            public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                                //判断回调过来的版本检测状态
                                switch (updateStatus) {
                                    case UpdateStatus.YES:
                                        //弹出更新提示
                                        UpdateVersionUtil.showDialog(HomeActivity.this, versionInfo);
                                        break;
                                    case UpdateStatus.NO:
                                        //没有新版本
                                        //ToastUtil.shortToast(getContext(), "已经是最新版本了!");
                                        break;
                                    case UpdateStatus.NOWIFI:

                                        UpdateVersionUtil.showDialog(HomeActivity.this, versionInfo);
                                        //当前是非wifi网络
                                        break;
                                    case UpdateStatus.ERROR:
                                        //检测失败
                                        ToastUtil.shortToast(getApplicationContext(), "检测失败，请稍后重试！");
                                        break;
                                    case UpdateStatus.TIMEOUT:
                                        //链接超时
                                        ToastUtil.shortToast(getApplicationContext(), "链接超时，请检查网络设置!");
                                        break;
                                }
                            }
                        }, info);
                    }
                    break;
            }
        }
    };

    private RelativeLayout rl_notonlie;
    private Button mbtrefresh;
    private boolean isFive;
    private boolean isOncreate;
    private ProgressBar mprogressbar;
    private boolean isFirst;
    private HomeListener mHomeWatcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        /**
         *
         * 最后去掉注释
         */
        isFirst = SpUtils.getBoolean(getApplicationContext(), GlobalContanstant.ISFIRSTLOCATE, true);

        initView();
        if (isNetworkAvailable(getApplicationContext())) {
//            mprogressbar.setVisibility(View.VISIBLE);
//            mViewpager.setVisibility(View.GONE);
            isOncreate = true;
            mViewpager.setVisibility(View.VISIBLE);
            rl_notonlie.setVisibility(View.GONE);
            mprogressbar.setVisibility(View.GONE);

            //开启服务

//            openJobService();

           /* if (ApkUtils.isServiceRunning(HomeActivity.this,"com.xytsz.xytsz.service.LocationService")){

                Intent intent = new Intent(HomeActivity.this, LocationService.class);

                startService(intent);

//                startService(new Intent(HomeActivity.this,LocationBinderService.class));

            }
*/
//
        } else {
            rl_notonlie.setVisibility(View.VISIBLE);
            mprogressbar.setVisibility(View.GONE);

        }
        initData();
        mbtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(getApplicationContext())) {
                    mViewpager.setVisibility(View.VISIBLE);
                    rl_notonlie.setVisibility(View.GONE);
                    mprogressbar.setVisibility(View.GONE);
                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请检查网络");
                }

            }
        });
        checkUpdate();

        final ScreenManager screenManager = ScreenManager.getInstance(HomeActivity.this);
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
        if (isVisibiable){
            if (!isResume){
                screenManager.finishActivity();
            }
        }


    }



    private void checkUpdate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String versionInfo = UpdateVersionUtil.getVersionInfo();
                    Message message = Message.obtain();
                    message.obj = versionInfo;
                    message.what = VERSIONINFO;
                    handler.sendMessage(message);
                } catch (Exception e) {

                }

            }
        }.start();
    }


    private void initView() {
        mRadiogroup = (RadioGroup) findViewById(R.id.homeactivity_rg_radiogroup);
        mViewpager = (NoScrollViewpager) findViewById(R.id.homeactivity_vp);


        rl_notonlie = (RelativeLayout) findViewById(R.id.rl_notonline);
        mprogressbar = (ProgressBar) findViewById(R.id.home_progressbar);
        mbtrefresh = (Button) findViewById(R.id.btn_refresh);
        //默认显示home界面
        mRadiogroup.check(R.id.homeactivity_rbtn_home);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new HomeFragment());
        fragments.add(new MoreFragment());
        fragments.add(new MeFragment());
        //把fragment填充到viewpager

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //当界面切换完成的时候
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = (BaseFragment) fragments.get(position);
                //加载的时候可能会出错
                try {
                    fragment.initData();
                } catch (Exception e) {
                    //ToastUtil.shortToast(getApplicationContext(),"网络异常");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.homeactivity_rbtn_home:
                        mViewpager.setCurrentItem(0, false);
                        break;

                    case R.id.homeactivity_rbtn_more:
                        mViewpager.setCurrentItem(1, false);
                        break;
                    case R.id.homeactivity_rbtn_me:
                        mViewpager.setCurrentItem(2, false);
                        break;


                }
            }
        });


        PermissionUtils.requestPermission(HomeActivity.this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);


    }


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    break;
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(HomeActivity.this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    /**
     * 防止误触退出
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.shortToast(HomeActivity.this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private  boolean isVisibiable;
    private  boolean isResume ;

    @Override
    protected void onStart() {
        super.onStart();

        isVisibiable =true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        //判断是否有网络
        //是否走过
        // isoncreate
        if (!isOncreate) {
            if (isNetworkAvailable(getApplicationContext())) {
                mViewpager.setVisibility(View.VISIBLE);
                rl_notonlie.setVisibility(View.GONE);
                mprogressbar.setVisibility(View.GONE);
            } else {
                ToastUtil.shortToast(getApplicationContext(), "未连接网络");
                rl_notonlie.setVisibility(View.VISIBLE);
                mViewpager.setVisibility(View.GONE);

            }
        }
        isOncreate = false;

//        isFive = isweekfive();
//        if (isFive){
//            String content ="主人,您已经使用一周的掌上市政了,花一分钟的时间对他评价一下吧！";
//
//            new AlertDialog.Builder(this).setTitle("掌上市政评价").setMessage(content).setNegativeButton("别烦我", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    isFive = false;
//                }
//            }).setPositiveButton("好的", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    IntentUtil.startActivity(HomeActivity.this,AppraiseActivity.class);
//                    dialog.dismiss();
//                    isFive = false;
//                }
//            }).create().show();
//        }
    }

    private Boolean isweekfive() {

        final long time = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time);

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        int hour = calendar.get(Calendar.HOUR);

        int minute = calendar.get(Calendar.MINUTE);
        if (week == 6 && hour == 17) {
            return true;
        }

        return false;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();
    }


    /**
     * @param context： 上下文
     * @return 网络是否可用
     */

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }
}
