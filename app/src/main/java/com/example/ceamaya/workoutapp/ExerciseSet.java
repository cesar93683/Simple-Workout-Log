package com.example.ceamaya.workoutapp;

public class ExerciseSet {
    private final int reps;
    private final int weight;
    private final int id;

    ExerciseSet(int reps, int weight, int id) {
        this.reps = reps;
        this.weight = weight;
        this.id = id;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return reps + " Reps @ " + (weight > 0 ? weight : "-") + " LB";
    }
}
