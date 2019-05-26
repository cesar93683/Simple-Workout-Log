package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Workout;

import java.text.DateFormat;
import java.util.ArrayList;

class WorkoutHistoryAdapter extends ArrayAdapter {

    private static final String TAG = "WorkoutHistoryAdapter";
    private final Context context;
    private final ArrayList<Workout> workouts;

    WorkoutHistoryAdapter(@NonNull Context context, ArrayList<Workout> workouts) {
        super(context, 0, workouts);
        this.context = context;
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_list_item,
                    parent, false);
        }
        Workout workout = workouts.get(position);

        String dateText = DateFormat.getDateInstance().format(workout.getDate().getTime());
        String timeText = DateFormat.getTimeInstance().format(workout.getDate().getTime());

        TextView dateTextView = convertView.findViewById(R.id.date_text_view);
        dateTextView.setText(dateText);
        TextView timeTextView = convertView.findViewById(R.id.time_text_view);
        timeTextView.setText(timeText);

        LinearLayout exerciseSetsContainer = convertView.findViewById(R.id.exercise_sets_container);
        exerciseSetsContainer.removeAllViews();
        for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
            @SuppressLint("InflateParams") TextView view = (TextView) LayoutInflater.from
                    (context).inflate(R.layout.simple_list_item, null);
            view.setText(exerciseSet.toString());
            exerciseSetsContainer.addView(view);
        }

        return convertView;
    }
}
