package com.example.ceamaya.workoutapp.routineActivity.editRoutineActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.example.ceamaya.workoutapp.labs.ExerciseLab;
import com.example.ceamaya.workoutapp.labs.RoutineExerciseLab;
import com.example.ceamaya.workoutapp.routineActivity.editRoutineActivity.addExerciseActivity
        .AddExercisesActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineFragment extends Fragment {
    private static final String ARG_ROUTINE_ID = "ARG_ROUTINE_ID";
    private static final String ARG_ROUTINE_NAME = "ARG_ROUTINE_NAME";

    private static final int REQ_ADD_EXERCISE = 1;
    private static final String EXTRA_NEW_EXERCISES = "EXTRA_NEW_EXERCISES";

    private int routineId;
    private String routineName;
    private Activity activity;
    private RoutineExerciseLab routineExerciseLab;
    private ArrayList<Exercise> exercises;
    private ExerciseAdapter exerciseAdapter;
    private ExerciseLab exerciseLab;

    public EditRoutineFragment() {
        // Required empty public constructor
    }

    public static EditRoutineFragment newInstance(int routineId, String routineName) {
        EditRoutineFragment fragment = new EditRoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ROUTINE_ID, routineId);
        args.putString(ARG_ROUTINE_NAME, routineName);
        fragment.setArguments(args);
        return fragment;
    }

    public static Intent returnNewExercisesIntent(int[] exercisesToAdd) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_EXERCISES, exercisesToAdd);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routineId = getArguments().getInt(ARG_ROUTINE_ID);
            routineName = getArguments().getString(ARG_ROUTINE_NAME);
        }
        activity = getActivity();
        exerciseLab = ExerciseLab.get(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_select_multiple_fab, container,
                false);

        routineExerciseLab = RoutineExerciseLab.get(activity);
        exercises = routineExerciseLab.getExercises(routineId);

        RecyclerView exerciseRecyclerView = fragmentView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
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

        FloatingActionButton addExercisesFab = fragmentView.findViewById(R.id.fab_action1);
        addExercisesFab.setTitle(getString(R.string.add_exercies));
        addExercisesFab.setOnClickListener(addExerciseFabClickListener());

        FloatingActionButton saveFab = fragmentView.findViewById(R.id.fab_action2);
        saveFab.setTitle(getString(R.string.save_text));
        saveFab.setOnClickListener(saveFabClickListener());
        return fragmentView;
    }

    @NonNull
    private View.OnClickListener addExerciseFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] exerciseIds = new int[exercises.size()];
                int i = 0;
                for (Exercise exercise : exercises) {
                    exerciseIds[i++] = exercise.getExerciseId();
                }
                Intent intent = AddExercisesActivity.newIntent(activity, exerciseIds, routineName);
                startActivityForResult(intent, REQ_ADD_EXERCISE);
            }
        };
    }

    @NonNull
    private View.OnClickListener saveFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.setResult(Activity.RESULT_OK, intent);
                routineExerciseLab.updateRoutineExercises(routineId, exercises);
                activity.finish();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_ADD_EXERCISE && data != null) {
            int[] newExerciseIds = data.getIntArrayExtra(EXTRA_NEW_EXERCISES);
            for (int exerciseId : newExerciseIds) {
                exercises.add(exerciseLab.getExerciseById(exerciseId));
            }
            exerciseAdapter.notifyDataSetChanged();
            Snackbar.make(activity.findViewById(android.R.id.content), "Exercises modified.",
                    Snackbar.LENGTH_SHORT).show();
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
