package com.devcesar.workoutapp.mainActivity;

import static com.devcesar.workoutapp.utils.Constants.SHOULD_AUTO_START_TIMER;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.database.InitDatabase;
import com.devcesar.workoutapp.databinding.FragmentSelectNoFabBinding;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

  private static final int TYPE_DEFAULT = 0;
  private static final int TYPE_CHECKABLE = 1;
  private CoordinatorLayout coordinatorLayout;

  public SettingsFragment() {
    // Required empty public constructor
  }

  public static SettingsFragment newInstance() {
    return new SettingsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    List<SettingsFragmentHelper> settingsFragmentHelpers = new ArrayList<>();

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        getString(R.string.delete_all_workouts),
        view -> showDeleteAllWorkoutsDialog(),
        TYPE_DEFAULT));

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        getString(R.string.delete_all_items),
        view -> showDeleteAllItemsDialog(),
        TYPE_DEFAULT));

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        getString(R.string.import_default_items),
        view -> showImportDefaultItemsDialog(),
        TYPE_DEFAULT));

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    boolean shouldAutoStartTimer = prefs.getBoolean(SHOULD_AUTO_START_TIMER, false);

    SettingsFragmentHelper autoStartTimer = new SettingsFragmentHelper(
        getString(R.string.auto_start_timer),
        view -> prefs.edit()
            .putBoolean(SHOULD_AUTO_START_TIMER,
                ((CheckBox) (view.findViewById(R.id.check_box))).isChecked())
            .apply(),
        TYPE_CHECKABLE);
    autoStartTimer.setCheckedByDefault(shouldAutoStartTimer);

    settingsFragmentHelpers.add(autoStartTimer);

    FragmentSelectNoFabBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_no_fab, container, false);

    coordinatorLayout = binding.coordinatorLayout;

    SettingsAdapter adapter = new SettingsAdapter(settingsFragmentHelpers);
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.setAdapter(adapter);

    return binding.getRoot();
  }

  private void showDeleteAllWorkoutsDialog() {
    new AlertDialog.Builder(getActivity())
        .setMessage("Are you sure want to delete all workouts?")
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              WorkoutLab.get(getActivity()).deleteAll();
              Snackbar.make(coordinatorLayout, "All workouts deleted", Snackbar.LENGTH_LONG).show();
            })
        .show();
  }

  private void showDeleteAllItemsDialog() {
    new AlertDialog.Builder(getActivity())
        .setMessage("Are you sure want to delete all exercises, categories, and routines?")
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              WorkoutLab.get(getActivity()).deleteAll();
              CategoryOrRoutineLab.getCategoryLab(getActivity()).deleteAll();
              CategoryOrRoutineLab.getRoutineLab(getActivity()).deleteAll();
              ExerciseLab.get(getActivity()).deleteAll();
              ((MainActivity) getActivity()).refreshFragments();
              Snackbar.make(coordinatorLayout, "All exercises, categories, and routines deleted",
                  Snackbar.LENGTH_LONG).show();
            })
        .show();
  }

  private void showImportDefaultItemsDialog() {
    new AlertDialog.Builder(getActivity())
        .setMessage("Are you sure want to import the default exercises, categories, and routines?")
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              InitDatabase.run(getActivity());
              ((MainActivity) getActivity()).refreshFragments();
              Snackbar.make(coordinatorLayout, "All exercises, categories, and routines deleted",
                  Snackbar.LENGTH_LONG).show();
            })
        .show();
  }

  private class SimpleViewHolder extends RecyclerView.ViewHolder {

    SimpleViewHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
    }

    void bind(SettingsFragmentHelper settingsFragmentHelper) {
      ((TextView) itemView).setText(settingsFragmentHelper.text);
      itemView.setOnClickListener(settingsFragmentHelper.onClickListener);
    }
  }

  private class CheckableViewHolder extends RecyclerView.ViewHolder {

    CheckableViewHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item_checkbox_right, parent, false));
    }

    void bind(SettingsFragmentHelper settingsFragmentHelper) {
      CheckBox checkBox = itemView.findViewById(R.id.check_box);
      checkBox.setChecked(settingsFragmentHelper.isCheckedByDefault());
      checkBox.setClickable(false);
      itemView.setOnClickListener(view -> {
        checkBox.setChecked(!checkBox.isChecked());
        settingsFragmentHelper.onClickListener.onClick(itemView);
      });
      TextView textView = itemView.findViewById(R.id.text_view);
      textView.setText(settingsFragmentHelper.text);
    }
  }

  private class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SettingsFragmentHelper> list;

    SettingsAdapter(List<SettingsFragmentHelper> list) {
      this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      switch (viewType) {
        case TYPE_DEFAULT:
          return new SimpleViewHolder(layoutInflater, parent);
        case TYPE_CHECKABLE:
          return new CheckableViewHolder(layoutInflater, parent);
      }
      throw new RuntimeException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      SettingsFragmentHelper item = list.get(position);
      switch (holder.getItemViewType()) {
        case TYPE_DEFAULT:
          ((SimpleViewHolder) holder).bind(item);
          break;
        case TYPE_CHECKABLE:
          ((CheckableViewHolder) holder).bind(item);
          break;
      }
    }

    @Override
    public int getItemViewType(int position) {
      return list.get(position).type;
    }

    @Override
    public int getItemCount() {
      return list.size();
    }

  }

  class SettingsFragmentHelper {

    private final String text;
    private final View.OnClickListener onClickListener;
    private final int type;
    private boolean isCheckedByDefault;

    SettingsFragmentHelper(String text, OnClickListener onClickListener, int type) {
      this.text = text;
      this.onClickListener = onClickListener;
      this.type = type;
    }

    boolean isCheckedByDefault() {
      return isCheckedByDefault;
    }

    void setCheckedByDefault(boolean checkedByDefault) {
      isCheckedByDefault = checkedByDefault;
    }
  }
}
