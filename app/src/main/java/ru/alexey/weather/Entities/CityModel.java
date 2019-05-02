package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityModel implements Serializable {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("coord")
    @Expose
    public CoordModel coord;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("population")
    @Expose
    public long population;
}
