package com.xytsz.xytsz.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.FirstAdapter;
import com.xytsz.xytsz.bean.FirstDiease;
import com.xytsz.xytsz.global.Data;

/**
 * Created by admin on 2017/3/9.
 * 二类病害
 */
public class SecondDieaseActivity extends AppCompatActivity {

    private ListView mfristlv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fristdisease);
        initView();
        initData();
    }

    private void initData() {

        FirstDiease firstDiease = new FirstDiease();
        for (int i = 0; i < Data.SecondImageID.length; i++) {
            firstDiease.desc.add(Data.SecondDesc[i]) ;

        }
            firstDiease.imageId.add(Data.SecondImageID);
            firstDiease.dieaseName = "二级病害";
        FirstAdapter adapter = new FirstAdapter(firstDiease);
        if (adapter != null) {

            mfristlv.setAdapter(adapter);
        }
    }

    private void initView() {
        mfristlv = (ListView) findViewById(R.id.lv_frist);
    }
}
