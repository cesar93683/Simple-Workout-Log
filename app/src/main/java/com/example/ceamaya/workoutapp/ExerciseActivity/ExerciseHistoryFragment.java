package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class ExerciseHistoryFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private int exerciseId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        HashMap<String, ArrayList<ExerciseSet>> exerciseSetMap = exerciseDB.getSets(exerciseId);

        return getLayoutInflater().inflate(R.layout.fragment_exercise_history, container, false);
    }

    public ExerciseHistoryFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int exerciseId) {
        ExerciseHistoryFragment fragment = new ExerciseHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_ID, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }
}
