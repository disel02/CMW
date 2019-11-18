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

public class ListAdapterClass3 extends BaseAdapter {

    Context context;
    List<subjects> valueList;

    public ListAdapterClass3(List<subjects> listValue, Context context)
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
        ViewItem3 viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem3();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.layout_items3, null);

            viewItem.tvday = (TextView)convertView.findViewById(R.id.tvday);
            viewItem.tvdebit = (TextView)convertView.findViewById(R.id.tvdebit);
            viewItem.tvcredit = (TextView)convertView.findViewById(R.id.tvcredit);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem3) convertView.getTag();
        }

        viewItem.tvday.setText(valueList.get(position).day);
        viewItem.tvdebit.setText(valueList.get(position).debit);
        viewItem.tvcredit.setText(valueList.get(position).credit);

        return convertView;
    }
}

class ViewItem3
{
    TextView tvday;
    TextView tvdebit;
    TextView tvcredit;

}
