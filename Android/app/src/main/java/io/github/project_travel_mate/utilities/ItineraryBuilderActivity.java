package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Objects;

import io.github.project_travel_mate.R;

public class ItineraryBuilderActivity extends AppCompatActivity {
    private static final String EXTRA_DAYS = "extra_days";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_builder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int days = getIntent().getIntExtra(EXTRA_DAYS, 3);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ItineraryBuilderFragment.newInstance(days);
        fragmentManager.beginTransaction().replace(R.id.itinerary_root_layout, fragment).commit();

        setTitle(getString(R.string.itinerary_builder));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static Intent getStartIntent(Context context, int days) {
        Intent intent = new Intent(context, ItineraryBuilderActivity.class);
        intent.putExtra(EXTRA_DAYS, days);
        return intent;
    }
}
