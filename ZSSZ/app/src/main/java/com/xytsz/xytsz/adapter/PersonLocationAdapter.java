package com.xytsz.xytsz.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.PersonLocationList;
import com.xytsz.xytsz.bean.Review;

import java.util.List;

/**
 * Created by admin on 2018/5/2.
 *
 * 人员列表适配器
 */
public class PersonLocationAdapter extends BaseAdapter {


    private List<PersonLocationList> personlocationListLists;

    public PersonLocationAdapter(List<PersonLocationList> personlocationListLists) {

        this.personlocationListLists = personlocationListLists;
    }

    @Override
    public int getCount() {
        return personlocationListLists.size();
    }

    @Override
    public Object getItem(int position) {
        return personlocationListLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_personlocation, null);
            holder.personName = (TextView) convertView.findViewById(R.id.tv_person_name);
            holder.location = (TextView) convertView.findViewById(R.id.tv_person_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.personName.setText(personlocationListLists.get(position).getPersonName());
        holder.location.setText(personlocationListLists.get(position).getLocationInfo());

        return convertView;
    }


    static class ViewHolder {
        public TextView personName;
        public TextView location;

    }

}
