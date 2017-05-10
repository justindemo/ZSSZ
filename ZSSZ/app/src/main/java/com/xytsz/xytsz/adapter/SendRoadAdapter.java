package com.xytsz.xytsz.adapter;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.xytsz.xytsz.activity.SendBigPhotoActivity;
import com.xytsz.xytsz.bean.ImageUrl;
import com.xytsz.xytsz.bean.PersonList;
import com.xytsz.xytsz.bean.Review;
import com.xytsz.xytsz.global.Data;
import com.xytsz.xytsz.global.GlobalContanstant;
import com.xytsz.xytsz.net.NetUrl;
import com.xytsz.xytsz.ui.TimeChoiceButton;
import com.xytsz.xytsz.R;

import com.xytsz.xytsz.util.SpUtils;
import com.xytsz.xytsz.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

/**
 * Created by admin on 2017/2/17.
 *
 */
public class SendRoadAdapter extends BaseAdapter {


    private static final int ISSEND = 1000002;
    private static final int ISSENDPERSON = 1000003;

    private String str;
    private Review.ReviewRoad reviewRoad;
    private List<List<ImageUrl>> imageUrlLists;
    private String[] servicePerson;
    private List<PersonList.PersonListBean> personlist;
    private Handler handler;
    private int requirementsComplete_person_id;
    private String imgurl;


    public SendRoadAdapter(Handler handler, Review.ReviewRoad reviewRoad, List<List<ImageUrl>> imageUrlLists, String[] servicePerson, List<PersonList.PersonListBean> personlist) {
        this.handler = handler;

        this.reviewRoad = reviewRoad;
        this.imageUrlLists = imageUrlLists;

        this.servicePerson = servicePerson;
        this.personlist = personlist;
    }

    @Override
    public int getCount() {
        //假数据
        //根据id去返回不同的数组

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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_send, null);
            holder.Vname = (TextView) convertView.findViewById(R.id.tv_send_Vname);
            holder.Pname = (TextView) convertView.findViewById(R.id.tv_send_Pname);
            holder.date = (TextView) convertView.findViewById(R.id.tv_send_date);
            holder.sendIcon = (ImageView) convertView.findViewById(R.id.iv_send_photo);
            holder.btSend = (Button) convertView.findViewById(R.id.bt_send_send);
            holder.btChoice = (TimeChoiceButton) convertView.findViewById(R.id.bt_send_choice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
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
        Message message = Message.obtain();
        message.what = ISSENDPERSON;
        message.obj = userName;
        handler.sendMessage(message);



        String uploadTime = reviewRoadDetail.getUploadTime();
        int level = reviewRoadDetail.getLevel();

        holder.btChoice.setReviewRoadDetail(this, reviewRoadDetail);
        //String userName = SpUtils.getString(parent.getContext(), GlobalContanstant.USERNAME);
        //赋值
        holder.Pname.setText(userName);
        holder.Vname.setText(Data.pbname[level]);
        holder.date.setText(uploadTime);



        if (reviewRoadDetail.getRequestTime() == null) {
            holder.btChoice.setText("要求时间");
        } else {
            holder.btChoice.setText(reviewRoadDetail.getRequestTime());
        }

        //选择派发人
        holder.btSend.setTag(position);


        holder.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //点击的时候弹出对话框 都是一样的 人员名字


                //改变bean类的参数
                if (reviewRoadDetail.getRequestTime()== null) {
                    ToastUtil.shortToast(v.getContext(), "请先选择要求时间");

                } else {
                    AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("请选择").setSingleChoiceItems(servicePerson, 0, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    str = servicePerson[which];
                                    reviewRoadDetail.setSendPerson(str);

                                    //做判断 求出上报人员的ID
                                    //reviewRoadDetail.setRequirementsComplete_Person_ID();
                                    Button btn = (Button) v;
                                    btn.setText(str);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                    System.out.println(str);
                                    //上传服务器数据
                                    int position = (int) btn.getTag();
                                    SendRoadAdapter.this.taskNumber = getTaskNumber(position);
                                    getRequstPersonID(position,str);
                                    requstPersonID = reviewRoadDetail.getRequirementsComplete_Person_ID();
                                    requstTime = getRequstTime(position);
                                    new Thread() {
                                        @Override
                                        public void run() {

                                            try {
                                                String isSend = toDispatching(SendRoadAdapter.this.taskNumber, requstPersonID, requstTime, GlobalContanstant.GETDEAL);

                                                Message message = Message.obtain();
                                                message.what = ISSEND;
                                                message.obj = isSend;
                                                handler.sendMessage(message);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        if (reviewRoadDetail.getSendPerson() == null) {
            holder.btSend.setText("派发");
        } else {
            holder.btSend.setText(reviewRoadDetail.getSendPerson());

        }

        //获取到当前点击的URL集合
        if(imageUrlLists.size() != 0) {
            urlList = imageUrlLists.get(position);
            //显示的第一张图片
            ImageUrl imageUrl = urlList.get(0);
            imgurl = imageUrl.getImgurl();

            Glide.with(parent.getContext()).load(imgurl).into(holder.sendIcon);
            holder.sendIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),SendBigPhotoActivity.class);
                    intent.putExtra("imageurl",imageUrlLists.get(position).get(0).getImgurl());
                    v.getContext().startActivity(intent);
                }
            });


        }




        return convertView;
    }


    private int id;
    private List<ImageUrl> urlList;

    private int getRequstPersonID(int position, String str) {
        Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
        String sendPerson = reviewRoadDetail.getSendPerson();
        /*for (int i = 0; i < Data.servicePerson.length; i++) {
            if (sendPerson.equals(Data.servicePerson[i])) {
                requirementsComplete_person_id = i + 4;

            }
        }

        //做判断 获取到当前选择人对应的ID  发送给服务器

        reviewRoadDetail.setRequirementsComplete_Person_ID(requirementsComplete_person_id);*/

        //str
        for (int i = 0; i <personlist.size() ; i++) {


            if (str == personlist.get(i).getName()) {
                requirementsComplete_person_id =  personlist.get(i).getId();
                reviewRoadDetail.setRequirementsComplete_Person_ID(requirementsComplete_person_id);
            }

        }



        return requirementsComplete_person_id;
    }

    private String getRequstTime(int position) {
        Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
        String requestTime = reviewRoadDetail.getRequestTime();

        return requestTime;
    }


    static class ViewHolder {
        public TextView Vname;
        public TextView date;
        public TextView Pname;
        public ImageView sendIcon;
        public Button btSend;
        public TimeChoiceButton btChoice;

    }

    private String requstTime;
    private String taskNumber;
    private int requstPersonID;

    private String getTaskNumber(int position) {
        Review.ReviewRoad.ReviewRoadDetail reviewRoadDetail = reviewRoad.getList().get(position);
        String taskNumber = reviewRoadDetail.getTaskNumber();
        return taskNumber;
    }


    private String toDispatching(String taskNumber, int requstPersonID, String requestTime, int phaseIndication) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.sendmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);
        soapObject.addProperty("RequirementsComplete_Person_ID", requstPersonID);
        soapObject.addProperty("RequirementsCompleteTime", requestTime);
        soapObject.addProperty("PhaseIndication", phaseIndication);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.toDispatching_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }


}
