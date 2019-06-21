package com.devcesar.workoutapp.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.FragmentSelectNoFabBinding;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

  private static final int TYPE_DEFAULT = 0;
  private static final int TYPE_CHECKABLE = 1;

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
    FragmentSelectNoFabBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_no_fab, container, false);

    List<SettingsFragmentHelper> settingsFragmentHelpers = new ArrayList<>();

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        getString(R.string.clear_all_workouts),
        view -> clearAllWorkouts(),
        TYPE_DEFAULT));

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        "Delete All Exercises, Categories, Routines",
        view -> deleteAll(),
        TYPE_DEFAULT));

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        "Import Default Exercises, Categories, Routines",
        view -> importDefault(),
        TYPE_DEFAULT));

    settingsFragmentHelpers.add(new SettingsFragmentHelper(
        "Auto start timer after set is added.",
        view -> autoStartTimer(),
        TYPE_CHECKABLE));

    SettingsAdapter adapter = new SettingsAdapter(settingsFragmentHelpers);
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.setAdapter(adapter);

    return binding.getRoot();
  }

  private void clearAllWorkouts() {
    Toast.makeText(getContext(), "clearAllWorkouts", Toast.LENGTH_SHORT).show();
  }

  private void deleteAll() {
    Toast.makeText(getContext(), "deleteAll", Toast.LENGTH_SHORT).show();
  }

  private void importDefault() {
    Toast.makeText(getContext(), "importDefault", Toast.LENGTH_SHORT).show();
  }

  private void autoStartTimer() {
    Toast.makeText(getContext(), "autoStartTimer", Toast.LENGTH_SHORT).show();
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
      itemView.setOnClickListener(view -> {
        settingsFragmentHelper.onClickListener.onClick(itemView);
        checkBox.setChecked(!checkBox.isChecked());
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

    SettingsFragmentHelper(String text, OnClickListener onClickListener, int type) {
      this.text = text;
      this.onClickListener = onClickListener;
      this.type = type;
    }

  }

}
