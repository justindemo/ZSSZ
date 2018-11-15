package com.xytsz.xytsz.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.ForUsActivity;
import com.xytsz.xytsz.activity.MainActivity;
import com.xytsz.xytsz.activity.MyDealedActivity;
import com.xytsz.xytsz.activity.MyInformationActivity;
import com.xytsz.xytsz.activity.MyReporteActivity;
import com.xytsz.xytsz.activity.MyReviewedActivity;
import com.xytsz.xytsz.activity.MySendActivity;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.bean.UpdateStatus;
import com.xytsz.xytsz.bean.VersionInfo;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.FileUtils;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;
import com.xytsz.xytsz.util.UpdateVersionUtil;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/1/4.
 * 我的界面
 */
public class MeFragment extends BaseFragment {

    private static final int ISNUMBER = 111123;
    private static final int RESULT = 12211;
    @Bind(R.id.tv_report_nume)
    TextView tvReportNume;
    @Bind(R.id.tv_deal_number)
    TextView tvDealNumber;
    @Bind(R.id.me_information)
    TextView meInformation;
    @Bind(R.id.for_us)
    TextView forUs;
    @Bind(R.id.me_exit)
    Button meExit;
    @Bind(R.id.me_update)
    TextView meUpdate;
    @Bind(R.id.tv_send_nume)
    TextView tvSendNume;
    @Bind(R.id.ll_my_send)
    LinearLayout llMySend;

    private ImageView mIvicon;
    private TextView mTvLogin;
    private TextView mTvReviewNumber;
    private LinearLayout mLLDealed;
    private LinearLayout mLLReported;
    private LinearLayout mLLReveiwed;
    private EditText mEtName;
    private EditText mEtDepartment;
    private EditText mEtPhone;
    public static final String ARGUMENT = "argument";
    private int personID;
    private List<String> numberlist;
    private String fileName;
    private TextView tvCleanCache;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        mIvicon = (ImageView) view.findViewById(R.id.iv_my_icon);
        mTvLogin = (TextView) view.findViewById(R.id.tv_my_login);

