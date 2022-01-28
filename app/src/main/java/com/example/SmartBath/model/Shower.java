package com.example.SmartBath.model;

public class Shower {
    private int waterTemperature;
    private int waterPressure;
    private int showerDuration;
    private int showerGelQuantity;
    private int showerShampooQuantity;
    private String name;

    public Shower(int waterTemperature, int waterPressure, int showerDuration, int showerGelQuantity, int showerShampooQuantity, String name) {
        this.waterTemperature = waterTemperature;
        this.waterPressure = waterPressure;
        this.showerDuration = showerDuration;
        this.showerGelQuantity = showerGelQuantity;
        this.showerShampooQuantity = showerShampooQuantity;
        this.name = name;
    }

    public void setWaterTemperature(int waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public int getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterPressure(int waterPressure) {
        this.waterPressure = waterPressure;
    }

    public int getWaterPressure() {
        return waterPressure;
    }

    public void setShowerDuration(int showerDuration) {
        this.showerDuration = showerDuration;
    }

    public int getShowerDuration() {
        return showerDuration;
    }

    public void setShowerGelQuantity(int showerGelQuantity) {
        this.showerGelQuantity = showerGelQuantity;
    }

    public int getShowerGelQuantity() {
        return showerGelQuantity;
    }

    public void setShowerShampooQuantity(int showerShampooQuantity) {
        this.showerShampooQuantity = showerShampooQuantity;
    }

    public int getShowerShampooQuantity() {
        return showerShampooQuantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Water Temperature: " + waterTemperature + '\n' + "Water Presure: " + waterPressure + '\n' + "Shower Duration: " + showerDuration + '\n'
                + "Shower Gel Quantity: " + showerGelQuantity + '\n' + "Shower Shampoo Quantity: " + showerShampooQuantity;
    }
}
