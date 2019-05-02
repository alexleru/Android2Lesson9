package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainModel implements Serializable {
    @SerializedName("temp")
    @Expose
    public double temp;
    @SerializedName("temp_min")
    @Expose
    public double temp_min;
    @SerializedName("temp_max")
    @Expose
    public double temp_max;
    @SerializedName("pressure")
    @Expose
    public double pressure;
    @SerializedName("sea_level")
    @Expose
    public double sea_level;
    @SerializedName("grnd_level")
    @Expose
    public double grnd_level;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("temp_kf")
    @Expose
    public double temp_kf;
}
