package com.sorum.sorum.controller;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sorum.sorum.view.home.DynamicFragment;

import java.util.ArrayList;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<String> questions_names;
    String exam;
    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> questions_names, String exam) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.exam = exam;
        this.questions_names = questions_names;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.newInstance(position,questions_names,exam);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}