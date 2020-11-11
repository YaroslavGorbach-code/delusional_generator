package com.YaroslavGorbach.delusionalgenerator.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.YaroslavGorbach.delusionalgenerator.Database.Models.Exercise;
import com.YaroslavGorbach.delusionalgenerator.R;

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
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }


    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView ex_name;

        public ExerciseViewHolder (@NonNull View itemView) {
            super(itemView);
            ex_name = itemView.findViewById(R.id.item_ex_name);
            itemView.setOnClickListener(c ->{
                onItemListClick.onClickListener(mExercises.get(getAdapterPosition()), getAdapterPosition());
            });

        }

    }

}