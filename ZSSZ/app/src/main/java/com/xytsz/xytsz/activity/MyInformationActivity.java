package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/11/30.
 */
public class MyInformationActivity extends AppCompatActivity {

    @Bind(R.id.my_name)
    EditText myName;
    @Bind(R.id.my_telephone)
    EditText myTelephone;
    @Bind(R.id.my_department)
    EditText myDepartment;
    @Bind(R.id.mine_modification_psd)
    Button mineModificationPsd;

    private String phone;
    private String username;
    private int role;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myinformation);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            String information = getString(R.string.mine_information);

            actionBar.setTitle(information);

        }
        phone = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);

        username = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);

        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);
        int department = SpUtils.getInt(getApplicationContext(), GlobalContanstant.DEPARATMENT);
        String phones = SpUtils.getString(getApplicationContext(), GlobalContanstant.PHONE);

        myName.setText(username);
        myName.setCursorVisible(false);
        myName.setFocusable(false);
        myName.setFocusableInTouchMode(false);
        if (phones != null) {
            myTelephone.setText(phones);
        }
        myDepartment.setText(Data.departments[department - 1]);


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.mine_modification_psd)
    public void onViewClicked() {
        Intent intent = new Intent(MyInformationActivity.this,ModificationActivity.class);
        startActivity(intent);
    }

}
