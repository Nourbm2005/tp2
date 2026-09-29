package com.example.tp1;

import java.util.ArrayList;

/**
 * TaskStore = a small "memory box" shared by the whole app.
 *
 * It is NOT a screen:
 *   - no XML layout
 *   - no "extends AppCompatActivity"
 *   - not declared in AndroidManifest.xml
 * It is just a normal Java class that holds data.
 */
public class TaskStore {

    // This list remembers if each task is done (true) or not done (false).
    //   done.get(0) -> is task number 1 done?
    //   done.get(1) -> is task number 2 done?
    //   ... same order as the list of titles in MainActivity.
    //
    // "static" -> belongs to the class itself, so every screen can use it
    //             directly (TaskStore.done) without writing "new TaskStore()".
    // "final"  -> the list itself is never replaced, but we can still
    //             add items and change items inside it.
    //
    // It only lives in memory: closing the app erases it.
    public static final ArrayList<Boolean> done = new ArrayList<>();
}