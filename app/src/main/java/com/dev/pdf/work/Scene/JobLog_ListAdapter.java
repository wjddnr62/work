package com.dev.pdf.work.Scene;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.R;

import java.util.ArrayList;

public class JobLog_ListAdapter extends BaseAdapter {

    private ArrayList<JobLog_ListItem> itemList = new ArrayList<>();

    public JobLog_ListAdapter(ArrayList<JobLog_ListItem> itemList) {
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
            convertView = inflater.inflate(R.layout.joblog_list_item, parent, false);
        }

        TextView area = (TextView) convertView.findViewById(R.id.area);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        final TextView status = (TextView) convertView.findViewById(R.id.status);
        final TextView evaluation = (TextView) convertView.findViewById(R.id.evaluation);

        final JobLog_ListItem item = itemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        area.setText(item.getArea());
        location.setText(item.getLocation());
        status.setText(item.getStatus());
        evaluation.setText(item.getEvaluation());
        
        evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.getText().toString().equals("작업 완료")){
                    if (evaluation.getText().toString().equals("평가필요")){
                        Intent intent = new Intent(context, Job_Eval_Activity.class);
                        intent.putExtra("job_eval_status", 0);
                        intent.putExtra("location", item.getLocation());
                        intent.putExtra("title", item.getTitle());
                        intent.putExtra("date", item.getDate());
                        intent.putExtra("user_id", item.getUser_id());
                        context.startActivity(intent);
                    }else if (evaluation.getText().toString().equals("평가완료")){
                        Toast.makeText(context, "이미 평가하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "작업 완료 후 평가하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
