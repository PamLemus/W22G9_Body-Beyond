package com.example.bodybeyond.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.ExerciseAdapter;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.databinding.ActivityExerciseBinding;
import com.example.bodybeyond.interfaces.ExerciseDao;
import com.example.bodybeyond.models.Exercise;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.ArrayList;
import java.util.List;


public class ExerciseActivity extends AppCompatActivity {

    ActivityExerciseBinding binding;

    List<Exercises> exercisesList = new ArrayList<>();
    ImageButton btnBack;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        String exercise_activity = "Light";// bundle.getString("EXERCISE_ACTIVITY", null);
        String exercise_type ="Cardio";// bundle.getString("EXERCISE_TYPE", null);
        AddData(exercise_activity, exercise_type);
        // Lookup the recyclerview in activity layout
        RecyclerView recyclerView = findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExerciseAdapter(exercisesList));
       // recyclerView.setItemAnimator(new SlideInUpAnimator());
        btnBack = binding.imgBackBtn;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExerciseActivity.this, HomeActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void AddData(String exerciseActivity, String exerciseType)
    {

        List<Exercise> exercises = null;
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        ExerciseDao exerciseDao = db.exerciseDao();
        try {
            if(exerciseActivity != null && exerciseType != null)
            {
                exercises = exerciseDao.getExercises(exerciseType, exerciseActivity);
                if(exercises.size() > 0) {
                    for (Exercise item: exercises) {
                        int resID = getResources().getIdentifier(item.getExerciseImg(), "drawable" , getPackageName()) ;
                        Exercises exeObj =new Exercises(item.getExerciseType(),resID,item.getExerciseDesc());
                        exercisesList.add(exeObj);
                    }
                }
                else
                {
                    Toast.makeText(this, "No Records Found. !!", Toast.LENGTH_SHORT).show();
                }
            }
           else {
               exercisesList.add(new Exercises("Burpees", R.drawable.bmi, "Place your hands on the floor, spring your feet back and do a press up. And then jump your feet back in and spring back up. Repeat."));
               exercisesList.add(new Exercises("FROG JUMPS", R.drawable.bmi, "These are a real killer and it’s surprising how quickly they get your heart rate up. Simply jump with both legs over the box and turn around to jump back over it again. Make sure you clear the box with a little room to spare to keep the pressure on."));
               exercisesList.add(new Exercises("MOUNTAIN CLIMBERS", R.drawable.bmi, "Place your hands on the step so that you’re in an incline press up position. Bring one knee in and then straighten it back out again. Then bring the other knee in and straighten it back out again. Do this quickly and keep going until the minute’s up!"));
               exercisesList.add(new Exercises("QUICK STEP UPS", R.drawable.bmi, "These may seem easy but done fast, they’re a really effective calorie burner. Set the raiser blocks to a good height and then step up and back down one foot at a time as fast as you can. Swap your leading foot halfway through."));
               exercisesList.add(new Exercises("LEG RAISES", R.drawable.bmi, "Doing these on the step will help you avoid the temptation to rest your feet on the floor, so you’ll definitely feel the burn. Lie on the step with your feet out straight. Raise your legs up together while keeping your back flat and your abs pushed down. Once they’re at a 90-degree angle to your body, carefully lower them back down to the starting position. Repeat."));
           }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }
}