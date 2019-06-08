package com.devcesar.workoutapp.mainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.labs.RoutineLab;
import com.devcesar.workoutapp.routineActivity.NameActivity;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFragment extends Fragment {

  private Activity activity;
  private ArrayList<NamedEntity> filtered;
  private String filter;
  private View fragmentView;
  private NamedEntityAdapter namedEntityAdapter;
  private NamedEntityLab lab;
  private int type;
  private String name;

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
        name = getString(R.string.category);
        lab = CategoryLab.get(getActivity());
        break;
      case Constants.TYPE_ROUTINE:
        name = getString(R.string.routine);
        lab = RoutineLab.get(getActivity());
        break;
      default:  // type == Constants.TYPE_EXERCISE
        name = getString(R.string.exercise);
        lab = ExerciseLab.get(getActivity());
        break;
    }

    filter = "";
    filtered = new ArrayList<>();
    filtered.addAll(lab.getFiltered(filter));
    Collections.sort(filtered);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    fragmentView = inflater.inflate(R.layout.fragment_select_with_filter, container, false);

    FloatingActionButton newFab = fragmentView.findViewById(R.id.fab);
    newFab.setOnClickListener(newFabClickListener());

    EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
    filterEditText.addTextChangedListener(filterEditTextListener());

    RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.addItemDecoration(new DividerItemDecoration(
        recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    namedEntityAdapter = new NamedEntityAdapter(filtered);
    recyclerView.setAdapter(namedEntityAdapter);


    return fragmentView;
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
    @SuppressLint("InflateParams") View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.dialog_text_input_layout, null);

    final TextInputLayout textInputLayout = dialogView.findViewById(R.id.text_input_layout);

    final AlertDialog alertDialog = new Builder(activity)
        .setView(dialogView)
        .setMessage(String.format(getString(R.string.new_x), name))
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
                String newName = textInputLayout.getEditText().getText().toString().trim();
                if (newName.isEmpty()) {
                  textInputLayout.setError(getString(R.string.error_no_name));
                } else if (lab.contains(newName)) {
                  textInputLayout
                      .setError(String.format(getString(R.string.x_already_exists), name));
                } else {
                  lab.insert(newName);
                  updateFiltered();
                  Snackbar.make(fragmentView,
                      String.format(getString(R.string.new_x_created), name.toLowerCase()),
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
    @SuppressLint("InflateParams") final View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.dialog_edit_or_delete, null);

    TextView editTextView = dialogView.findViewById(R.id.edit_text_view);
    editTextView.setText(R.string.dialog_rename_text);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    alertDialog.setView(dialogView);

    dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createRenameDialog(namedEntity);
            alertDialog.dismiss();
          }
        });
    dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
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
    @SuppressLint("InflateParams") final View dialogView =
        activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);
    final AlertDialog alertDialog = new Builder(activity)
        .setView(dialogView)
        .setMessage(String.format(getString(R.string.edit_x), name.toLowerCase()))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    final TextInputLayout textInputLayout = dialogView.findViewById(R.id.text_input_layout);
    textInputLayout.getEditText().setText(oldNamedEntity.getName());

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String newNamedEntity = textInputLayout.getEditText().getText().toString().trim();
            if (oldNamedEntity.getName().equals(newNamedEntity)) {
              textInputLayout.setError(getString(R.string.error_same_name));
            } else if (newNamedEntity.isEmpty()) {
              textInputLayout.setError(getString(R.string.error_no_name));
            } else if (lab.contains(newNamedEntity)) {
              textInputLayout.setError(String.format(getString(R.string.x_already_exists), name));
            } else {
              lab.updateName(oldNamedEntity.getId(), newNamedEntity);
              updateFiltered();
              Snackbar.make(fragmentView, R.string.rename_successful, Snackbar.LENGTH_SHORT).show();
              alertDialog.dismiss();
            }
          }
        });
      }
    });

    alertDialog.show();
  }

  private void createDeleteDialog(final NamedEntity namedEntity) {
    new Builder(activity)
        .setMessage(String.format(getString(R.string.are_you_sure_delete_x), name.toLowerCase()))
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            lab.delete(namedEntity.getId());
            updateFiltered();
            Snackbar.make(fragmentView, String.format(getString(R.string.x_deleted), name),
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
