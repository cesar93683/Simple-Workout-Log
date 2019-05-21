package com.example.ceamaya.workoutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
//        String exercise = getIntent().getStringExtra(ExerciseSelectFragment.EXTRA_EXERCISE_NAME);
    }
}
