package com.example.bodybeyond.viewmodel;

public class Diet {
    public String DietDescription;
    public int DietImageItem;
    public String DietName;

    public Diet(String dietDescription, int dietImageItem, String dietName) {
        DietDescription = dietDescription;
        DietImageItem = dietImageItem;
        DietName = dietName;
    }

    public String getDietDescription() {
        return DietDescription;
    }

    public void setDietDescription(String dietDescription) {
        DietDescription = dietDescription;
    }

    public int getDietImageItem() {
        return DietImageItem;
    }

    public void setDietImageItem(int dietImageItem) {
        DietImageItem = dietImageItem;
    }

    public String getDietName() {
        return DietName;
    }

    public void setDietName(String dietName) {
        DietName = dietName;
    }
}
