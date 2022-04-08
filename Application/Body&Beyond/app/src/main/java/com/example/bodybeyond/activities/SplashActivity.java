package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.DietDao;
import com.example.bodybeyond.interfaces.ExerciseDao;
import com.example.bodybeyond.models.Diet;
import com.example.bodybeyond.models.Exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        List<Diet> diet_list = getAllDiet();
        List<Exercise> exercise_list = getAllExercise();
        if(diet_list.size() <=0 || diet_list == null)
        {
           insertDiet(ReadDietCSV());
        }

        if(exercise_list.size() <=0 || exercise_list == null)
        {
            insertExercise(ReadExerciseCSV());
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 5000);
    }

    private List<Exercise> ReadExerciseCSV(){
        List<Exercise> exerciseList = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.exercise);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String exerciseLine;
            while ((exerciseLine = reader.readLine()) != null){
                String[] exerciseData = exerciseLine.split(",");
                Exercise exercise = new Exercise(0, exerciseData[0], exerciseData[1],
                        exerciseData[2],exerciseData[3]);
                exerciseList.add(exercise);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return exerciseList;
    }

    private List<Diet> ReadDietCSV(){
        List<Diet> dietList = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.diet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String dietLine;
            while ((dietLine = reader.readLine()) != null){
                String[] exerciseData = dietLine.split(",");
                Diet diet = new Diet(0, exerciseData[0],exerciseData[1],exerciseData[2]
                        ,exerciseData[3],exerciseData[4],exerciseData[5]);

                dietList.add(diet);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dietList;
    }

    private void insertExercise(List<Exercise> exerciseList)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        ExerciseDao exerciseDao = db.exerciseDao();
        try {
            exerciseDao.insertExercises(exerciseList);

        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }

    private void insertDiet(List<Diet> dietList)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        DietDao dietDao = db.dietDao();
        try {
            dietDao.insertDiets(dietList);

        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }

    private List<Exercise> getAllExercise()
    {
        List<Exercise> exercises = null;
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        ExerciseDao exerciseDao = db.exerciseDao();
        try {
            exercises = exerciseDao.getAllExercises();

        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return exercises;
    }

    private List<Diet> getAllDiet()
    {
        List<Diet> diets = null;
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        DietDao dietDao = db.dietDao();
        try {
          diets = dietDao.getAllDiets();

        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return diets;
    }
}