package com.devcesar.workoutapp.mainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
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
import com.devcesar.workoutapp.Category;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.labs.CategoryLab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryFragment extends Fragment {

  private Activity activity;
  private ArrayList<Category> filteredCategories;
  private String filter;
  private View fragmentView;
  private CategoryAdapter categoryAdapter;
  private CategoryLab categoryLab;

  public CategoryFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance() {
    return new CategoryFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    categoryLab = CategoryLab.get(getActivity());

    filter = "";
    filteredCategories = new ArrayList<>();
    filteredCategories.addAll(categoryLab.getFilteredCategories(filter));
    Collections.sort(filteredCategories);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    fragmentView = inflater.inflate(R.layout.fragment_select_with_filter, container, false);

    FloatingActionButton newCategoryFab = fragmentView.findViewById(R.id.fab);
    newCategoryFab.setOnClickListener(newCategoryFabClickListener());

    EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
    filterEditText.addTextChangedListener(filterEditTextListener());

    RecyclerView categoryRecyclerView = fragmentView.findViewById(R.id.recycler_view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    categoryRecyclerView.setLayoutManager(linearLayoutManager);
    categoryAdapter = new CategoryAdapter(filteredCategories);
    categoryRecyclerView.setAdapter(categoryAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        categoryRecyclerView.getContext(), linearLayoutManager.getOrientation());
    categoryRecyclerView.addItemDecoration(dividerItemDecoration);

    return fragmentView;
  }


  @NonNull
  private View.OnClickListener newCategoryFabClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        createNewCategoryDialog();
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
        updateFilteredCategories();
      }
    };
  }

  private void createNewCategoryDialog() {
    @SuppressLint("InflateParams") View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.dialog_text_input_layout, null);

    final TextInputLayout newCategoryTextInputLayout = dialogView
        .findViewById(R.id.text_input_layout);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(R.string.new_category)
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
                String newCategoryName = newCategoryTextInputLayout.getEditText().getText()
                    .toString().trim();
                if (newCategoryName.isEmpty()) {
                  newCategoryTextInputLayout.setError(getString(R.string.error_no_name));
                } else if (categoryLab.contains(newCategoryName)) {
                  newCategoryTextInputLayout
                      .setError(getString(R.string.error_category_already_exists));
                } else {
                  categoryLab.insertCategory(newCategoryName);
                  updateFilteredCategories();
                  Snackbar.make(fragmentView, R.string.new_category_created, Snackbar.LENGTH_SHORT)
                      .show();
                  alertDialog.dismiss();
                }
              }
            });
      }
    });

    alertDialog.show();
  }

  private void updateFilteredCategories() {
    filteredCategories.clear();
    filteredCategories.addAll(categoryLab.getFilteredCategories(filter));
    Collections.sort(filteredCategories);
    categoryAdapter.notifyDataSetChanged();
  }

  private void createRenameOrDeleteDialog(final Category category) {
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
            createRenameCategoryDialog(category);
            alertDialog.dismiss();
          }
        });
    dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createDeleteCategoryDialog(category);
            alertDialog.dismiss();
          }
        });
    alertDialog.show();
  }

  private void createRenameCategoryDialog(final Category oldCategory) {
    @SuppressLint("InflateParams") final View dialogView =
        activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);
    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(R.string.edit_category)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    final TextInputLayout newCategoryTextInputLayout = dialogView
        .findViewById(R.id.text_input_layout);
    newCategoryTextInputLayout.getEditText().setText(oldCategory.getName());

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String newCategory = newCategoryTextInputLayout.getEditText().getText().toString()
                .trim();
            if (oldCategory.getName().equals(newCategory)) {
              newCategoryTextInputLayout.setError(getString(R.string.error_same_name));
            } else if (newCategory.isEmpty()) {
              newCategoryTextInputLayout.setError(getString(R.string.error_no_name));
            } else if (categoryLab.contains(newCategory)) {
              newCategoryTextInputLayout
                  .setError(getString(R.string.error_category_already_exists));
            } else {
              categoryLab.updateCategoryName(oldCategory.getId(), newCategory);
              updateFilteredCategories();
              Snackbar.make(fragmentView, R.string.rename_successful, Snackbar.LENGTH_SHORT).show();
              alertDialog.dismiss();
            }
          }
        });
      }
    });

    alertDialog.show();
  }

  private void createDeleteCategoryDialog(final Category categoryToRemove) {
    new AlertDialog.Builder(activity)
        .setMessage(R.string.are_you_sure_delete_category)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            categoryLab.deleteCategory(categoryToRemove.getId());
            updateFilteredCategories();
            Snackbar.make(fragmentView, R.string.category_deleted, Snackbar.LENGTH_SHORT).show();
          }
        })
        .show();
  }

  private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
      View.OnLongClickListener {

    private Category category;

    CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    void bind(Category category) {
      this.category = category;
      ((TextView) itemView).setText(category.getName());
    }

    @Override
    public void onClick(View view) {
//      Intent intent = RoutineActivity
//          .newIntent(activity, routine.getName(), routine.getId());
//      startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
      createRenameOrDeleteDialog(category);
      return true;
    }

  }

  private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

    private final List<Category> categories;

    CategoryAdapter(List<Category> categories) {
      this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new CategoryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
      Category category = categories.get(position);
      holder.bind(category);
    }

    @Override
    public int getItemCount() {
      return categories.size();
    }
  }
}
