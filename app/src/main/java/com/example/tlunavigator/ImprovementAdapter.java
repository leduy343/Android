package com.example.tlunavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImprovementAdapter extends RecyclerView.Adapter<ImprovementAdapter.ViewHolder> {

    private Context context;
    private List<String> subjectList;
    public ImprovementAdapter(Context context,List<String> list) {
        this.context = context;
        this.subjectList = list;
    }
    @NonNull
    @Override
    public ImprovementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_impsubject,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImprovementAdapter.ViewHolder holder, int position) {
        String subject = subjectList.get(position);

        int lastColon = subject.lastIndexOf(":");
        String name = subject.substring(0, lastColon).trim();
        String grade = subject.substring(lastColon + 1).trim();

        holder.tvSubjectName.setText(name);
        holder.tvSubjectGrade.setText(grade);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubjectName,tvSubjectGrade;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectGrade = itemView.findViewById(R.id.tvSubjectGrade);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
        }
    }
}
