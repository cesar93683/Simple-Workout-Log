package com.example.ceamaya.workoutapp;

public class ExerciseSet {
    private int reps;
    private int weight;
    private final int exerciseId;
    private int setNumber;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private long timeStamp;

    public ExerciseSet(int reps, int weight, int exerciseId, int setNumber) {
        this.reps = reps;
        this.weight = weight;
        this.exerciseId = exerciseId;
        this.setNumber = setNumber;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    @Override
    public String toString() {
        return "Set " + setNumber + " - " + reps + " Reps @ " + (weight > 0 ? weight : "-") + " LB";
    }
}
