package com.example.ceamaya.workoutapp.routineActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.labs.RoutineExerciseLab;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineActivity extends AppCompatActivity {
    private static final int REQ_ADD_EXERCISE = 1;
    private static final String EXTRA_ROUTINE_ID = "EXTRA_ROUTINE_ID";
    private Activity activity;
    private int routineId;
    private RoutineExerciseLab routineExerciseLab;
    private ArrayList<Exercise> exercises;
    private ExerciseAdapter exerciseAdapter;

    public static Intent newIntent(Context packageContext, int routineId) {
        Intent intent = new Intent(packageContext, EditRoutineActivity.class);
        intent.putExtra(EXTRA_ROUTINE_ID, routineId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_multiple_fab);
        activity = this;
        routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        if (routineId == -1) {
            finish();
        }
        routineExerciseLab = RoutineExerciseLab.get(activity);
        exercises = routineExerciseLab.getExercises(routineId);

        RecyclerView exerciseRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(linearLayoutManager);
        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int viewHolderPosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(exercises, viewHolderPosition, targetPosition);
                exerciseAdapter.notifyItemMoved(viewHolderPosition, targetPosition);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(exerciseRecyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                exerciseRecyclerView.getContext(), linearLayoutManager.getOrientation());
        exerciseRecyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton addExercisesFab = findViewById(R.id.fab_action1);
        addExercisesFab.setTitle("Add Exercises");
        addExercisesFab.setOnClickListener(addExerciseFabClickListener());

        FloatingActionButton saveFab = findViewById(R.id.fab_action2);
        saveFab.setTitle("Save");
        saveFab.setOnClickListener(saveFabClickListener());
    }

    @NonNull
    private View.OnClickListener saveFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                routineExerciseLab.updateRoutineExercises(routineId, exercises);
                finish();
            }
        };
    }

    @NonNull
    private View.OnClickListener addExerciseFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddExercisesActivity.newIntent(activity, routineId);
                startActivityForResult(intent, REQ_ADD_EXERCISE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_ADD_EXERCISE) {
            exercises.clear();
            exercises.addAll(routineExerciseLab.getExercises(routineId));
            exerciseAdapter.notifyDataSetChanged();
            Snackbar.make(activity.findViewById(android.R.id.content),
                    "Exercises modified.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder {

        ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.simple_list_item, parent, false));
        }

        void bind(Exercise exercise) {
            ((TextView) itemView).setText(exercise.getExerciseName());
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
