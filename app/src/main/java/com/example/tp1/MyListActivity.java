package com.example.tp1;

// ----- IMPORTS -----
import android.os.Bundle;               // envelope of key -> value data
import android.widget.ArrayAdapter;     // turns a list of texts into rows
import android.widget.ListView;         // the scrollable list on screen
import android.widget.TextView;         // a piece of text on screen

import androidx.appcompat.app.AppCompatActivity;   // base class of every screen

import java.util.ArrayList;             // a list that can grow

/**
 * MyListActivity = a read-only SUMMARY screen.
 * Layout file: activity_my_list.xml
 * It shows a counter ("2 of 5 tasks done") and every task with ✔ or ☐.
 * It only displays: it changes nothing and opens no other screen.
 */
public class MyListActivity extends AppCompatActivity {

    // Android calls onCreate automatically when this screen opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // required Android setup
        setContentView(R.layout.activity_my_list);     // connect to the XML design

        // Find the two elements of the XML by their ids
        TextView tvSummary = findViewById(R.id.tvSummary);   // the counter text
        ListView lvMyList = findViewById(R.id.lvMyList);     // the list of tasks

        // The rows we will show. It starts empty.
        ArrayList<String> rows = new ArrayList<>();

        // Default text, used if there is no task or no Bundle
        tvSummary.setText(R.string.no_tasks_yet);

        // ---------- RECEIVE THE BUNDLE ----------
        // getIntent() = the Intent that opened this screen
        // getBundleExtra("LIST_BUNDLE") = take the Bundle out of it
        // The key must match the one used in MainActivity.
        Bundle bundle = getIntent().getBundleExtra("LIST_BUNDLE");

        if (bundle != null) {
            // Read the two things that were put in the Bundle
            ArrayList<String> titles = bundle.getStringArrayList("TITLES");   // all task names
            boolean[] done = bundle.getBooleanArray("DONE");                  // true/false for each task

            // Only continue if both arrived and there is at least one task
            if (titles != null && done != null && !titles.isEmpty()) {

                int doneCount = 0;   // how many tasks are done

                // Go through every task, one by one
                for (int i = 0; i < titles.size(); i++) {
                    if (done[i]) doneCount++;   // count it if done
                    // Build the row: ✔ if done, ☐ if not, then the title
                    rows.add((done[i] ? "✔ " : "☐ ") + titles.get(i));
                }

                // Show the counter, for example: "2 of 5 tasks done"
                tvSummary.setText(doneCount + " of " + titles.size() + " tasks done");
            }
        }

        // Put the rows into the ListView:
        // (screen, row design ready-made by Android, the list to show)
        lvMyList.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, rows));
    }
}