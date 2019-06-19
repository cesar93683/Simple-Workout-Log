package com.devcesar.workoutapp.addExerciseActivity;

import static com.devcesar.workoutapp.utils.NamedEntitiesUtils.getIds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.FragmentSelectWithFilterBinding;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AddExerciseFragment extends Fragment {

  public static final String EXTRA_NEW_EXERCISE_IDS = "EXTRA_NEW_EXERCISE_IDS";
  private static final String ARGS_EXERCISE_IDS = "ARGS_EXERCISE_IDS";
  private HashSet<NamedEntity> exercisesToAdd;
  private HashSet<Integer> exerciseIdsToExclude;
  private String textFilter;
  private ArrayList<NamedEntity> filteredExercises;
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

    textFilter = "";
    filteredExercises = new ArrayList<>();
    exerciseAdapter = new ExerciseAdapter(filteredExercises);
    updateFilteredExercises();
  }

  private void updateFilteredExercises() {
    filteredExercises.clear();
    filteredExercises.addAll(ExerciseLab.get(getActivity()).getFiltered(textFilter));
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

    binding.fab.setOnClickListener(view -> save());
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

  private void save() {
      Intent intent = new Intent();
      intent.putExtra(EXTRA_NEW_EXERCISE_IDS, getIds(exercisesToAdd));
      getActivity().setResult(Activity.RESULT_OK, intent);
      getActivity().finish();
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
        textFilter = s.toString();
        updateFilteredExercises();
      }
    };
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textView;
    private final CheckBox checkBox;
    private NamedEntity exercise;

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

    void bind(NamedEntity exercise) {
      this.exercise = exercise;
      checkBox.setChecked(exercisesToAdd.contains(exercise));
      textView.setText(exercise.getName());
    }

    @Override
    public void onClick(View view) {
      checkBox.setChecked(!checkBox.isChecked());
      checkBoxChanged(checkBox.isChecked());
    }
  }

  private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

    private final List<NamedEntity> exercises;

    ExerciseAdapter(List<NamedEntity> exercises) {
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
      NamedEntity exercise = exercises.get(position);
      holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
      return exercises.size();
    }
  }
}
