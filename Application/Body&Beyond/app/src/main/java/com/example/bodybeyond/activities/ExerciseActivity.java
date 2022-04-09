package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
    TextView title;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        String exercise_activity = bundle.getString("EXERCISE_ACTIVITY", null);
        String exercise_type = bundle.getString("EXERCISE_TYPE", null);
        title = binding.txtExercixeTitle;
        title.setText("Exercise for " + exercise_type);
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

        List<Exercise> exercises;
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        ExerciseDao exerciseDao = db.exerciseDao();
        try {
            if(exerciseActivity != null && exerciseType != null)
            {
                exercises = exerciseDao.getExercises(exerciseType, exerciseActivity);
                if(exercises.size() > 0) {
                    exercisesList.clear();
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
                Toast.makeText(this, "Query parameters is null. ", Toast.LENGTH_SHORT).show();
           }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }
}