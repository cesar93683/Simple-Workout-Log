package com.devcesar.workoutapp.mainActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.DialogEditOrDeleteBinding;
import com.devcesar.workoutapp.databinding.DialogInputBinding;
import com.devcesar.workoutapp.databinding.FragmentSelectWithFilterBinding;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.labs.RoutineLab;
import com.devcesar.workoutapp.nameActivity.NameActivity;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFragment extends Fragment {

  private Activity activity;
  private ArrayList<NamedEntity> filtered;
  private String filter;
  private NamedEntityAdapter namedEntityAdapter;
  private NamedEntityLab lab;
  private int type;
  private String nameType;
  private FragmentSelectWithFilterBinding binding;

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
    activity = getActivity();
    switch (type) {
      case Constants.TYPE_CATEGORY:
        nameType = getString(R.string.category);
        lab = CategoryLab.get(getActivity());
        break;
      case Constants.TYPE_ROUTINE:
        nameType = getString(R.string.routine);
        lab = RoutineLab.get(getActivity());
        break;
      case Constants.TYPE_EXERCISE:
        nameType = getString(R.string.exercise);
        lab = ExerciseLab.get(getActivity());
        break;
      default:
        throw new RuntimeException("ERROR: type is invalid");
    }

    filter = "";
    filtered = new ArrayList<>();
    namedEntityAdapter = new NamedEntityAdapter(filtered);
    updateFiltered();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_select_with_filter, container, false);

    binding.fab.setOnClickListener(newFabClickListener());
    binding.filterEditText.addTextChangedListener(filterEditTextListener());

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(namedEntityAdapter);

    return binding.getRoot();
  }

  @NonNull
  private View.OnClickListener newFabClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        createNewDialog();
      }
    };
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
        filter = s.toString();
        updateFiltered();
      }
    };
  }

  private void createNewDialog() {
    final DialogInputBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogBinding.getRoot())
        .setMessage(String.format(getString(R.string.new_x), nameType))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                String newName = dialogBinding.textInputLayout.getEditText().getText().toString()
                    .trim();
                if (newName.isEmpty()) {
                  dialogBinding.textInputLayout.setError(getString(R.string.error_no_name));
                } else if (lab.contains(newName)) {
                  dialogBinding.textInputLayout
                      .setError(String.format(getString(R.string.x_already_exists), nameType));
                } else {
                  lab.insert(newName);
                  updateFiltered();
                  Snackbar.make(binding.getRoot(),
                      String.format(getString(R.string.new_x_created), nameType.toLowerCase()),
                      Snackbar.LENGTH_SHORT).show();
                  alertDialog.dismiss();
                }
              }
            });
      }
    });

    alertDialog.show();
  }

  private void updateFiltered() {
    filtered.clear();
    filtered.addAll(lab.getFiltered(filter));
    Collections.sort(filtered);
    namedEntityAdapter.notifyDataSetChanged();
  }

  private void createRenameOrDeleteDialog(final NamedEntity namedEntity) {
    final DialogEditOrDeleteBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_or_delete, null, false);

    dialogBinding.editTextView.setText(R.string.dialog_rename_text);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    alertDialog.setView(dialogBinding.getRoot());

    dialogBinding.editLinearLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createRenameDialog(namedEntity);
            alertDialog.dismiss();
          }
        });
    dialogBinding.deleteLinearLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createDeleteDialog(namedEntity);
            alertDialog.dismiss();
          }
        });
    alertDialog.show();
  }

  private void createRenameDialog(final NamedEntity oldNamedEntity) {
    final DialogInputBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_input, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogBinding.getRoot())
        .setMessage(String.format(getString(R.string.rename_x), nameType))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    dialogBinding.textInputLayout.getEditText().setText(oldNamedEntity.getName());

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                String newNamedEntity = dialogBinding.textInputLayout.getEditText().getText()
                    .toString().trim();
                if (oldNamedEntity.getName().equals(newNamedEntity)) {
                  dialogBinding.textInputLayout.setError(getString(R.string.error_same_name));
                } else if (newNamedEntity.isEmpty()) {
                  dialogBinding.textInputLayout.setError(getString(R.string.error_no_name));
                } else if (lab.contains(newNamedEntity)) {
                  dialogBinding.textInputLayout
                      .setError(String.format(getString(R.string.x_already_exists), nameType));
                } else {
                  lab.updateName(oldNamedEntity.getId(), newNamedEntity);
                  updateFiltered();
                  Snackbar
                      .make(binding.getRoot(), R.string.rename_successful, Snackbar.LENGTH_SHORT)
                      .show();
                  alertDialog.dismiss();
                }
              }
            });
      }
    });

    alertDialog.show();
  }

  private void createDeleteDialog(final NamedEntity namedEntity) {
    new AlertDialog.Builder(activity)
        .setMessage(
            String.format(getString(R.string.are_you_sure_delete_x), nameType.toLowerCase()))
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            lab.delete(namedEntity.getId());
            updateFiltered();
            Snackbar.make(binding.getRoot(), String.format(getString(R.string.x_deleted), nameType),
                Snackbar.LENGTH_SHORT).show();
          }
        })
        .show();
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
      if (type == Constants.TYPE_CATEGORY || type == Constants.TYPE_ROUTINE) {
        Intent intent = NameActivity
            .newIntent(activity, namedEntity.getName(), namedEntity.getId(), type);
        startActivity(intent);
      } else if (type == Constants.TYPE_EXERCISE) {
        Intent intent = ExerciseActivity
            .newIntent(activity, namedEntity.getName(), namedEntity.getId());
        startActivity(intent);
      }
    }

    @Override
    public boolean onLongClick(View v) {
      createRenameOrDeleteDialog(namedEntity);
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
