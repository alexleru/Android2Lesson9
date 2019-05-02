package ru.alexey.weather.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class SysModel implements Serializable {
    @SerializedName("pod")
    @Expose
    public String pod;
}
