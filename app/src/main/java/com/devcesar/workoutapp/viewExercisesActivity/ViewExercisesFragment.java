package com.devcesar.workoutapp.viewExercisesActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISES;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.databinding.FragmentSelectBinding;
import com.devcesar.workoutapp.editRoutineActivity.EditRoutineActivity;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.ExerciseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewExercisesFragment extends Fragment {

  private static final String ARG_ID = "ARG_ID";
  private static final String ARG_NAME = "ARG_NAME";
  private static final String ARG_TYPE = "ARG_TYPE";
  private static final int REQ_EDIT = 1;
  private static final int REQ_ADD = 2;

  private int id;
  private String name;
  private int type;
  private String nameType;
  private CategoryOrRoutineLab lab;
  private ArrayList<Exercise> exercises;
  private ExerciseAdapter exerciseAdapter;

  public ViewExercisesFragment() {
    // Required empty public constructor
  }

  public static ViewExercisesFragment newInstance(int id, String name, int type) {
    ViewExercisesFragment fragment = new ViewExercisesFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ID, id);
    args.putInt(ARG_TYPE, type);
    args.putString(ARG_NAME, name);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    id = getArguments().getInt(ARG_ID);
    name = getArguments().getString(ARG_NAME);
    type = getArguments().getInt(ARG_TYPE);
    switch (type) {
      case Constants.TYPE_ROUTINE:
        nameType = getString(R.string.routine);
        lab = CategoryOrRoutineLab.getRoutineLab(getActivity());
        exercises = lab.getExercises(id);
        break;
      case Constants.TYPE_CATEGORY:
        nameType = getString(R.string.category);
        lab = CategoryOrRoutineLab.getCategoryLab(getActivity());
        exercises = lab.getExercises(id);
        Collections.sort(exercises);
        break;
      default:
        throw new RuntimeException("ERROR: type is invalid");
    }
    exerciseAdapter = new ExerciseAdapter(exercises);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FragmentSelectBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select, container, false);

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(exerciseAdapter);

    if (type == Constants.TYPE_ROUTINE) {
      binding.fab.setImageDrawable(
          ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_24dp));
    }
    binding.fab.setOnClickListener(editFabClickListener());
    return binding.getRoot();
  }

  private View.OnClickListener editFabClickListener() {
    return v -> {
      if (type == Constants.TYPE_ROUTINE) {
        Intent intent = EditRoutineActivity.newIntent(getActivity(), id, name);
        startActivityForResult(intent, REQ_EDIT);
      } else {
        Intent intent = AddExercisesActivity.newIntent(getActivity(), exercises, name);
        startActivityForResult(intent, REQ_ADD);
      }
    };
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQ_EDIT) {
        exercises.clear();
        exercises.addAll(lab.getExercises(id));
      } else if (requestCode == REQ_ADD) {
        int[] newExerciseIds = data.getIntArrayExtra(EXTRA_NEW_EXERCISES);
        exercises.addAll(ExerciseUtils.getExercises(newExerciseIds, getContext()));
        lab.updateExercises(id, exercises);
      }
      exerciseAdapter.notifyDataSetChanged();
      Snackbar.make(getActivity().findViewById(android.R.id.content),
          String.format(getString(R.string.x_updated), nameType), Snackbar.LENGTH_SHORT).show();
    }
  }

  private void createDeleteExerciseDialog(final int exerciseId) {
    new AlertDialog.Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_exercise)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
          lab.deleteExercise(id, exerciseId);
          exercises.clear();
          exercises.addAll(lab.getExercises(id));
          exerciseAdapter.notifyDataSetChanged();
          Snackbar
              .make(getActivity().findViewById(android.R.id.content), R.string.exercise_deleted,
                  Snackbar.LENGTH_SHORT).show();
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
      ((TextView) itemView).setText(exercise.getName());
    }

    @Override
    public void onClick(View view) {
      Intent intent = ExerciseActivity
          .newIntent(getActivity(), exercise.getName(), exercise.getId());
      startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
      createDeleteExerciseDialog(exercise.getId());
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
