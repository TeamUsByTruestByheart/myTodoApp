package com.truestbyheart.todoapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.truestbyheart.todoapp.Adapters.TodoListAdapter;
import com.truestbyheart.todoapp.SQLite.DBHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private DBHelper mDatabase;
    public static TodoListAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = new DBHelper(this);

        recyclerView = findViewById(R.id.rv_todo);
        adapter = new TodoListAdapter(this, mDatabase.getAllTask());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(addTaskIntent);
            }
        });

    }
    public static void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()){
           case R.id.archive_task:
               startActivity(new Intent(this, ArchiveActivity.class));
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }

    }


}
