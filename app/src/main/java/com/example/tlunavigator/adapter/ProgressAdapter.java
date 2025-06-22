package com.example.tlunavigator.adapter;

import android.content.Context;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.R;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public ProgressAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("Size", ""+getItemCount());
        String subjectName = list.get(position);
        if(subjectName!=null){
            holder.txtsubjectname.setText(subjectName);
        }else {
            holder.txtsubjectname.setText("Lỗi");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtsubjectname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtsubjectname = itemView.findViewById(R.id.txtsubjectname);
        }
    }
}

