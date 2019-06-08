package com.devcesar.workoutapp.addExerciseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.labs.ExerciseLab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AddExerciseFragment extends Fragment {

  public static final String EXTRA_NEW_EXERCISES = "EXTRA_NEW_EXERCISES";
  private static final String ARGS_EXERCISE_IDS = "ARGS_EXERCISE_IDS";
  private int[] exerciseIds;
  private Activity activity;
  private ExerciseLab exerciseLab;
  private HashSet<Integer> exercisesIdsToAdd;
  private HashSet<Integer> includedExerciseIds;
  private String filter;
  private ArrayList<Exercise> filteredExercises;
  private ExerciseAdapter exerciseAdapter;

  public AddExerciseFragment() {
    // Required empty public constructor
  }

  public static AddExerciseFragment newInstance(int[] exerciseIds) {
    AddExerciseFragment fragment = new AddExerciseFragment();
    Bundle args = new Bundle();
    args.putIntArray(ARGS_EXERCISE_IDS, exerciseIds);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();

    if (getArguments() != null) {
      exerciseIds = getArguments().getIntArray(ARGS_EXERCISE_IDS);
    }

    includedExerciseIds = new HashSet<>();
    for (int exerciseId : exerciseIds) {
      includedExerciseIds.add(exerciseId);
    }

    exercisesIdsToAdd = new HashSet<>();

    exerciseLab = ExerciseLab.get(activity);

    filter = "";
    filteredExercises = exerciseLab.getFilteredExercise(filter);
    Collections.sort(filteredExercises);
    for (int i = filteredExercises.size() - 1; i >= 0; i--) {
      if (includedExerciseIds.contains(filteredExercises.get(i).getId())) {
        filteredExercises.remove(i);
      }
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View fragmentView = inflater.inflate(R.layout.fragment_select_with_filter, container, false);

    EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
    filterEditText.addTextChangedListener(filterEditTextListener());

    RecyclerView exerciseRecyclerView = fragmentView.findViewById(R.id.recycler_view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    exerciseRecyclerView.setLayoutManager(linearLayoutManager);
    exerciseAdapter = new ExerciseAdapter(filteredExercises);
    exerciseRecyclerView.setAdapter(exerciseAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        exerciseRecyclerView.getContext(), linearLayoutManager.getOrientation());
    exerciseRecyclerView.addItemDecoration(dividerItemDecoration);

    FloatingActionButton saveFab = fragmentView.findViewById(R.id.fab);
    saveFab.setOnClickListener(saveFab());
    return fragmentView;
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
        int[] exerciseIds = new int[exercisesIdsToAdd.size()];
        int i = 0;
        for (Integer val : exercisesIdsToAdd) {
          exerciseIds[i++] = val;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_EXERCISES, exerciseIds);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
      }
    };
  }

  private void updateFilteredExercises() {
    filteredExercises.clear();
    filteredExercises.addAll(exerciseLab.getFilteredExercise(filter));
    Collections.sort(filteredExercises);
    for (int i = filteredExercises.size() - 1; i >= 0; i--) {
      if (includedExerciseIds.contains(filteredExercises.get(i).getId())) {
        filteredExercises.remove(i);
      }
    }
    exerciseAdapter.notifyDataSetChanged();
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textView;
    private final CheckBox checkBox;
    private Exercise exercise;

    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.checkable_list_item, parent, false));
      textView = itemView.findViewById(R.id.text_view);
      checkBox = itemView.findViewById(R.id.check_box);
      checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          checkChanged(isChecked);
        }
      });
      itemView.setOnClickListener(this);
    }

    private void checkChanged(boolean isChecked) {
      if (isChecked) {
        exercisesIdsToAdd.add(exercise.getId());
      } else {
        exercisesIdsToAdd.remove(exercise.getId());
      }
    }

    void bind(Exercise exercise) {
      this.exercise = exercise;
      checkBox.setChecked(exercisesIdsToAdd.contains(exercise.getId()));
      textView.setText(exercise.getName());
    }

    @Override
    public void onClick(View v) {
      checkBox.setChecked(!checkBox.isChecked());
      checkChanged(checkBox.isChecked());
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
