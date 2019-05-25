package com.example.ceamaya.workoutapp;

import java.util.ArrayList;
import java.util.Date;

public class Workout {
    private Date date;
    private ArrayList<ExerciseSet> exerciseSets;

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
