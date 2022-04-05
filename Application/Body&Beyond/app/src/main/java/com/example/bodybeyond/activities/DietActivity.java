package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.DietAdapter;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.databinding.ActivityDietBinding;
import com.example.bodybeyond.interfaces.DietDao;
import com.example.bodybeyond.models.Diet;
import com.example.bodybeyond.viewmodel.Diets;

import java.util.ArrayList;
import java.util.List;


public class DietActivity extends AppCompatActivity {

    ActivityDietBinding dietBinding;
    List<Diets> dietList = new ArrayList<>();
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dietBinding = ActivityDietBinding.inflate(getLayoutInflater());
        View view = dietBinding.getRoot();
        setContentView(view);
        Bundle bundle = getIntent().getExtras();
        String diet_range = bundle.getString("DIET_RANGE", null);
        String diet_type = bundle.getString("DIET_TYPE", null);
        String diet_day = bundle.getString("DIET_DAY", null);
        AddData(diet_type,diet_range,diet_day);
        RecyclerView recyclerView = findViewById(R.id.recyclerListViewDiet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DietAdapter(dietList));
       // recyclerView.setItemAnimator(new SlideInUpAnimator());
        backBtn = dietBinding.imageButtonback;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DietActivity.this, HomeActivity.class));
            }
        });
    }
    private void AddData(String dietType, String dietRange, String dietDay)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        DietDao dietDao = db.dietDao();
        try {
            if(dietType != null && dietRange != null && dietDay != null ) {
                List<Diet> diets = dietDao.getDiets(dietType, dietRange, dietDay);
                if (diets.size() > 0) {
                    for (Diet item : diets) {
                        int resID = getResources().getIdentifier("bmi", "drawable", getPackageName());
                        Diets dietObj = new Diets(item.getDietDesc(), resID, item.getDietName());
                        dietList.add(dietObj);
                    }
                }
                else
                {
                    Toast.makeText(this, "No Records Found. !!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                dietList.add(new Diets("Two eggs fried in butter served with sauteed greens",R.drawable.bmi,"Breakfast"));
                dietList.add(new Diets("A bunless burger topped with cheese, mushrooms, and avocado atop a bed of greens",R.drawable.bmi,"Lunch"));
                dietList.add(new Diets("Pork chops with green beans sauteed in olive oil",R.drawable.bmi,"Dinner"));

            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }
}