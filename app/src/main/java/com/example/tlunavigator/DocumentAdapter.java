package com.example.tlunavigator;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlunavigator.model.Document;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<Document> documentList;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Documents");

    public DocumentAdapter(Context context, List<Document> documentList) {
        this.context = context;
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.tvSubjectName.setText("Tên tài liệu: " + document.subjectName);
        holder.tvDocumentName.setText("Mã: " + document.documentName);
        holder.tvType.setText("Số tín: " + document.type);

        holder.btnEdit.setOnClickListener(v -> showEditDialog(document));
        holder.btnDelete.setOnClickListener(v -> deleteDocument(document));
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvDocumentName, tvType;
        ImageButton btnEdit, btnDelete;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            tvType = itemView.findViewById(R.id.tvType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showEditDialog(Document document) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_document, null);
        builder.setView(view);

        EditText etSubjectsName = view.findViewById(R.id.etSubjectsName);
        EditText etDocumentName = view.findViewById(R.id.etDocumentName);
        EditText etType = view.findViewById(R.id.etType);

        etSubjectsName.setText(document.subjectName);
        etDocumentName.setText(document.documentName);
        etType.setText(document.type);


        builder.setTitle("Chỉnh sửa tài liệu")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newSubjectName = etSubjectsName.getText().toString();
                    String newDocumentName = etDocumentName.getText().toString();
                    String newType = etType.getText().toString();

                    Document updated = new Document(document.id, newSubjectName, newDocumentName, newType);
                    dbRef.child(document.id).setValue(updated)
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteDocument(Document document) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa tài liệu")
                .setMessage("Bạn có chắc muốn xóa tài liệu này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbRef.child(document.id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
