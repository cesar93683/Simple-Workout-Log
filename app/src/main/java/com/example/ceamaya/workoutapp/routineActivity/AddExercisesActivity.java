package com.example.ceamaya.workoutapp.routineActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.labs.ExerciseLab;
import com.example.ceamaya.workoutapp.labs.RoutineExerciseLab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddExercisesActivity extends AppCompatActivity {

    private static final String EXTRA_ROUTINE_ID = "EXTRA_ROUTINE_ID";
    private ArrayList<Exercise> includedExercises;
    private ArrayList<Exercise> filteredExercises;
    private ExerciseLab exerciseLab;
    private int routineId;
    private String filter;
    private Activity activity;
    private ExerciseAdapter exerciseAdapter;
    private RoutineExerciseLab routineExerciseLab;
    private int position;

    public static Intent newIntent(Context packageContext, int routineId) {
        Intent intent = new Intent(packageContext, AddExercisesActivity.class);
        intent.putExtra(EXTRA_ROUTINE_ID, routineId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_with_filter);

        activity = this;
        routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        if (routineId == -1) {
            finish();
        }

        exerciseLab = ExerciseLab.get(this);
        routineExerciseLab = RoutineExerciseLab.get(this);

        includedExercises = routineExerciseLab.getExercises(routineId);
        position = includedExercises.size();

        filter = "";
        filteredExercises = exerciseLab.getFilteredExercise(filter);
        Collections.sort(filteredExercises);

        EditText filterEditText = findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(filterEditTextListener());

        RecyclerView exerciseRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(linearLayoutManager);
        exerciseAdapter = new ExerciseAdapter(filteredExercises);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                exerciseRecyclerView.getContext(), linearLayoutManager.getOrientation());
        exerciseRecyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton saveFab = findViewById(R.id.fab);
        saveFab.setOnClickListener(saveFab());
    }

    @NonNull
    private TextWatcher filterEditTextListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter = s.toString();
                updateFilteredExercises();
            }
        };
    }

    private View.OnClickListener saveFab() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                routineExerciseLab.updateRoutineExercises(routineId, includedExercises);
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        };
    }

    private void updateFilteredExercises() {
        filteredExercises.clear();
        filteredExercises.addAll(exerciseLab.getFilteredExercise(filter));
        Collections.sort(filteredExercises);
        exerciseAdapter.notifyDataSetChanged();
    }

    private boolean isExerciseIncluded(int exerciseId) {
        for (Exercise exercise : includedExercises) {
            if (exercise.getExerciseId() == exerciseId) {
                return true;
            }
        }
        return false;
    }

    private void removeIncludedExercise(int exerciseId) {
        for (int i = 0; i < includedExercises.size(); i++) {
            if (includedExercises.get(i).getExerciseId() == exerciseId) {
                includedExercises.remove(i);
                break;
            }
        }
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textView;
        private final CheckBox checkBox;
        private Exercise exercise;

        ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.checkable_list_item, parent, false));
            textView = itemView.findViewById(R.id.text_view);
            checkBox = itemView.findViewById(R.id.check_box);
            itemView.setOnClickListener(this);
        }

        void bind(Exercise exercise) {
            this.exercise = exercise;
            checkBox.setChecked(isExerciseIncluded(exercise.getExerciseId()));
            textView.setText(exercise.getExerciseName());
        }

        @Override
        public void onClick(View v) {
            if (checkBox.isChecked()) {
                removeIncludedExercise(exercise.getExerciseId());
                checkBox.setChecked(false);
            } else {
                Exercise newExercise = new Exercise(exercise.getExerciseName(),
                        exercise.getExerciseId());
                newExercise.setPosition(position++);
                includedExercises.add(newExercise);
                checkBox.setChecked(true);
            }
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
