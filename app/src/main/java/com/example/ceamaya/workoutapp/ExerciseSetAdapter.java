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

public class ExerciseSetAdapter extends ArrayAdapter<ExerciseSet> {

    private final ArrayList<ExerciseSet> exercises;
    private final Context context;

    public ExerciseSetAdapter(@NonNull Context context, @NonNull ArrayList<ExerciseSet> exercises) {
        super(context, 0, exercises);
        this.exercises = exercises;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_list_item, parent,
                    false);
        }
        ((TextView) convertView).setText(String.format(
                context.getString(R.string.exercise_set_adapter_text), position + 1,
                exercises.get(position).toString()));
        return convertView;
    }
}
