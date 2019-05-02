package ru.alexey.weather.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alexey.weather.ActivityAboutWeather;
import ru.alexey.weather.Database.DataBaseHelper;
import ru.alexey.weather.Database.WeatherTable;
import ru.alexey.weather.Entities.ListModel;
import ru.alexey.weather.Entities.WeatherModel;
import ru.alexey.weather.Entities.WeatherRequestModel;
import ru.alexey.weather.ModelOfData.CityModelOfData;
import ru.alexey.weather.ModelOfData.WeatherModelOfData;
import ru.alexey.weather.R;
import ru.alexey.weather.Rest.OpenWeatherRepo;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentSearch extends Fragment {
    public static final String ABOUT_WEATHER = "ABOUT_WEATHER";
    public static final String ABOUT_APP = "ABOUT_APP";
    public static final String FEEDBACK = "FEEDBACK";
    public final static String CITY = "CITY";
    public final static String ABOUT = "ABOUT";
    public final static String RESPONSE = "RESPONSE";
    private final static int MY_PERMISSIONS_REQUEST = 1;
    private Boolean statusPermission = false;
    private View view;
    private String cityName;
    private boolean isExitFragmentAboutWeather;
    private WeatherRequestModel responseSerializable;
    private SQLiteDatabase database;
    private LocationManager locationManager;
    private String provider;
    private String latitude;
    private String longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initDB();
        requestPermissionFor();
    }

    //Инициализируем базу данных, передаем возмножность читать.
    private void initDB() {
        database = new DataBaseHelper(getContext()).getWritableDatabase();
    }

    //Запрашиваем разрешения если они отсутствуют!
    private void requestPermissionFor() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && isPermissionName(Manifest.permission.ACCESS_COARSE_LOCATION)
                && isPermissionName(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        } else {
            statusPermission = true;
        }
    }

    private boolean isPermissionName(String permissionName) {
        return ContextCompat.checkSelfPermission(getActivity(),
                permissionName) != PackageManager.PERMISSION_GRANTED;
    }

    //Получаем ответ на разрешение и в зависимости от ответа действуем
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "Permission allowed!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),
                            "You can't use this function. You should check permissions.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Определяем ориентацию эрана
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isExitFragmentAboutWeather =
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE;
    }

    private Intent getIntentAboutWeather() {
        Intent intent = new Intent(getActivity(), ActivityAboutWeather.class);
        intent.putExtra(CITY, cityName);
        intent.putExtra(ABOUT, ABOUT_WEATHER);
        intent.putExtra(RESPONSE, responseSerializable);
        return intent;
    }

    private Intent getIntentAboutApp() {
        Intent intent = new Intent(getActivity(), ActivityAboutWeather.class);
        intent.putExtra(ABOUT, ABOUT_APP);
        return intent;
    }

    private Intent getIntentFeedBack() {
        Intent intent = new Intent(getActivity(), ActivityAboutWeather.class);
        intent.putExtra(ABOUT, FEEDBACK);
        return intent;
    }

    private Bundle getBundleAboutWeather() {
        Bundle bundle = new Bundle();
        bundle.putString(CITY, cityName);
        bundle.putSerializable(RESPONSE, responseSerializable);
        return bundle;
    }

    private void initView() {
        view.findViewById(R.id.cardViewMoscow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCellSearch(Objects.requireNonNull(getResources().getString(R.string.moscow)));
            }
        });
        view.findViewById(R.id.cardViewSaintPeterburg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCellSearch(Objects.requireNonNull(getResources().getString(R.string.saint_petersburg)));
            }
        });
        view.findViewById(R.id.cardViewСoordinate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCellSearch(Objects.requireNonNull(getResources().getString(R.string.weather_by_coordinates)));
            }
        });
    }

    private void onClickCellSearch(String cityNameOfCard) {
        cityName = cityNameOfCard;
        if (cityNameOfCard.equals(getResources().getString(R.string.weather_by_coordinates))) {
            requestPermissionFor();
            requestLocation();
        } else requestRetrofit(cityName);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = Double.toString(location.getLatitude());
                    longitude = Double.toString(location.getLongitude());
                    requestRetrofitCoordinate(latitude, longitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            }, null);
        }
    }


    // В зависимости от ориентации экрана создаем фрагмент или новое активити
    private void chooseOrientationAndStart() {
        if(isExitFragmentAboutWeather){
            showFragmentAboutWeather();
        }
        else {
            startActivity(getIntentAboutWeather());
        }
    }

    private void requestRetrofit(final String cityName){
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(cityName,
                "metric",
                16,
                "f3f2763fe63803beef4851d6365c83bc").enqueue(getCallback());
    }

    private void requestRetrofitCoordinate(final String lat, final String lon){
        OpenWeatherRepo.getSingleton().getAPI().loadWeatherCoordinate(lat, lon,
                "metric",
                16,
                "f3f2763fe63803beef4851d6365c83bc").enqueue(getCallback());
    }

    private Callback<WeatherRequestModel> getCallback() {
        return new Callback<WeatherRequestModel>() {
    @Override
    public void onResponse(@NonNull Call<WeatherRequestModel> call,
                           @NonNull Response<WeatherRequestModel> response) {
        if(response.body() != null && response.isSuccessful()){
            responseSerializable = response.body();
            addElements(responseSerializable);
            chooseOrientationAndStart();
        }
    }

    @Override
    public void onFailure(@NonNull Call<WeatherRequestModel> call,
                          @NonNull Throwable t) {
        Toast.makeText(getContext(),  getResources().getString(R.string.error) + t, Toast.LENGTH_LONG).show();
    }
};
    }

    //Сохранение данных в базу данных. При создании таблицы я добавил ключ на два поля город и дату-время
    // , поэтому здесь делаю insert, который добавляет данные (игнорируя существующие insertWithOnConflict)
    // и update, который обновяет существующие
    private void addElements(@NonNull WeatherRequestModel response){
        WeatherModelOfData[] weatherModelOfDatas = new WeatherModelOfData[response.list.length];
        int i=0;
        for(ListModel list : response.list){
            WeatherModelOfData weatherModelOfData = new WeatherModelOfData();
            weatherModelOfData.date = list.dt;
            weatherModelOfData.date_txt = list.dt_txt;
            weatherModelOfData.temperature = list.main.temp;
            weatherModelOfData.humidity = list.main.humidity;
            weatherModelOfData.wind = list.wind.speed;
            weatherModelOfData.wind_of_direction = list.wind.deg;
            weatherModelOfData.pressure = list.main.pressure;
            weatherModelOfData.picture = list.weather[0].main;
            weatherModelOfDatas[i++] = weatherModelOfData;
        }
        String cityName = response.city.name;
        CityModelOfData cityModelOfData = new CityModelOfData(cityName, weatherModelOfDatas);
        WeatherTable.insertElement(cityModelOfData, database);
        WeatherTable.updateElement(cityModelOfData, database);
    }


    public void onClickMenuAboutApp() {
        if(isExitFragmentAboutWeather){
            showFragmentAboutApp();
        }
        else{
            startActivity(getIntentAboutApp());
        }
    }

    public void onClickMenuFeedback() {
        if(isExitFragmentAboutWeather){
            showFragmentFeedback();
        }
        else{
            startActivity(getIntentFeedBack());
        }
    }

    private void showFragmentAboutWeather(){
        FragmentAboutWeather detail = FragmentAboutWeather.create(getBundleAboutWeather());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_about_weather, detail);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.commit();
    }

    private void showFragmentAboutApp() {
        FragmentAboutApp detail = new FragmentAboutApp();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_about_weather, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }

    private void showFragmentFeedback() {
        FragmentFeedback detail = new FragmentFeedback();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_about_weather, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }
}
