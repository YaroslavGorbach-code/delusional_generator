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

    private final Exercise mExercise;
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
        mExercise = mRepo.getExercise(name);

        // init immediately
        setShortDesc();
        setWord();
    }

    @Override
    public void onNext() {
        if (mExercise.getCategory() == Exercise.Category.TONGUE_TWISTER){
            mClickCount++;
            if (mClickCount >=  mExercise.getShortDescIds().length){
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
        if (mExercise.getCategory() == Exercise.Category.SPEAKING){
            mRepo.addStatistics(new Statistics(
                    mExercise.getName(), mStatisticsManager.getNumberWords(), new Date().getTime()));
        }else {
            mRepo.addStatistics(new Statistics(
                    mExercise.getName(), mStatisticsManager.getAverageTime(), new Date().getTime()));
        }
    }

    @Override
    public void onStartStopRecord(Context context) {
        if (mVoiceRecorder.getState()){
            mVoiceRecorder.stop();
            mIsRecording.postValue(false);
        }else {
            mVoiceRecorder.start(context, context.getString(mExercise.getName().getNameId()));
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
        List<String> words2;
        switch (mExercise.getName()){
            case LINGUISTIC_PYRAMIDS:
            case WHAT_I_SEE_I_SING_ABOUT:
            case MAGIC_NAMING:
            case BUYING_SELLING:
            case RORSCHACH_TEST:
                words = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case RAVEN_LOOK_LIKE_A_TABLE:
                words = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                words2 = mRepo.getWords(Repo.WordType.ALIVE, mResources);
                mWord.postValue(words2.get(mRandom.nextInt(words2.size())) + " - " + words.get(mRandom.nextInt(words.size())));
                break;
            case STORYTELLER_IMPROVISER:
                words = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                words2 = mRepo.getWords(Repo.WordType.ALIVE, mResources);
                mWord.postValue(words2.get(mRandom.nextInt(words2.size())) + ", " + words.get(mRandom.nextInt(words.size())) + ", "+
                        words2.get(mRandom.nextInt(words2.size())) + ", " + words.get(mRandom.nextInt(words.size())));
                break;
            case REMEMBER_ALL:
                words = mRepo.getWords(Repo.WordType.LETTER, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case OTHER_ABBREVIATIONS:
                words = mRepo.getWords(Repo.WordType.ABBREVIATION, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case ADVANCED_BINDING:
                words = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())) + " - " + words.get(mRandom.nextInt(words.size())));
                break;
            case CO_AUTHORED_WITH_DAHL:
                words = mRepo.getWords(Repo.WordType.TERMS, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case WILL_NOT_BE_WORSE:
                words = mRepo.getWords(Repo.WordType.PROFESSIONS, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case QUESTION_ANSWER:
                words = mRepo.getWords(Repo.WordType.QUESTIONS, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case RAVEN_LOOK_LIKE_A_TABLE_FILINGS:
                words = mRepo.getWords(Repo.WordType.FILLING, mResources);
                words2 = mRepo.getWords(Repo.WordType.NOT_ALIVE, mResources);
                mWord.postValue(words2.get(mRandom.nextInt(words2.size())) + " - " + words.get(mRandom.nextInt(words.size())));
                break;
            case EASY_TONGUE_TWISTERS:
                words = mRepo.getWords(Repo.WordType.EASY_T_T, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case DIFFICULT_TONGUE_TWISTERS:
                words = mRepo.getWords(Repo.WordType.DIFFICULT_T_T, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
            case VERY_DIFFICULT_TONGUE_TWISTERS:
                words = mRepo.getWords(Repo.WordType.VERY_DIFFICULT_T_T, mResources);
                mWord.postValue(words.get(mRandom.nextInt(words.size())));
                break;
        }
    }

    private void setShortDesc() {
        if (mExercise.getCategory() == Exercise.Category.TONGUE_TWISTER) {
            mShortDesc.postValue(mExercise.getShortDescIds()[mClickCount]);
        }else {
            mShortDesc.postValue(mExercise.getShortDescIds()[mRandom.nextInt(mExercise.getShortDescIds().length)]);
        }
    }
}
