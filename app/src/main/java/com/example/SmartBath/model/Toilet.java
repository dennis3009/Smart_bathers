package com.example.SmartBath.model;

public class Toilet {
    private int seatTemp;
    private int weight;
    private int isOccupied;
    private int isNightLightOn;
    private String name;

    public Toilet(int seatTemp, int isOccupied, int isNightLightOn) {
        this.seatTemp = seatTemp;
        this.isOccupied = isOccupied;
        this.isNightLightOn = isNightLightOn;
    }

    public int getSeatTemp(){
        return seatTemp;
    }

    public float getWeight(){
        return weight;
    }

    public int isOccupied(){
        return isOccupied;
    }

    public int isNightLightOn(){
        return isNightLightOn;
    }


    public void setSeatTemp(int temp){
        this.seatTemp = temp;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void switchNightLight(){
        this.isNightLightOn = 1 - this.isNightLightOn;
    }

    public void setNightLightOn(int isOn){
        this.isNightLightOn = isOn;
    }

    public void setIsOccupied(int isOccupied){
        this.isOccupied = isOccupied;
    }

    public void flush(){

    }
}
