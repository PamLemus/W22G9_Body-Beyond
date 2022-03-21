package com.example.bodybeyond.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.ExerciseAdapter;
import com.example.bodybeyond.databinding.ActivityExerciseBinding;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ExerciseActivity extends AppCompatActivity {

    ActivityExerciseBinding binding;

    List<Exercises> exercisesList = new ArrayList<>();
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        AddData();
        // Lookup the recyclerview in activity layout
        RecyclerView recyclerView = findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseAdapter(exercisesList));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        btnBack = binding.imgBackBtn;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExerciseActivity.this, HomeActivity.class));
            }
        });
    }

    private void AddData()
    {
        exercisesList.add(new Exercises("Burpees", R.drawable.bmi, "Place your hands on the floor, spring your feet back and do a press up. And then jump your feet back in and spring back up. Repeat."));
        exercisesList.add(new Exercises("FROG JUMPS",  R.drawable.bmi,"These are a real killer and it’s surprising how quickly they get your heart rate up. Simply jump with both legs over the box and turn around to jump back over it again. Make sure you clear the box with a little room to spare to keep the pressure on."));
        exercisesList.add(new Exercises("MOUNTAIN CLIMBERS", R.drawable.bmi, "Place your hands on the step so that you’re in an incline press up position. Bring one knee in and then straighten it back out again. Then bring the other knee in and straighten it back out again. Do this quickly and keep going until the minute’s up!"));
        exercisesList.add(new Exercises("QUICK STEP UPS", R.drawable.bmi,"These may seem easy but done fast, they’re a really effective calorie burner. Set the raiser blocks to a good height and then step up and back down one foot at a time as fast as you can. Swap your leading foot halfway through."));
        exercisesList.add(new Exercises("LEG RAISES", R.drawable.bmi,"Doing these on the step will help you avoid the temptation to rest your feet on the floor, so you’ll definitely feel the burn. Lie on the step with your feet out straight. Raise your legs up together while keeping your back flat and your abs pushed down. Once they’re at a 90-degree angle to your body, carefully lower them back down to the starting position. Repeat."));
    }
}