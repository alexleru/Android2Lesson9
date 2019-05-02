package ru.alexey.weather.ModelOfData;

import java.io.Serializable;

public class CityModelOfData implements Serializable {
    public String city;
    public WeatherModelOfData[] weatherModelOfData;

    public CityModelOfData(String city, WeatherModelOfData[] weatherModelOfData) {
        this.city = city;
        this.weatherModelOfData = weatherModelOfData;
    }
}
