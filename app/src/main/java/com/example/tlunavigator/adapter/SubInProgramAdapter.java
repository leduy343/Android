package com.example.tlunavigator.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.AddSubjectToProgramActivity;
import com.example.tlunavigator.R;
import com.example.tlunavigator.SubjectinProgram;
import com.example.tlunavigator.model.SubjectWithType;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SubInProgramAdapter extends RecyclerView.Adapter<SubInProgramAdapter.SubInProgramViewHolder> {

    private Context context;
    private List<SubjectWithType> subjectList;
    private  String programid;
    private DatabaseReference dbRef;
    public SubInProgramAdapter(Context context,List<SubjectWithType> list,String programid){
        this.context = context;
        this.subjectList = list;
        this.programid = programid;
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

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddSubjectToProgramActivity.class);
            intent.putExtra("ProgramId", programid);
            intent.putExtra("subjectId",subject.id);
            intent.putExtra("subjectname",subject.name);
            intent.putExtra("subjectType",s.type);
            context.startActivity(intent);
        });
        holder.btnDelete.setOnClickListener(v -> deleteSubject(subject));
    }
    private void deleteSubject(Subject subject) {
        dbRef = FirebaseDatabase.getInstance().getReference("Program").child(programid).child("subjects");
        new AlertDialog.Builder(context)
                .setTitle("Xóa môn học")
                .setMessage("Bạn có chắc muốn xóa môn học này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbRef.child(subject.id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
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
