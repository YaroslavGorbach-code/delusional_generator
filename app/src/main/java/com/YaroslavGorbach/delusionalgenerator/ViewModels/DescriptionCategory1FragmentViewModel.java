package com.YaroslavGorbach.delusionalgenerator.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.YaroslavGorbach.delusionalgenerator.Database.Models.Description_category_1;
import com.YaroslavGorbach.delusionalgenerator.Database.Repo;

import java.util.List;

public class DescriptionCategory1FragmentViewModel extends AndroidViewModel {
    private final LiveData<Description_category_1> mDescription;

    public DescriptionCategory1FragmentViewModel(@NonNull Application application, int exId) {
        super(application);
        Repo mRepo = new Repo(application);
         mDescription = mRepo.getDescription_category_1_ByExId(exId);
    }

    public LiveData<Description_category_1> getDescription(){
        return mDescription;
    }
}
