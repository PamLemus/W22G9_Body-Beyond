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

public class DietCatAdapter extends RecyclerView.Adapter<DietCatAdapter.DietCatViewHolder> {

    List<Diets> DietsCatList;
    OnItemClickListener onItemClickListener;

    public DietCatAdapter(List<Diets> dietsList) { DietsCatList = dietsList;}

    public DietCatAdapter(List<Diets> dietsList, OnItemClickListener onItemClickListener) {
        DietsCatList = dietsList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DietCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View dietCatView = layoutInflater.inflate(R.layout.layout_diet_exercise,parent,false);
        DietCatAdapter.DietCatViewHolder dietCatViewHolder = new DietCatAdapter.DietCatViewHolder(dietCatView);
        return dietCatViewHolder;
   }

    @Override
    public void onBindViewHolder(@NonNull DietCatViewHolder holder, int position) {
        Diets diet = DietsCatList.get(position);
        holder.itemImgView.setImageResource(DietsCatList.get(position).getDietImageItem());
        holder.itemTxtView.setText(DietsCatList.get(position).getDietDescription());

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
        return DietsCatList.size();
    }

    public class DietCatViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImgView;
        TextView itemTxtView;
        //you can add other views as needed in your layout
        public DietCatViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImgView = itemView.findViewById(R.id.imgViewDietExercise);
            itemTxtView = itemView.findViewById(R.id.txtViewDietExercise);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}