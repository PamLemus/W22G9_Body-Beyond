package com.example.bodybeyond.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bodybeyond.R;

public class ExerciseFragment extends Fragment {

    private String activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);


    }

    private void UserActivityPref(String activity) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EXERCISE_ACTIVITY", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("EXERCISE_ACTIVITY", activity);
        edit.commit();
    }

    private void UserExerciseTypePref(String exerciseType) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EXERCISE_TYPE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("EXERCISE_TYPE", exerciseType);
        edit.commit();
    }
}