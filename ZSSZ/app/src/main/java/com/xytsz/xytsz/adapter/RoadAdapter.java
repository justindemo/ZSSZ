package com.xytsz.xytsz.adapter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytsz.R;
import com.xytsz.xytsz.activity.PhotoShowActivity;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.ui.SwipeLayoutManager;
import com.xytsz.xytsz.util.SpUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.List;


/**
 * Created by admin on 2017/1/11.
 *
 */
public class RoadAdapter extends BaseAdapter {

    private static final int ISPASS = 100001;
    private static final int ISFAIL = 100002;

    private int phaseIndication = 0;
    private Handler handler ;
    private Review.ReviewRoad reviewRoad;
    private List<List<ImageUrl>> imageUrlLists;
    private String isPass;
    private String isFail;
    private List<ImageUrl> urlList;
    private int personID;


    public RoadAdapter(Handler handler, Review.ReviewRoad reviewRoad, List<List<ImageUrl>> imageUrlLists) {
        this.handler = handler;
        this.reviewRoad = reviewRoad;
        //返回的URl 集合
        this.imageUrlLists = imageUrlLists;
    }


    @Override
    public int getCount() {

        return reviewRoad.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return reviewRoad.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_road, null);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_swipe_icon);
            holder.tvProblemname = (TextView) convertView.findViewById(R.id.tv_problem_name);
            holder.tvProblemreporter = (TextView) convertView.findViewById(R.id.tv_problem_reporter);
            holder.tvProblemtime = (TextView) convertView.findViewById(R.id.tv_problem_reportertime);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tvPass = (TextView) convertView.findViewById(R.id.tv_pass);
            holder.rlroad = (RelativeLayout) convertView.findViewById(R.id.rl_road);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示图片 //


        Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
        int level = reviewRoadDetail.getLevel();

        holder.tvProblemname.setText(Data.pbname[level]);
        String upload_person_id = reviewRoadDetail.getUpload_Person_ID()+"";
        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
        List<String> personNamelist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONNAMELIST);
        List<String> personIDlist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONIDLIST);

        for (int i = 0; i < personIDlist.size(); i++) {
            if (upload_person_id.equals(personIDlist.get(i))){
                id = i;
            }
        }

        String userName = personNamelist.get(id);



        String uploadTime = reviewRoadDetail.getUploadTime();


        //获取到当前点击的URL集合
        /**
         * 如果size 不为空
         */
        if (imageUrlLists.size()!= 0) {
            urlList = imageUrlLists.get(position);
            //显示的第一张图片
            ImageUrl imageUrl = urlList.get(0);
            String imgurl = imageUrl.getImgurl();
            Glide.with(parent.getContext()).load(imgurl).into(holder.ivIcon);
            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(v.getContext(),PhotoShowActivity.class);
                    intent.putExtra("imageUrllist",(Serializable)imageUrlLists.get(position));
                    v.getContext().startActivity(intent);

                }
            });

        }

        holder.tvProblemreporter.setText(userName);
        holder.tvProblemtime.setText(uploadTime);
        holder.tvDelete.setTag(position);
        holder.tvPass.setTag(position);
        holder.tvDelete.setOnClickListener(listener);
        holder.tvPass.setOnClickListener(listener);

        return convertView;
    }

    private  int id;
    static class ViewHolder {
        private TextView tvProblemname;
        private TextView tvProblemreporter;
        private TextView tvProblemtime;
        private ImageView ivIcon;
        private TextView tvDelete;
        private TextView tvPass;
        private RelativeLayout rlroad;

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            switch (v.getId()) {

                case R.id.tv_delete:
                    int position = (int) v.getTag();
                    phaseIndication = 5;
                    taskNumber = getTaskNumber(position);
                    personID = SpUtils.getInt(v.getContext(), GlobalContanstant.PERSONID);

                    new Thread(){
                        @Override
                        public void run() {

                            try {
                                isFail = toExamine(taskNumber, phaseIndication,personID);
                                Message message = Message.obtain();
                                message.what = ISFAIL;
                                message.obj = isFail;
                                handler.sendMessage(message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                    reviewRoad.getList().remove(position);
                    notifyDataSetChanged();
                    //点击的时候关闭这个条目
                    SwipeLayoutManager.getInstance().getSwipeLayout().close(false);
                    //上传当前状态给服务器


                    break;
                case R.id.tv_pass:
                    int position1 = (int) v.getTag();


                    phaseIndication = 1;
                    taskNumber = getTaskNumber(position1);
                    personID = SpUtils.getInt(v.getContext(), GlobalContanstant.PERSONID);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                isPass = toExamine(taskNumber, phaseIndication, personID);

                                Message message = Message.obtain();
                                message.what = ISPASS;
                                message.obj = isPass;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                    reviewRoad.getList().remove(position1);
                    //点击的时候关闭这个条目
                    notifyDataSetChanged();
                    SwipeLayoutManager.getInstance().getSwipeLayout().close(false);
            }


        }
    };
    private String taskNumber;

    private String getTaskNumber(int position) {
        Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
        String taskNumber = reviewRoadDetail.getTaskNumber();
        return taskNumber;
    }



    private String toExamine(final String taskNumber, final int phaseIndication, int personID) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);
        soapObject.addProperty("PhaseIndication", phaseIndication);
        soapObject.addProperty("PersonId",personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.toExamine_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result =  object.getProperty(0).toString();
        return result;

    }


}

