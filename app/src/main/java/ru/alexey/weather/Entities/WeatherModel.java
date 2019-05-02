package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherModel implements Serializable {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("main")
    @Expose
    public String main;
    @SerializedName("description")
    @Expose
    public String decription;
    @SerializedName("icon")
    @Expose
    public String icon;
}
