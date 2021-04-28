package com.YaroslavGorbach.delusionalgenerator.screen.nav;

import com.YaroslavGorbach.delusionalgenerator.component.vocabularyEx.VocabularyEx;
import com.YaroslavGorbach.delusionalgenerator.data.Exercise;

public interface Navigation {
    void openSpeakingEx(Exercise.Name name);
    void openVocabularyEx(Exercise.Name name);
    void openDescription(Exercise.Name name);
    void showFinishDialog(VocabularyEx.Result result);
    void openDailyTraining();
    void up();
}