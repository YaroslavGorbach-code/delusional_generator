package com.YaroslavGorbach.delusionalgenerator.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.YaroslavGorbach.delusionalgenerator.data.local.SharedPrefStorage;
import com.YaroslavGorbach.delusionalgenerator.feature.ad.AdManager;

public class SharedPrefStorageImp implements SharedPrefStorage {
    private final SharedPreferences mSharedPreferences;

    public SharedPrefStorageImp(Context context){
        mSharedPreferences = context.getSharedPreferences("com.YaroslavGorbach.delusionalgenerator", Context.MODE_PRIVATE);
    }

    @Override
    public boolean getFirstOpen() {
        return mSharedPreferences.getBoolean("firstOpen", true);
    }

    @Override
    public void setFirstOpen(boolean firstOpen) {
        mSharedPreferences.edit().putBoolean("firstOpen", firstOpen).apply();
    }

    @Override
    public int getNotificationHour() {
        return mSharedPreferences.getInt("notificationHour", 12);
    }

    @Override
    public void setNotificationHour(int hour) {
        mSharedPreferences.edit().putInt("notificationHour", hour).apply();
    }

    @Override
    public int getNotificationMinute() {
        return mSharedPreferences.getInt("notificationMinute", 30);
    }

    @Override
    public void setNotificationMinute(int minute) {
        mSharedPreferences.edit().putInt("notificationMinute", minute).apply();
    }

    @Override
    public String getNotificationText() {
        return mSharedPreferences.getString("notificationText", "Time to speak");
    }

    @Override
    public void setNotificationText(String text) {
        mSharedPreferences.edit().putString("notificationText", text).apply();
    }

    @Override
    public boolean getNotificationIsAllow() {
        return mSharedPreferences.getBoolean("notificationIsAllow", true);
    }

    @Override
    public void setNotificationIsAllow(boolean isEnable) {
        mSharedPreferences.edit().putBoolean("notificationIsAllow", isEnable).apply();
    }

    @Override
    public void setInterstitialAdCount(int count) {
        mSharedPreferences.edit().putInt("interstitialAdCount", count).apply();
    }

    @Override
    public int getInterstitialAdCount() {
        return mSharedPreferences.getInt("interstitialAdCount", AdManager.INTERSTITIAL_SHOW_LIMIT);
    }

    @Override
    public void setNightMod(boolean nightMod) {
        mSharedPreferences.edit().putBoolean("nightMod", nightMod).apply();
    }

    @Override
    public boolean getNightMod() {
        return mSharedPreferences.getBoolean("nightMod", false);
    }

    @Override
    public boolean getAdIsAllow() {
        return mSharedPreferences.getBoolean("adIsAllow", true);
    }

    @Override
    public void setAdIsAllow(boolean isAllow) {
        mSharedPreferences.edit().putBoolean("adIsAllow", isAllow).apply();
    }

    @Override
    public void setTimeLastReviewAsc(long time) {
        mSharedPreferences.edit().putLong("timeLastReviewAsc", time).apply();
    }

    @Override
    public long getTimeLastReviewAsc() {
        return mSharedPreferences.getLong("timeLastReviewAsc", 0);
    }

}
