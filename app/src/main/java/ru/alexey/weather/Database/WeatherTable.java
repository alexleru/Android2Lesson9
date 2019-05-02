package ru.alexey.weather.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.alexey.weather.ModelOfData.CityModelOfData;
import ru.alexey.weather.ModelOfData.WeatherModelOfData;

public class WeatherTable {
    private final static String TABLE_NAME = "Weather";
    private final static String ROW_ID = "_id";
    private final static String ROW_CITY = "city";
    private final static String ROW_DATE = "date";
    private final static String ROW_DATE_TXT = "date_txt";
    private final static String ROW_TEMPERATURE = "temperature";
    private final static String ROW_HUMIDITY = "humidity";
    private final static String ROW_WIND = "wind";
    private final static String ROW_WIND_DIRECTION = "wind_direction";
    private final static String ROW_PRESSURE = "pressure";
    private final static String ROW_PICTURE = "picture";

    public static void createTable(SQLiteDatabase database){
        database.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_CITY + " TEXT NOT NULL,"
                + ROW_DATE + " TEXT NOT NULL,"
                + ROW_DATE_TXT + " TEXT NOT NULL,"
                + ROW_TEMPERATURE + " DOUBLE NOT NULL,"
                + ROW_HUMIDITY + " INTEGER NOT NULL,"
                + ROW_WIND + " DOUBLE NOT NULL,"
                + ROW_WIND_DIRECTION + " INTEGER NOT NULL,"
                + ROW_PRESSURE + " DOUBLE NOT NULL,"
                + ROW_PICTURE + " TEXT NOT NULL,"
                + " UNIQUE(" + ROW_CITY + "," +  ROW_DATE + ")" +
                ");");
        Log.d("---DB---", "CREATE-DB");
    }

    public static void insertElement(CityModelOfData cityModelOfData, SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put(ROW_CITY, cityModelOfData.city);
        for (WeatherModelOfData weatherModelOfData : cityModelOfData.weatherModelOfData){
            values.put(ROW_DATE, weatherModelOfData.date);
            values.put(ROW_DATE_TXT, weatherModelOfData.date_txt);
            values.put(ROW_TEMPERATURE, weatherModelOfData.temperature);
            values.put(ROW_HUMIDITY, weatherModelOfData.humidity);
            values.put(ROW_WIND, weatherModelOfData.wind);
            values.put(ROW_WIND_DIRECTION, weatherModelOfData.wind_of_direction);
            values.put(ROW_PRESSURE, weatherModelOfData.pressure);
            values.put(ROW_PICTURE, weatherModelOfData.picture);

            database.insertWithOnConflict(TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public static void updateElement(CityModelOfData cityModelOfData, SQLiteDatabase database){
        ContentValues values = new ContentValues();
        for (WeatherModelOfData weatherModelOfData : cityModelOfData.weatherModelOfData){
            values.put(ROW_TEMPERATURE, weatherModelOfData.temperature);
            values.put(ROW_HUMIDITY, weatherModelOfData.humidity);
            values.put(ROW_WIND, weatherModelOfData.wind);
            values.put(ROW_WIND_DIRECTION, weatherModelOfData.wind_of_direction);
            values.put(ROW_PRESSURE, weatherModelOfData.pressure);
            values.put(ROW_PICTURE, weatherModelOfData.picture);

            database.update(TABLE_NAME, values,
                    ROW_CITY + "=" + "\"" + cityModelOfData.city + "\""
                            + " AND " + ROW_DATE + "=" + weatherModelOfData.date, null );
        }
    }

    public static CityModelOfData selectElementWhereCity(String city, SQLiteDatabase database){
        CityModelOfData cityModelOfData = null ;
        WeatherModelOfData[] weatherModelOfDatas;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME +
                "WHERE" + ROW_CITY  + "=" + city, null);
        if (cursor != null && cursor.moveToFirst()){
            weatherModelOfDatas = new WeatherModelOfData[cursor.getCount()];
            int i = 0;
            do {
                WeatherModelOfData weatherModelOfData = new WeatherModelOfData();
                weatherModelOfData.date = cursor.getLong(cursor.getColumnIndex(ROW_DATE));
                weatherModelOfData.date_txt = cursor.getString(cursor.getColumnIndex(ROW_DATE_TXT));
                weatherModelOfData.temperature = cursor.getDouble(cursor.getColumnIndex(ROW_TEMPERATURE));
                weatherModelOfData.humidity = cursor.getInt(cursor.getColumnIndex(ROW_HUMIDITY));
                weatherModelOfData.wind = cursor.getDouble(cursor.getColumnIndex(ROW_WIND));
                weatherModelOfData.wind_of_direction = cursor.getInt(cursor.getColumnIndex(ROW_WIND_DIRECTION));
                weatherModelOfData.pressure = cursor.getDouble(cursor.getColumnIndex(ROW_PRESSURE));
                weatherModelOfData.picture = cursor.getString(cursor.getColumnIndex(ROW_PICTURE));
                weatherModelOfDatas[i++] = weatherModelOfData;
            }while (cursor.moveToNext());
            cityModelOfData = new CityModelOfData(city, weatherModelOfDatas);
        }
        try { cursor.close(); } catch (Exception ignored) {}
        return cityModelOfData;
    }
}
