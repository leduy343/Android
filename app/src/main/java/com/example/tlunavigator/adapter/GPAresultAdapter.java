package com.example.tlunavigator.adapter;

import android.Manifest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPAresultAdapter extends RecyclerView.Adapter<GPAresultAdapter.ViewHolder> {

    private Context context;
    private List<Map<String,String>> list;
    public GPAresultAdapter(Context context,List<Map<String,String>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public GPAresultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_gparesult,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GPAresultAdapter.ViewHolder holder, int position) {
        Map<String,String> item  = list.get(position);
        String semester = "";
        String gpa = "";

        for (Map.Entry<String, String> entry : item.entrySet()) {
            semester = entry.getKey();
            gpa = entry.getValue();
            break;
        }
        holder.txtsemester.setText(semester);
        holder.txtGPA.setText(gpa);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtsemester,txtGPA;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtsemester = itemView.findViewById(R.id.txtsemester);
            txtGPA = itemView.findViewById(R.id.txtGPA);
        }
    }
}
