package com.xytsz.xytsz.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.UpdateStatus;
import com.xytsz.xytsz.bean.VersionInfo;
import com.xytsz.xytsz.net.NetUrl;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;


/**
 * Created by admin on 2017/4/6.
 *
 */
public class UpdateVersionUtil {


    private static String versionInfo;

    public static String getVersionInfo() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getVersionInfoMethodName);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getVersionInfo_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    /**
     * 接口回调
     */
    public interface UpdateListener {
        void onUpdateReturned(int updateStatus, VersionInfo versionInfo);
    }

    public UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }



    /**
     * 检测版本测试
     */
    public static void localCheckedVersion(final Context context, final UpdateListener updateListener) {

        new Thread() {
            @Override
            public void run() {
                try {
                    versionInfo = getVersionInfo();

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }.start();


        if (versionInfo != null) {
            VersionInfo mVersionInfo = JsonUtil.jsonToBean(versionInfo, VersionInfo.class);
            int clientVersionCode = ApkUtils.getVersionCode(context);
            int serverVersionCode = mVersionInfo.getVersionCode();

            //有新版本
            if (serverVersionCode != 0) {
                if (clientVersionCode < serverVersionCode) {
                    int i = NetworkUtil.checkedNetWorkType(context);
                    if (i == NetworkUtil.NOWIFI) {
                        updateListener.onUpdateReturned(UpdateStatus.NOWIFI, mVersionInfo);
                    } else if (i == NetworkUtil.WIFI) {
                        updateListener.onUpdateReturned(UpdateStatus.YES, mVersionInfo);
                    }
                } else {
                    //无新本
                    updateListener.onUpdateReturned(UpdateStatus.NO, null);
                }
            }
        }else {
            ToastUtil.shortToast(context,"检测失败，稍后重试");
        }

    }


    /**
     * 弹出新版本提示
     *
     * @param context     上下文
     * @param versionInfo 更新内容
     */
    public static void showDialog(final Context context, final VersionInfo versionInfo) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        final File file = new File("/sdcard/ZZSZ/updateVersion/zssz-app.apk");
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);

        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        tvContent.setText(versionInfo.getMessage());
        tvUpdateTile.setText("最新版本：" + versionInfo.getVersionName());


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.btn_update_id_ok) {
                    //新版本已经下载
                    if (file.exists() && file.getName().equals("zssz-app.apk")) {
                        Intent intent = ApkUtils.getInstallIntent(file);
                        context.startActivity(intent);
                    } else {


                        showDownloadDialog(context, versionInfo);

                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static void showDownloadDialog(final Context context, VersionInfo versionInfo) {
        final ProgressDialog dialog = new ProgressDialog(context);
        //下载对话框.
        dialog.setTitle("下载进度");
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//有一个进度可以展示
        dialog.setButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //home
                dialog.dismiss();
            }
        });
        dialog.show();


        HttpUtils httpUtils = new HttpUtils(5000);

        String url = versionInfo.getDownloadUrl();
        final File updateFile = new File("/sdcard/ZZSZ/updateVersion/zssz-app.apk");

        RequestCallBack<File> callback = new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

                int percent = (int) (current * 100f / total + 0.5f);
                dialog.setProgress(percent);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Intent installIntent = ApkUtils.getInstallIntent(updateFile);
                context.startActivity(installIntent);
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtil.shortToast(context, "下载出现错误!");
            }
        };
        httpUtils.download(url, updateFile.getAbsolutePath(), false, callback);

    }


}
