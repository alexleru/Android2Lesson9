package ru.alexey.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.alexey.weather.CustomView.CustomView;
import ru.alexey.weather.Fragments.FragmentSearch;
import ru.alexey.weather.Services.WeatherIntentService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String DATA_FOR_SERVICE = "DATA_FOR_SERVICE";
    String old_data = "0";
    SingletonForPreferences singleton = SingletonForPreferences.getInstance();
    TextView textViewTemp;
    TextView textViewHum;
    Intent intentToService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initSensor();
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        singleton.setAddData(0, sharedPreferences.
                getBoolean("show_wind_menu", false));
        singleton.setAddData(1, sharedPreferences.
                getBoolean("show_humidity_menu", false));
        singleton.setAddData(2, sharedPreferences.
                getBoolean("show_pressure_menu", false));
    }
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        CustomView customView = header.findViewById(R.id.custom_view);
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        textViewTemp = findViewById(R.id.temperature);
        textViewHum = findViewById(R.id.humidity);
    }

    private void initSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (sensorTemp != null){
            sensorManager.registerListener(sensorEventListenerTemp,
                    sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor sensorHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (sensorHum != null){
            sensorManager.registerListener(sensorEventListenerHum,
                    sensorHum, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    SensorEventListener sensorEventListenerTemp = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            String temp = "Temp: " + event.values[0] + "°C";
            startIntentService(temp);
            textViewTemp.setText(temp);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    SensorEventListener sensorEventListenerHum = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            String hum = "Hum: " + event.values[0] + "%";
            textViewHum.setText(hum);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void startIntentService(String data){
        if(!old_data.equals(data)){
            intentToService = new Intent(MainActivity.this, WeatherIntentService.class);
            intentToService.putExtra(DATA_FOR_SERVICE, data);
            startService(intentToService);
            old_data = data;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.show_wind_menu).setChecked(sharedPreferences.
                getBoolean("show_wind_menu", false));
        menu.findItem(R.id.show_humidity_menu).setChecked(sharedPreferences.
                getBoolean("show_humidity_menu", false));
        menu.findItem(R.id.show_pressure_menu).setChecked(sharedPreferences.
                getBoolean("show_pressure_menu", false));
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.show_wind_menu:
                singleton.setAddData(0, !item.isChecked());
                editorCommitSharedPreferences(item, "show_wind_menu");
                item.setChecked(!item.isChecked());
                return true;
            case R.id.show_humidity_menu:
                singleton.setAddData(1, !item.isChecked());
                editorCommitSharedPreferences(item, "show_humidity_menu");
                item.setChecked(!item.isChecked());
                return true;
            case R.id.show_pressure_menu:
                singleton.setAddData(2, !item.isChecked());
                editorCommitSharedPreferences(item, "show_pressure_menu");
                item.setChecked(!item.isChecked());
                return true;
            default:
                return false;
        }
    }

    private void editorCommitSharedPreferences(MenuItem item, String show_wind_menu) {
        editor.putBoolean(show_wind_menu, !item.isChecked());
        editor.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentSearch fragmentSearch;
        switch (id){
            case R.id.about_app:
                fragmentSearch =
                        (FragmentSearch) fragmentManager.findFragmentById(R.id.fragment_search);
                fragmentSearch.onClickMenuAboutApp();
                closeNav();
                return true;
            case R.id.feedback:
                fragmentSearch =
                        (FragmentSearch) fragmentManager.findFragmentById(R.id.fragment_search);
                fragmentSearch.onClickMenuFeedback();
                closeNav();
                return true;
            default:
                closeNav();
                return false;
        }
    }

    private void closeNav() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentToService);
    }
}
