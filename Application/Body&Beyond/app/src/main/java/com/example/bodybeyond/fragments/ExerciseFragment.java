package com.example.bodybeyond.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bodybeyond.R;
import com.example.bodybeyond.activities.DietActivity;
import com.example.bodybeyond.activities.ExerciseActivity;
import com.example.bodybeyond.adapters.ExerciseCatAdapter;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private Context context;
    private String activity;
    List<Exercises> ExercisesCat = new ArrayList<>();
    private RecyclerView exerciseRecyclerView;
    ExerciseCatAdapter exerciseCatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();

        AddData();

        exerciseRecyclerView = view.findViewById(R.id.exerciseRecyclerView);

        GridLayoutManager gm = new GridLayoutManager(this.context,1);
        exerciseRecyclerView.setLayoutManager(gm);

        exerciseCatAdapter = new ExerciseCatAdapter(ExercisesCat, new ExerciseCatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {

                SharedPreferences sharedActivity = getActivity().getSharedPreferences("CATEGORY_EXERCISE", MODE_PRIVATE);
                activity = sharedActivity.getString("EXERCISE_ACTIVITY","null");

                Bundle bundle = new Bundle();
                bundle.putString("EXERCISE_TYPE",ExercisesCat.get(i).getDescription());
                bundle.putString("EXERCISE_ACTIVITY",activity);

                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        exerciseRecyclerView.setAdapter(exerciseCatAdapter);

    }

        private void AddData(){
        ExercisesCat.add(new Exercises("Cardio",R.drawable.cardio));
        ExercisesCat.add(new Exercises("Yoga",R.drawable.yoga));
        ExercisesCat.add(new Exercises("Weight training",R.drawable.weight_training));
    }
    }


