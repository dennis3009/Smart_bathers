package com.example.SmartBath;

import android.content.Context;
import android.view.View;

import junit.framework.TestCase;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTTest extends TestCase {

    static String MQTTHOST = "tcp://broker.hivemq.com:1883";

    String clientId = MqttClient.generateClientId();
    Context context;
    View v;
    MqttAndroidClient client = new MqttAndroidClient(context, MQTTHOST, clientId);
    MqttConnectOptions options = new MqttConnectOptions();

    String topic = "test/test/";
    String message = "testMessage";

//    public void testPub() {
//        assertEquals(true, pub(v));
//    }

    public void testConn() {
        conn(v);
        assertEquals(true, client.isConnected());
    }

    public void testDisonn() {
        conn(v);
        disconn(v);
        assertEquals(true, !client.isConnected());
    }

    public void testSub() {

    }

    public void testUnsub() {

    }

//    public boolean pub(View v) {
//        try {
//            conn(v);
//            client.publish(topic, message.getBytes(), 0, false);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

    public void conn(View v) {
        try {
            IMqttToken token = client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconn(View v) {
        try {
            IMqttToken token = client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
//
//    private void setSubscription() {
//
//        status = (TextView) findViewById(R.id.lightview_status);
//
//        try {
//            client.subscribe(topicResp, 0);
//            status.setText(new String("Subscribed"));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setUnsubscription() {
//
//        status = (TextView) findViewById(R.id.lightview_status);
//
//        try {
//            client.unsubscribe(topicResp);
//            status.setText(new String("Unsubscribed"));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

}
