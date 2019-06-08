package com.devcesar.workoutapp.mainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.Utils.NamedEntity;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.routineActivity.RoutineActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFragment extends Fragment {

  public static final int TYPE_CATEGORY = 1;
  public static final int TYPE_ROUTINE = 2;
  private static final String TYPE = "TYPE";
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
    args.putInt(TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getArguments().getInt(TYPE);
    activity = getActivity();
    if (type == TYPE_CATEGORY) {
      name = "Category";
//      lab = CategoryLab.get(getActivity());
    } else {
      name = "Routine";
//      lab = RoutineLab.get(getActivity());
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
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(linearLayoutManager);
    namedEntityAdapter = new NamedEntityAdapter(filtered);
    recyclerView.setAdapter(namedEntityAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        recyclerView.getContext(), linearLayoutManager.getOrientation());
    recyclerView.addItemDecoration(dividerItemDecoration);

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

    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(String.format("New %s", name.toLowerCase()))
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
                  textInputLayout.setError(String.format("%s already exists.", name));
                } else {
                  lab.insert(newName);
                  updateFiltered();
                  Snackbar.make(fragmentView, String.format("New %s created.", name.toLowerCase()),
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
    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(String.format("Edit %s", name.toLowerCase()))
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
              textInputLayout.setError(name + " already exists.");
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
    new AlertDialog.Builder(activity)
        .setMessage(String.format("Are you sure you want to delete this %s?", name.toLowerCase()))
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            lab.delete(namedEntity.getId());
            updateFiltered();
            Snackbar.make(fragmentView, String.format("%s deleted.", name), Snackbar.LENGTH_SHORT)
                .show();
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
      if (type == TYPE_CATEGORY) {
        // todo
      } else {
        Intent intent = RoutineActivity
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
