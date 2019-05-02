package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoordModel implements Serializable {
    @SerializedName("lat")
    @Expose
    public double lat;
    @SerializedName("lon")
    @Expose
    public double lon;
}
