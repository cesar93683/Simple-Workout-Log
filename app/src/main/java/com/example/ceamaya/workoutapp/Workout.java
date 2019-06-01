package com.example.ceamaya.workoutapp;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Workout implements Comparable<Workout> {
    private final ArrayList<ExerciseSet> exerciseSets;
    private final long timeStamp;

    public Workout(long timeStamp, ArrayList<ExerciseSet> exerciseSets) {
        this.timeStamp = timeStamp;
        this.exerciseSets = exerciseSets;
    }

    public ArrayList<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }

    @Override
    public int compareTo(@NonNull Workout o) {
        return (int) (o.getTimeStamp() - getTimeStamp());
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
