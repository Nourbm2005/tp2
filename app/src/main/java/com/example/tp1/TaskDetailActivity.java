package com.example.tp1; //hedhi li fiha getbundleextra

// ----- IMPORTS -----
import android.os.Bundle;           // envelope of key -> value data
import android.widget.Button;       // the Close button
import android.widget.CheckBox;     // the done / undone box
import android.widget.TextView;     // a piece of text on screen

import androidx.appcompat.app.AppCompatActivity;   // base class of every screen

/**
 * TaskDetailActivity = the screen that shows ONE task.
 * Layout file: activity_task_detail.xml  (this is the one with NESTED layouts:
 * RelativeLayout > LinearLayout > RelativeLayout / LinearLayout).
 * The user can check or uncheck the task to mark it done or not done.
 */
public class TaskDetailActivity extends AppCompatActivity {

    // Android calls onCreate automatically when this screen opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);             // required Android setup
        setContentView(R.layout.activity_task_detail);  // connect to the XML design

        // Find every element of the XML by its id
        TextView tvTitle = findViewById(R.id.tvDetailTitle);    // shows the task name
        TextView tvNumber = findViewById(R.id.tvDetailNumber);  // shows "#3"
        TextView tvStatus = findViewById(R.id.tvStatus);        // shows "Done ✔" or "Not done"
        CheckBox cbDone = findViewById(R.id.cbDone);            // the box to check
        Button btnClose = findViewById(R.id.btnClose);          // the Close button

        // ---------- RECEIVE THE BUNDLE ----------
        // getIntent() = the Intent (delivery van) that opened this screen
        // getBundleExtra("TASK_BUNDLE") = take the Bundle (envelope) out of it
        // The key must match the one used in TaskListActivity.
        Bundle bundle = getIntent().getBundleExtra("TASK_BUNDLE");

        // Position of this task in the list, starting at 0.
        // "final" = it is set once and never changed (needed to use it inside the listener below).
        final int index;

        // Check the Bundle really arrived, otherwise the app could crash
        if (bundle != null) {
            // Read the values. The 2nd argument is a default, used if the key is missing.
            tvTitle.setText(bundle.getString("TASK_TITLE", "No title"));

            int position = bundle.getInt("TASK_POSITION", 0);   // starts at 1
            tvNumber.setText("#" + position);
            index = position - 1;                               // back to a list index (starts at 0)

            boolean done = bundle.getBoolean("TASK_DONE", false);
            cbDone.setChecked(done);   // tick the box if the task is already done
            tvStatus.setText(done ? R.string.status_done : R.string.status_not_done);
        } else {
            index = -1;   // -1 = "no task", so nothing will be saved later
        }

        // ---------- WHEN THE USER CHECKS OR UNCHECKS THE BOX ----------
        // This code is only registered now. It runs every time the box changes.
        cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new state in TaskStore so other screens can see it
            // (the checks avoid a crash if the index is not valid)
            if (index >= 0 && index < TaskStore.done.size()) {
                TaskStore.done.set(index, isChecked);
            }
            // Update the status text on screen
            tvStatus.setText(isChecked ? R.string.status_done : R.string.status_not_done);
        });

        // ---------- CLOSE BUTTON ----------
        // finish() closes this screen and goes back to the list.
        // The list then runs onResume() and refreshes its check marks.
        btnClose.setOnClickListener(v -> finish());
    }
}