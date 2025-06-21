package com.example.tlunavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.model.Major;

import java.util.List;

public class MajorAdapter extends RecyclerView.Adapter<MajorAdapter.MajorViewHolder> {
    private List<Major> majors;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Major major);
    }

    public MajorAdapter(List<Major> majors, OnItemClickListener listener) {
        this.majors = majors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MajorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_major, parent, false);
        return new MajorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MajorViewHolder holder, int position) {
        Major major = majors.get(position);
        holder.textView.setText(major.getCode() + " - " + major.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(major));
    }

    @Override
    public int getItemCount() {
        return majors.size();
    }

    static class MajorViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MajorViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvMajor);
        }
    }
}

