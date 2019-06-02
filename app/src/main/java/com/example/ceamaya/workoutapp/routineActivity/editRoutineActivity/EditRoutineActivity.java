package com.example.ceamaya.workoutapp.routineActivity.editRoutineActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ceamaya.workoutapp.R;

public class EditRoutineActivity extends AppCompatActivity {
    private static final String EXTRA_ROUTINE_ID = "EXTRA_ROUTINE_ID";
    private static final String EXTRA_ROUTINE_NAME = "EXTRA_ROUTINE_NAME";
    private static final String TAG = "EditRoutineActivity";

    public static Intent newIntent(Context packageContext, int routineId, String routineName) {
        Intent intent = new Intent(packageContext, EditRoutineActivity.class);
        intent.putExtra(EXTRA_ROUTINE_ID, routineId);
        intent.putExtra(EXTRA_ROUTINE_NAME, routineName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);
        int routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        String routineName = getIntent().getStringExtra(EXTRA_ROUTINE_NAME);
        if (routineId == -1) {
            finish();
        }
        setTitle(routineName);
        setContentView(R.layout.activity_edit_exercise);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = EditRoutineFragment.newInstance(routineId, routineName);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
