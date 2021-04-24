package com.YaroslavGorbach.delusionalgenerator.data;

import android.content.Context;
import android.content.res.Resources;

import com.YaroslavGorbach.delusionalgenerator.screen.chartView.data.InputData;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface Repo {
    List<ExModel> getExercises();
    ExModel getExercise(ExModel.Name name);
    List<String> getWords(WordType type, Resources resources);
    Observable<Statistics> getStatistics(ExModel.Name name);
    Observable<Statistics> getStatisticsNext(ExModel.Name name, List<InputData> currentData);
    Observable<Statistics> getStatisticsPrevious(ExModel.Name name, List<InputData> currentData);
    void addStatistics(Statistics statistics);
    File[] getRecords(Context context);

    class RepoProvider{
        public RepoImp provideRepo(Context context){
            Database database = Database.getInstance(context);
            return new RepoImp(database);
        }
    }
    enum WordType {
        ALIVE,
        NOT_ALIVE,
        ABBREVIATION,
        FILLING,
        LETTER,
        PROFESSIONS,
        TERMS,
        EASY_T_T,
        DIFFICULT_T_T,
        VERY_DIFFICULT_T_T
    }

}
