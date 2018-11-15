package com.xytsz.xytsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.PersonLocationAdapter;
import com.xytsz.xytsz.bean.PersonLocationList;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.util.JsonUtil;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/2.
 * <p>
 * 人员信息
 */
public class PersonLocationActivity extends AppCompatActivity {


    @Bind(R.id.personla_lv)
    ListView personlaLv;
    @Bind(R.id.personla_progressbar)
    ProgressBar personlaProgressbar;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.MYSENDSUCCESS:
                    personlaProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (!json.equals("[]")){
                        final List<PersonLocationList> personlocationListLists = JsonUtil.jsonToBean(json,new
                                TypeToken<List<PersonLocationList>>(){}.getType());

                        PersonLocationAdapter personLocationAdapter = new PersonLocationAdapter(personlocationListLists);
                        personlaLv.setAdapter(personLocationAdapter);
                        personlaLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(PersonLocationActivity.this,PersonLocationInfoActivity.class);
                                intent.putExtra("personInfo",personlocationListLists.get(position));
                                startActivity(intent);
                            }
                        });
                    }

                    break;
                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getApplicationContext(),"数据未获取");
                    personlaProgressbar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personlocation);
        ButterKnife.bind(this);
        initAcitionbar();
        initData();

    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.personlocation);

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initData() {
        personlaProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {

                try {
                    String data = getData();
                    if (data != null){
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 获取人员
     * @return 人员列表json
     */
    private String getData() throws Exception{
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getpersonLocationList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }
}
