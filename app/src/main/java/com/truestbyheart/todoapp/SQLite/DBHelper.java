package com.truestbyheart.todoapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.truestbyheart.todoapp.MainActivity;
import com.truestbyheart.todoapp.Models.TaskModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    public static final String DATABASE_NAME = "todo.db";

    /**
     * @author Daniel Charles Mwangila
     * @description Declare the table name together with the column that will be used for by the app.
     * */
    public static final String TABLE_NAME = "todo";
    public static final String ARCHIVE_TABLE_NAME = "archive";

    // Todo table  column
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_ISDONE = "isDone";

    // Archive table column
    public static final String ARCHIVE_ID = "id";
    public static final String ARCHIVE_TASK_ID = "todo_id";



    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "create table " + TABLE_NAME +
                "(" + COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_TASK + " text," + COLUMN_DATE + " date," +
                COLUMN_TIME + " time," +
                COLUMN_ISDONE + " boolean)";


        String CREATE_ARCHIVE_TABLE_QUERY = "create table " + ARCHIVE_TABLE_NAME +
                "(" + ARCHIVE_ID + " integer primary key autoincrement, "+ ARCHIVE_TASK_ID +
                " integer)";


        db.execSQL(CREATE_TABLE_QUERY);
        db.execSQL(CREATE_ARCHIVE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARCHIVE_TABLE_NAME);
        onCreate(db);
    }

    /**
     * @description let create the CRUD operations.
     *
     * */

    public boolean InsertTask(TaskModel task){
       SQLiteDatabase db = this.getWritableDatabase();
       try {
           ContentValues data = new ContentValues();
           data.put("task",task.task);
           data.put("date", task.date);
           data.put("time", task.time);
           data.put("isDone", task.isDone);
           db.insert(TABLE_NAME,null,data);
       } catch (SQLException error) {
           Log.d(TAG, "InsertTask: " + error);
       }


        return true;
    }

    public ArrayList<TaskModel> getAllTask() {
        ArrayList<TaskModel> todoList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where isDone = 0", null);
        try{
            if(res.moveToFirst()){
                do{
                    TaskModel todoTask = new TaskModel();
                    todoTask.id = res.getInt(res.getColumnIndex(COLUMN_ID));
                    todoTask.task = res.getString(res.getColumnIndex(COLUMN_TASK));
                    todoTask.date = res.getString(res.getColumnIndex(COLUMN_DATE));
                    todoTask.time = res.getString(res.getColumnIndex(COLUMN_TIME));

                    todoList.add(todoTask);
                } while(res.moveToNext());
            }
        }catch (Exception error) {
            Log.d(TAG, "getAllTask: " + error);
        } finally {
            if(res != null && !res.isClosed()){
                res.close();
            }
        }

        return todoList;
    }


    public int UpdateTaskStatus(TaskModel task, Context context){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISDONE,task.isDone);

           try{
               int result = db.update(TABLE_NAME,values,"id = ?",new String[]{ task.id.toString() } );
               MainActivity.notifyAdapter();
               return result;
           }catch(Exception e){
               Toast.makeText(context, "Failed to complete the task" + e, Toast.LENGTH_SHORT).show();
               return 0;
           }
    }

    public TaskModel getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        TaskModel task = new TaskModel();
        Cursor cursor = db.rawQuery("select * from todo where id = " + id, null);

        try{
            if(cursor.moveToFirst()){
                task.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                task.task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK));
                task.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                task.time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            }
        }catch(SQLException e){
            Log.d(TAG, "getTask: "+ e);
        } finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

      return task;
    }

    public boolean updateTask(TaskModel task, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK,task.task);
        values.put(COLUMN_DATE,task.date);
        values.put(COLUMN_TIME,task.time);

        try{
            db.update(TABLE_NAME,values,"id = ?",new String[]{ task.id.toString() } );
            return true;
        }catch(Exception e){
            Toast.makeText(context, "Failed to complete the task" + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean DeleteTask(int id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(TABLE_NAME, COLUMN_ID + "=" + id,null) > 0;
        } catch(SQLException e) {
            Toast.makeText(context, "Failed to Execute Task" + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ArrayList<TaskModel> getAllArchive() {
        ArrayList<TaskModel> archiveList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where isDone = 1", null);
        try{
            if(res.moveToFirst()){
                do{
                    TaskModel todoTask = new TaskModel();
                    todoTask.id = res.getInt(res.getColumnIndex(COLUMN_ID));
                    todoTask.task = res.getString(res.getColumnIndex(COLUMN_TASK));
                    todoTask.date = res.getString(res.getColumnIndex(COLUMN_DATE));
                    todoTask.time = res.getString(res.getColumnIndex(COLUMN_TIME));

                    archiveList.add(todoTask);
                } while(res.moveToNext());
            }
        }catch (Exception error) {
            Log.d(TAG, "getAllTask: " + error);
        } finally {
            if(res != null && !res.isClosed()){
                res.close();
            }
        }

        return archiveList;
    }


}
