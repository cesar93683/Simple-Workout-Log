package com.example.ceamaya.workoutapp.routineActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.R;

public class RoutineActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTINE_NAME = "EXTRA_ROUTINE_NAME";
    private static final String EXTRA_ROUTINE_ID = "EXTRA_ROUTINE_ID";


    public static Intent newIntent(Context packageContext, String routineName, int routineId) {
        Intent intent = new Intent(packageContext, RoutineActivity.class);
        intent.putExtra(EXTRA_ROUTINE_NAME, routineName);
        intent.putExtra(EXTRA_ROUTINE_ID, routineId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        String routineName = getIntent().getStringExtra(EXTRA_ROUTINE_NAME);
        int routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        if (routineId == -1) {
            finish();
        }
        setTitle(routineName);
        setContentView(R.layout.activity_edit_exercise);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = RoutineFragment.newInstance(routineId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
