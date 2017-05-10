package com.xytsz.xytsz.activity;


import android.content.Intent;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xytsz.xytsz.bean.PersonInfo;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.R;

import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.service.LocationService;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * 登陆页面
 */
public class MainActivity extends AppCompatActivity {

    private EditText login_id;
    private EditText passWord;
    private Button login;
    private Button register;
    private CheckBox save;
    private String loginID;
    private String pWD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        login_id = (EditText) findViewById(R.id.login_id);
        passWord = (EditText) findViewById(R.id.passWord);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        save = (CheckBox) findViewById(R.id.ck);
    }

    private void initData() {

        //点击保存密码的时候
       /* save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loginID = login_id.getText().toString();
                    pWD = passWord.getText().toString();
                    SpUtils.saveString(getApplicationContext(), GlobalContanstant.LOGINID, loginID);
                    SpUtils.saveString(getApplicationContext(), GlobalContanstant.PASSWORD, pWD);

                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请保存密码");
                }
            }
        });*/


        //点击登陆按钮   从服务器获取到数据 username和pwd
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.shortToast(getApplicationContext(),"正在登录");

                loginID = login_id.getText().toString();
                pWD = passWord.getText().toString();


                if (TextUtils.isEmpty(loginID) || TextUtils.isEmpty(pWD)) {
                    ToastUtil.shortToast(getApplicationContext(), "用户名或密码不能为空");

                }else{
                    SpUtils.saveString(getApplicationContext(), GlobalContanstant.LOGINID, loginID);
                    SpUtils.saveString(getApplicationContext(), GlobalContanstant.PASSWORD, pWD);

                }


                //上传服务器
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            final String json = tologin(loginID, pWD);
                            if (json != null) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (json.equals("false")) {
                                            ToastUtil.shortToast(getApplicationContext(), "用户名或密码错误");
                                            return;
                                        } else {


                                            final PersonInfo personInfo = JsonUtil.jsonToBean(json, PersonInfo.class);
                                            //保存到本地  ID  名字
                                            int personID = personInfo.get_id();
                                            String userName = personInfo.get_username();
                                            String phone = personInfo.get_telephone();
                                            int department_id = personInfo.get_department_id();

                                            int role = personInfo.get_role_id();
                                            //sp 保存

                                            SpUtils.saveInt(getApplicationContext(), GlobalContanstant.PERSONID, personID);
                                            SpUtils.saveString(getApplicationContext(), GlobalContanstant.USERNAME, userName);
                                            SpUtils.saveString(getApplicationContext(), GlobalContanstant.PHONE, phone);
                                            SpUtils.saveInt(getApplicationContext(), GlobalContanstant.DEPARATMENT, department_id);
                                            SpUtils.saveInt(getApplicationContext(),GlobalContanstant.ROLE,role);


                                            ToastUtil.shortToast(getApplicationContext(), "登录成功");

                                            Intent intent = new Intent(MainActivity.this, LocationService.class);
                                            startService(intent);
                                            finish();


                                        }
                                    }
                                });

                            } else if (json.equals("[]")){
                                ToastUtil.shortToast(getApplicationContext(), "请检查网络");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.shortToast(getApplicationContext(), "请检查网络");
                        }

                    }
                }.start();


            }
        });


    }

    private String tologin(String loginID, String pWD) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.loginmethodName);
        soapObject.addProperty("loginID", loginID);
        soapObject.addProperty("password", pWD);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            return false;
//        }
        return false;
    }



}
