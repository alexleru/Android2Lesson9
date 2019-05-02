package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CloudsModel implements Serializable {
    @SerializedName("all")
    @Expose
    public int all;
}
