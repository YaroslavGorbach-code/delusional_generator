package com.YaroslavGorbach.delusionalgenerator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.YaroslavGorbach.delusionalgenerator.feature.BillingManager;
import com.YaroslavGorbach.delusionalgenerator.data.Exercise;
import com.YaroslavGorbach.delusionalgenerator.data.Repo;
import com.YaroslavGorbach.delusionalgenerator.feature.notifycation.MyNotificationManagerImp;
import com.YaroslavGorbach.delusionalgenerator.screen.aboutapp.AboutAppFragment;
import com.YaroslavGorbach.delusionalgenerator.workflow.ExerciseWorkflow;
import com.YaroslavGorbach.delusionalgenerator.workflow.NavWorkflow;
import com.YaroslavGorbach.delusionalgenerator.workflow.TrainingWorkflow;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavWorkflow.Router {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Repo repo = new Repo.RepoProvider().provideRepo(this);

        // if first open shack system theme and set it as app
        if (repo.getFirstOpen()){
            int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            repo.setNightMod(nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
        }

        if (repo.getNightMod()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        createChannel();

        BillingManager billingManager = new BillingManager(this, () -> {});
        billingManager.queryPurchases(() -> repo.setAdIsAllow(false));

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {});

        if (savedInstanceState == null) {
            Fragment fragment = new NavWorkflow();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_container, fragment)
                    .setPrimaryNavigationFragment(fragment)
                    .commit();

            // show trip and notification tomorrow if it is the first app open
            if (repo.getFirstOpen()){
                repo.setNotificationText(getString(R.string.notification_text));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new AboutAppFragment())
                        .addToBackStack(null)
                        .commit();

                NotificationManager notificationManager = ContextCompat.getSystemService(this, NotificationManager.class);
                new MyNotificationManagerImp().sendNotificationOnTime(
                        notificationManager,
                        this,
                        repo.getNotificationCalendar().getTimeInMillis() + TimeUnit.DAYS.toMillis(1),
                        repo.getNotificationText());
            }
            repo.setFirstOpen(false);
        }
    }

    @Override
    public void openTraining() {
        Fragment fragment = new TrainingWorkflow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .setPrimaryNavigationFragment(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void openExercise(Exercise.Name name, Exercise.Type type) {
        Fragment fragment = new ExerciseWorkflow();
        fragment.setArguments(ExerciseWorkflow.argsOf(name, type));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .setPrimaryNavigationFragment(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "1",
                    "App notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("App notification");
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}


