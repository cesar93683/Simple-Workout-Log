package com.example.ceamaya.workoutapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ExerciseAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> exercises;
    private final Context context;

    ExerciseAdapter(@NonNull Context context, @NonNull ArrayList<String> exercises) {
        super(context, 0, exercises);
        this.exercises = exercises;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.exercise_list_item, parent,
                    false);
        }
        TextView exerciseTextView = convertView.findViewById(R.id.exercise_text_view);
        exerciseTextView.setText(exercises.get(position));
        return convertView;
    }
}
