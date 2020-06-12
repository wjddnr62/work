package com.dev.pdf.work.Buisnessman;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Data.Scene_Apply_Data;
import com.dev.pdf.work.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scene_Apply_Adapter extends BaseAdapter {

    private ArrayList<Scene_Apply_Data> scene_apply_data_list = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference worklist_ref;

    public Scene_Apply_Adapter(){
    }

    @Override
    public int getCount() {
        return scene_apply_data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return scene_apply_data_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        final Context context = parent.getContext();

        database = FirebaseDatabase.getInstance();
        worklist_ref = database.getReference("worklist");

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.scene_apply_list_item, parent, false);
        }

        TextView apply_name = convertView.findViewById(R.id.apply_name);
        TextView apply_phone = convertView.findViewById(R.id.apply_phone);
        TextView apply_work = convertView.findViewById(R.id.apply_work);
        TextView apply_career = convertView.findViewById(R.id.apply_career);
        TextView apply_ok = convertView.findViewById(R.id.apply_ok);

        final Scene_Apply_Data scene_apply_data = scene_apply_data_list.get(position);

        apply_name.setText(scene_apply_data.getApply_name());
        apply_phone.setText(scene_apply_data.getApply_phone());
        apply_work.setText(scene_apply_data.getApply_work());
        apply_career.setText(scene_apply_data.getApply_career());
        apply_ok.setText(scene_apply_data.getApply_ok());

        apply_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> taskmap = new HashMap<>();
                taskmap.put(scene_apply_data.getApply_key()+"/status", 1);
                worklist_ref.updateChildren(taskmap);
                //Toast.makeText(context, scene_apply_data.getApply_phone() + "," + scene_apply_data.getApply_work() + "," + scene_apply_data.getApply_career(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public void addItem(String Apply_name, String Apply_phone, String Apply_work, String Apply_career, String Apply_ok, String Apply_key){
        Scene_Apply_Data item = new Scene_Apply_Data();
        item.setApply_name(Apply_name);
        item.setApply_phone(Apply_phone);
        item.setApply_work(Apply_work);
        item.setApply_career(Apply_career);
        item.setApply_ok(Apply_ok);
        item.setApply_key(Apply_key);

        scene_apply_data_list.add(item);
    }

    public void clearitem(){
        scene_apply_data_list.clear();
    }
}
