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

import com.example.tlunavigator.DocumentOfSubjectActivity;
import com.example.tlunavigator.R;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context context;
    private List<Subject> subjectList;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Subjects");

    public SubjectAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.tvName.setText("Tên môn: " + subject.name);
        holder.tvCode.setText("Mã: " + subject.code);
        holder.tvCredit.setText("Số tín: " + subject.credit);

        holder.btnEdit.setOnClickListener(v -> showEditDialog(subject));
        holder.btnDelete.setOnClickListener(v -> deleteSubject(subject));

        holder.itemView.setOnClickListener(v-> {
            Intent intent = new Intent(context, DocumentOfSubjectActivity.class);
            intent.putExtra("subjectid",subject.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvCredit;
        ImageButton btnEdit, btnDelete;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSubjectName);
            tvCode = itemView.findViewById(R.id.tvSubjectCode);
            tvCredit = itemView.findViewById(R.id.tvSubjectCredit);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showEditDialog(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_subject, null);
        builder.setView(view);

        EditText etName = view.findViewById(R.id.etSubjectName);
        EditText etCode = view.findViewById(R.id.etSubjectCode);
        EditText etCredit = view.findViewById(R.id.etSubjectCredit);

        etName.setText(subject.name);
        etCode.setText(subject.code);
        etCredit.setText(String.valueOf(subject.credit));

        builder.setTitle("Chỉnh sửa môn học")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = etName.getText().toString();
                    String newCode = etCode.getText().toString();
                    int newCredit = Integer.parseInt(etCredit.getText().toString());

                    Subject updated = new Subject(subject.id, newName, newCode, newCredit);
                    dbRef.child(subject.id).setValue(updated)
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteSubject(Subject subject) {
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
}
