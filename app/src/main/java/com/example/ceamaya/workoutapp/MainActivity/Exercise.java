package com.example.ceamaya.workoutapp.MainActivity;

import android.support.annotation.NonNull;

public class Exercise implements Comparable<Exercise> {
    private String exercise;
    private int exerciseId;

    public Exercise(String exercise, int exerciseId) {
        this.exercise = exercise;
        this.exerciseId = exerciseId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public String getExercise() {
        return exercise;
    }

    @Override
    public int compareTo(@NonNull Exercise o) {
        return getExercise().compareTo(o.getExercise());
    }
}
