package com.webnik.in.kanvamart.ListAdapter;

/**
 * Created by IN on 12/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webnik.in.kanvamart.R;

import java.util.List;

public class ListAdapterClass2 extends BaseAdapter {

    Context context;
    List<subjects> valueList;

    public ListAdapterClass2(List<subjects> listValue, Context context)
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
        ViewItem2 viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem2();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.layout_items2, null);

            viewItem.tvcredit_value = (TextView)convertView.findViewById(R.id.tvvalue);
            viewItem.tvcredit_res = (TextView)convertView.findViewById(R.id.tvtext);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem2) convertView.getTag();
        }

        viewItem.tvcredit_value.setText(valueList.get(position).credit_value);
        viewItem.tvcredit_res.setText(valueList.get(position).credit_res);

        return convertView;
    }
}

class ViewItem2
{
    TextView tvcredit_value;
    TextView tvcredit_res;

}
