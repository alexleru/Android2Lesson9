package ru.alexey.weather.Rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.alexey.weather.Entities.WeatherRequestModel;

public interface IOpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequestModel> loadWeather (@Query("q") String city,
                                           @Query("units") String units,
                                           @Query("cnt") int lines,
                                           @Query("appid") String keyApi);
    @GET("data/2.5/forecast")
    Call<WeatherRequestModel> loadWeatherCoordinate (@Query("lat") String lat,
                                                     @Query("lon") String lon,
                                                     @Query("units") String units,
                                                     @Query("cnt") int lines,
                                                     @Query("appid") String keyApi);
}
