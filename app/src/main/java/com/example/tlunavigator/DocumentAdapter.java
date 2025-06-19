package com.example.tlunavigator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        holder.tvSubjectName.setText("Môn: " + document.subjectName);
        holder.tvDocumentName.setText("Tài liệu: " + document.documentName);
        holder.tvType.setText("Loại: " + document.type);

        // Nếu có link YouTube thì hiển thị và cho click mở
        if (document.youtubeLink != null && !document.youtubeLink.isEmpty()) {
            holder.tvDocument.setText("Video: " + document.youtubeLink);
            holder.tvDocument.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(document.youtubeLink));
                context.startActivity(intent);
            });
        } else {
            holder.tvDocument.setText("Thêm link YouTube");
            holder.tvDocument.setOnClickListener(v -> showYouTubeDialog(document));
        }

        holder.btnEdit.setOnClickListener(v -> showEditDialog(document));
        holder.btnDelete.setOnClickListener(v -> deleteDocument(document));
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvDocumentName, tvType, tvDocument;
        ImageButton btnEdit, btnDelete;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            tvType = itemView.findViewById(R.id.tvType);
            tvDocument = itemView.findViewById(R.id.tvDocument);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showYouTubeDialog(Document document) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nhập link YouTube");

        final EditText input = new EditText(context);
        input.setHint("https://www.youtube.com/...");
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String link = input.getText().toString().trim();
            if (!link.isEmpty() && link.startsWith("http")) {
                document.youtubeLink = link;
                dbRef.child(document.id).setValue(document)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(context, "Đã lưu link YouTube", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Lỗi khi lưu link", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(context, "Link không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
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
                    String newSubject = etSubjectsName.getText().toString();
                    String newName = etDocumentName.getText().toString();
                    String newType = etType.getText().toString();

                    Document updated = new Document(document.id, newSubject, newName, newType, document.youtubeLink);
                    dbRef.child(document.id).setValue(updated)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
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
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
