package ru.alexey.weather;

public class SingletonForPreferences {
    private static SingletonForPreferences instance;
    private boolean[] addData = new boolean[3];
    private SingletonForPreferences(){}

    public static SingletonForPreferences getInstance(){
        if(instance == null){
            instance = new SingletonForPreferences();
        }
        return instance;
    }

    public boolean[] getAddData() {
        return addData;
    }

    public void setAddData(int index, boolean value) {
        this.addData[index] = value;
    }
}
