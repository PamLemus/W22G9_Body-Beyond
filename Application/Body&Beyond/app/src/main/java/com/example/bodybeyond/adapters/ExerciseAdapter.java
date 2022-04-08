package com.example.bodybeyond.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bodybeyond.R;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    List<Exercises> exercisesList;
    Context context;
    public ExerciseAdapter(List<Exercises> exercisesList) {
        this.exercisesList = exercisesList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseViewHolder viewHolder = null;
        try{
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            // Inflate the custom layout
            View exerciseExtView = layoutInflater.inflate(R.layout.layout_exercises_ext, parent, false);
            // Return a new holder instance
            viewHolder = new ExerciseViewHolder(exerciseExtView);
            context = parent.getContext();
        }
        catch (Exception e)
        {
            Log.e("EXERCISE_ADAPTER", e.getMessage());
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        try
        {
            Exercises exercises;
            exercises = exercisesList.get(position);
            holder.exerciseName.setText(exercises.getExerciseName());
            holder.exerciseDescription.setText(exercises.getDescription());
            Glide.with(context).load(exercises.getImageItem()).into(holder.exerciseImgItem);
            //holder.exerciseImgItem.setImageResource(exercises.getImageItem());
        }
        catch(Exception e)
        {
            Log.e("EXERCISE_ADAPTER", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        ImageView exerciseImgItem;
        TextView exerciseDescription;
        TextView exerciseName;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseImgItem = (ImageView) itemView.findViewById(R.id.exerciseListImageViewId);
            // Adding the gif here using glide library

            exerciseDescription = (TextView) itemView.findViewById(R.id.txtExerciseDescId);
            exerciseName = (TextView) itemView.findViewById(R.id.txtExerciseName);
        }
    }
}
