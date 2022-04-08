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
import com.example.bodybeyond.adapters.DietCatAdapter;
import com.example.bodybeyond.viewmodel.Diets;

import java.util.ArrayList;
import java.util.List;


public class DietFragment extends Fragment {

    private Context context;
    List<Diets> DietsCat = new ArrayList<>();
    private RecyclerView dietRecyclerView;
    DietCatAdapter dietCatAdapter;

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
                SharedPreferences sharedActivity = getActivity().getSharedPreferences("CATEGORY_DIET", MODE_PRIVATE);
                String range = sharedActivity.getString("DIET_RANGE","null");

                Bundle bundle = new Bundle();
                bundle.putString("DIET_TYPE",DietsCat.get(i).getDietDescription());
                bundle.putString("DIET_RANGE",range);

                Intent intent = new Intent(getActivity(), DietActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dietRecyclerView.setAdapter(dietCatAdapter);
    }

    private void AddData(){
        DietsCat.add(new Diets("Vegan",R.drawable.vegan));
        DietsCat.add(new Diets("Combination",R.drawable.combination));
    }
  }