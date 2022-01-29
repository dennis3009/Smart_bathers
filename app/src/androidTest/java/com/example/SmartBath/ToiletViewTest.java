package com.example.SmartBath;

import junit.framework.TestCase;

public class ToiletViewTest extends TestCase {

    public void testIsValidName() {
        assertEquals(true, ToiletView.isValidName("Andrei"));
        assertEquals(false, ToiletView.isValidName("0000"));
    }

    public void testIsValidOccupied() {
        assertEquals(true, ToiletView.isValidOccupied("Yes"));
        assertEquals(false, ToiletView.isValidOccupied("Da"));
    }

    public void testIsValidNightLight() {
        assertEquals(true, ToiletView.isValidNightLight("No"));
        assertEquals(false, ToiletView.isValidNightLight("Da"));
    }
}