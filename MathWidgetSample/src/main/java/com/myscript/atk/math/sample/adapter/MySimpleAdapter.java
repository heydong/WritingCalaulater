package com.myscript.atk.math.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.myscript.atk.math.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hd_chen on 2016/8/19.
 */
public class MySimpleAdapter extends SimpleAdapter {

    @Bind(R.id.tv_item)
    TextView textView;
    @Bind(R.id.grid_item)
    RelativeLayout gridItem;

    Context context;
    List<? extends Map<String, ?>> data;

    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gird_item, null);
        }
        ButterKnife.bind(this,convertView);
        if (position % 4 == 3) {
            gridItem.setBackgroundResource(R.color.specialKey);
            textView.setBackgroundResource(R.color.specialKey);
        }
        textView.setText(data.get(position).get("text").toString());
        return convertView;
    }
}
