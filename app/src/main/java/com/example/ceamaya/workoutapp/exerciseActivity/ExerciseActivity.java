package com.example.ceamaya.workoutapp.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
        int INVALID_ID = -1;
        exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, INVALID_ID);
        exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

        if(exerciseId == INVALID_ID) {
            finish();
        }

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
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R
                .id.container + ":" + mViewPager.getCurrentItem());
        if (fragment instanceof ExerciseFragment) {
            ((ExerciseFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
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
