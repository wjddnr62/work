package com.dev.pdf.work.Buisnessman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.pdf.work.R;

import java.util.ArrayList;
import java.util.List;

public class Spinner_Adapter extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    LayoutInflater inflater;

    public Spinner_Adapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item_dropdown, parent, false);
        }

        String text = data.get(position);
        ((TextView) convertView.findViewById(R.id.spinner_text)).setText(text);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        String text = data.get(position);
        ((TextView) convertView.findViewById(R.id.spinner_text)).setText(text);

        return convertView;
    }
}
