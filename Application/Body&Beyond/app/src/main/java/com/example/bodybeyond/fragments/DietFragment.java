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
import com.example.bodybeyond.adapters.DietCatAdapter;
import com.example.bodybeyond.viewmodel.Diets;

import java.util.ArrayList;
import java.util.List;


public class DietFragment extends Fragment {

    private Context context;
    List<Diets> DietsCat = new ArrayList<>();
    private RecyclerView dietRecyclerView;
    DietCatAdapter dietCatAdapter;
    String dietType;
    String useremail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();

        AddData();

        dietRecyclerView = view.findViewById(R.id.dietRecyclerView);

        GridLayoutManager gm = new GridLayoutManager(this.context,1);
        dietRecyclerView.setLayoutManager(gm);

        dietCatAdapter = new DietCatAdapter(DietsCat, new DietCatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(getActivity(), DietActivity.class);
                startActivity(intent);
                dietType = DietsCat.get(i).getDietDescription();
            }
        });
        dietRecyclerView.setAdapter(dietCatAdapter);
        UserDietTypePref(dietType);


    }

    private void AddData(){
        DietsCat.add(new Diets("Vegan",R.drawable.vegan));
        DietsCat.add(new Diets("Combination",R.drawable.combination));
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