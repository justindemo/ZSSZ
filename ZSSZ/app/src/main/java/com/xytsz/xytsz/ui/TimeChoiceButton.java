package com.xytsz.xytsz.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.adapter.SendRoadAdapter;
import com.xytsz.xytsz.bean.Review;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2017/3/15.
 */
public class TimeChoiceButton extends Button {

    public  Calendar time = Calendar.getInstance(Locale.CHINA);
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Button dataView;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog dialog;
    private  Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail ;

    public TimeChoiceButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeChoiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    //dataView 是当前button
    public TimeChoiceButton(Context context, Button dataView) {
        super(context);
        this.dataView = dataView;

    }

    public AlertDialog dateTimePickerDialog(){
        View dateTimeLayout = LayoutInflater.from(getContext()).inflate(R.layout.date_time_picker, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datapicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);

        init();
        TimePicker.OnTimeChangedListener timeListener= new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);

            }
        };

        timePicker.setOnTimeChangedListener(timeListener);

        DatePicker.OnDateChangedListener dateListener = new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                //
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, monthOfYear);
                time.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        };

        datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), dateListener);
        timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(time.get(Calendar.MINUTE));


        dialog = new AlertDialog.Builder(getContext()).setTitle("设置日期时间").setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        datePicker.clearFocus();
                        timePicker.clearFocus();
                        time.set(Calendar.YEAR, datePicker.getYear());
                        time.set(Calendar.MONTH, datePicker.getMonth());
                        time.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        time.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                        updateLabel();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    dialog.dismiss();
                    }
                }).show();
        return dialog;
    }



    private void init() {
        TimeChoiceButtonManager.getInstance().setTimeChoiceButton(TimeChoiceButton.this);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTextColor(Color.BLACK);
        this.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                dateTimePickerDialog();
                updateLabel();
            }
        });


    }
    public void updateLabel() {
        if(dataView != null){
            dataView.setText(format.format(new Date(System.currentTimeMillis())));
        }
        TimeChoiceButtonManager manager = TimeChoiceButtonManager.getInstance();
        //是不是当前这个条目
        if(manager.getTimeChoiceButton() == TimeChoiceButton.this) {
            reviewRoadDetail.setRequestTime(getDateString());
            sendRoadAdapter.notifyDataSetChanged();
        }

    }

    /**
     * @return 获得时间字符串"yyyy-MM-dd HH:mm:ss"
     */
    public String getDateString() {
        return format.format(time.getTime());
    }

    private SendRoadAdapter sendRoadAdapter;
    public void setReviewRoadDetail(SendRoadAdapter sendRoadAdapter, Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail){
        this.reviewRoadDetail = reviewRoadDetail;
        this.sendRoadAdapter = sendRoadAdapter;
    }




}

