package com.example.SmartBath;

import junit.framework.TestCase;

public class LightViewTest extends TestCase {

    public void testIsValidName() {
        assertEquals(true, LightView.isValidName("Alex"));
        assertEquals(false, LightView.isValidName("TR8-R"));
    }

    public void testIsValidMode() {
        assertEquals(true, LightView.isValidMode("HSB"));
        assertEquals(false, LightView.isValidMode("Color"));
    }

    public void testIsValidColor() {
        assertEquals(true, LightView.isValidColor(255, 0, 213));
        assertEquals(false, LightView.isValidColor(-5, 10, 45));
    }

    public void testIsValidBrightness() {
        assertEquals(true, LightView.isValidBrightness(255));
        assertEquals(false, LightView.isValidBrightness(-15));
    }
}