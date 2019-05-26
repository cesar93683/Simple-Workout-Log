package com.example.ceamaya.workoutapp;

import java.util.ArrayList;
import java.util.Date;

public class Workout {
    private final Date date;
    private final ArrayList<ExerciseSet> exerciseSets;

    public Workout(Date date, ArrayList<ExerciseSet> exerciseSets) {
        this.date = date;
        this.exerciseSets = exerciseSets;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }

}
