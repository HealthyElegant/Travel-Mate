package io.github.project_travel_mate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import io.github.project_travel_mate.login.LoginActivity;

import static utils.Constants.USER_TOKEN;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final PathView pathView = findViewById(R.id.pathView);
        pathView.getPathAnimator()
                .delay(1000)
                .duration(1000)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();

        pathView.useNaturalColors();
        pathView.setFillAfter(true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Check for the user_token here & redirect to corresponding class
        // If token is null -> LoginActivity, else MainActivity
        new Handler().postDelayed(() -> {
            Intent intent;
            if (mSharedPreferences.getString(USER_TOKEN, null) != null) {
                intent = MainActivity.getStartIntent(SplashActivity.this);
            } else {
                intent = LoginActivity.getStartIntent(SplashActivity.this);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
