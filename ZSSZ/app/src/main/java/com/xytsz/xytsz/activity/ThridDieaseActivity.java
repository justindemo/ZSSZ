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
 * Created by admin on 2017/3/9.‘
 * 三类病害
 */
public class ThridDieaseActivity extends AppCompatActivity {

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
        for (int i = 0; i < Data.ThridImageID.length; i++) {
            firstDiease.desc.add(Data.ThridDesc[i]);
        }
        firstDiease.imageId.add(Data.ThridImageID);
        firstDiease.dieaseName = "三级病害";
        FirstAdapter adapter = new FirstAdapter(firstDiease);
        if (adapter != null) {

            mfristlv.setAdapter(adapter);
        }
    }

    private void initView() {
        mfristlv = (ListView) findViewById(R.id.lv_frist);
    }
}
