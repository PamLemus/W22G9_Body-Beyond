package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.DietAdapter;
import com.example.bodybeyond.databinding.ActivityDietBinding;
import com.example.bodybeyond.viewmodel.Diet;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DietActivity extends AppCompatActivity {

    ActivityDietBinding dietBinding;
    List<Diet> dietList = new ArrayList<>();
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dietBinding = ActivityDietBinding.inflate(getLayoutInflater());
        View view = dietBinding.getRoot();
        setContentView(view);
        AddData();
        RecyclerView recyclerView = findViewById(R.id.recyclerListViewDiet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DietAdapter(dietList));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        backBtn = dietBinding.imageButtonback;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DietActivity.this, HomeActivity.class));
            }
        });
    }
    private void AddData()
    {
        dietList.add(new Diet("Two eggs fried in butter served with sauteed greens",R.drawable.bmi,"Breakfast"));
        dietList.add(new Diet("A bunless burger topped with cheese, mushrooms, and avocado atop a bed of greens",R.drawable.bmi,"Lunch"));
        dietList.add(new Diet("Pork chops with green beans sauteed in olive oil",R.drawable.bmi,"Dinner"));

    }
}