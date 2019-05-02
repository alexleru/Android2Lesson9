package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListModel implements Serializable {
    @SerializedName("dt")
    @Expose
    public long dt;
    @SerializedName("main")
    @Expose
    public MainModel main;
    @SerializedName("weather")
    @Expose
    public WeatherModel[] weather;
    @SerializedName("clouds")
    @Expose
    public CloudsModel clouds;
    @SerializedName("wind")
    @Expose
    public WindModel wind;
    @SerializedName("sys")
    @Expose
    public SysModel sys;
    @SerializedName("dt_txt")
    @Expose
    public String dt_txt;
}
