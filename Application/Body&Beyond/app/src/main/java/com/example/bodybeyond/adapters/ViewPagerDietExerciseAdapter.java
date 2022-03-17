package com.example.bodybeyond.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerDietExerciseAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentArrayListDE = new ArrayList<>();
    ArrayList<String> fragmentTitlesDE = new ArrayList<>();

    public ViewPagerDietExerciseAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayListDE.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayListDE.size();
    }

    public void addFragmentsDE(Fragment fragment, String fragmentTitle){
        fragmentArrayListDE.add(fragment);
        fragmentTitlesDE.add(fragmentTitle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitlesDE.get(position);
    }
}
