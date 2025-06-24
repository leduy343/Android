package com.example.tlunavigator.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.DocumentOfSubjectActivity;
import com.example.tlunavigator.R;
import com.example.tlunavigator.ViewDocumentActivity;
import com.example.tlunavigator.model.Document;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<Document> documentList;
    private boolean isAdmin;
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Documents");

    public DocumentAdapter(Context context, List<Document> documentList, boolean isAdmin) {
        this.context = context;
        this.documentList = documentList;
        this.isAdmin = isAdmin;
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

        // ✅ Chỉ hiện dòng YouTube khi có link hoặc là admin
        if (document.youtubeLink != null && !document.youtubeLink.isEmpty()) {
            holder.tvDocument.setVisibility(View.VISIBLE);
            holder.tvDocument.setText("Video: " + document.youtubeLink);
            holder.tvDocument.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(document.youtubeLink));
                context.startActivity(intent);
            });
        } else if (isAdmin) {
            holder.tvDocument.setVisibility(View.VISIBLE);
            holder.tvDocument.setText("Thêm link YouTube");
            holder.tvDocument.setOnClickListener(v -> showYouTubeDialog(document));
        } else {
            holder.tvDocument.setVisibility(View.GONE); // ❌ user không thấy dòng nào
        }

        // ✅ Xử lý nút sửa / xóa
        if (isAdmin) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> showEditDialog(document));
            holder.btnDelete.setOnClickListener(v -> deleteDocument(document));
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewDocumentActivity.class);
                intent.putExtra("name",document.documentName);
                intent.putExtra("link",document.youtubeLink);
                context.startActivity(intent);
            });
        }


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

        AutoCompleteTextView autoCompleteSubject = view.findViewById(R.id.autoCompleteSubject);
        EditText etDocumentName = view.findViewById(R.id.etDocumentName);
        EditText etType = view.findViewById(R.id.etType);

        autoCompleteSubject.setText(document.subjectName);
        etDocumentName.setText(document.documentName);
        etType.setText(document.type);

        List<String> subjectNames = new ArrayList<>();
        Map<String, String> subjectIdMap = new HashMap<>();

        DatabaseReference subjectsRef = FirebaseDatabase.getInstance().getReference("Subjects");
        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    String id = data.getKey();
                    subjectNames.add(name);
                    subjectIdMap.put(name, id);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, subjectNames);
                autoCompleteSubject.setAdapter(adapter);
                autoCompleteSubject.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi tải môn học", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle("Chỉnh sửa tài liệu")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newSubject = autoCompleteSubject.getText().toString();
                    String newName = etDocumentName.getText().toString();
                    String newType = etType.getText().toString();
                    String subjectId = subjectIdMap.get(newSubject);

                    Document updated = new Document(document.id, subjectId, newSubject, newName, newType, document.youtubeLink);
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
