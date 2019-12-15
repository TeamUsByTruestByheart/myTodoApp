package com.truestbyheart.todoapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.truestbyheart.todoapp.AddTask;
import com.truestbyheart.todoapp.MainActivity;
import com.truestbyheart.todoapp.Models.TaskModel;
import com.truestbyheart.todoapp.R;
import com.truestbyheart.todoapp.SQLite.DBHelper;

import java.util.ArrayList;


public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private static final String TAG = "TodoListAdapter";
    private DBHelper db;

    Context mContext;
    ArrayList<TaskModel> dataList;
    TaskModel taskData;

    public TodoListAdapter(Context context, ArrayList<TaskModel> dataSet) {
      this.mContext = context;
      this.dataList = dataSet;
      this.db = new DBHelper(context);
      this.taskData = new TaskModel();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(
               R.layout.todo,parent, false
       );
       ViewHolder viewHolder = new ViewHolder(view);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.todo_title.setText(this.dataList.get(position).task);
        holder.todo_date.setText(this.dataList.get(position).date);
        holder.todo_time.setText(this.dataList.get(position).time);



        holder.task_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()) {
                    taskData.isDone = true;
                    taskData.id = dataList.get(position).id;
                   if(db.UpdateTaskStatus(taskData, mContext) == 1) {
                       holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                       Toast.makeText(mContext, "Task completed successfully", Toast.LENGTH_SHORT).show();
                   }
                } else {
                    taskData.isDone = false;
                    taskData.id = dataList.get(position).id;
                    if(db.UpdateTaskStatus(taskData, mContext) == 1) {
                        holder.todo_title.setPaintFlags(holder.todo_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        Toast.makeText(mContext, "Task restored successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.todo_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(mContext, AddTask.class);
                addTaskIntent.putExtra("TASK_ID", position + 1);
                mContext.startActivity(addTaskIntent);
            }
        });
        holder.delete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              boolean response = db.DeleteTask(dataList.get(position).id, mContext);
              if(response) {
                  Toast.makeText(mContext, "Task " + holder.todo_title.getText() + " is successfully deleted", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView todo_title, todo_date, todo_time;
        CheckBox task_complete;
        CardView todo_item;
        Button delete_task;

        public ViewHolder(View view){
            super(view);
            todo_title = view.findViewById(R.id.todoTitle);
            todo_date = view.findViewById(R.id.todoDate);
            todo_time = view.findViewById(R.id.todoTime);
            task_complete = view.findViewById(R.id.taskIsDone);
            todo_item = view.findViewById(R.id.todoItem);
            delete_task = view.findViewById(R.id.deleteTask);

        }
    }


}