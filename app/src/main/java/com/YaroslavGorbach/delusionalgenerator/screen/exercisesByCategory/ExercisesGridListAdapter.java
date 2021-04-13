package com.YaroslavGorbach.delusionalgenerator.screen.exercisesByCategory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.YaroslavGorbach.delusionalgenerator.data.oldDataLayer.Exercise;
import com.YaroslavGorbach.delusionalgenerator.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ExercisesGridListAdapter extends RecyclerView.Adapter<ExercisesGridListAdapter.ExerciseViewHolder> {

    private List<Exercise> mExercises;

    private final onItemListClick onItemListClick;

    public interface onItemListClick {
        void onClickListener(Exercise exercise, int position);
    }

    public ExercisesGridListAdapter(List<Exercise> exercises, onItemListClick onItemListClick) {
        this.mExercises = exercises;
        this.onItemListClick = onItemListClick;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excersice_grid_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.ex_name.setText(mExercises.get(position).name);
        Glide.with(holder.itemView.getContext()).load(mExercises.get(position).pic).into(holder.ex_image);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }


    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView ex_name;
        private ImageView ex_image;

        public ExerciseViewHolder (@NonNull View itemView) {
            super(itemView);
            ex_name = itemView.findViewById(R.id.item_grid_ex_name);
            ex_image = itemView.findViewById(R.id.ex_grid_item_image);

            itemView.setOnClickListener(c ->{
                onItemListClick.onClickListener(mExercises.get(getAdapterPosition()), getAdapterPosition());
            });

        }

    }

}