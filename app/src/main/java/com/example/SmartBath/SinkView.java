package com.example.SmartBath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SmartBath.model.DatabaseHandler;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinkView extends AppCompatActivity {

    private TextView sinkview_username;
    private EditText sinkview_flow;
    private EditText sinkview_temperature;
    private EditText sinkview_isSoap;
    private EditText sinkview_soap;
    private Button sinkview_submit;
    private Button sinkview_publish;
    private Button sinkview_connect;
    private Button sinkview_disconnect;
    private Button sinkview_subscribe;
    private Button sinkview_unsubscribe;
    private TextView status;
    private TextView response;

    public SharedPreferences sp;

    // tcp://broker.hivemq.com:1883
    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String USERNAME = "tester";
    static String PASSWORD = "Abc12345";
    String topicStr = "SmartBath/Sink/out/";
    String topicResp = "SmartBath/Sink/in/";
    String messageToSend = "Default";

    boolean connected = false;

    MqttAndroidClient client;

    public static boolean isValidSoap(final String soap) {
        Pattern pattern;
        Matcher matcher;

        int len = soap.length();

        final String SOAP_PATTERN = "^^([A-Z][-,a-z]+)";
        pattern = Pattern.compile(SOAP_PATTERN);
        matcher = pattern.matcher(soap);

        return (len >= 1 && matcher.matches());
    }

    public static boolean isValidFlow(final String flow) {
        if(flow.equals("Off") || flow.equals("Low")||
                flow.equals("Medium")|| flow.equals("High")) return true;
        return false;
    }

    public static boolean isValidIsSoap(final String isSoap) {
        if(isSoap.equals("Yes") || isSoap.equals("No")) return true;
        return false;
    }

    public static boolean isValidTemperature(final Double temperature) {
        if(temperature < 49 && -10 < temperature) return true;
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_sink_view);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        sinkview_username = (TextView) findViewById(R.id.sinkview_username);
        sinkview_flow = (EditText) findViewById(R.id.sinkview_flow);
        sinkview_temperature = (EditText) findViewById(R.id.sinkview_temperature);
        sinkview_isSoap = (EditText) findViewById(R.id.sinkview_isSoap);
        sinkview_soap = (EditText) findViewById(R.id.sinkview_soap);
        sinkview_submit = (Button) findViewById(R.id.sinkview_submit);
        sinkview_publish = (Button) findViewById(R.id.sinkview_publish);
        sinkview_connect = (Button) findViewById(R.id.sinkview_connect);
        sinkview_disconnect = (Button) findViewById(R.id.sinkview_disconnect);
        sinkview_subscribe = (Button) findViewById(R.id.sinkview_subscribe);
        sinkview_unsubscribe = (Button) findViewById(R.id.sinkview_unsubscribe);

//        String hello = "Hello, " + sp.getString("username","You are not logged in");
//        sinkview_username.setText(hello);
        String role = sp.getString("role","");

        if (role.equals("User")) {

        }
        else if (role.equals("Admin")) {

        }

        sinkview_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinkView.this, MainActivity.class);
                String username = sinkview_username.getText().toString();
                String flow = sinkview_flow.getText().toString();
                Double temperature = Double.parseDouble(sinkview_temperature.getText().toString());
                String isSoap = sinkview_isSoap.getText().toString();
                String soap = sinkview_soap.getText().toString();

                boolean goodFlow = false;
                boolean goodTemperature = false;
                boolean goodIsSoap = false;
                boolean goodSoap = false;

                if (!isValidSoap(soap)) {
                    sinkview_soap.setError("The soap name must have at least 1 character! The first one should be uppercase!");
                } else {
                    goodSoap = true;
                }

                if (!isValidTemperature(temperature)) {
                    sinkview_temperature.setError("The temperature must be between -10 and 49!");
                } else {
                    goodTemperature = true;
                }

                if (!isValidFlow(flow)) {
                    sinkview_flow.setError("The flow should be Off or Low or Medium or High!");
                } else {
                    goodFlow = true;
                }

                if (!isValidIsSoap(isSoap)) {
                    sinkview_isSoap.setError("The Soap container must be true or false!");
                } else {
                    goodIsSoap = true;
                }

                if (role.equals("User")) {
//                    if ( !isValidCondition(condition) ) {
//                        profileview_condition.setError("The condition must have at least 5 characters!");
//                    }
//                    else {
//                        goodCondition = true;
//                    }
                }
                else if (role.equals("Admin")) {
//                    if ( !isValidSpecialization(specialization) ) {
//                        profileview_specialization.setError("The specialization must have at least 5 characters!");
//                    }
//                    else {
//                        goodSpecialization = true;
//                    }
                }

                if( goodFlow && goodSoap && goodIsSoap && goodTemperature) {
                    messageToSend = "flow=" + flow + ";";
                    messageToSend += "soap=" + soap + ";";
                    messageToSend += "isSoap=" + isSoap + ";";
                    messageToSend += "temperature=" + temperature + ";";

                    pub(v);
                }
            }
        });

        sinkview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinkView.this, MainActivity.class);
                String username = sinkview_username.getText().toString();
                String flow = sinkview_flow.getText().toString();
                Double temperature = Double.parseDouble(sinkview_temperature.getText().toString());
                String isSoap = sinkview_isSoap.getText().toString();
                String soap = sinkview_soap.getText().toString();

                boolean goodFlow = false;
                boolean goodTemperature = false;
                boolean goodIsSoap = false;
                boolean goodSoap = false;

                if (!isValidSoap(soap)) {
                    sinkview_soap.setError("The soap name must have at least 1 character! The first one should be uppercase!");
                } else {
                    goodSoap = true;
                }

                if (!isValidTemperature(temperature)) {
                    sinkview_temperature.setError("The temperature must be between -10 and 49!");
                } else {
                    goodTemperature = true;
                }

                if (!isValidFlow(flow)) {
                    sinkview_flow.setError("The flow should be Off or Low or Medium or High!");
                } else {
                    goodFlow = true;
                }

                if (!isValidIsSoap(isSoap)) {
                    sinkview_isSoap.setError("The Soap container must be true or false!");
                } else {
                    goodIsSoap = true;
                }

                if (role.equals("User")) {
//                    if ( !isValidCondition(condition) ) {
//                        profileview_condition.setError("The condition must have at least 5 characters!");
//                    }
//                    else {
//                        goodCondition = true;
//                    }
                }
                else if (role.equals("Admin")) {
//                    if ( !isValidSpecialization(specialization) ) {
//                        profileview_specialization.setError("The specialization must have at least 5 characters!");
//                    }
//                    else {
//                        goodSpecialization = true;
//                    }
                }

                if( goodFlow && goodSoap && goodIsSoap && goodTemperature) {
                    i.putExtra("flow", flow);
                    i.putExtra("goodSoap", goodSoap);
                    i.putExtra("goodIsSoap", goodIsSoap);
                    i.putExtra("goodTemperature", goodTemperature);

//                    if (role.equals("User")) {
//                        i.putExtra("condition", condition);
//                    }
//                    else if (role.equals("Admin")) {
//                        i.putExtra("specialization", specialization);
//                    }

                    i.putExtra("method", "lightview");
                    startActivity(i);
                }
            }
        });

        sinkview_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn(v);
            }
        });

        sinkview_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconn(v);
            }
        });

        sinkview_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSubscription();
            }
        });

        sinkview_unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnsubscription();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(sp.getBoolean("logged", false)){
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(true);
            MenuItem itemReg = menu.findItem(R.id.register);
            itemReg.setVisible(false);
            MenuItem items = menu.findItem(R.id.login);
            items.setVisible(false);
        }
        else {
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(true);
            MenuItem items = menu.findItem(R.id.logout);
            items.setVisible(false);
            MenuItem itemReg = menu.findItem(R.id.register);
            itemReg.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.login){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        if (id == R.id.register) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
        if (id == R.id.logout) {
            Intent intent = new Intent(this, MainActivity.class);
            sp.edit().putBoolean("logged", false).apply();
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void pub(View v) {
        if(connected) {
            String topic = topicStr;
            String message = messageToSend;
            status = (TextView) findViewById(R.id.sinkview_status);
            try {
                client.publish(topic, message.getBytes(), 0, false);
                status.setText(new String("Sent"));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void conn(View v) {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST,
                clientId);

        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName(USERNAME);
//        options.setPassword(PASSWORD.toCharArray());

        response = (TextView) findViewById(R.id.sinkview_response);
        status = (TextView) findViewById(R.id.sinkview_status);

        try {
            IMqttToken token = client.connect();
//            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(SinkView.this, "connected!! :)", Toast.LENGTH_LONG).show();
                    System.out.println("connected!! :)");
                    status.setText(new String("Connected"));
                    connected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(SinkView.this, "not connected.. :(", Toast.LENGTH_LONG).show();
                    System.out.println("not connected.. :(");
                    status.setText(new String("Not connected"));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Mesaj primit!! :)");
                response.setText(new String(message.getPayload()));
                status.setText(new String("Recieved"));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void disconn(View v) {

        status = (TextView) findViewById(R.id.sinkview_status);

        try {
            IMqttToken token = client.disconnect();
//            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(SinkView.this, "disconnected!! :)", Toast.LENGTH_LONG).show();
                    System.out.println("disconnected!! :)");
                    status.setText(new String("Disconnected"));
                    connected = false;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(SinkView.this, "not disconnected.. :(", Toast.LENGTH_LONG).show();
                    System.out.println("not disconnected.. :(");
                    status.setText(new String("Not disconnected"));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription() {

        status = (TextView) findViewById(R.id.sinkview_status);

        try {
            client.subscribe(topicResp, 0);
            status.setText(new String("Subscribed"));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setUnsubscription() {

        status = (TextView) findViewById(R.id.sinkview_status);

        try {
            client.unsubscribe(topicResp);
            status.setText(new String("Unsubscribed"));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
