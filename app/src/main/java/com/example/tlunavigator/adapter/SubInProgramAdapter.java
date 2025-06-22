package com.example.tlunavigator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.R;
import com.example.tlunavigator.model.SubjectWithType;
import com.example.tlunavigator.model.Subject;

import java.util.List;

public class SubInProgramAdapter extends RecyclerView.Adapter<SubInProgramAdapter.SubInProgramViewHolder> {

    private Context context;
    private List<SubjectWithType> subjectList;
    public SubInProgramAdapter(Context context,List<SubjectWithType> list){
        this.context = context;
        this.subjectList = list;
    }
    @NonNull
    @Override
    public SubInProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_subprogram,parent,false);
        return new SubInProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubInProgramViewHolder holder, int position) {
        SubjectWithType s = subjectList.get(position);
        Subject subject = s.subject;
        holder.tvName.setText("Tên môn: " + subject.name);
        holder.tvCode.setText("Mã: " + subject.code);
        holder.tvCredit.setText("Số tín: " + subject.credit);
        holder.tvType.setText("Loại: " + ("required".equals(s.type) ? "Bắt buộc" : "Tự chọn"));

        holder.btnEdit.setOnClickListener(v -> showEditDialog(subject));
        holder.btnDelete.setOnClickListener(v -> deleteSubject(subject));
    }

    private void deleteSubject(Subject subject) {

    }

    private void showEditDialog(Subject subject) {

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class SubInProgramViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvCode, tvCredit,tvType;
        ImageButton btnEdit, btnDelete;
        public SubInProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSubjectName);
            tvCode = itemView.findViewById(R.id.tvSubjectCode);
            tvCredit = itemView.findViewById(R.id.tvSubjectCredit);
            tvType = itemView.findViewById(R.id.tvType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
