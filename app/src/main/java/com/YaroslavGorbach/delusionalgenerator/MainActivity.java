package com.YaroslavGorbach.delusionalgenerator;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.YaroslavGorbach.delusionalgenerator.data.Exercise;
import com.YaroslavGorbach.delusionalgenerator.data.Repo;
import com.YaroslavGorbach.delusionalgenerator.feature.billing.BillingManager;
import com.YaroslavGorbach.delusionalgenerator.feature.billing.BillingManagerImp;
import com.YaroslavGorbach.delusionalgenerator.feature.notifycation.MyNotificationManagerImp;
import com.YaroslavGorbach.delusionalgenerator.screen.aboutapp.AboutAppFragment;
import com.YaroslavGorbach.delusionalgenerator.screen.nav.NavFragment;
import com.YaroslavGorbach.delusionalgenerator.workflow.ExerciseWorkflow;
import com.YaroslavGorbach.delusionalgenerator.workflow.TrainingWorkflow;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavFragment.Router {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Repo repo = ((App) getApplication()).provideRepo();
        BillingManager billingManager = new BillingManagerImp(this);
        billingManager.queryPurchases(isRemoved -> repo.setAdIsAllow(!isRemoved));

        if (savedInstanceState == null) {
            Fragment fragment = new NavFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_container, fragment)
                    .setPrimaryNavigationFragment(fragment)
                    .commit();

            // show trip and notification tomorrow if it is the first app open
            if (repo.getFirstOpen()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new AboutAppFragment())
                        .addToBackStack(null)
                        .commit();

                repo.setNotificationText(getString(R.string.notification_text));
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


}


