package com.example.bodybeyond.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bodybeyond.R;


public class DietFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false);




    }

    private void UserCalRangePref(String range) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DIET_RANGE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("DIET_RANGE", range);
        edit.commit();
    }

    private void UserDietTypePref(String dietType) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DIET_TYPE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("DIET_TYPE", dietType);
        edit.commit();
    }
}