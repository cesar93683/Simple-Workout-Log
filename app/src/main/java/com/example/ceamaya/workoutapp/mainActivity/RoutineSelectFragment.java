package com.example.ceamaya.workoutapp.mainActivity;

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

import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Routine;
import com.example.ceamaya.workoutapp.labs.RoutineLab;
import com.example.ceamaya.workoutapp.routineActivity.RoutineActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutineSelectFragment extends Fragment {
    private Activity activity;
    private ArrayList<Routine> filteredRoutines;
    private String filter;
    private View fragmentView;
    private RoutineAdapter routineAdapter;
    private RoutineLab routineLab;

    public RoutineSelectFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new RoutineSelectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        routineLab = RoutineLab.get(getActivity());

        filter = "";
        filteredRoutines = new ArrayList<>();
        filteredRoutines.addAll(routineLab.getFilteredRoutines(filter));
        Collections.sort(filteredRoutines);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_select_with_filter, container,
                false);

        FloatingActionButton newRoutineFab = fragmentView.findViewById(R.id.new_fab);
        newRoutineFab.setOnClickListener(newRoutineFabClickListener());

        EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(filterEditTextListener());

        RecyclerView routineRecyclerView = fragmentView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        routineRecyclerView.setLayoutManager(linearLayoutManager);
        routineAdapter = new RoutineAdapter(filteredRoutines);
        routineRecyclerView.setAdapter(routineAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                routineRecyclerView.getContext(), linearLayoutManager.getOrientation());
        routineRecyclerView.addItemDecoration(dividerItemDecoration);

        return fragmentView;
    }

    @NonNull
    private View.OnClickListener newRoutineFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createNewRoutineDialog();
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
                updateFilteredRoutines();
            }
        };
    }

    private void createNewRoutineDialog() {
        @SuppressLint("InflateParams") View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);

        final TextInputLayout newRoutineTextInputLayout = dialogView.findViewById(
                R.id.text_input_layout);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setMessage("New Routine")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String newRoutine = newRoutineTextInputLayout.
                                        getEditText().getText().toString().trim();
                                if (newRoutine.isEmpty()) {
                                    newRoutineTextInputLayout.setError("Please enter a name.");
                                } else if (routineLab.contains(newRoutine)) {
                                    newRoutineTextInputLayout.setError("Routine already exists.");
                                } else {
                                    routineLab.insertRoutine(newRoutine);
                                    updateFilteredRoutines();
                                    Snackbar.make(fragmentView, "New routine created.",
                                            Snackbar.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
            }
        });

        alertDialog.show();
    }

    private void updateFilteredRoutines() {
        filteredRoutines.clear();
        filteredRoutines.addAll(routineLab.getFilteredRoutines(filter));
        Collections.sort(filteredRoutines);
        routineAdapter.notifyDataSetChanged();
    }

    private void createRenameOrDeleteDialog(final Routine routine) {
        @SuppressLint("InflateParams") final View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_edit_or_delete, null);

        TextView editTextView = dialogView.findViewById(R.id.edit_text_view);
        editTextView.setText(R.string.dialog_rename_text);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setView(dialogView);

        dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createRenameRoutineDialog(routine);
                        alertDialog.dismiss();
                    }
                });
        dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDeleteRoutineDialog(routine);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void createRenameRoutineDialog(final Routine oldRoutine) {
        @SuppressLint("InflateParams") final View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setMessage("Edit Routine")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        final TextInputLayout newRoutineTextInputLayout = dialogView.findViewById(
                R.id.text_input_layout);
        newRoutineTextInputLayout.getEditText().setText(oldRoutine.getRoutineName());

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newRoutine = newRoutineTextInputLayout.
                                getEditText().getText().toString().trim();
                        if (oldRoutine.getRoutineName().equals(newRoutine)) {
                            newRoutineTextInputLayout.setError("Same name.");
                        } else if (newRoutine.isEmpty()) {
                            newRoutineTextInputLayout.setError("Please enter a name.");
                        } else if (routineLab.contains(newRoutine)) {
                            newRoutineTextInputLayout.setError("Routine already exists.");
                        } else {
                            routineLab.updateRoutine(oldRoutine.getRoutineId(), newRoutine);
                            updateFilteredRoutines();
                            Snackbar.make(fragmentView, "Rename successful",
                                    Snackbar.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void createDeleteRoutineDialog(final Routine routineToRemove) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this routine?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        routineLab.deleteRoutine(routineToRemove.getRoutineId());
                        updateFilteredRoutines();
                        Snackbar.make(fragmentView, "Routine deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private class RoutineHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private Routine routine;

        RoutineHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.simple_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(Routine routine) {
            this.routine = routine;
            ((TextView) itemView).setText(routine.getRoutineName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = RoutineActivity.newIntent(activity, routine.getRoutineName(),
                    routine.getRoutineId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            createRenameOrDeleteDialog(routine);
            return true;
        }

    }

    private class RoutineAdapter extends RecyclerView.Adapter<RoutineHolder> {

        private final List<Routine> routines;

        RoutineAdapter(List<Routine> routines) {
            this.routines = routines;
        }

        @NonNull
        @Override
        public RoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RoutineHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RoutineHolder holder, int position) {
            Routine routine = routines.get(position);
            holder.bind(routine);
        }

        @Override
        public int getItemCount() {
            return routines.size();
        }
    }
}
