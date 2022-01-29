package com.example.SmartBath;

import junit.framework.TestCase;

public class ShowerViewTest extends TestCase {

    public void testIsValidWaterTemperature() {
        assertEquals(true, ShowerView.isValidWaterTemperature(30));
        assertEquals(false, ShowerView.isValidWaterTemperature(9001));
    }

    public void testIsValidWaterPressure() {
        assertEquals(true, ShowerView.isValidWaterPressure(15));
        assertEquals(false, ShowerView.isValidWaterPressure(-10));
    }

    public void testIsValidShowerDuration() {
        assertEquals(true, ShowerView.isValidShowerDuration(15));
        assertEquals(false, ShowerView.isValidShowerDuration(-10));
    }

    public void testIsValidShowerGelQuantity() {
        assertEquals(true, ShowerView.isValidShowerGelQuantity(15));
        assertEquals(false, ShowerView.isValidShowerGelQuantity(-10));
    }

    public void testIsValidShowerShampooQuantity() {
        assertEquals(true, ShowerView.isValidShowerShampooQuantity(15));
        assertEquals(false, ShowerView.isValidShowerShampooQuantity(-10));
    }

    public void testIsValidName() {
        assertEquals(true, ShowerView.isValidName("Nivea"));
        assertEquals(false, ShowerView.isValidName("20"));
    }
}