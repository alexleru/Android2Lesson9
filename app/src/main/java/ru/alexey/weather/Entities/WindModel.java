package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WindModel implements Serializable {
    @SerializedName("speed")
    @Expose
    public double speed;
    @SerializedName("deg")
    @Expose
    public double deg;
}
