package com.example.SmartBath;

import junit.framework.TestCase;

public class SinkViewTest extends TestCase {

    public void testIsValidSoap() {
        assertEquals(true, SinkView.isValidSoap("Dove"));
        assertEquals(false, SinkView.isValidSoap("0x3523432"));
    }

    public void testIsValidFlow() {
        assertEquals(true, SinkView.isValidFlow("Medium"));
        assertEquals(false, SinkView.isValidFlow("Strong"));
    }

    public void testIsValidIsSoap() {
        assertEquals(true, SinkView.isValidIsSoap("Yes"));
        assertEquals(false, SinkView.isValidIsSoap("True"));
    }

    public void testIsValidTemperature() {
        assertEquals(true, SinkView.isValidTemperature(30.0));
        assertEquals(false, SinkView.isValidTemperature(-20.0));
    }
}