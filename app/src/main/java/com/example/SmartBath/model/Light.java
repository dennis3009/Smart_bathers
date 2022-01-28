package com.example.SmartBath.model;

public class Light {
    private int id = 0;
    private int color[] = {0, 0 , 0};  // 0 - 255
    private String name = "Default";
    private int brightness = 0;        // 0 - 255
    private String mode = "RGB";        // RGB or HSB

    public Light() {
        this.id = -1;
        for(byte i = 0; i < 3; i++) {
            this.color[i] = 0;
        }
        this.name = "Default";
        this.brightness = 0;
        this.mode = "RGB";
    }

    public Light(int id, byte color[], String name, int brightness, String mode) {
        this.id = id;
        for(byte i = 0; i < 3; i++) {
            this.color[i] = color[i];
        }
        this.name = name;
        this.brightness = brightness;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
