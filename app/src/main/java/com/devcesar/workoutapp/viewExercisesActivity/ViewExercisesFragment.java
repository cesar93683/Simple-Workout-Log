package com.devcesar.workoutapp.viewExercisesActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISE_IDS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.databinding.FragmentSelectBinding;
import com.devcesar.workoutapp.editRoutineActivity.EditRoutineActivity;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewExercisesFragment extends Fragment {

  private static final String ARG_ID = "ARG_ID";
  private static final String ARG_NAME = "ARG_NAME";
  private static final String ARG_TYPE = "ARG_TYPE";
  private static final int REQ_EDIT = 1;
  private static final int REQ_ADD = 2;

  private int type;
  private String nameType;
  private CategoryOrRoutineLab lab;
  private List<NamedEntity> exercises;
  private ExerciseAdapter exerciseAdapter;
  private NamedEntity namedEntity;
  private CoordinatorLayout coordinatorLayout;

  public ViewExercisesFragment() {
    // Required empty public constructor
  }

  public static ViewExercisesFragment newInstance(NamedEntity namedEntity, int type) {
    ViewExercisesFragment fragment = new ViewExercisesFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ID, namedEntity.getId());
    args.putInt(ARG_TYPE, type);
    args.putString(ARG_NAME, namedEntity.getName());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int id = getArguments().getInt(ARG_ID);
    String name = getArguments().getString(ARG_NAME);
    namedEntity = new NamedEntity(name, id);

    type = getArguments().getInt(ARG_TYPE);
    switch (type) {
      case Constants.TYPE_ROUTINE:
        nameType = getString(R.string.routine);
        lab = CategoryOrRoutineLab.getRoutineLab(getActivity());
        exercises = lab.getExercises(id, getContext());
        break;
      case Constants.TYPE_CATEGORY:
        nameType = getString(R.string.category);
        lab = CategoryOrRoutineLab.getCategoryLab(getActivity());
        exercises = lab.getExercises(id, getContext());
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
    binding.fab.setOnClickListener(view -> edit());

    coordinatorLayout = binding.coordinatorLayout;

    return binding.getRoot();
  }

  private void edit() {
    if (type == Constants.TYPE_ROUTINE) {
      Intent intent = EditRoutineActivity.newIntent(getActivity(), namedEntity);
      startActivityForResult(intent, REQ_EDIT);
    } else {
      Intent intent = AddExercisesActivity
          .newIntent(getActivity(), exercises, namedEntity.getName());
      startActivityForResult(intent, REQ_ADD);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQ_EDIT) {
        exercises.clear();
        exercises.addAll(lab.getExercises(namedEntity.getId(), getContext()));
      } else if (requestCode == REQ_ADD) {
        ArrayList<Integer> newExerciseIds = data.getIntegerArrayListExtra(EXTRA_NEW_EXERCISE_IDS);
        exercises.addAll(ExerciseLab.get(getContext()).findExercises(newExerciseIds));
        Collections.sort(exercises);
        lab.updateExercises(namedEntity.getId(), exercises);
      }
      exerciseAdapter.notifyDataSetChanged();
      showSnackbar(String.format(getString(R.string.item_updated), nameType));
    }
  }

  private void showSnackbar(String text) {
    Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT).show();
  }

  private void showDeleteExerciseDialog(int exerciseId) {
    new AlertDialog.Builder(getActivity())
        .setMessage(String
            .format(getString(R.string.delete_item_confirmation),
                getString(R.string.exercise).toLowerCase()))
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteExercise(exerciseId))
        .show();
  }

  private void deleteExercise(int exerciseId) {
    lab.deleteExercise(namedEntity.getId(), exerciseId, getContext());
    exercises.clear();
    exercises.addAll(lab.getExercises(namedEntity.getId(), getContext()));
    exerciseAdapter.notifyDataSetChanged();
    showSnackbar(String.format(getString(R.string.item_deleted), getString(R.string.exercise)));
  }

  private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

    private final List<NamedEntity> exercises;

    ExerciseAdapter(List<NamedEntity> exercises) {
      this.exercises = exercises;
    }

    @Override
    public int getItemCount() {
      return exercises.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
      NamedEntity exercise = exercises.get(position);
      holder.bind(exercise);
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new ExerciseHolder(layoutInflater, parent);
    }
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
      View.OnLongClickListener {

    private NamedEntity exercise;

    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    void bind(NamedEntity exercise) {
      this.exercise = exercise;
      ((TextView) itemView).setText(exercise.getName());
    }

    @Override
    public void onClick(View view) {
      Intent intent = ExerciseActivity.newIntent(getActivity(), exercise);
      startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
      showDeleteExerciseDialog(exercise.getId());
      return true;
    }

  }

}
