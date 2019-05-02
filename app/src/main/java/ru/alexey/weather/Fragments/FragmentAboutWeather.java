package ru.alexey.weather.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.alexey.weather.Entities.WeatherRequestModel;
import ru.alexey.weather.R;
import ru.alexey.weather.SingletonForPreferences;

public class FragmentAboutWeather extends Fragment {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    SingletonForPreferences singleton = SingletonForPreferences.getInstance();
    public static FragmentAboutWeather create(Bundle bundle){
        FragmentAboutWeather fragmentAboutWeather = new FragmentAboutWeather();
        fragmentAboutWeather.setArguments(bundle);
        return fragmentAboutWeather;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_fragment_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        weatherAdapter = new WeatherAdapter(getAddData(), getResources(), getWeatherRequestModel());
        recyclerView.setAdapter(weatherAdapter);
    }

    private String getCityName(){
        String cityName;
        try {
            cityName = getArguments().getString(FragmentSearch.CITY, "empty search");
        } catch (Exception e) {
            e.printStackTrace();
            cityName = "empty search";
        }
        return cityName;
    }

    private WeatherRequestModel getWeatherRequestModel(){
        WeatherRequestModel weatherRequestModel;
        weatherRequestModel = (WeatherRequestModel) getArguments().getSerializable(FragmentSearch.RESPONSE);

        return weatherRequestModel;
    }

    private boolean [] getAddData(){
        return singleton.getAddData();
    }
}
