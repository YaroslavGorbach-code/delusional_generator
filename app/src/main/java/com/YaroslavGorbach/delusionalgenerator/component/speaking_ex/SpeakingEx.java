package com.YaroslavGorbach.delusionalgenerator.component.speaking_ex;

import androidx.lifecycle.LiveData;

public interface SpeakingEx {
    void nextWord();
    void startPauseChronometer();
    void startStopRecord();
    LiveData<Boolean> getRecordingState();
    String getShortDesc();
    LiveData<String> getWord();
}