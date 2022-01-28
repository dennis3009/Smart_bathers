package com.example.SmartBath.model;

public class Sink
{
    //Flux apă
    //Temperatură C
    //Adăugare săpun
    //Selectare tip săpun
    //Verificarea existentei săpunului selectat

    private String Flow; //Off, Low, Medium, High
    private Double Temperature;
    private Boolean IsSoap;
    private String Soap; //Type of Soap: None, Honey, Rose

    public Sink()
    {
        this.Flow="Off";
        this.Temperature=0.0;
        this.IsSoap=false;
        this.Soap="None";
    }
    public Sink(String Flow, Double Temperature, Boolean IsSoap, String Soap)
    {
        this.Flow = Flow;
        this.Temperature = Temperature;
        this.IsSoap = IsSoap;
        this.Soap = Soap;
    }

    public String getFlow() {
        return Flow;
    }

    public void setId(String Flow) {
        this.Flow = Flow;
    }

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double Temperature) {
        this.Temperature = Temperature;
    }

    public Boolean getIsSoap() {
        return IsSoap;
    }

    public void setIsSoap(Boolean IsSoap) {
        this.IsSoap = IsSoap;
    }

    public String getSoap() {
        return Soap;
    }

    public void setSoap(String Soap) {
        this.Soap = Soap;
    }

}
