package com.dev.pdf.work.Scene;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Buisnessman.SceneEvalActivity;
import com.dev.pdf.work.R;

import java.util.ArrayList;

public class WorkListAdapter extends BaseAdapter {

    private ArrayList<WorkListItem> itemList = new ArrayList<>();

    public WorkListAdapter(ArrayList<WorkListItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.scene_list_item, parent, false);
        }

        TextView area = (TextView) convertView.findViewById(R.id.area);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView work_day = (TextView) convertView.findViewById(R.id.work_day);
        final TextView money = (TextView) convertView.findViewById(R.id.money);

        final WorkListItem item = itemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        area.setText(item.getArea());
        location.setText(item.getLocation());
        work_day.setText(item.getWorkDay());
        money.setText(item.getMoney());

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money.getText().toString().equals("평가하기")){
                    Intent intent = new Intent(context, SceneEvalActivity.class);
                    Log.e("eeee ", item.getTitle() + ", " + item.getLocation());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("location", item.getLocation());
                    context.startActivity(intent);
                }

            }
        });

        return convertView;
    }
}
