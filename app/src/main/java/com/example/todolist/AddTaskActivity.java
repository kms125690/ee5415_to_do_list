package com.example.todolist;

import static com.example.todolist.MainActivity.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class AddTaskActivity extends AppCompatActivity implements TextWatcher {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        EditText editText = (EditText) findViewById(R.id.editText);
        Intent i = getIntent();
        noteId = i.getIntExtra("taskId", -1);
        if (noteId != -1) {
            editText.setText(tasks.get(noteId));
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tasks.set(noteId, String.valueOf(s));
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.todolist", Context.MODE_PRIVATE);
        if (MainActivity.set == null) {
            MainActivity.set = new HashSet<String>();
        } else {
            MainActivity.set.clear();
        }
        MainActivity.set.addAll(tasks);
        sharedPreferences.edit().remove("tasks").apply();
        sharedPreferences.edit().putStringSet("tasks", MainActivity.set).apply();
    }
    @Override
    public void afterTextChanged(Editable s) {

    }

    public void addTask(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
