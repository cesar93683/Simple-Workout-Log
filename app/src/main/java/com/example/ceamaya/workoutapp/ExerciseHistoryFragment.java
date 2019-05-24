package com.example.ceamaya.workoutapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExerciseHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.fragment_exercise_history, container, false);
    }

    public ExerciseHistoryFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new ExerciseHistoryFragment();
    }
}
