package com.example.ceamaya.workoutapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ExerciseSelectFragment extends Fragment {

    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_select, container, false);
        ListView exerciseListView = view.findViewById(R.id.exercise_list_view);
        ArrayList<String> exercises = new ArrayList<>();
        exercises.add("workout 1");
        exercises.add("workout 2");
        activity = getActivity();
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(activity, exercises);
        exerciseListView.setAdapter(exerciseAdapter);
        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ExerciseActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
