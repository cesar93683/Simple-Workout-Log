package com.devcesar.workoutapp.routineActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISES;

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
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityExerciseLab;
import com.devcesar.workoutapp.labs.RoutineLab;
import com.devcesar.workoutapp.routineActivity.editRoutineActivity.EditRoutineActivity;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.Exercise;
import java.util.ArrayList;
import java.util.List;

public class NameFragment extends Fragment {

  private static final String ARG_ID = "ARG_ID";
  private static final String ARG_NAME = "ARG_NAME";
  private static final String ARG_TYPE = "ARG_TYPE";
  private static final int REQ_EDIT = 1;
  private static final int REQ_ADD = 2;

  private int id;
  private String nameType;
  private String name;
  private Activity activity;
  private NamedEntityExerciseLab lab;
  private ArrayList<Exercise> exercises;
  private ExerciseAdapter exerciseAdapter;
  private View fragmentView;
  private int type;

  public NameFragment() {
    // Required empty public constructor
  }

  public static NameFragment newInstance(int id, String name, int type) {
    NameFragment fragment = new NameFragment();
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
    activity = getActivity();
    if (type == Constants.TYPE_ROUTINE) {
      nameType = getString(R.string.routine);
      lab = RoutineLab.get(activity);
    } else { // type == SelectFragment.TYPE_CATEGORY
      nameType = getString(R.string.category);
      lab = CategoryLab.get(activity);
    }
    exercises = lab.getExercises(id);
    exerciseAdapter = new ExerciseAdapter(exercises);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    fragmentView = inflater.inflate(R.layout.fragment_select, container, false);

    RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.addItemDecoration(
        new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    recyclerView.setAdapter(exerciseAdapter);

    FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
    if (type == Constants.TYPE_ROUTINE) {
      fab.setImageDrawable(
          ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_24dp));
    }
    fab.setOnClickListener(editFabClickListener());
    return fragmentView;
  }

  private View.OnClickListener editFabClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        if (type == Constants.TYPE_ROUTINE) {
          Intent intent = EditRoutineActivity.newIntent(activity, id, name);
          startActivityForResult(intent, REQ_EDIT);
        } else {
          Intent intent = AddExercisesActivity.newIntent(activity, exercises, name);
          startActivityForResult(intent, REQ_ADD);
        }
      }
    };
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQ_EDIT) {
      exercises.clear();
      exercises.addAll(lab.getExercises(id));
      exerciseAdapter.notifyDataSetChanged();
      Snackbar.make(fragmentView, String.format(getString(R.string.x_updated), nameType),
          Snackbar.LENGTH_SHORT).show();
    } else if (resultCode == Activity.RESULT_OK && requestCode == REQ_ADD) {
      int[] newExerciseIds = data.getIntArrayExtra(EXTRA_NEW_EXERCISES);
      for (int exerciseId : newExerciseIds) {
        exercises.add(ExerciseLab.get(getContext()).getExerciseById(exerciseId));
      }
      lab.updateExercises(id, exercises);
      exerciseAdapter.notifyDataSetChanged();
    }
  }

  private void createDeleteExerciseDialog(final int exerciseId) {
    new AlertDialog.Builder(activity)
        .setMessage(R.string.are_you_sure_delete_exercise)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            lab.deleteExercise(id, exerciseId);
            exercises.clear();
            exercises.addAll(lab.getExercises(id));
            exerciseAdapter.notifyDataSetChanged();
            Snackbar.make(fragmentView, R.string.exercise_deleted, Snackbar.LENGTH_SHORT).show();
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
      ((TextView) itemView).setText(exercise.getName());
    }

    @Override
    public void onClick(View view) {
      Intent intent = ExerciseActivity
          .newIntent(activity, exercise.getName(), exercise.getId());
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
