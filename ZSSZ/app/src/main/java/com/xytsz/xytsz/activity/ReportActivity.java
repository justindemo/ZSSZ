package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.Deal;
import com.xytsz.xytsz.bean.DealType;
import com.xytsz.xytsz.bean.DiseaseInformation;
import com.xytsz.xytsz.bean.DiseaseType;
import com.xytsz.xytsz.bean.FacilityName;
import com.xytsz.xytsz.bean.FacilitySpecifications;
import com.xytsz.xytsz.bean.FacilityType;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by admin on 2017/1/10.
 * 上报页面
 */
public class ReportActivity extends AppCompatActivity {

    private LocationClient locationClient;
    public BDLocationListener myListener = new MyListener();
    private ImageView mIvphoto1;
    private ImageView mIvphoto2;
    private ImageView mIvphoto3;
    private EditText mEtDesc;
    private EditText mEtName;
    private Button mbtReport;
    //图片的存储位置
    private static  final  String iconPath = "/sdcard/Image/";//图片的存储目录
    private Spinner spGrades;
    private Spinner spPbName;
    private Spinner spDep;
    private Spinner spFatype;
    private Spinner spPbtype;
    private Spinner spFaName;
    private Spinner spFaSize;
    private Spinner spRoadName;
    private Spinner spDealFatype;
    ArrayAdapter<String> gradesAdapter;
    ArrayAdapter<String> departmentAdapter;
    ArrayAdapter<String> fatypeAdapter;
    ArrayAdapter<String> pbtypeAdapter;
    ArrayAdapter<String> fanameAdapter;
    ArrayAdapter<String> fasizeAdapter;
    ArrayAdapter<String> roadnameAdapter;
    ArrayAdapter<String> pbnameAdapter;
    ArrayAdapter<String> dealtypeAdapter;
    private int fatypePosition ;
    private int dealtypePostion ;
    private DiseaseInformation diseaseInformation;
    private String reportResult;
    ReportTask reportTask = new ReportTask();
    private String isphotoSuccess1;

