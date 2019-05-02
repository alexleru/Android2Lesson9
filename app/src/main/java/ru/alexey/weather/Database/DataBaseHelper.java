package ru.alexey.weather.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int VERSION_DATABASE = 1;

    public DataBaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, VERSION_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WeatherTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //оставляю для будущих изменений в структруе БД
    }
}
