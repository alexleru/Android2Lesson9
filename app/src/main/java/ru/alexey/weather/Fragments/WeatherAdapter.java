package ru.alexey.weather.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.alexey.weather.Entities.WeatherRequestModel;
import ru.alexey.weather.R;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private String cityName;
    private boolean [] addData;
    private Resources resources;
    private WeatherRequestModel weatherRequestModel;

    public WeatherAdapter(boolean[] addData,
                          Resources resources,
                          WeatherRequestModel weatherRequestModel) {
        this.cityName = weatherRequestModel.city.name;
        this.addData = addData;
        this.resources = resources;
        this.weatherRequestModel = weatherRequestModel;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item, viewGroup, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int i) {
        weatherViewHolder.textCityName.setText(cityName);
        String showAddData = showAddData(i);
        weatherViewHolder.textCell.setText(showAddData);
        loadImage(i, weatherViewHolder.imageView);
    }


    @Override
    public int getItemCount() {
        int i  = weatherRequestModel.list.length;
        return i;
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView textCityName;
        TextView textCell;
        ImageView imageView;

        WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            textCityName = view.findViewById(R.id.text_view_city_name);
            textCell = view.findViewById(R.id.cell_text);
            imageView = view.findViewById(R.id.cell_image);
        }
    }

    private String showAddData(int index){
        String delimiter = "\n";
        String addInfo = substringLess2(weatherRequestModel.list[index].dt_txt);
        addInfo = addInfo.concat(delimiter
                + round(weatherRequestModel.list[index].main.temp, 1)
                + " ℃");
        for (int i = 0 ; i < addData.length; i++){
            if (addData[i] && i == 0) {
                addInfo = addInfo.concat(delimiter
                        + round(weatherRequestModel.list[index].wind.speed, 0)
                        + " " + resources.getString(R.string.metr_per_sec)
                        + ",  "
                        + windDirectionToString(weatherRequestModel.list[index].wind.deg));
            }
            if (addData[i] && i == 1) {
                addInfo = addInfo.concat(delimiter + weatherRequestModel.list[index].main.humidity + " %");
            }
            if (addData[i] && i == 2) {
                //Поделил на 1,33322, что бы получить значения из кПа в мм.рт.ст
                addInfo = addInfo.concat(delimiter
                        + round(weatherRequestModel.list[index].main.pressure/1.33322, 1)
                        + " " + resources.getString(R.string.mmhg));
                }
        }
        return addInfo;
    }

    //метод, который округляет на заданное кол-во знаков
    private double round(double value, int number){
        return Math.round(value * Math.pow(10, number))/Math.pow(10, number);
    }

    //Метод убирает последние два символа у строки
    private String substringLess2(String str){
        return str.substring(0, str.length()-3);
    }

    //получаем направление ветра в градусах и возращаем в описательном виде
    // (90 - это Восточный, 210 Юго-Западный)
    private String windDirectionToString(double windDirectionDouble){

        if (windDirectionDouble >= 0 && windDirectionDouble < 22.5
                && windDirectionDouble >= 337.5 && windDirectionDouble <= 360){
            return resources.getString(R.string.north);
        }else if (windDirectionDouble >= 22.5 && windDirectionDouble < 67.5){
            return resources.getString(R.string.northeast);
        }else if (windDirectionDouble >= 67.5 && windDirectionDouble < 112.5){
            return resources.getString(R.string.east);
        }else if (windDirectionDouble >= 112.5 && windDirectionDouble < 157.5){
            return resources.getString(R.string.southeast);
        }else if (windDirectionDouble >= 157.5 && windDirectionDouble < 202.5){
            return resources.getString(R.string.south);
        }else if (windDirectionDouble >= 202.5 && windDirectionDouble < 247.5){
            return resources.getString(R.string.southwest);
        }else if (windDirectionDouble >= 247.5 && windDirectionDouble < 292.5){
            return resources.getString(R.string.west);
        }else if (windDirectionDouble >= 292.5 && windDirectionDouble < 337.5){
            return resources.getString(R.string.northwest);
        }
        return resources.getString(R.string.none_data);
    }

    //  Используя Picasso вставляем картинки

    private void loadImage(int index, ImageView imageView) {
        String url =  "http://alexleru.h1n.ru/pic_weather/" + getNameDrawable(index) + ".png";
        Picasso.get()
                .load(url)
                .into(imageView);
    }
    private String getNameDrawable(int index) {
        String imageDescription = weatherRequestModel.list[index].weather[0].main;

        switch (imageDescription){
            case "Clouds":
                return "Overcast";
            case "Clear":
                return "Sunny";
            case "Rain":
                return "HailHeavy";
            case "Snow":
                return "Snowflake";
            default:
                return "http://alexleru.h1n.ru/pic_weather/SnowAndSun.png";
        }
    }

}
