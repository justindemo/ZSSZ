package com.xytsz.xytsz.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.SpUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/1/4.
 */
public class MeFragment extends BaseFragment {

    private static final int ISNUMBER = 11123;
    @Bind(R.id.tv_report_nume)
    TextView tvReportNume;
    @Bind(R.id.tv_deal_number)
    TextView tvDealNumber;
    private ImageView mIvicon;
    private TextView mTvLogin;
    private TextView mTvData;
    private TextView mTvReport;
    private TextView mTvDetail;
    private EditText mEtName;
    private EditText mEtDepartment;
    private EditText mEtPhone;
    public static final String ARGUMENT = "argument";
    private static String pathString = Environment.getExternalStorageDirectory().toString() + "/zzsz/icon_bitmap/" + "myicon.jpg";
    private int personID;
    private List<String> numberlist;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        mIvicon = (ImageView) view.findViewById(R.id.iv_my_icon);
        mTvLogin = (TextView) view.findViewById(R.id.tv_my_login);
        mTvData = (TextView) view.findViewById(R.id.tv_data);
        mTvReport = (TextView) view.findViewById(R.id.tv_report_nume);
        mTvDetail = (TextView) view.findViewById(R.id.tv_deal_number);
        mEtName = (EditText) view.findViewById(R.id.et_user_name);
        mEtDepartment = (EditText) view.findViewById(R.id.et_user_department);
        mEtPhone = (EditText) view.findViewById(R.id.et_user_phone);
        return view;
    }


    @Override
    public void initData() {
        mTvLogin.setText(SpUtils.getString(getContext(), GlobalContanstant.USERNAME));
        mTvLogin.setClickable(false);
        mEtName.setText(SpUtils.getString(getContext(), GlobalContanstant.USERNAME));
        mEtPhone.setText(SpUtils.getString(getContext(), GlobalContanstant.PHONE));
        int department_id = SpUtils.getInt(getContext(), GlobalContanstant.DEPARATMENT);
        mEtDepartment.setText(Data.departments[department_id]);
        mTvData.setVisibility(View.INVISIBLE);
        //icon 调用图库
        mIvicon.setOnClickListener(listener);

        String loginID = SpUtils.getString(getContext(), GlobalContanstant.LOGINID);

        String phone = SpUtils.getString(getContext(), GlobalContanstant.PHONE);
        int department_ID = SpUtils.getInt(getContext(), GlobalContanstant.DEPARATMENT);
        //第一次点进去的时候获取用户名
        String userName = SpUtils.getString(getContext(), GlobalContanstant.USERNAME);


        if (!TextUtils.isEmpty(loginID)) {



            mTvLogin.setText(userName);
            mTvLogin.setClickable(false);
        }

        //如果有数据显示
        if (!TextUtils.isEmpty(userName)) {
            mEtName.setText(userName);
            mEtPhone.setText(phone);
            mEtDepartment.setText(Data.departments[department_ID]);
        }


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
            e.printStackTrace();
        }

        return bitmap;

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_my_icon:
                    //
                    Intent intent1 = new Intent("android.intent.action.PICK");
                    intent1.setType("image/*");
                    startActivityForResult(intent1, 0);
                    break;


            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    //保存到本地
                    try {
                        saveFile(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mIvicon.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveFile(Bitmap bitmap) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString() + "/zzsz/icon_bitmap/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myIconFile = new File(path + "myicon.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.close();
        bos.flush();
    }

    @Override
    public void onStart() {
        super.onStart();
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
                case ISNUMBER:
                    numberlist = (List<String>) msg.obj;
                    if (tvReportNume != null && tvDealNumber != null) {
                        tvDealNumber.setText(numberlist.get(0));
                        tvReportNume.setText(numberlist.get(1));
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {

                try {
                    personID = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
                    String taskCountOfDealNumber = getTaskCountOfDeal(personID);
                    String taskCountOfReportNumber = getTaskCountOfReport(personID);
                    numbers.add(taskCountOfDealNumber);
                    numbers.add(taskCountOfReportNumber);


                    /*getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tvDealNumber.setText(taskCountOfDealNumber);
                            tvReportNume.setText(taskCountOfReportNumber);
                        }
                    });*/

                    Message message = Message.obtain();
                    message.obj = numbers;
                    message.what = ISNUMBER;
                    handler.sendMessage(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
