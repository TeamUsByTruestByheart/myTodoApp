package com.truestbyheart.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.truestbyheart.todoapp.Adapters.ArchiveListAdapter;
import com.truestbyheart.todoapp.Adapters.TodoListAdapter;
import com.truestbyheart.todoapp.SQLite.DBHelper;

public class ArchiveActivity extends AppCompatActivity {
    private DBHelper mDatabase;
    ArchiveListAdapter mAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        mDatabase = new DBHelper(this);

        recyclerView = findViewById(R.id.rv_archive);
        mAdapter = new ArchiveListAdapter(this, mDatabase.getAllArchive());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
