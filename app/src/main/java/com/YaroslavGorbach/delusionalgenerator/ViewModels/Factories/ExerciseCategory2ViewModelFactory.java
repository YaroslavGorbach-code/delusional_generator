package com.YaroslavGorbach.delusionalgenerator.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.YaroslavGorbach.delusionalgenerator.ViewModels.ExerciseCategory2ViewModel;

public class ExerciseCategory2ViewModelFactory  extends ViewModelProvider.NewInstanceFactory{

    private int id;
    private Application application;
    public ExerciseCategory2ViewModelFactory(Application application, int exId){
        super();
        id = exId;
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExerciseCategory2ViewModel.class)) {
            return (T)  new ExerciseCategory2ViewModel(application, id);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}