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

import java.time.MonthDay;
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
        String diet_range = "Range 1";// bundle.getString("DIET_RANGE", null);
        String diet_type = "Vegan";// bundle.getString("DIET_TYPE", null);
        String diet_day = "Monday";// bundle.getString("DIET_DAY", null);
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