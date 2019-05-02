package ru.alexey.weather;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ru.alexey.weather.Fragments.FragmentAboutApp;
import ru.alexey.weather.Fragments.FragmentAboutWeather;
import ru.alexey.weather.Fragments.FragmentFeedback;
import ru.alexey.weather.Fragments.FragmentSearch;

public class ActivityAboutWeather extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_weather);
        String about = getIntent().getStringExtra(FragmentSearch.ABOUT);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        startFragment(savedInstanceState, about);
    }

    private void startFragment(Bundle savedInstanceState, String about) {
        if (savedInstanceState == null) {
            Fragment details;
            switch (about) {
                case FragmentSearch.ABOUT_WEATHER:
                    details = new FragmentAboutWeather();
                    details.setArguments(getIntent().getExtras());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_city_layout, details).commit();
                    break;
                case FragmentSearch.ABOUT_APP:
                    details = new FragmentAboutApp();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_city_layout, details).commit();
                    break;
                case FragmentSearch.FEEDBACK:
                    details = new FragmentFeedback();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_city_layout, details).commit();
                    break;
            }
        }
    }
}
