package com.example.tp1;

// ----- IMPORTS: tools we bring in from Android / Java -----
import android.content.Intent;      // message used to open another screen
import android.os.Bundle;           // envelope of key -> value data
import android.widget.Button;       // a normal button
import android.widget.Toast;        // small popup message at the bottom

import androidx.activity.result.ActivityResultLauncher;             // opens a screen AND waits for its answer
import androidx.activity.result.contract.ActivityResultContracts;   // ready-made rules for that back-and-forth
import androidx.appcompat.app.AppCompatActivity;                    // base class of every screen
import com.google.android.material.floatingactionbutton.FloatingActionButton; // the round "+" button

import java.util.ArrayList;         // a list that can grow

/**
 * MainActivity = the HOME screen.
 * It owns the list of task titles and opens the other screens.
 * Layout file: activity_main.xml
 */
public class MainActivity extends AppCompatActivity {

    // The "walkie-talkie" used to open AddTaskActivity and receive its answer.
    // It is only declared here; it gets created in onCreate.
    private ActivityResultLauncher<Intent> addTaskLauncher;

    // All the task titles. Only MainActivity owns and changes this list.
    // "final" = the list is never replaced, we only add items inside it.
    private final ArrayList<String> taskList = new ArrayList<>();

    // onCreate = Android calls it automatically when this screen is created.
    // It is the place to set everything up.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // required: lets Android do its own setup
        setContentView(R.layout.activity_main);        // connect this Java file to its XML design

        // ---------- PART 1: register what to do when AddTaskActivity sends a result ----------
        // IMPORTANT: this only REGISTERS the code. It does NOT run now.
        // It runs later, when AddTaskActivity closes with an answer.
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),   // rule: "open a screen, get a result back"
                result -> {
                    // Was it a success (RESULT_OK) and did it send data?
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        // Read the title sent by AddTaskActivity (same key: "TASK_TITLE")
                        String taskTitle = result.getData().getStringExtra("TASK_TITLE");

                        taskList.add(taskTitle);        // save the title in the list
                        TaskStore.done.add(false);      // a new task starts as "not done"

                        Toast.makeText(this, "Task added: " + taskTitle, Toast.LENGTH_SHORT).show();

                        // Open the list screen and give it all the titles
                        Intent listIntent = new Intent(MainActivity.this, TaskListActivity.class);
                        listIntent.putStringArrayListExtra("TASK_LIST", taskList);
                        startActivity(listIntent);
                    }
                }
        );

        // ---------- PART 2: the round "+" button ----------
        // findViewById = find the element in the XML by its id, to control it from Java
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);

        // When "+" is tapped: open AddTaskActivity through the launcher
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);   // launch = open the screen AND wait for its answer
        });

        // ---------- PART 3: the "See my list" button ----------
        Button btnSeeList = findViewById(R.id.btnSeeList);

        btnSeeList.setOnClickListener(v -> {
            // A Bundle can carry an array of true/false, so we copy the
            // done/undone values from TaskStore into a boolean[] array.
            boolean[] flags = new boolean[taskList.size()];
            for (int i = 0; i < flags.length; i++) {
                // The "i < size" check avoids a crash if the two lists differ in size
                flags[i] = i < TaskStore.done.size() && TaskStore.done.get(i);
            }

            // Fill the Bundle (the envelope)
            Bundle listBundle = new Bundle();
            listBundle.putStringArrayList("TITLES", taskList);   // all the titles
            listBundle.putBooleanArray("DONE", flags);           // done/undone of each task

            // Put the Bundle in the Intent (the delivery van) and open MyListActivity
            Intent intent = new Intent(MainActivity.this, MyListActivity.class);
            intent.putExtra("LIST_BUNDLE", listBundle);
            startActivity(intent);
        });
    }
}