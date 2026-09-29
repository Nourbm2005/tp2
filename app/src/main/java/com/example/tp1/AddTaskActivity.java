package com.example.tp1;

// ----- IMPORTS -----
import android.content.Intent;      // used to send the answer back
import android.os.Bundle;           // data saved between screen recreations (used by onCreate)
import android.widget.Button;       // the Save button
import android.widget.EditText;     // the box where the user types
import android.widget.Toast;        // small popup message

import androidx.appcompat.app.AppCompatActivity;   // base class of every screen

/**
 * AddTaskActivity = the screen where the user types a new task.
 * Layout file: activity_add_task.xml
 * It does NOT save the task itself: it sends the title BACK to MainActivity.
 */
public class AddTaskActivity extends AppCompatActivity {

    // Android calls onCreate automatically when this screen opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // required Android setup
        setContentView(R.layout.activity_add_task);    // connect to the XML design

        // Find the two elements of the XML by their ids
        EditText etTaskTitle = findViewById(R.id.etTaskTitle);   // the text box
        Button btnSaveTask = findViewById(R.id.btnSaveTask);     // the Save button

        // The code inside runs ONLY when the user taps Save
        btnSaveTask.setOnClickListener(v -> {

            // Read the text and remove spaces at the start and the end
            String taskTitle = etTaskTitle.getText().toString().trim();

            // If nothing was typed: warn the user and STOP (return = leave this code)
            if (taskTitle.isEmpty()) {
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare the answer: a new Intent that only carries data back
            Intent resultIntent = new Intent();
            resultIntent.putExtra("TASK_TITLE", taskTitle);   // label "TASK_TITLE" + the text
            // By using putExtra(), you can pass some information to the activity you are intending to start

            // Say "everything went fine" and attach the answer
            setResult(RESULT_OK, resultIntent);

            // Close this screen. Android goes back to MainActivity,
            // and the launcher's callback there receives resultIntent.
            finish();
        });
    }
}