        mTvReviewNumber = (TextView) view.findViewById(R.id.tv_review_nume);
        mLLReported = (LinearLayout) view.findViewById(R.id.ll_my_reporte);
        mLLReveiwed = (LinearLayout) view.findViewById(R.id.ll_my_review);
        mLLDealed = (LinearLayout) view.findViewById(R.id.ll_my_deal);
        tvCleanCache = (TextView) view.findViewById(R.id.me_clean_cache);
        return view;
    }


    @Override
    public void initData() {

        int roled = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
        personID = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);

        mTvLogin.setText(SpUtils.getString(getContext(), GlobalContanstant.USERNAME));
        mTvLogin.setClickable(false);

        //icon 调用图库
        mIvicon.setOnClickListener(listener);

        String loginID = SpUtils.getString(getContext(), GlobalContanstant.LOGINID);

        //第一次点进去的时候获取用户名
        String userName = SpUtils.getString(getContext(), GlobalContanstant.USERNAME);

        if (!TextUtils.isEmpty(loginID)) {
            mTvLogin.setText(userName);
            mTvLogin.setClickable(false);
        }

        if (roled == 1 || roled == 2) {
            mLLReveiwed.setVisibility(View.VISIBLE);
        }
        if(roled == 1|| roled == 3){
            llMySend.setVisibility(View.VISIBLE);
        }


        mLLReported.setOnClickListener(listener);
        mLLDealed.setOnClickListener(listener);
        mLLReveiwed.setOnClickListener(listener);
        tvCleanCache.setOnClickListener(listener);

    }

    private void getNumber() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String taskCountOfDealNumber = getTaskCountOfDeal(personID);
                    String taskCountOfReportNumber = getTaskCountOfReport(personID);
                    String taskCountOfReviewNumber = getTaskCountOfReview(personID);
                    String taskCountOfSendNumber = getTaskCountOfSend(personID);

                    numbers.clear();
                    numbers.add(taskCountOfDealNumber);
                    numbers.add(taskCountOfReportNumber);
                    numbers.add(taskCountOfReviewNumber);
                    numbers.add(taskCountOfSendNumber);


                    Message message = Message.obtain();
                    message.obj = numbers;
                    message.what = ISNUMBER;
                    handler.sendMessage(message);

                } catch (Exception e) {
                }
            }
        }.start();
    }

    private String getTaskCountOfSend(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfSend);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfSend_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();

        return number;
    }


    private String getTaskCountOfReport(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfReport);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfReport_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();

        return number;
    }

    private String getTaskCountOfDeal(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfDeal);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfDeal_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();
        return number;


    }

    private Bitmap getdiskbitmap(String pathString) {

        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {

        }

        return bitmap;

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_my_review:
                    IntentUtil.startActivity(v.getContext(), MyReviewedActivity.class);
                    break;

                case R.id.iv_my_icon:
                    //
                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1, 200);
                    break;

                case R.id.ll_my_reporte:
                    //点击跳转到自己上报的数据界面
                    IntentUtil.startActivity(MeFragment.this.getContext(), MyReporteActivity.class);
                    break;

                case R.id.ll_my_deal:
                    //点击显示自己处置界面
                    IntentUtil.startActivity(v.getContext(), MyDealedActivity.class);
                    break;

                case R.id.me_clean_cache:
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String cleanPath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                                "/Zssz/";
                        //判断这个文件夹是否存在

                        FileUtils.cleanCustomCache(cleanPath);


                        if (Build.VERSION.SDK_INT >=24){
                            FileUtils.cleanExternalCache(getContext());
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(getContext(),"清理完成");
                            }
                        },1500);
                    }
                    break;

            }
        }
    };

    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_CANCELED) {

            return;
        }


        Bitmap photo = null;
        if (data != null) {
            if (requestCode == 200) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                photo = getBitmap(mIvicon, picturePath);
                //保存到本地
                String photoName = saveToSDCard(photo);
                String encode = photo2Base64(path);
                upload(photoName, encode);
                //mIvicon.setImageBitmap(photo);


            }
        }
    }

    private String path;
    private String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Zssz/myIcon/";
    private final String pathString =Environment.getExternalStorageDirectory().getAbsolutePath()+  "/Zssz/myIcon/myicon.jpg";

    public String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createFileName();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(iconPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = iconPath + "myicon.jpg";
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//把图片数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            ToastUtil.shortToast(getActivity(), "SD卡不存在");
        }

        return photoName;
    }

    private Bitmap getBitmap(ImageView imageView, String path) {
        Bitmap bitmap;
        int width = imageView.getWidth();

        int height = imageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth / width, imageHeight
                / height);

        // Decode the image file into a Bitmap sized to fill the
        // View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path,
                factoryOptions);
        return bitmap;
    }


    /**
     * 上传头像
     *
     * @param photoName ： 照片名称
     * @param encode：   64位编码
     */
    private void upload(final String photoName, final String encode) {

        new Thread() {
            @Override
            public void run() {
                SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.uploadHeadImg);
                soapObject.addProperty("personid", personID);
                soapObject.addProperty("FileName", photoName);  //文件类型
                soapObject.addProperty("ImgBase64String", encode);   //参数2  图片字符串

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet = true;
                envelope.bodyOut = soapObject;
                envelope.setOutputSoapObject(soapObject);

                HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
                try {
                    httpTransportSE.call(NetUrl.uploadHeadImg_SOAP_ACTION, envelope);

                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String result = object.getProperty(0).toString();

                    Message message = Message.obtain();
                    message.obj = result;
                    message.what = RESULT;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }


            }
        }.start();


    }

    private String photo2Base64(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = Base64.encode(baos.toByteArray()) + "";
            Log.i("upload", uploadBuffer);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        return fileName;

    }


    @Override
    public void onStart() {
        super.onStart();

        getNumber();
        Bitmap bitmap = getdiskbitmap(pathString);
        if (bitmap != null) {
            mIvicon.setImageBitmap(bitmap);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private List<String> numbers = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getContext(), "头像设置失败");
                    break;
                case ISNUMBER:
                    numberlist = (List<String>) msg.obj;
                    if (tvReportNume != null && tvDealNumber != null) {
                        if (numberlist != null && numberlist.size() != 0){
                            tvDealNumber.setText(numberlist.get(0));
                            tvReportNume.setText(numberlist.get(1));
                            mTvReviewNumber.setText(numberlist.get(2));
                            tvSendNume.setText(numberlist.get(3));
                        }

                    }
                    break;

                case RESULT:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            ToastUtil.shortToast(getContext(), "头像设置完成");

                            Bitmap bitmap = getdiskbitmap(pathString);
                            if (bitmap != null) {
                                mIvicon.setImageBitmap(bitmap);
                            }
                        }
                    }
                    break;

                case VERSIONINFO:
                    String info = (String) msg.obj;
                    if (info != null) {
                        //检查更新
                        UpdateVersionUtil.localCheckedVersion(MeFragment.this.getActivity().getApplicationContext(), new UpdateVersionUtil.UpdateListener() {

                            @Override
                            public void onUpdateReturned(int updateStatus, final VersionInfo versionInfo) {
                                //判断回调过来的版本检测状态
                                switch (updateStatus) {
                                    case UpdateStatus.YES:
                                        //弹出更新提示
                                        UpdateVersionUtil.showDialog(MeFragment.this.getActivity(), versionInfo);
                                        break;
                                    case UpdateStatus.NO:
                                        //没有新版本
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "已经是最新版本了!");
                                        break;
                                    case UpdateStatus.NOWIFI:

                                        new AlertDialog.Builder(MeFragment.this.getActivity()).setTitle("温馨提示").setMessage("当前非wifi网络,下载会消耗手机流量!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                UpdateVersionUtil.showDialog(MeFragment.this.getActivity().getApplicationContext(), versionInfo);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();


                                        break;
                                    case UpdateStatus.ERROR:
                                        //检测失败
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "检测失败，请稍后重试！");
                                        break;
                                    case UpdateStatus.TIMEOUT:
                                        //链接超时
                                        ToastUtil.shortToast(MeFragment.this.getActivity().getApplicationContext(), "链接超时，请检查网络设置!");
                                        break;
                                }
                            }
                        }, info);
                    }
                    break;

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    private String getTaskCountOfReview(int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskCountOfReview);
        soapObject.addProperty("personId", personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTaskCountOfReview_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        String number = object.getProperty(0).toString();

        return number;

    }


    private static final int VERSIONINFO = 100211;

    @OnClick({R.id.me_information, R.id.for_us, R.id.me_exit, R.id.me_update,R.id.ll_my_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.me_update:
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

                break;

            case R.id.me_information:
                IntentUtil.startActivity(view.getContext(), MyInformationActivity.class);
                break;
            case R.id.for_us:
                IntentUtil.startActivity(getContext(), ForUsActivity.class);
                break;

            case R.id.me_exit:
                SpUtils.exit(getActivity().getApplicationContext());
                SpUtils.saveBoolean(getContext(), GlobalContanstant.ISFIRSTENTER, false);
                Intent intent1 = new Intent(MeFragment.this.getActivity(), MainActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                MeFragment.this.getActivity().finish();
                break;
            case R.id.ll_my_send:
                IntentUtil.startActivity(view.getContext(), MySendActivity.class);
                break;
        }
    }



}
