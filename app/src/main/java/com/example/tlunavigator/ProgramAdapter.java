package com.example.tlunavigator;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.model.TrainingProgram;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {

    private Context context ;
    private List<TrainingProgram> trainingPrograms;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("TrainingProgram");
    public ProgramAdapter (Context context,List<TrainingProgram> trainingPrograms){
        this.context = context;
        this.trainingPrograms = trainingPrograms;
    }
    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program,parent,false);
        return  new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ProgramViewHolder holder, int position) {
        TrainingProgram Program = trainingPrograms.get(position);
        holder.tvName.setText("Tên chương trình: " + Program.programName);
        holder.tvCode.setText("Mã chương trình: " + Program.id);
        holder.tvCredit.setText("Số tín chỉ: " + Program.credit);

        holder.btnDelete.setOnClickListener(v-> DeleteProgram(Program));

    }

    private void DeleteProgram(TrainingProgram program) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa chương trình đào tạo")
                .setMessage("Bạn có chắc muốn xóa chương trình đào tạo này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbRef.child(program.id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return trainingPrograms.size();
    }

    public static class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvCredit;
        ImageButton btnEdit, btnDelete;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProgramName);
            tvCode = itemView.findViewById(R.id.tvProgramCode);
            tvCredit = itemView.findViewById(R.id.tvProgramCredit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