    private String path;
    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();
    private String taskNumber;
    private Uri fileUri;
    public  String[][][][] facilitysizes;
    public  String[][][] facilitynames;
    public  String[][][] problemtypes;
    public  String[][] facilitytypes;
    public  String[] dealtype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        try {
            String dealTypejson = Data.getJson(NetUrl.dealtypemethodName, NetUrl.dealtype_SOAP_ACTION);
            String fatypejson = Data.getJson(NetUrl.fatypemethodName, NetUrl.fatype_SOAP_ACTION);
            String pbTypejson = Data.getJson(NetUrl.problemmethodName, NetUrl.pbtype_SOAP_ACTION);
            String faNamejson = Data.getJson(NetUrl.faNamemethodName, NetUrl.faname_SOAP_ACTION);
            String faSizejson = Data.getJson(NetUrl.faSizemethodName, NetUrl.fasize_SOAP_ACTION);

            List<DealType> dealtypeList = JsonUtil.jsonToBean(dealTypejson, new TypeToken<List<DealType>>() {
            }.getType());

            List<FacilityType> fatypeList = JsonUtil.jsonToBean(fatypejson, new TypeToken<List<DealType>>() {
            }.getType());
            List<DiseaseType> pbtypeList = JsonUtil.jsonToBean(pbTypejson, new TypeToken<List<DealType>>() {
            }.getType());
            List<FacilityName> faNameList = JsonUtil.jsonToBean(faNamejson, new TypeToken<List<DealType>>() {
            }.getType());
            List<FacilitySpecifications> faSizeList = JsonUtil.jsonToBean(faSizejson, new TypeToken<List<DealType>>() {
            }.getType());

            if (dealtypeList.size() != 0 && fatypeList.size() != 0 && pbtypeList.size() != 0 && faNameList.size() != 0 && faSizeList.size() != 0 ){
                //开始添加数据
                dealtype = new String[dealtypeList.size()];
                for (int i = 0 ; i< dealtypeList.size();i++){
                    dealtype[i] = dealtypeList.get(i).getDealType_Name();
                }

                facilitytypes = new String[dealtypeList.size()][fatypeList.size()];
                //外圈的数组
                for (int i = 0; i< dealtypeList.size();i++){

                    for (int j = 0 ; j<fatypeList.size();j++){

                        int dealtype_id = fatypeList.get(j).get_dealtype_id() -1;
                        if (dealtype_id ==i) {
                            facilitytypes[i][dealtype_id] = fatypeList.get(j).get_facilitytype_name();
                        }
                    }
                }
                Log.i("faci",facilitytypes.toString());


                problemtypes = new String [dealtypeList.size()][fatypeList.size()][pbtypeList.size()];

                for (int i = 0 ; i <dealtypeList.size() ;i ++){
                    for (int j = 0; j < fatypeList.size(); j ++){
                        for (int k = 0 ; k < pbtypeList.size(); k++ ){
                            int facilitytype_id = pbtypeList.get(k).get_facilitytype_id() -1;
                            if (facilitytype_id == j){
                                problemtypes[i][j][facilitytype_id] = pbtypeList.get(k).get_diseasetype_name();
                            }
                        }
                    }
                }


                facilitynames= new String [dealtypeList.size()][fatypeList.size()][faNameList.size()];

                for (int i = 0 ; i <dealtypeList.size() ;i ++){
                    for (int j = 0; j < fatypeList.size(); j ++){
                        for (int k = 0 ; k < faNameList.size(); k++ ){
                            int facilityName_id = faNameList.get(k).get_facilityname_id() -1;
                            if (facilityName_id == j){
                                facilitynames[i][j][facilityName_id] = faNameList.get(k).get_facilityname_name();
                            }
                        }
                    }
                }


                // 设施规格

                facilitysizes = new String[dealtypeList.size()][fatypeList.size()][faNameList.size()][faSizeList.size()];
                for (int i = 0 ; i <dealtypeList.size() ;i ++){
                    for (int j = 0; j < fatypeList.size(); j ++){
                        for (int k = 0 ; k < pbtypeList.size(); k++ ){
                           for (int g = 0 ; g <faSizeList.size(); g ++){
                               int facilitySize_id = faSizeList.get(g).get_fs_id() -1;
                               if (facilitySize_id == j){
                                   facilitysizes[i][j][k][facilitySize_id] = faSizeList.get(g).get_facilityspecifications_name();
                               }
                           }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();
        initData();
    }

    private void initView() {
        spPbName = (Spinner) findViewById(R.id.sp_problemname);
        spGrades = (Spinner) findViewById(R.id.sp_grades);
        spDep = (Spinner) findViewById(R.id.sp_department);
        spFatype = (Spinner) findViewById(R.id.sp_facilitytype);
        spDealFatype = (Spinner) findViewById(R.id.sp_deal_facility);
        spPbtype = (Spinner) findViewById(R.id.sp_problemtype);
        spFaName = (Spinner) findViewById(R.id.sp_facilityname);
        spFaSize = (Spinner) findViewById(R.id.sp_facilitysize);
        spRoadName = (Spinner) findViewById(R.id.sp_roadname);
        mIvphoto1 = (ImageView) findViewById(R.id.iv_report_icon1);
        mIvphoto2 = (ImageView) findViewById(R.id.iv_report_icon2);
        mIvphoto3 = (ImageView) findViewById(R.id.iv_report_icon3);
        mEtDesc = (EditText) findViewById(R.id.problemDesc);
        mbtReport = (Button) findViewById(R.id.report);

    }

    private void initData() {
        diseaseInformation = new DiseaseInformation();

        //初始化上传图片列表
        taskNumber = getTaskNumber();
        diseaseInformation.taskNumber= taskNumber;
        locat();
        //病害名称
        pbnameAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.pbname);
        pbnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPbName.setAdapter(pbnameAdapter);
        String pbname = spPbName.getSelectedItem().toString();
        for (int i = 0; i < Data.pbname.length; i++) {
            if (Data.pbname[i].equals(pbname)) {
                diseaseInformation.level = i;
            }
        }
        //处置等级
        gradesAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.grades);
        gradesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrades.setAdapter(gradesAdapter);
        spGrades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String grade = spGrades.getSelectedItem().toString();
                for (int i = 0; i < Data.grades.length; i++) {
                    if (Data.grades[i].equals(grade)) {
                        i++;
                        diseaseInformation.disposalLevel_ID = i;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //部门
        departmentAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.departments);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDep.setAdapter(departmentAdapter);
        spDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String department = spDep.getSelectedItem().toString();
                for (int i = 0; i < Data.departments.length; i++) {
                    if (Data.departments[i].equals(department)) {
                        i++;
                        diseaseInformation.department_ID = i;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //处置设施
        dealtypeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, dealtype);
        dealtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDealFatype.setAdapter(dealtypeAdapter);

        spDealFatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //道路名称
                roadnameAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.roads);
                roadnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRoadName.setAdapter(roadnameAdapter);
                //设施类型
                fatypeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, facilitytypes[position]);
                fatypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFatype.setAdapter(fatypeAdapter);


                //拿到当前处置设施的position
                dealtypePostion = position;

                diseaseInformation.dealtype_ID = ++position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //道路信息
        roadnameAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.roads);
        roadnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoadName.setAdapter(roadnameAdapter);

        spRoadName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             /*   String roadname = spRoadName.getSelectedItem().toString();
                for (int i = 0; i < Data.roads.length; i++) {
                    if (Data.roads[i].equals(roadname)) {
                        i = i+4;
                        diseaseInformation.streetAddress_ID = i;
                    }
                }*/
                //
                position = position +4;
                diseaseInformation.streetAddress_ID =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设施类型
        fatypeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.facilitytypes[0]);
        fatypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFatype.setAdapter(fatypeAdapter);
        //病害类型
        pbtypeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.problemtypes[0][0]);
        spPbtype.setAdapter(pbtypeAdapter);
        //设施名称
        fanameAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.facilitynames[0][0]);
        spFaName.setAdapter(fanameAdapter);
        //设施规格
        fasizeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.facilitysizes[0][0][0]);
        spFaSize.setAdapter(fasizeAdapter);


        spFatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pbtypeAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.problemtypes[dealtypePostion][position]);
                pbtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPbtype.setAdapter(pbtypeAdapter);
                fanameAdapter = new ArrayAdapter<>(ReportActivity.this, android.R.layout.simple_spinner_item, Data.facilitynames[dealtypePostion][position]);
                fanameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFaName.setAdapter(fanameAdapter);
                fatypePosition = position;
                //设施类型
                diseaseInformation.facilityType_ID = ++position;
                Log.i("fa", diseaseInformation.facilityType_ID + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPbtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //病害类型
                diseaseInformation.diseaseType_ID = ++position;
                Log.i("病害类型", position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spFaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fasizeAdapter = new ArrayAdapter<>(ReportActivity.this,
                        android.R.layout.simple_spinner_item, Data.facilitysizes[dealtypePostion][fatypePosition][position]);
                fasizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFaSize.setAdapter(fasizeAdapter);
                //设施名称
                diseaseInformation.facilityName_ID = ++position;
                Log.i("设施名称", position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFaSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //设施规格
                diseaseInformation.facilitySize_ID = ++position;
                Log.i("设施规格", position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mIvphoto1.setOnClickListener(listener);
        mIvphoto2.setOnClickListener(listener);
        mIvphoto3.setOnClickListener(listener);
        mbtReport.setOnClickListener(listener);
    }

    private void locat() {
        //进入上报页面的 时候 开始定位
        locationClient = new LocationClient(this);
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

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_report_icon1:
                    //拍照
                    Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");

                    File file = new File(getPhotopath(1));
                    fileUri = Uri.fromFile(file);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(intent1, 1);
                    break;
                case R.id.iv_report_icon2:
                    //拍照
                    Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file2 = new File(getPhotopath(2));
                    fileUri = Uri.fromFile(file2);
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);


                    startActivityForResult(intent2, 2);
                    break;
                case R.id.iv_report_icon3:
                    //拍照
                    Intent intent3 = new Intent("android.media.action.IMAGE_CAPTURE");

                    File file3 = new File(getPhotopath(3));
                    fileUri = Uri.fromFile(file3);
                    intent3.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(intent3, 3);
                    //并显示到iv 上
                    break;
                case R.id.report:


                    diseaseInformation.diseaseDescription= mEtDesc.getText().toString();
                    //保存到服务器  弹吐司

                    diseaseInformation.uploadTime = getCurrentTime();
                    diseaseInformation.photoName = createPhotoName();

                    diseaseInformation.upload_Person_ID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);

                    /**
                     * 上报信息
                     */
                   if (imageBase64Strings.size() != 0) {

                       try {
                           reportTask.execute(diseaseInformation);
                       } catch (Exception e) {
                           e.printStackTrace();
                           ToastUtil.shortToast(getApplicationContext(), "网络异常，请稍后");
                       }
                       ToastUtil.shortToast(getApplicationContext(), "开始上传");
                   }else {
                       ToastUtil.shortToast(getApplicationContext(),"至少选择一张上报图片");
                       return;
                   }

                    break;
            }
        }
    };

    //上传所有的数据
    public String getRemoteInfo(DiseaseInformation diseaseInformation) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reportmethodName);
        //传递的参数
        soapObject.addProperty("Level", diseaseInformation.level);
        soapObject.addProperty("TaskNumber", diseaseInformation.taskNumber);
        soapObject.addProperty("DisposalLevel_ID", diseaseInformation.disposalLevel_ID);
        soapObject.addProperty("DealType_ID",diseaseInformation.dealtype_ID);
        soapObject.addProperty("FacilityType_ID", diseaseInformation.facilityType_ID);
        soapObject.addProperty("DiseaseType_ID", diseaseInformation.diseaseType_ID);
        soapObject.addProperty("FacilityName_ID", diseaseInformation.facilityName_ID);
        soapObject.addProperty("FacilitySpecifications_ID", diseaseInformation.facilitySize_ID);
        soapObject.addProperty("StreetAddress_ID", diseaseInformation.streetAddress_ID);
        soapObject.addProperty("DiseaseDescription", diseaseInformation.diseaseDescription);
        //上报类型
        soapObject.addProperty("Channel", diseaseInformation.channel);
        //任务阶段标识
        soapObject.addProperty("PhaseIndication", diseaseInformation.phaseIndication);
        soapObject.addProperty("UploadTime", diseaseInformation.uploadTime);
        soapObject.addProperty("Upload_Person_ID", diseaseInformation.upload_Person_ID);
        soapObject.addProperty("Department_ID", diseaseInformation.department_ID);
        soapObject.addProperty("Longitude", diseaseInformation.longitude);
        soapObject.addProperty("Latitude", diseaseInformation.latitude);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        Log.i("Soap", soapObject.toString());
        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.report_SOAP_ACTION, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        reportResult = object.getProperty(0).toString();
        Log.i("debug", reportResult);
        return reportResult;

    }


    class ReportTask extends AsyncTask<DiseaseInformation, Integer, String> {

        @Override
        protected String doInBackground(DiseaseInformation... params) {
            try {
                Log.i("tag", params[0] + "");
                reportResult = getRemoteInfo(params[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //将结果返回给onPostExecute方法
            return reportResult;
        }

        @Override
        //此方法可以在主线程改变UI
        protected void onPostExecute(String reportResult) {
            // 将WebService返回的结果显示在TextView中


            if (reportResult != null){
                if (reportResult.equals("true")) {
                    ToastUtil.shortToast(getApplicationContext(), "正在上传图片...");


                    /**
                     * 上报图片
                     */
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < fileNames.size(); i++) {
                                diseaseInformation.photoName = fileNames.get(i);
                                diseaseInformation.encode = imageBase64Strings.get(i);
                                diseaseInformation.taskNumber = taskNumber;
                                Log.i("taskNumber", diseaseInformation.taskNumber);
                                try {
                                    isphotoSuccess1 = connectWebService(diseaseInformation);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }

                                Message message = Message.obtain();
                                message.what = IS_PHOTO_SUCCESS1;
                                message.obj = isphotoSuccess1;
                                handler.sendMessage(message);
                            }

                        }
                    }.start();

                }else if (reportResult.equals("false")) {
                    ToastUtil.shortToast(getApplicationContext(), "网络出错，请稍后重试");
                }

                }

            finish();
        }
    }


    private String connectWebService(DiseaseInformation diseaseInformation) throws Exception {
        //构建初始化soapObject
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("TaskNumber", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        soapObject.addProperty("PhaseId",GlobalContanstant.GETREVIEW);
        Log.i("soapo", soapObject.toString());
        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String isphotoSuccess = object.getProperty(0).toString();
        return isphotoSuccess;
    }

    /**
     * 给拍的照片命名
     */
    public String createPhotoName() {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        return fileName;
    }


    /**
     * 获取原图片存储路径
     * @return
     * @param i
     */
    private String getPhotopath(int i) {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = "/sdcard/Image/mymy/";
        String imageName = "imageTest"+i+".jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
    }





    /**
     * 保存到本地
     */
    public String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(iconPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = iconPath + photoName;
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//把图片数据写入文件
                photo2Base64(path);
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
            ToastUtil.shortToast(getApplicationContext(), "SD卡不存在");
        }

        return photoName;
    }


    /**
     * 显示图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            //当data为空的时候，不做任何处理
            //ToastUtil.shortToast(ReportActivity.this, "请重新选择");
        //} else {
            Bitmap bitmap = null;
            if (requestCode == 0) {
                //获取从相册界面返回的缩略图
                bitmap = data.getParcelableExtra("data");
                if (bitmap == null) {//如果返回的图片不够大，就不会执行缩略图的代码，因此需要判断是否为null,如果是小图，直接显示原图即可
                    try {
                        //通过URI得到输入流
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        //通过输入流得到bitmap对象
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 1) {
                //bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = getBitmap(mIvphoto1);

                String fileName1 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto1.setImageBitmap(bitmap);
                mIvphoto1.setFocusable(false);
                String encode1 = photo2Base64(path);
                fileNames.add(fileName1);
                imageBase64Strings.add(encode1);

            } else if (requestCode == 2) {
               // bitmap = (Bitmap) data.getExtras().get("data");

                bitmap = getBitmap(mIvphoto2);
                String fileName2 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto2.setImageBitmap(bitmap);
                mIvphoto2.setFocusable(false);

                String encode2 = photo2Base64(path);

                fileNames.add(fileName2);

                imageBase64Strings.add(encode2);

            } else if (requestCode == 3) {
                //bitmap = (Bitmap) data.getExtras().get("data");

                bitmap = getBitmap(mIvphoto3);
                String fileName3 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto3.setImageBitmap(bitmap);
                mIvphoto3.setFocusable(false);

                String encode3 = photo2Base64(path);
                fileNames.add(fileName3);

                imageBase64Strings.add(encode3);

            }


        }
    }

    private Bitmap getBitmap(ImageView imageView) {
        Bitmap bitmap;
        int width = imageView.getWidth();

        int height = imageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

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

        bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                factoryOptions);
        return bitmap;
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
            String uploadBuffer = Base64.encode(baos.toByteArray())+"";
            Log.i("upload",uploadBuffer);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int IS_PHOTO_SUCCESS1 = 101001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IS_PHOTO_SUCCESS1:
                    String isPhotoSuccess = (String) msg.obj;
                    if (isPhotoSuccess != null) {
                        if (isPhotoSuccess.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "上传图片成功");
                        } else {
                            ToastUtil.shortToast(getApplicationContext(), "网络异常，请稍后重试");
                            return;
                        }
                    }
                    break;

            }
        }
    };

    private class MyListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //经度
            diseaseInformation.longitude = bdLocation.getLongitude() + "";
            //维度
            diseaseInformation.latitude = bdLocation.getLatitude() + "";

        }
    }

    //得到任务单号的方法
    private String getTaskNumber() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }

    //得到上穿时间
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }

    @Override
    protected void onStart() {
        locationClient.start();
        super.onStart();
    }

    @Override
    protected void onPause() {
        locationClient.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
