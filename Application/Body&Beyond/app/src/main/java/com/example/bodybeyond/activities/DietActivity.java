package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
    TextView title;
    Button monBtn;
    Button tueBtn;
    Button wedBtn;
    Button thruBtn;
    Button friBtn;
    final String MONDAY = "Monday";
    final String TUESDAY = "Tuesday";
    final String WEDNESDAY = "Wednesday";
    final String THRUSDAY = "Thursday";
    final String FRIDAY = "Friday";
    String diet_range;
    String diet_type;
    String diet_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dietBinding = ActivityDietBinding.inflate(getLayoutInflater());
        View view = dietBinding.getRoot();
        setContentView(view);
        monBtn = dietBinding.mondayBtn;
        tueBtn = dietBinding.tuesdayBtn;
        wedBtn = dietBinding.wednesdayBtn;
        thruBtn = dietBinding.thursdayBtn;
        friBtn = dietBinding.fridayBtn;
        Bundle bundle = getIntent().getExtras();
        diet_range = bundle.getString("DIET_RANGE", null);
        diet_type = bundle.getString("DIET_TYPE", null);
        diet_day = MONDAY;
        title = dietBinding.TextViewDietTitle;
        title.setText("Diet for " + diet_type);
        AddData(diet_type,diet_range,diet_day);
        RecyclerView recyclerView = findViewById(R.id.recyclerListViewDiet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DietAdapter(dietList));
        backBtn = dietBinding.imageButtonback;

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DietActivity.this, HomeActivity.class));
            }
        });

        monBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(diet_type,diet_range,MONDAY);
                recyclerView.setAdapter(new DietAdapter(dietList));
            }
        });
        tueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(diet_type,diet_range,TUESDAY);
                recyclerView.setAdapter(new DietAdapter(dietList));
            }
        });
        wedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(diet_type,diet_range,WEDNESDAY);
                recyclerView.setAdapter(new DietAdapter(dietList));
            }
        });
        thruBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(diet_type,diet_range,THRUSDAY);
                recyclerView.setAdapter(new DietAdapter(dietList));
            }
        });
        friBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData(diet_type,diet_range,FRIDAY);
                recyclerView.setAdapter(new DietAdapter(dietList));
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
                    dietList.clear();
                    for (Diet item : diets) {
                        int resID = getResources().getIdentifier(item.getDietImg(), "drawable", getPackageName());
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
                Toast.makeText(this, "Query parameters is null. ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
    }
}