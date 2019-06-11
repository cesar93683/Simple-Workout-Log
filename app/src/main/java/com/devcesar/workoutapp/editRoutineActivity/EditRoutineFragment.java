package com.devcesar.workoutapp.editRoutineActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISE_IDS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.databinding.FragmentSelectMultipleFabBinding;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.ExerciseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineFragment extends Fragment {

  private static final String ARG_ROUTINE_ID = "ARG_ROUTINE_ID";
  private static final String ARG_ROUTINE_NAME = "ARG_ROUTINE_NAME";

  private static final int REQ_ADD_EXERCISE = 1;

  private int routineId;
  private String routineName;
  private ArrayList<Exercise> exercises;
  private ExerciseAdapter exerciseAdapter;
  private boolean hasBeenModified;
  private ItemTouchHelper itemTouchHelper;

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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    routineId = getArguments().getInt(ARG_ROUTINE_ID);
    routineName = getArguments().getString(ARG_ROUTINE_NAME);
    exercises = CategoryOrRoutineLab.getRoutineLab(getActivity()).getExercises(routineId);
    exerciseAdapter = new ExerciseAdapter(exercises);
    hasBeenModified = false;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FragmentSelectMultipleFabBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_multiple_fab, container, false);

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(exerciseAdapter);

    itemTouchHelper = new ItemTouchHelper(
        new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
          @Override
          public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
              RecyclerView.ViewHolder target) {
            int viewHolderPosition = viewHolder.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();
            Collections.swap(exercises, viewHolderPosition, targetPosition);
            exerciseAdapter.notifyItemMoved(viewHolderPosition, targetPosition);
            hasBeenModified = true;
            return false;
          }

          @Override
          public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

          }

          @Override
          public boolean isLongPressDragEnabled() {
            return false;
          }
        });
    itemTouchHelper.attachToRecyclerView(binding.recyclerView);

    binding.fabAction1.setTitle(getString(R.string.add_exercises));
    binding.fabAction1.setOnClickListener(addExerciseFabClickListener());

    binding.fabAction2.setTitle(getString(R.string.save_text));
    binding.fabAction2.setOnClickListener(saveFabClickListener());
    return binding.getRoot();
  }

  @NonNull
  private View.OnClickListener addExerciseFabClickListener() {
    return v -> {
      Intent intent = AddExercisesActivity.newIntent(getActivity(), exercises, routineName);
      startActivityForResult(intent, REQ_ADD_EXERCISE);
    };
  }

  @NonNull
  private View.OnClickListener saveFabClickListener() {
    return v -> saveExercisesToRoutine();
  }

  private void saveExercisesToRoutine() {
    Intent intent = new Intent();
    getActivity().setResult(Activity.RESULT_OK, intent);
    CategoryOrRoutineLab.getRoutineLab(getActivity()).updateExercises(routineId, exercises);
    getActivity().finish();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQ_ADD_EXERCISE && data != null) {
      ArrayList<Integer> newExerciseIds = data.getIntegerArrayListExtra(EXTRA_NEW_EXERCISE_IDS);
      exercises.addAll(ExerciseUtils.getExercises(newExerciseIds, getContext()));
      exerciseAdapter.notifyDataSetChanged();
      hasBeenModified = true;
      Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.exercise_updated,
          Snackbar.LENGTH_SHORT).show();
    }
  }

  public void onBackPressed() {
    if (hasBeenModified) {
      createSaveChangesDialog();
    } else {
      getActivity().finish();
    }
  }

  private void createSaveChangesDialog() {
    new AlertDialog.Builder(getActivity())
        .setTitle(R.string.save_changes)
        .setMessage(R.string.would_save_changes_routine)
        .setNeutralButton(R.string.cancel, null)
        .setNegativeButton(R.string.discard, (dialog, which) -> getActivity().finish())
        .setPositiveButton(R.string.save, (dialog, which) -> saveExercisesToRoutine())
        .show();
  }

  private void createDeleteExerciseDialog(final int exerciseId) {
    new AlertDialog.Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_exercise)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
          for (int j = 0; j < exercises.size(); j++) {
            if (exercises.get(j).getId() == exerciseId) {
              exercises.remove(j);
              break;
            }
          }
          hasBeenModified = true;
          exerciseAdapter.notifyDataSetChanged();
          Snackbar
              .make(getActivity().findViewById(android.R.id.content), R.string.exercise_deleted,
                  Snackbar.LENGTH_SHORT).show();
        })
        .show();
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder {

    final TextView textView;
    Exercise exercise;

    @SuppressLint("ClickableViewAccessibility")
    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.draggable_list_item, parent, false));
      textView = itemView.findViewById(R.id.text_view);
      ImageView imageView = itemView.findViewById(R.id.drag_image_view);
      imageView.setOnTouchListener((v, event) -> {
        itemTouchHelper.startDrag(ExerciseHolder.this);
        return true;
      });
      itemView.setOnLongClickListener(v -> {
        createDeleteExerciseDialog(exercise.getId());
        return false;
      });
    }

    void bind(Exercise exercise) {
      this.exercise = exercise;
      textView.setText(exercise.getName());
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
