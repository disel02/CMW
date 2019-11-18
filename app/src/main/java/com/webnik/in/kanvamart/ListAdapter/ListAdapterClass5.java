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

public class ListAdapterClass5 extends BaseAdapter {
    Context context;
    List<subjects> valueList;
    public ListAdapterClass5(List<subjects> listValue, Context context)
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
        ViewItem5 viewItem = null;
        if(convertView == null)
        {
            viewItem = new ViewItem5();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.layout_items4, null);
            viewItem.vUsername = (TextView)convertView.findViewById(R.id.tvusername);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem5) convertView.getTag();
        }
        viewItem.vUsername.setText(valueList.get(position).sUsername);
        return convertView;
    }
}

class ViewItem5
{
    TextView vUsername;
}

