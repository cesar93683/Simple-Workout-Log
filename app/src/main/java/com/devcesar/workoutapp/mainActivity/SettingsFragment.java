package com.devcesar.workoutapp.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.FragmentSelectNoFabBinding;

public class SettingsFragment extends Fragment {

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
    return binding.getRoot();
  }

}
