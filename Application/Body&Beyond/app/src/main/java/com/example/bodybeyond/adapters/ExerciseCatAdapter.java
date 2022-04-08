package com.example.bodybeyond.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodybeyond.R;
import com.example.bodybeyond.viewmodel.Diets;
import com.example.bodybeyond.viewmodel.Exercises;

import java.util.List;

public class ExerciseCatAdapter extends RecyclerView.Adapter<ExerciseCatAdapter.ExerciseCatViewHolder> {

    List<Exercises> ExerciseCatList;
    OnItemClickListener onItemClickListener;

    public ExerciseCatAdapter(List<Exercises> exerciseList) { ExerciseCatList = exerciseList;}

    public ExerciseCatAdapter(List<Exercises> exerciseList, OnItemClickListener onItemClickListener) {
        ExerciseCatList = exerciseList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ExerciseCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View exerciseCatView = layoutInflater.inflate(R.layout.layout_diet_exercise,parent,false);
        ExerciseCatAdapter.ExerciseCatViewHolder exerciseCatViewHolder = new ExerciseCatAdapter.ExerciseCatViewHolder(exerciseCatView);
        return exerciseCatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseCatViewHolder holder, int position) {
        Exercises exercises = ExerciseCatList.get(position);
        holder.itemImgView.setImageResource(ExerciseCatList.get(position).getImageItem());
        holder.itemTxtView.setText(ExerciseCatList.get(position).getDescription());

        holder.itemImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setBackgroundColor(Color.rgb(255,229,144));
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ExerciseCatList.size();
    }

    public class ExerciseCatViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImgView;
        TextView itemTxtView;
        //you can add other views as needed in your layout
        public ExerciseCatViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImgView = itemView.findViewById(R.id.imgViewDietExercise);
            itemTxtView = itemView.findViewById(R.id.txtViewDietExercise);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}