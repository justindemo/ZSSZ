package com.xytsz.xytsz.fragment;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.MainActivity;
import com.xytsz.xytsz.bean.UpdateStatus;
import com.xytsz.xytsz.bean.VersionInfo;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.util.UpdateVersionUtil;
import com.xytsz.xytsz.util.VersionUtil;

/**
 * Created by admin on 2017/1/4.
 *
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private Button mBtAccess;
    private Button mBtData;
    private Button mBtVersion;
    private Button mBtExit;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        mBtAccess = (Button) view.findViewById(R.id.set_getAccess);
        mBtData = (Button) view.findViewById(R.id.set_updateData);
        mBtVersion = (Button) view.findViewById(R.id.set_getVersion);
        mBtExit = (Button) view.findViewById(R.id.set_exit);
        return view;
    }

    @Override
    public void initData() {
        mBtAccess.setOnClickListener(this);
        mBtData.setOnClickListener(this);
        mBtVersion.setOnClickListener(this);
        mBtExit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_exit:
                SpUtils.exit(v.getContext());
                Intent intent = new Intent(this.getActivity(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.set_getAccess:
                ToastUtil.shortToast(getActivity(), "已获取执法资格");
                break;
            case R.id.set_updateData:
                //联网 更新数据 提示跟新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //请求数据：
                        // 刷新

                        ToastUtil.shortToast(getActivity(), "已更新数据");
                    }
                }, 3000);

                break;
            case R.id.set_getVersion:

                //检查更新
                UpdateVersionUtil.localCheckedVersion(getContext(),new UpdateVersionUtil.UpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                        //判断回调过来的版本检测状态
                        switch (updateStatus) {
                            case UpdateStatus.YES:
                                //弹出更新提示
                                UpdateVersionUtil.showDialog(getContext(),versionInfo);
                                break;
                            case UpdateStatus.NO:
                                //没有新版本
                                ToastUtil.shortToast(getContext(), "已经是最新版本了!");
                                break;
                            case UpdateStatus.NOWIFI:
                                //当前是非wifi网络
                                ToastUtil.shortToast(getContext(), "只有在wifi下更新！");
//							DialogUtils.showDialog(MainActivity.this, "温馨提示","当前非wifi网络,下载会消耗手机流量!", "确定", "取消",new DialogOnClickListenner() {
//								@Override
//								public void btnConfirmClick(Dialog dialog) {
//									dialog.dismiss();
//									//点击确定之后弹出更新对话框
//									UpdateVersionUtil.showDialog(SystemActivity.this,versionInfo);
//								}
//
//								@Override
//								public void btnCancelClick(Dialog dialog) {
//									dialog.dismiss();
//								}
//							});
                                break;
                            case UpdateStatus.ERROR:
                                //检测失败
                                ToastUtil.shortToast(getContext(), "检测失败，请稍后重试！");
                                break;
                            case UpdateStatus.TIMEOUT:
                                //链接超时
                                ToastUtil.shortToast(getContext(), "链接超时，请检查网络设置!");
                                break;
                        }
                    }
                });

                break;
        }
    }

    private void showDialog() {
        //获取版本信息 并展示 下方一个按钮 关闭
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.set_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        Button mBtDismiss = (Button) view.findViewById(R.id.set_dismiss);
        TextView mtvVersion = (TextView) view.findViewById(R.id.set_text_version);
        //拿到packagemanger  展示
        String versionName = VersionUtil.getVersionName(getContext());
        int versionCode = VersionUtil.getVersionCode(getContext());

        StringBuilder sb = new StringBuilder();
        sb.append("版本名：" + versionName + "\r\n");
        sb.append("版本号：" + versionCode);
        mtvVersion.setText(sb.toString());
        //

        mBtDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
