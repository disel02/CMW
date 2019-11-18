package com.webnik.in.kanvamart.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webnik.in.kanvamart.R;

import java.util.List;

public class ListAdapterClass4 extends BaseAdapter {
    Context context;
    List<subjects> valueList;
    public ListAdapterClass4(List<subjects> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }
    @Override
    public int getCount()
    {
        return this.valueList.size();
    }
    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem4 viewItem = null;
        if(convertView == null)
        {
            viewItem = new ViewItem4();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.row, null);
            viewItem.vTitle = (TextView)convertView.findViewById(R.id.tvtitle);
            viewItem.vDate = (TextView)convertView.findViewById(R.id.tvdate);
            viewItem.vTime = (TextView)convertView.findViewById(R.id.tvtime);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem4) convertView.getTag();
        }
        viewItem.vTitle.setText(valueList.get(position).sTitle);
        viewItem.vDate.setText(valueList.get(position).sDate);
        viewItem.vTime.setText(valueList.get(position).sTime);
        return convertView;
    }
}

class ViewItem4
{
    TextView vTitle,vDate,vTime;
}

