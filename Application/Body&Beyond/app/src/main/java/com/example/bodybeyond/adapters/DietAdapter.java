package com.example.bodybeyond.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodybeyond.R;
import com.example.bodybeyond.viewmodel.Diets;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder>{
    List<Diets> DietList;

    public DietAdapter(List<Diets> dietList) {
        DietList = dietList;
    }

    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View dietView = layoutInflater.inflate(R.layout.layout_diet_ext,parent,false);
        DietViewHolder dietViewHolder = new DietViewHolder(dietView);
        return dietViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder holder, int position) {
        Diets diet = DietList.get(position);
        holder.dietName.setText(diet.getDietName());
        holder.dietImg.setImageResource(diet.getDietImageItem());
        holder.dietDescription.setText(diet.getDietDescription());

    }

    @Override
    public int getItemCount() {
        return DietList.size();
    }

    public class DietViewHolder extends RecyclerView.ViewHolder{
        ImageView dietImg;
        TextView dietDescription;
        TextView dietName;

        public  DietViewHolder(@NonNull View itemView) {
            super(itemView);

            dietImg = itemView.findViewById(R.id.dietListImageView);
            dietDescription = itemView.findViewById(R.id.txtDietDesc);
            dietName = itemView.findViewById(R.id.txtDietName);

        }
    }
}
