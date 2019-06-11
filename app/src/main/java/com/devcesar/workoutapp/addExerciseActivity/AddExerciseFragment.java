package com.devcesar.workoutapp.addExerciseActivity;

import static com.devcesar.workoutapp.utils.ExerciseUtils.getExerciseIds;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.FragmentSelectWithFilterBinding;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.Exercise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AddExerciseFragment extends Fragment {

  public static final String EXTRA_NEW_EXERCISE_IDS = "EXTRA_NEW_EXERCISE_IDS";
  private static final String ARGS_EXERCISE_IDS = "ARGS_EXERCISE_IDS";
  private HashSet<Exercise> exercisesToAdd;
  private HashSet<Integer> exerciseIdsToExclude;
  private String filter;
  private ArrayList<Exercise> filteredExercises;
  private ExerciseAdapter exerciseAdapter;

  public AddExerciseFragment() {
    // Required empty public constructor
  }

  public static AddExerciseFragment newInstance(ArrayList<Integer> exerciseIdsToExclude) {
    AddExerciseFragment fragment = new AddExerciseFragment();
    Bundle args = new Bundle();
    args.putIntegerArrayList(ARGS_EXERCISE_IDS, exerciseIdsToExclude);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    exerciseIdsToExclude = new HashSet<>(getArguments().getIntegerArrayList(ARGS_EXERCISE_IDS));
    exercisesToAdd = new HashSet<>();

    filter = "";
    filteredExercises = new ArrayList<>();
    exerciseAdapter = new ExerciseAdapter(filteredExercises);
    updateFilteredExercises();
  }

  private void updateFilteredExercises() {
    filteredExercises.clear();
    filteredExercises.addAll(ExerciseLab.get(getActivity()).getFilteredExercises(filter));
    // api 24 to remove for-loop
//    filteredExercises.removeIf(exercise -> exerciseIdsToExclude.contains(exercise.getId()));
    for (int i = filteredExercises.size() - 1; i >= 0; i--) {
      if (exerciseIdsToExclude.contains(filteredExercises.get(i).getId())) {
        filteredExercises.remove(i);
      }
    }
    Collections.sort(filteredExercises);
    exerciseAdapter.notifyDataSetChanged();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FragmentSelectWithFilterBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_with_filter, container, false);

    binding.fab.setOnClickListener(saveFab());
    binding.fab.setImageDrawable(
        ContextCompat.getDrawable(getContext(), R.drawable.ic_check_black_24dp));

    binding.filterEditText.addTextChangedListener(filterEditTextListener());

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(exerciseAdapter);

    return binding.getRoot();
  }

  private View.OnClickListener saveFab() {
    return v -> {
      Intent intent = new Intent();
      intent.putExtra(EXTRA_NEW_EXERCISE_IDS, getExerciseIds(exercisesToAdd));
      getActivity().setResult(Activity.RESULT_OK, intent);
      getActivity().finish();
    };
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

  private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textView;
    private final CheckBox checkBox;
    private Exercise exercise;

    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.checkable_list_item, parent, false));
      itemView.setOnClickListener(this);
      textView = itemView.findViewById(R.id.text_view);
      checkBox = itemView.findViewById(R.id.check_box);
      checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChanged(isChecked));
    }

    private void checkBoxChanged(boolean isChecked) {
      if (isChecked) {
        exercisesToAdd.add(exercise);
      } else {
        exercisesToAdd.remove(exercise);
      }
    }

    void bind(Exercise exercise) {
      this.exercise = exercise;
      checkBox.setChecked(exercisesToAdd.contains(exercise));
      textView.setText(exercise.getName());
    }

    @Override
    public void onClick(View v) {
      checkBox.setChecked(!checkBox.isChecked());
      checkBoxChanged(checkBox.isChecked());
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
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
