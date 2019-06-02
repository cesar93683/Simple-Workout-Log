package com.example.ceamaya.workoutapp.routineActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.exerciseActivity.ExerciseActivity;
import com.example.ceamaya.workoutapp.labs.RoutineExerciseLab;
import com.example.ceamaya.workoutapp.routineActivity.editRoutineActivity.EditRoutineActivity;

import java.util.ArrayList;
import java.util.List;

public class RoutineFragment extends Fragment {
    private static final String ARG_ROUTINE_ID = "ARG_ROUTINE_ID";
    private static final String ARG_ROUTINE_NAME = "ARG_ROUTINE_NAME";
    private static final int REQ_EDIT_ROUTINE = 1;

    private int routineId;
    private String routineName;
    private Activity activity;
    private RoutineExerciseLab routineExerciseLab;
    private ArrayList<Exercise> exercises;
    private ExerciseAdapter exerciseAdapter;
    private View fragmentView;

    public RoutineFragment() {
        // Required empty public constructor
    }

    public static RoutineFragment newInstance(int routineId, String routineName) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        args.putString(ARG_ROUTINE_NAME, routineName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID);
            routineName = getArguments().getString(ARG_ROUTINE_NAME);
        }
        activity = getActivity();
        routineExerciseLab = RoutineExerciseLab.get(activity);
        exercises = routineExerciseLab.getExercises(routineId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_select, container, false);

        RecyclerView exerciseRecyclerView = fragmentView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        exerciseRecyclerView.setLayoutManager(linearLayoutManager);
        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                exerciseRecyclerView.getContext(), linearLayoutManager.getOrientation());
        exerciseRecyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton editRoutineFab = fragmentView.findViewById(R.id.fab);
        editRoutineFab.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.ic_mode_edit_black_24dp));
        editRoutineFab.setOnClickListener(editRoutineFabClickListener());
        return fragmentView;
    }

    private View.OnClickListener editRoutineFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = EditRoutineActivity.newIntent(activity, routineId, routineName);
                startActivityForResult(intent, REQ_EDIT_ROUTINE);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_EDIT_ROUTINE) {
            exercises.clear();
            exercises.addAll(routineExerciseLab.getExercises(routineId));
            exerciseAdapter.notifyDataSetChanged();
            Snackbar.make(fragmentView, "Routine modified.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createDeleteExerciseDialog(final int exerciseId) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this exercise?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        routineExerciseLab.deleteExerciseFromRoutine(routineId, exerciseId);
                        exercises.clear();
                        exercises.addAll(routineExerciseLab.getExercises(routineId));
                        exerciseAdapter.notifyDataSetChanged();
                        Snackbar.make(fragmentView, "Exercise deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private Exercise exercise;

        ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.simple_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(Exercise exercise) {
            this.exercise = exercise;
            ((TextView) itemView).setText(exercise.getExerciseName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = ExerciseActivity.newIntent(activity, exercise.getExerciseName(),
                    exercise.getExerciseId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            createDeleteExerciseDialog(exercise.getExerciseId());
            return true;
        }

    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

        private final List<Exercise> exercises;

        ExerciseAdapter(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        @NonNull
        @Override
        public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            return new ExerciseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
            Exercise exercise = exercises.get(position);
            holder.bind(exercise);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }
    }

}
