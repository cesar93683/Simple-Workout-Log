package com.example.ceamaya.workoutapp.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Workout;
import com.example.ceamaya.workoutapp.labs.WorkoutLab;

import java.util.ArrayList;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity implements SaveSets {

    private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    private ViewPager mViewPager;
    private int exerciseId;
    private String exerciseName;
    private WorkoutLab workoutLab;

    public static Intent newIntent(Context packageContext, String exerciseName, int exerciseId) {
        Intent intent = new Intent(packageContext, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        workoutLab = WorkoutLab.get(this);
        exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, 0);
        exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(exerciseName);
        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (mViewPager));
    }

    public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
        long timeStamp = new Date().getTime();
        Workout workout = new Workout(timeStamp, exerciseSets);
        workoutLab.insertWorkout(workout);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R
                .id.container + ":" + mViewPager.getCurrentItem());
        if (fragment instanceof ExerciseFragment) {
            ((ExerciseFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_exercise_placeholder_fragment,
                    container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt
                    (ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = ExerciseFragment.newInstance(exerciseId);
                    break;
                case 1:
                    fragment = WorkoutHistoryFragment.newInstance(exerciseId, exerciseName);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Exercise";
                case 1:
                    return "History";
            }
            return null;
        }
    }
}
