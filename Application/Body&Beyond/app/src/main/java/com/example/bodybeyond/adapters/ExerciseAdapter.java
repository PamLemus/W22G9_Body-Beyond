package com.example.bodybeyond.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodybeyond.R;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    List<Exercises> exercisesList;

    public ExerciseAdapter(List<Exercises> exercisesList) {
        this.exercisesList = exercisesList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View exerciseExtView = layoutInflater.inflate(R.layout.layout_exercises_ext, parent, false);
        // Return a new holder instance
        ExerciseViewHolder viewHolder = new ExerciseViewHolder(exerciseExtView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercises exercises = exercisesList.get(position);
        holder.exerciseName.setText(exercises.getExerciseName());
        holder.exerciseDescription.setText(exercises.getDescription());
        holder.exerciseImgItem.setImageResource(exercises.getImageItem());
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
            exerciseDescription = (TextView) itemView.findViewById(R.id.txtExerciseDescId);
            exerciseName = (TextView) itemView.findViewById(R.id.txtExerciseName);
        }
    }
}
