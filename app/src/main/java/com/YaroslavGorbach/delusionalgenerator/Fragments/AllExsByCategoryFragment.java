package com.YaroslavGorbach.delusionalgenerator.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.YaroslavGorbach.delusionalgenerator.Adapters.ExercisesGridListAdapter;
import com.YaroslavGorbach.delusionalgenerator.Database.ViewModels.ExercisesViewModel;
import com.YaroslavGorbach.delusionalgenerator.R;
import com.YaroslavGorbach.delusionalgenerator.Utility;



public class AllExsByCategoryFragment extends Fragment {



    private int mExCategoryId;
    private ExercisesViewModel mExercisesViewModel;
    private ExercisesGridListAdapter mAdapter;
    private RecyclerView mRecycler;


    public AllExsByCategoryFragment() {
        // Required empty public constructor
    }


    public static AllExsByCategoryFragment newInstance(){
        return new AllExsByCategoryFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.bttm_nav).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_all_exs_by_category, container, false);
    mRecycler = view.findViewById(R.id.list_exs_by_category);
    mExCategoryId = AllExsByCategoryFragmentArgs.fromBundle(getArguments()).getCategoryId();
    mExercisesViewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);


        /*Создаем адаптер*/
        mExercisesViewModel.getExByCategory(mExCategoryId).observe(getViewLifecycleOwner(), exercises -> {
            mAdapter = new ExercisesGridListAdapter(exercises, (exercise, position) -> {
                int id = exercise.id;
                NavDirections action = AllExsByCategoryFragmentDirections.
                        actionAllExsByCategoryFragmentToExercisesDescriptionFragment().setExId(id);
                Navigation.findNavController(view).navigate(action);
            });

           int mNoOfColumns = Utility.calculateNoOfColumns(getContext(), 190);
            mRecycler.setHasFixedSize(true);
            mRecycler.setLayoutManager(new StaggeredGridLayoutManager( mNoOfColumns, StaggeredGridLayoutManager.VERTICAL));
            mRecycler.setAdapter(mAdapter);
        });

        return view;
    }

}