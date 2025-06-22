package com.example.tlunavigator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.R;
import com.example.tlunavigator.UserAllDocumentsActivity;
import com.example.tlunavigator.UserSubjectActivity;
import com.example.tlunavigator.model.Subject;

import java.util.ArrayList;
import java.util.List;


public class UsersubjectAdapter extends RecyclerView.Adapter<UsersubjectAdapter.ViewHolder> {

    Context context;
    List<Subject> subjectslist;
    public  UsersubjectAdapter(Context context, List<Subject>list){
        this.context = context;
        this.subjectslist = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_user,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject item = subjectslist.get(position);
        holder.txtsubjectname.setText(item.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserAllDocumentsActivity.class);
                intent.putExtra("subjectid",item.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtsubjectname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtsubjectname = itemView.findViewById(R.id.txtsubjectname);
        }
    }
}
