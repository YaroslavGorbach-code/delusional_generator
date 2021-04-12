package com.YaroslavGorbach.delusionalgenerator.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.YaroslavGorbach.delusionalgenerator.data.Database;
import com.YaroslavGorbach.delusionalgenerator.data.Exercise_dao;
import com.YaroslavGorbach.delusionalgenerator.data.Description_dao;
import com.YaroslavGorbach.delusionalgenerator.data.Description_category_1;
import com.YaroslavGorbach.delusionalgenerator.data.Description_category_2;
import com.YaroslavGorbach.delusionalgenerator.data.Description_category_3;
import com.YaroslavGorbach.delusionalgenerator.data.Exercise;

import java.util.List;

public class RepoImpOLD {
    private final Exercise_dao mExDao;
    private final Description_dao mDescriptionDao;

    public RepoImpOLD(Application application){
        Database database = Database.getInstance(application);
        mExDao = database.exercise_dao();
        mDescriptionDao = database.description_dao();
    }

    public void update(Exercise exercise){
        new Thread(() -> mExDao.update(exercise)).start();
    }

    public LiveData<List<Exercise>> getExByCategory(int category) {
        return mExDao.getExercisesByCategory(category);
    }

    public LiveData<List<Exercise>> getFavoriteExs() {
        return mExDao.getFavoriteExercises();
    }

    public LiveData<Exercise> getExerciseById(int id) {
        return mExDao.getExerciseById(id);
    }

    public LiveData<Description_category_1> getDescription_category_1_ByExId(int exId){
        return mDescriptionDao.getDescription_category_1_ByExId(exId);
    }

    public LiveData<Description_category_2> getDescription_category_2_ByExId(int exId){
        return mDescriptionDao.getDescription_category_2_ByExId(exId);
    }

    public LiveData<Description_category_3> getDescription_category_3_ByExId(int exId){
        return mDescriptionDao.getDescription_category_3_ByExId(exId);
    }
}
