package com.YaroslavGorbach.delusionalgenerator.component.speakingEx;

import android.content.Context;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.YaroslavGorbach.delusionalgenerator.R;
import com.YaroslavGorbach.delusionalgenerator.data.Exercise;
import com.YaroslavGorbach.delusionalgenerator.data.Repo;
import com.YaroslavGorbach.delusionalgenerator.data.Statistics;
import com.YaroslavGorbach.delusionalgenerator.feature.statistics.StatisticsManager;
import com.YaroslavGorbach.delusionalgenerator.feature.voiceRecorder.VoiceRecorder;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class SpeakingExImp implements SpeakingEx {
    private final MutableLiveData<String> mWord = new MutableLiveData<>("null");
    private final MutableLiveData<Integer> mShortDesc = new MutableLiveData<>(R.string.short_desc_tt_1);
    private final MutableLiveData<Boolean> mIsRecording = new MutableLiveData<>();
    private final VoiceRecorder mVoiceRecorder;
    private final StatisticsManager mStatisticsManager;

    private Exercise mExercise;
    private final Repo mRepo;
    private final Resources mResources;
    private final Random mRandom = new Random();
    private int mClickCount = 0;

    public SpeakingExImp(
            Exercise.Name name,
            Repo repo,
            StatisticsManager statisticsManager,
            Resources resources,
            VoiceRecorder voiceRecorder
    ){
        mRepo = repo;
        mStatisticsManager = statisticsManager;
        mResources = resources;
        mVoiceRecorder = voiceRecorder;
        mRepo.getExercise(name).subscribe(exModel -> mExercise = exModel).dispose();

        // init immediately
        setShortDesc();
        setWord();
    }

    @Override
    public void onNext() {
        if (mExercise.category == Exercise.Category.TONGUE_TWISTER){
            mClickCount++;
            if (mClickCount >=  mExercise.shortDescIds.length){
                mClickCount = 0;
                setWord();
            }
        }else {
            setWord();
        }
        setShortDesc();
    }

    @Override
    public LiveData<Integer> getShortDescId() {
        return mShortDesc;
    }

    @Override
    public LiveData<String> getWord() {
        return mWord;
    }


    @Override
    public void saveStatistics() {
        if (mExercise.category == Exercise.Category.SPEAKING){
            mRepo.addStatistics(new Statistics(
                    mExercise.name, mStatisticsManager.getNumberWords(), new Date().getTime()));
        }else {
            mRepo.addStatistics(new Statistics(
                    mExercise.name, mStatisticsManager.getAverageTime(), new Date().getTime()));
        }
    }

    @Override
    public void onStartStopRecord(Context context) {
        if (mVoiceRecorder.getState()){
            mVoiceRecorder.stop();
            mIsRecording.postValue(false);
        }else {
            mVoiceRecorder.start(context, context.getString(mExercise.name.getNameId()));
            mIsRecording.postValue(true);
        }
    }

    @Override
    public LiveData<Boolean> getRecordingState() {
        return mIsRecording;
    }

    private void setWord(){
        mStatisticsManager.calNumberWords();
        mStatisticsManager.calAverageTime();

        List<String> words;
        switch (mExercise.name){
            case LINGUISTIC_PYRAMIDS:
                words = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case EASY_TONGUE_TWISTERS:
                words = mRepo.getWords(Repo.WordType.EASY_T_T, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
        }
    }

    private void setShortDesc() {
        if (mExercise.category == Exercise.Category.TONGUE_TWISTER) {
            mShortDesc.postValue(mExercise.shortDescIds[mClickCount]);
        }else {
            mShortDesc.postValue(mExercise.shortDescIds[mRandom.nextInt(mExercise.shortDescIds.length)]);
        }
    }
}
