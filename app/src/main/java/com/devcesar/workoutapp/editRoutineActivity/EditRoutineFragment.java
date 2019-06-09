package com.devcesar.workoutapp.editRoutineActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISES;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.databinding.FragmentSelectMultipleFabBinding;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.RoutineLab;
import com.devcesar.workoutapp.utils.Exercise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineFragment extends Fragment {

  private static final String ARG_ROUTINE_ID = "ARG_ROUTINE_ID";
  private static final String ARG_ROUTINE_NAME = "ARG_ROUTINE_NAME";

  private static final int REQ_ADD_EXERCISE = 1;

  private int routineId;
  private String routineName;
  private Activity activity;
  private RoutineLab routineLab;
  private ArrayList<Exercise> exercises;
  private ExerciseAdapter exerciseAdapter;
  private ExerciseLab exerciseLab;
  private boolean hasBeenModified;
  private ItemTouchHelper itemTouchHelper;
  private FragmentSelectMultipleFabBinding binding;

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
    if (getArguments() != null) {
      routineId = getArguments().getInt(ARG_ROUTINE_ID);
      routineName = getArguments().getString(ARG_ROUTINE_NAME);
    }
    activity = getActivity();
    exerciseLab = ExerciseLab.get(activity);
    routineLab = RoutineLab.get(activity);
    exercises = routineLab.getExercises(routineId);
    exerciseAdapter = new ExerciseAdapter(exercises);
    hasBeenModified = false;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = DataBindingUtil
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
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = AddExercisesActivity.newIntent(activity, exercises, routineName);
        startActivityForResult(intent, REQ_ADD_EXERCISE);
      }
    };
  }

  @NonNull
  private View.OnClickListener saveFabClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveExercisesToRoutine();
      }
    };
  }

  private void saveExercisesToRoutine() {
    Intent intent = new Intent();
    activity.setResult(Activity.RESULT_OK, intent);
    routineLab.updateExercises(routineId, exercises);
    activity.finish();
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
      hasBeenModified = true;
      Snackbar.make(binding.getRoot(), R.string.exercise_updated, Snackbar.LENGTH_SHORT).show();
    }
  }

  public void onBackPressed() {
    if (hasBeenModified) {
      createSaveChangesDialog();
    } else {
      activity.finish();
    }
  }

  private void createSaveChangesDialog() {
    new AlertDialog.Builder(activity)
        .setTitle(R.string.save_changes)
        .setMessage(R.string.would_save_changes_routine)
        .setNeutralButton(R.string.cancel, null)
        .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            activity.finish();
          }
        })
        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            saveExercisesToRoutine();
          }
        })
        .show();
  }

  private void createDeleteExerciseDialog(final int exerciseId) {
    new AlertDialog.Builder(activity)
        .setMessage(R.string.are_you_sure_delete_exercise)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            for (int j = 0; j < exercises.size(); j++) {
              if (exercises.get(j).getId() == exerciseId) {
                exercises.remove(j);
                break;
              }
            }
            hasBeenModified = true;
            exerciseAdapter.notifyDataSetChanged();
            Snackbar.make(binding.getRoot(), R.string.exercise_deleted,
                Snackbar.LENGTH_SHORT).show();
          }
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
      imageView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          itemTouchHelper.startDrag(ExerciseHolder.this);
          return true;
        }
      });
      itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          createDeleteExerciseDialog(exercise.getId());
          return false;
        }
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
