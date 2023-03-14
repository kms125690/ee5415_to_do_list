package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> tasks = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("tasks", null);
        tasks.clear();
        if (set != null) {
            tasks.addAll(set);
        } else {
            tasks.add("E.g. Take a break ...");
            set = new HashSet<String>();
            set.addAll(tasks);
            sharedPreferences.edit().putStringSet("tasks", set).apply();
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
                i.putExtra("taskId", position);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tasks.remove(position);
                                SharedPreferences sharedPreferences =MainActivity.this.getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
                                if (set == null) {
                                    set = new HashSet<String>();
                                } else {
                                    set.clear();
                                }
                                set.addAll(tasks);
                                sharedPreferences.edit().remove("tasks").apply();
                                sharedPreferences.edit().putStringSet("tasks", set).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add) {
            tasks.add("");
            SharedPreferences sharedPreferences =
                    this.getSharedPreferences("com.example.todo", Context.MODE_PRIVATE);
            if (set == null) {
                set = new HashSet<String>();
            } else {
                set.clear();
            }
            set.addAll(tasks);
            arrayAdapter.notifyDataSetChanged();
            sharedPreferences.edit().remove("tasks").apply();
            sharedPreferences.edit().putStringSet("tasks", set).apply();
            Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
            i.putExtra("taskId", tasks.size() - 1);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}