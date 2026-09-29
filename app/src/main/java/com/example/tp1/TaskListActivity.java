package com.example.tp1;

// ----- IMPORTS -----
import android.content.Intent;          // message used to open another screen
import android.os.Bundle;               // envelope of key -> value data
import android.widget.ArrayAdapter;     // turns a list of texts into rows
import android.widget.ListView;         // the scrollable list on screen

import androidx.appcompat.app.AppCompatActivity;   // base class of every screen

import java.util.ArrayList;             // a list that can grow

/**
 * TaskListActivity = the screen that shows all tasks, each with a check mark.
 * Layout file: activity_task_list.xml
 * Tapping a task opens TaskDetailActivity and sends it a Bundle.
 */
public class TaskListActivity extends AppCompatActivity {

    // Two lists on purpose:
    //  - titles  = the REAL task names (what we send to the detail screen)
    //  - display = the same names with a mark in front: "✔ Buy milk" or "☐ Buy milk"
    //              (what the user SEES in the list)
    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<String> display = new ArrayList<>();

    // The adapter connects the "display" list to the ListView on screen
    private ArrayAdapter<String> adapter;

    // Android calls onCreate automatically when this screen opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);             // required Android setup
        setContentView(R.layout.activity_task_list);    // connect to the XML design

        // Find the ListView in the XML
        ListView lvTasks = findViewById(R.id.lvTasks);

        // Read the titles sent by MainActivity (same key: "TASK_LIST")
        ArrayList<String> received = getIntent().getStringArrayListExtra("TASK_LIST");
        if (received != null) titles.addAll(received);   // copy them, only if something arrived

        // Create the adapter: (screen, row design ready-made by Android, the list to show)
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        lvTasks.setAdapter(adapter);   // plug the adapter into the ListView

        // Runs ONLY when the user taps a row.
        // "position" = which row was tapped, starting at 0.
        lvTasks.setOnItemClickListener((parent, view, position, id) -> {

            // Fill the Bundle (the envelope) with the task's data
            Bundle bundle = new Bundle();
            bundle.putString("TASK_TITLE", titles.get(position));            // the real title
            bundle.putInt("TASK_POSITION", position + 1);                    // +1: humans count from 1
            bundle.putBoolean("TASK_DONE", TaskStore.done.get(position));    // is it done?

            // Put the Bundle in the Intent (the delivery van) and open the detail screen
            Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
            intent.putExtra("TASK_BUNDLE", bundle);   // the whole Bundle travels under ONE key
            startActivity(intent);
        });
    }

    // onResume = Android calls it every time this screen becomes visible,
    // including when the user comes back from the detail screen.
    // That is how the check marks refresh after a task is checked.
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    // Rebuild what the user sees, using the current done/undone values
    private void refreshList() {
        display.clear();   // empty the visible list first
        for (int i = 0; i < titles.size(); i++) {
            // Is task i done? (the "i < size" check avoids a crash)
            boolean isDone = i < TaskStore.done.size() && TaskStore.done.get(i);
            // condition ? ifTrue : ifFalse   ->  ✔ if done, ☐ if not
            display.add((isDone ? "✔ " : "☐ ") + titles.get(i));
        }
        adapter.notifyDataSetChanged();   // tell the ListView to redraw itself
    }
}