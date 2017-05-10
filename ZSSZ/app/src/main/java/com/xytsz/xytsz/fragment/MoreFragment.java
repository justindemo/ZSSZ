package com.xytsz.xytsz.fragment;


import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.FloodActivity;
import com.xytsz.xytsz.activity.LibActivity;
import com.xytsz.xytsz.activity.MakerProblemActivty;
import com.xytsz.xytsz.activity.MemberLocationActivity;
import com.xytsz.xytsz.adapter.MoreAdapter;
import com.xytsz.xytsz.base.BaseFragment;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.util.IntentUtil;
import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

/**
 * Created by admin on 2017/1/4.
 *
 */
public class MoreFragment extends BaseFragment {

    private GridView mGv;
    private static final int PERSON = 0;
    private static final int PROBLEM = 1;
    private static final int LIB = 2;
    private static final int NOTICE = 4;
    private static final int FLOOD = 3;
    private int role;

    @Override
    public View initView() {

        View view = View.inflate(getActivity(), R.layout.activity_more, null);
        mGv = (GridView) view.findViewById(R.id.more_gv);
        return view;
    }

    @Override
    public void initData() {
        MoreAdapter adapter = new MoreAdapter();
        if (adapter != null) {
            mGv.setAdapter(adapter);
        }
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case PERSON:
                        //定位
                        if (role == 1) {
                            IntentUtil.startActivity(parent.getContext(), MemberLocationActivity.class);
                        } else {
                            ToastUtil.shortToast(getContext(), "您没有权限");
                        }

                        break;
                    case PROBLEM:
                        if (role == 1) {
                            IntentUtil.startActivity(parent.getContext(), MakerProblemActivty.class);
                        } else {
                            ToastUtil.shortToast(getContext(), "您没有权限");
                        }
                            //IntentUtil.startActivity(parent.getContext(), MakerProblemActivty.class);

                        break;
                    case LIB:
                        IntentUtil.startActivity(parent.getContext(), LibActivity.class);
                        //井盖
                        break;
                    case NOTICE:
                        //公告
                        showDialog();
                        break;
                    case FLOOD:
                        IntentUtil.startActivity(parent.getContext(), FloodActivity.class);
                        //防汛
                        break;
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        View view = View.inflate(getContext(), R.layout.set_dialog, null);
        builder.setView(view);
        builder.setTitle("公告：");
        final AlertDialog dialog = builder.show();
        Button mBtDismiss = (Button) view.findViewById(R.id.set_dismiss);
        TextView mtvVersion = (TextView) view.findViewById(R.id.set_text_version);
        //
        //

        mBtDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
    }
}
