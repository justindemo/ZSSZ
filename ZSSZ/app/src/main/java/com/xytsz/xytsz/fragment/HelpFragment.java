package com.xytsz.xytsz.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.FristDieaseActivity;
import com.xytsz.xytsz.activity.SecondDieaseActivity;
import com.xytsz.xytsz.activity.ThridDieaseActivity;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.util.IntentUtil;

/**
 * Created by admin on 2017/1/4.
 */
public class HelpFragment extends BaseFragment {


    private ListView mlv;
    private Button mbtnFrist;
    private Button mbtnsecond;
    private Button mbtnthrid;

    @Override
    public View initView() {
        View view = View.inflate(getContext(), R.layout.activity_help, null);

        mbtnFrist = (Button) view.findViewById(R.id.btn_help_frist);
        mbtnsecond = (Button) view.findViewById(R.id.btn_help_second);
        mbtnthrid = (Button) view.findViewById(R.id.btn_help_thrid);

        return view;
    }

    @Override
    public void initData() {
        mbtnFrist.setOnClickListener(listener);
        mbtnsecond.setOnClickListener(listener);
        mbtnthrid.setOnClickListener(listener);
    }

    private View.OnClickListener listener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_help_frist:
                    IntentUtil.startActivity(v.getContext(),FristDieaseActivity.class);
                    break;
                case R.id.btn_help_second:
                    IntentUtil.startActivity(v.getContext(),SecondDieaseActivity.class);
                    break;
                case R.id.btn_help_thrid:
                    IntentUtil.startActivity(v.getContext(),ThridDieaseActivity.class);
                    break;
            }
        }
    };
}
