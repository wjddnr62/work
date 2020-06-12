package com.dev.pdf.work.Buisnessman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Data.SceneEvalListItem;
import com.dev.pdf.work.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SceneEvalAdapter extends BaseAdapter {

    private ArrayList<SceneEvalListItem> sceneEvalListItems = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference worklist_ref;

    public SceneEvalAdapter() {
    }

    @Override
    public int getCount() {
        return sceneEvalListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return sceneEvalListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        database = FirebaseDatabase.getInstance();
        worklist_ref = database.getReference("worklist");

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sceneeval_list_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView job_content = convertView.findViewById(R.id.job_content);
        final TextView eval = convertView.findViewById(R.id.eval);

        final SceneEvalListItem sceneEvalListItem = sceneEvalListItems.get(position);

        name.setText(sceneEvalListItem.getName());
        job_content.setText(sceneEvalListItem.getJob_content());
        eval.setText(sceneEvalListItem.getEval());

        eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (eval.getText().toString().equals("평가하기")){
                        Intent intent = new Intent(context, SceneRegi2Activity.class);
                        intent.putExtra("name", sceneEvalListItem.getName());
                        intent.putExtra("tel", sceneEvalListItem.getTel());
                        intent.putExtra("address", sceneEvalListItem.getAddress());
                        intent.putExtra("title", sceneEvalListItem.getTitle());
                        intent.putExtra("location", sceneEvalListItem.getLocation());
                        intent.putExtra("user_id", sceneEvalListItem.getUser_id());
                        context.startActivity(intent);
                    }else if (eval.getText().toString().equals("작업완료처리")){
                        Map<String, Object> taskmap = new HashMap<>();
                        taskmap.put(sceneEvalListItem.getUser_key()+"/status", 2);
                        worklist_ref.updateChildren(taskmap);
                    }else {
                        Toast.makeText(context, "이미 평가하였습니다.", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        return convertView;
    }

    public void clearitem(){
        sceneEvalListItems.clear();
    }

    public void addItem(String name, String job_content, String eval, String tel, String address, String title, String location, String user_id, String user_key){
        SceneEvalListItem item = new SceneEvalListItem();
        item.setName(name);
        item.setJob_content(job_content);
        item.setEval(eval);
        item.setTel(tel);
        item.setAddress(address);
        item.setTitle(title);
        item.setLocation(location);
        item.setUser_id(user_id);
        item.setUser_key(user_key);
        sceneEvalListItems.add(item);
    }

}
