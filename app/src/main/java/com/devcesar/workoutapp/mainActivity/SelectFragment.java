package com.devcesar.workoutapp.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.DialogEditOrDeleteBinding;
import com.devcesar.workoutapp.databinding.DialogInputBinding;
import com.devcesar.workoutapp.databinding.FragmentSelectWithFilterBinding;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.viewExercisesActivity.ViewExercisesActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFragment extends Fragment {

  private ArrayList<NamedEntity> filtered;
  private String textFilter;
  private NamedEntityAdapter namedEntityAdapter;
  private CoordinatorLayout coordinatorLayout;
  private int type;
  private String name;
  private NamedEntityLab lab;

  public SelectFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance(int type) {
    Fragment fragment = new SelectFragment();
    Bundle args = new Bundle();
    args.putInt(Constants.TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getInt(Constants.TYPE);

    switch (type) {
      case Constants.TYPE_EXERCISE:
        name = getString(R.string.exercise);
        lab = ExerciseLab.get(getActivity());
        break;
      case Constants.TYPE_CATEGORY:
        name = getString(R.string.category);
        lab = CategoryOrRoutineLab.getCategoryLab(getActivity());
        break;
      case Constants.TYPE_ROUTINE:
        name = getString(R.string.routine);
        lab = CategoryOrRoutineLab.getRoutineLab(getActivity());
        break;
      default:
        throw new RuntimeException("Error: type does not exists");
    }

    textFilter = "";
    filtered = new ArrayList<>();
    namedEntityAdapter = new NamedEntityAdapter(filtered);
    updateFiltered();
  }

  private void updateFiltered() {
    filtered.clear();
    filtered.addAll(lab.getFiltered(textFilter));
    Collections.sort(filtered);
    namedEntityAdapter.notifyDataSetChanged();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentSelectWithFilterBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_with_filter, container, false);

    binding.fab.setOnClickListener(view -> showNewDialog());
    binding.filterEditText.addTextChangedListener(filterEditTextListener());

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(namedEntityAdapter);
    coordinatorLayout = binding.coordinatorLayout;
    return binding.getRoot();
  }

  private void showNewDialog() {
    final DialogInputBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogBinding.getRoot())
        .setMessage(String.format(getString(R.string.new_item), name))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    alertDialog.setOnShowListener(
        dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            view -> {
              TextInputLayout textInputLayout = dialogBinding.textInputLayout;
              String newName = textInputLayout.getEditText().getText().toString().trim();
              if (validateNewName(newName, textInputLayout)) {
                createNewNamedEntity(newName);
                alertDialog.dismiss();
              }
            }));

    alertDialog.show();
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
        updateFiltered();
      }
    };
  }

  private boolean validateNewName(String name, TextInputLayout textInputLayout) {
    return validateNotEmpty(name, textInputLayout) && validateNameUnique(name, textInputLayout);
  }

  private void createNewNamedEntity(String newName) {
    lab.insert(newName);
    updateFiltered();
    showSnackbar(String.format(getString(R.string.new_item_created), name.toLowerCase()));
  }

  private boolean validateNotEmpty(String name, TextInputLayout textInputLayout) {
    if (name.isEmpty()) {
      textInputLayout.setError(getString(R.string.please_enter_name));
      return false;
    }
    return true;
  }

  private boolean validateNameUnique(String name, TextInputLayout textInputLayout) {
    if (lab.contains(name)) {
      textInputLayout.setError(String.format(getString(R.string.item_already_exists), name));
      return false;
    }
    return true;
  }

  private void showSnackbar(String message) {
    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
  }

  private void showRenameOrDeleteDialog(final NamedEntity namedEntity) {
    final DialogEditOrDeleteBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_or_delete, null, false);

    dialogBinding.editTextView.setText(R.string.rename);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogBinding.getRoot()).create();

    dialogBinding.editLinearLayout.setOnClickListener(
        view -> {
          showRenameDialog(namedEntity);
          alertDialog.dismiss();
        });
    dialogBinding.deleteLinearLayout.setOnClickListener(
        view -> {
          showDeleteDialog(namedEntity);
          alertDialog.dismiss();
        });

    alertDialog.show();
  }

  private void showRenameDialog(final NamedEntity namedEntity) {
    final DialogInputBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogBinding.getRoot())
        .setMessage(String.format(getString(R.string.rename_item), name))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    String oldName = namedEntity.getName();

    TextInputLayout textInputLayout = dialogBinding.textInputLayout;
    textInputLayout.getEditText().setText(oldName);

    alertDialog.setOnShowListener(
        dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener(view -> {
              String newName = textInputLayout.getEditText().getText().toString().trim();
              if (validateRename(oldName, newName, textInputLayout)) {
                renameNamedEntity(namedEntity, newName);
                alertDialog.dismiss();
              }
            }));

    alertDialog.show();
  }

  private void showDeleteDialog(final NamedEntity namedEntity) {
    String deleteConfirmationMsg = String
        .format(getString(R.string.delete_item_confirmation), name.toLowerCase());

    new AlertDialog.Builder(getActivity())
        .setMessage(deleteConfirmationMsg)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteNamedEntity(namedEntity))
        .show();
  }

  private void deleteNamedEntity(NamedEntity namedEntity) {
    lab.delete(namedEntity.getId(), getContext());
    showSnackbar(String.format(getString(R.string.item_deleted), name));
    updateFiltered();
  }

  private void renameNamedEntity(NamedEntity namedEntity, String newName) {
    lab.updateName(namedEntity.getId(), newName);
    updateFiltered();
    showSnackbar(getString(R.string.rename_successful));
  }

  private boolean validateRename(String oldName, String newName, TextInputLayout textInputLayout) {
    return validateNotEmpty(newName, textInputLayout)
        && validateNotSameName(oldName, newName, textInputLayout)
        && validateNameUnique(newName, textInputLayout);
  }

  private boolean validateNotSameName(String oldName, String newName,
      TextInputLayout textInputLayout) {
    if (oldName.equals(newName)) {
      textInputLayout.setError(getString(R.string.same_name));
      return false;
    }
    return true;
  }

  private class NamedEntityHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
      View.OnLongClickListener {

    private NamedEntity namedEntity;

    NamedEntityHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    void bind(NamedEntity namedEntity) {
      this.namedEntity = namedEntity;
      ((TextView) itemView).setText(namedEntity.getName());
    }

    @Override
    public void onClick(View view) {
      Intent intent;
      switch (type) {
        case Constants.TYPE_EXERCISE:
          intent = ExerciseActivity.newIntent(getActivity(), namedEntity);
          break;
        case Constants.TYPE_CATEGORY:
        case Constants.TYPE_ROUTINE:
          intent = ViewExercisesActivity.newIntent(getActivity(), namedEntity, type);
          break;
        default:
          throw new RuntimeException("Error: type does not exists");
      }
      startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
      showRenameOrDeleteDialog(namedEntity);
      return true;
    }

  }

  private class NamedEntityAdapter extends RecyclerView.Adapter<NamedEntityHolder> {

    private final List<NamedEntity> namedEntities;

    NamedEntityAdapter(List<NamedEntity> namedEntities) {
      this.namedEntities = namedEntities;
    }

    @NonNull
    @Override
    public NamedEntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new NamedEntityHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NamedEntityHolder holder, int position) {
      NamedEntity namedEntity = namedEntities.get(position);
      holder.bind(namedEntity);
    }

    @Override
    public int getItemCount() {
      return namedEntities.size();
    }
  }
}
