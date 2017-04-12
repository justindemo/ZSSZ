package com.xytsz.xytsz.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;


/**
 * Created by admin on 2017/1/5.
 * 个人资料详细单
 */
public class DetailActivity extends AppCompatActivity {


    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtDepartment;
    private Button mBtSave;
    private String name;
    private String phone;
    private String department;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
    }

    //初始化控件
    private void initView() {

        mEtName = (EditText) findViewById(R
                .id.et_user_name);
        mEtPhone = (EditText) findViewById(R.id.et_user_phone);
        mEtDepartment = (EditText) findViewById(R.id.et_user_department);
        mBtSave = (Button) findViewById(R.id.bt_user_getData);
    }

    private void initData() {
        //返回数据到mefragment  发给服务器
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString();
                phone = mEtPhone.getText().toString();
                department = mEtDepartment.getText().toString();
                Log.i("name",name);
             if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(department)) {
                    ToastUtil.shortToast(getApplicationContext(), "姓名电话或者部门不能为空");
                    return;
                }
                //保存到本地
                SpUtils.saveString(getApplicationContext(), GlobalContanstant.NAME,name);
                SpUtils.saveString(getApplicationContext(),GlobalContanstant.PHONE, phone);
                SpUtils.saveString(getApplicationContext(),GlobalContanstant.DEPARATMENT, department);
                //发送到Mefragment
                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("department",department);
                setResult(20,intent);
                finish();
            }

        });
    }


}





