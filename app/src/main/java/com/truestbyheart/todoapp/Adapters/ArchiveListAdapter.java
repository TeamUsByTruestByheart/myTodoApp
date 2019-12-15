package com.truestbyheart.todoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.truestbyheart.todoapp.Models.TaskModel;
import com.truestbyheart.todoapp.R;
import com.truestbyheart.todoapp.SQLite.DBHelper;

import java.util.ArrayList;

public class ArchiveListAdapter extends RecyclerView.Adapter<ArchiveListAdapter.ArchiveViewHolder>{
    private DBHelper db;

    Context mContext;
    ArrayList<TaskModel> dataList;

    public ArchiveListAdapter(Context mContext, ArrayList<TaskModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.db = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.archive_task, parent,false
        );
        ArchiveViewHolder archiveViewHolder = new ArchiveViewHolder(view);
        return archiveViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArchiveViewHolder holder, final int position) {
        holder.archive_title.setText(this.dataList.get(position).task);
        holder.archive_date.setText(this.dataList.get(position).date);
        holder.archive_time.setText(this.dataList.get(position).time);

        holder.delete_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean response = db.DeleteTask(dataList.get(position).id, mContext);
                if(response) {
                    Toast.makeText(mContext, "Task " + holder.archive_title.getText() + " is successfully deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    public static class ArchiveViewHolder extends RecyclerView.ViewHolder{
        TextView archive_title, archive_date, archive_time;
        Button delete_archive;

        public ArchiveViewHolder(@NonNull View view) {
            super(view);
            archive_title = view.findViewById(R.id.archiveTitle);
            archive_date = view.findViewById(R.id.archiveDate);
            archive_time = view.findViewById(R.id.archiveTime);
            delete_archive = view.findViewById(R.id.deleteArchive);
        }
    }
}
