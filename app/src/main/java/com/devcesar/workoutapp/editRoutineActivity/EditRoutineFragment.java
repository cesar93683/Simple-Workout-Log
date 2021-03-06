package com.devcesar.workoutapp.editRoutineActivity;

import static com.devcesar.workoutapp.addExerciseActivity.AddExerciseFragment.EXTRA_NEW_EXERCISE_IDS;
import static com.devcesar.workoutapp.utils.NamedEntitiesUtils.getIds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.addExerciseActivity.AddExercisesActivity;
import com.devcesar.workoutapp.databinding.FragmentSelectMultipleFabBinding;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditRoutineFragment extends Fragment {

  private static final String ARG_ROUTINE_ID = "ARG_ROUTINE_ID";
  private static final String ARG_ROUTINE_NAME = "ARG_ROUTINE_NAME";

  private static final int REQ_ADD_EXERCISE = 1;
  private static final String EXTRA_EXERCISE_IDS = "EXTRA_EXERCISE_IDS";
  private static final String EXTRA_HAS_BEEN_MODIFIED = "EXTRA_HAS_BEEN_MODIFIED";

  private List<NamedEntity> exercises;
  private ExerciseAdapter exerciseAdapter;
  private boolean hasBeenModified;
  private ItemTouchHelper itemTouchHelper;
  private NamedEntity routine;
  private CoordinatorLayout coordinatorLayout;

  public EditRoutineFragment() {
    // Required empty public constructor
  }

  public static EditRoutineFragment newInstance(NamedEntity routine) {
    EditRoutineFragment fragment = new EditRoutineFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ROUTINE_ID, routine.getId());
    args.putString(ARG_ROUTINE_NAME, routine.getName());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int routineId = getArguments().getInt(ARG_ROUTINE_ID);
    String routineName = getArguments().getString(ARG_ROUTINE_NAME);
    routine = new NamedEntity(routineName, routineId);

    if (savedInstanceState == null) {
      CategoryOrRoutineLab routineLab = CategoryOrRoutineLab.getRoutineLab(getContext());
      exercises = routineLab.getExercises(routineId, getContext());
      hasBeenModified = false;
    } else {
      ArrayList<Integer> exerciseIds = savedInstanceState.getIntegerArrayList(EXTRA_EXERCISE_IDS);
      exercises = ExerciseLab.get(getContext()).findExercises(exerciseIds);
      hasBeenModified = savedInstanceState.getBoolean(EXTRA_HAS_BEEN_MODIFIED);
    }

    exerciseAdapter = new ExerciseAdapter(exercises);
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

    itemTouchHelper = new ItemTouchHelper(getItemTouchHelperCallback());
    itemTouchHelper.attachToRecyclerView(binding.recyclerView);

    binding.fabAction1.setTitle(getString(R.string.add_exercises));
    binding.fabAction1.setOnClickListener(view -> addExercise());

    binding.fabAction2.setTitle(getString(R.string.save));
    binding.fabAction2.setOnClickListener(view -> saveExercises());

    coordinatorLayout = binding.coordinatorLayout;

    return binding.getRoot();
  }

  private SimpleCallback getItemTouchHelperCallback() {
    return new SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int viewHolderPosition = viewHolder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        Collections.swap(exercises, viewHolderPosition, targetPosition);
        exerciseAdapter.notifyItemMoved(viewHolderPosition, targetPosition);
        hasBeenModified = true;
        return false;
      }

      @Override
      public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

      }

      @Override
      public boolean isLongPressDragEnabled() {
        return false;
      }
    };
  }

  private void addExercise() {
    Intent intent = AddExercisesActivity.newIntent(getActivity(), exercises, routine.getName());
    startActivityForResult(intent, REQ_ADD_EXERCISE);
  }

  private void saveExercises() {
    CategoryOrRoutineLab.getRoutineLab(getActivity()).updateExercises(routine.getId(), exercises);
    Intent intent = new Intent();
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().finish();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putIntegerArrayList(EXTRA_EXERCISE_IDS, getIds(exercises));
    outState.putBoolean(EXTRA_HAS_BEEN_MODIFIED, hasBeenModified);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQ_ADD_EXERCISE && data != null) {
      ArrayList<Integer> newExerciseIds = data.getIntegerArrayListExtra(EXTRA_NEW_EXERCISE_IDS);
      List<NamedEntity> newExercises = ExerciseLab.get(getContext()).findExercises(newExerciseIds);
      Collections.sort(newExercises);
      exercises.addAll(newExercises);
      exerciseAdapter.notifyDataSetChanged();
      hasBeenModified = true;
      showSnackbar(String.format(getString(R.string.item_updated), getString(R.string.routine)));
    }
  }

  private void showSnackbar(String text) {
    Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT).show();
  }

  void onBackPressed() {
    if (hasBeenModified) {
      showSaveChangesDialog();
    } else {
      getActivity().finish();
    }
  }

  private void showSaveChangesDialog() {
    new AlertDialog.Builder(getActivity())
        .setTitle(R.string.save_changes)
        .setMessage(R.string.would_save_changes_routine)
        .setNeutralButton(R.string.cancel, null)
        .setNegativeButton(R.string.discard, (dialog, which) -> getActivity().finish())
        .setPositiveButton(R.string.save, (dialog, which) -> saveExercises())
        .show();
  }

  private void showDeleteExerciseDialog(final int exerciseId) {
    new AlertDialog.Builder(getActivity())
        .setMessage(String.format(getString(R.string.delete_item_confirmation),
            getString(R.string.exercise).toLowerCase()))
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteExercise(exerciseId))
        .show();
  }

  private void deleteExercise(int exerciseId) {
    for (int j = 0; j < exercises.size(); j++) {
      if (exercises.get(j).getId() == exerciseId) {
        exercises.remove(j);
        break;
      }
    }
    hasBeenModified = true;
    exerciseAdapter.notifyDataSetChanged();
    showSnackbar(String.format(getString(R.string.item_deleted), getString(R.string.exercise)));
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder {

    private final TextView textView;
    private NamedEntity exercise;

    @SuppressLint("ClickableViewAccessibility")
    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.draggable_list_item, parent, false));

      textView = itemView.findViewById(R.id.text_view);

      ImageView imageView = itemView.findViewById(R.id.drag_image_view);
      imageView.setOnTouchListener((view, event) -> {
        itemTouchHelper.startDrag(ExerciseHolder.this);
        return true;
      });

      itemView.setOnLongClickListener(view -> {
        showDeleteExerciseDialog(exercise.getId());
        return false;
      });
    }

    void bind(NamedEntity exercise) {
      this.exercise = exercise;
      textView.setText(exercise.getName());
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
