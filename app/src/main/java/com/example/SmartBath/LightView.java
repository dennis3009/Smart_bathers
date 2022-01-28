package com.example.SmartBath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NetworkAsyncTaskLight extends AsyncTask<Void, Void, Void> {

    String type;
    String name;
    String mode;
    int brightness;
    int color1;
    int color2;
    int color3;
    String info;

    protected void setType(String t) {
        this.type = t;
    }
    protected void setName(String n) { this.name = n;}
    protected void setMode(String mode) {
        this.mode = mode;
    }
    protected void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    protected void setColor1(int color1) {
        this.color1 = color1;
    }
    protected void setColor2(int color2) {
        this.color2 = color2;
    }
    protected void setColor3(int color3) {
        this.color3 = color3;
    }
    protected String getInfo() {return info;}

    @Override
    protected Void doInBackground(Void... params) {
        String type = this.type;
        String name = this.name;
        String mode = this.mode;
        int brightness = this.brightness;
        int color1 = this.color1;
        int color2 = this.color2;
        int color3 = this.color3;
        String data = "";

        // Create data variable for sent values to server
//        System.out.println("hash");
//        System.out.println(hash);
        // URLEncoder.encode(name, "UTF-8");
        try {
            data += URLEncoder.encode("type", "UTF-8") + "="
                    + type;

            data += "&" + URLEncoder.encode("name", "UTF-8") + "="
                    + name;

            data += "&" + URLEncoder.encode("mode", "UTF-8") + "="
                    + mode;

            data += "&" + URLEncoder.encode("brightness", "UTF-8") + "="
                    + brightness;

            data += "&" + URLEncoder.encode("color1", "UTF-8") + "="
                    + color1;

            data += "&" + URLEncoder.encode("color2", "UTF-8") + "="
                    + color2;

            data += "&" + URLEncoder.encode("color3", "UTF-8") + "="
                    + color3;

//            System.out.println("data!!!");
//            System.out.println(data);
            String text = "";
            BufferedReader reader = null;
        }
        catch(IOException ioe){
            System.out.println("whatever makes you happy");
        }
        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://10.0.2.2:8000/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            int datalen = data.length();
            conn.setRequestProperty("Content-length", Integer.toString(datalen));
            conn.setRequestMethod("POST");
            System.out.println("pana aici totul e in regula");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );

            wr.flush();

            // Get the server response

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            String text = sb.toString();
            System.out.println("text:");
            System.out.println(text);
            this.info = text;
            System.out.println("infoNAT: " + this.info);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class LightView extends AppCompatActivity {

    private TextView lightview_username;
    private EditText lightview_name;
    private EditText lightview_mode;
    private EditText lightview_color1;
    private EditText lightview_color2;
    private EditText lightview_color3;
    private EditText lightview_brightness;
    private Button lightview_submit;
    private Button lightview_publish;
    private Button lightview_connect;
    private Button lightview_disconnect;
    private TextView response;

    public SharedPreferences sp;

    // tcp://broker.hivemq.com:1883
    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String USERNAME = "tester";
    static String PASSWORD = "Abc12345";
    String topicStr = "SmartBath/Light/out/";
    String topicResp = "SmartBath/Light/in/";
    String messageToSend = "Default";

    boolean connected = false;

    MqttAndroidClient client;


    public static boolean isValidName(final String name) {
        Pattern pattern;
        Matcher matcher;

        int len = name.length();

        final String NAME_PATTERN = "^^([A-Z][-,a-z]+)";
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);

        return (len >= 3 && matcher.matches());
    }

    public static boolean isValidMode(final String mode) {
        if(mode.equals("RGB") || mode.equals("HSB")) return true;
        return false;
    }

    public static boolean isValidColor(final int color1, final int color2, final int color3) {
        if(color1 < 0 || 255 < color1) return false;
        if(color2 < 0 || 255 < color2) return false;
        if(color3 < 0 || 255 < color3) return false;
        return true;
    }

    public static boolean isValidBrightness(final int brightness) {
        if(brightness < 0 || 255 < brightness) return false;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_light_view);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        lightview_username = (TextView) findViewById(R.id.lightview_username);
        lightview_name = (EditText) findViewById(R.id.lightview_name);
        lightview_mode = (EditText) findViewById(R.id.lightview_mode);
        lightview_color1 = (EditText) findViewById(R.id.lightview_color1);
        lightview_color2 = (EditText) findViewById(R.id.lightview_color2);
        lightview_color3 = (EditText) findViewById(R.id.lightview_color3);
        lightview_brightness = (EditText) findViewById(R.id.lightview_brightness);
        lightview_submit = (Button) findViewById(R.id.lightview_submit);
        lightview_publish = (Button) findViewById(R.id.lightview_publish);
        lightview_connect = (Button) findViewById(R.id.lightview_connect);
        lightview_disconnect = (Button) findViewById(R.id.lightview_disconnect);



        String hello = "Hello, " + sp.getString("username","You are not logged in");
        lightview_username.setText(hello);
        String role = sp.getString("role","");

        // old host: "tcp://broker.hivemq.com:1883"

        Cursor cursorLight = databaseHandler.searchUserInLights(sp.getString("username",""));
        while (cursorLight.moveToNext()) {
            lightview_name.setText(cursorLight.getString(1));
            lightview_mode.setText(cursorLight.getString(2));
            lightview_brightness.setText(cursorLight.getString(3));
            lightview_color1.setText(cursorLight.getString(4));
            lightview_color2.setText(cursorLight.getString(5));
            lightview_color3.setText(cursorLight.getString(6));
        }

        lightview_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username = cursorLight.getString(7);
                String name = lightview_name.getText().toString();
                int brightness = Integer.parseInt(lightview_brightness.getText().toString());
                int color1 = Integer.parseInt(lightview_color1.getText().toString());
                int color2 = Integer.parseInt(lightview_color2.getText().toString());
                int color3 = Integer.parseInt(lightview_color3.getText().toString());
                String mode = lightview_mode.getText().toString();

                boolean goodName = false;
                boolean goodColor = false;
                boolean goodBrightness = false;
                boolean goodMode = false;

                if ( !isValidName(name) ) {
                    lightview_name.setError("The name must have at least 5 characters! The first one should be uppercase!");
                }
                else {
                    goodName = true;
                }

                if ( !isValidBrightness(brightness) ) {
                    lightview_brightness.setError("The brightness must be between 0 and 255!");
                }
                else {
                    goodBrightness = true;
                }

                if ( !isValidMode(mode) ) {
                    lightview_mode.setError("The mode should be RGB or HSB!");
                }
                else {
                    goodMode = true;
                }

                if ( !isValidColor(color1, color2, color3) ) {
                    lightview_color1.setError("The color must be between 0 and 255!");
                    lightview_color2.setError("The color must be between 0 and 255!");
                    lightview_color3.setError("The color must be between 0 and 255!");
                }
                else {
                    goodColor = true;
                }

                if (role.equals("User")) {
//
                }
                else if (role.equals("Admin")) {
//
                }

                if( goodName && goodBrightness && goodColor && goodMode) {
                    messageToSend = "name=" + name + ";";
                    messageToSend += "mode=" + mode + ";";
                    messageToSend += "brightness=" + brightness + ";";
                    messageToSend += "color1=" + color1 + ";";
                    messageToSend += "color2=" + color2 + ";";
                    messageToSend += "color3=" + color3 + ";";

                    pub(v);
                }
            }
        });


        lightview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LightView.this, MainActivity.class);
                //String username = cursorLight.getString(7);
                String name = lightview_name.getText().toString();
                int brightness = Integer.parseInt(lightview_brightness.getText().toString());
                int color1 = Integer.parseInt(lightview_color1.getText().toString());
                int color2 = Integer.parseInt(lightview_color2.getText().toString());
                int color3 = Integer.parseInt(lightview_color3.getText().toString());
                String mode = lightview_mode.getText().toString();

                boolean goodName = false;
                boolean goodColor = false;
                boolean goodBrightness = false;
                boolean goodMode = false;

                if ( !isValidName(name) ) {
                    lightview_name.setError("The name must have at least 5 characters! The first one should be uppercase!");
                }
                else {
                    goodName = true;
                }

                if ( !isValidBrightness(brightness) ) {
                    lightview_brightness.setError("The brightness must be between 0 and 255!");
                }
                else {
                    goodBrightness = true;
                }

                if ( !isValidMode(mode) ) {
                    lightview_mode.setError("The mode should be RGB or HSB!");
                }
                else {
                    goodMode = true;
                }

                if ( !isValidColor(color1, color2, color3) ) {
                    lightview_color1.setError("The color must be between 0 and 255!");
                    lightview_color2.setError("The color must be between 0 and 255!");
                    lightview_color3.setError("The color must be between 0 and 255!");
                }
                else {
                    goodColor = true;
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

                if( goodName && goodBrightness && goodColor && goodMode) {
                    i.putExtra("name", name);
                    i.putExtra("mode", mode);
                    i.putExtra("brightness", brightness);
                    i.putExtra("color1", color1);
                    i.putExtra("color2", color2);
                    i.putExtra("color3", color3);

//                    if (role.equals("User")) {
//                        i.putExtra("condition", condition);
//                    }
//                    else if (role.equals("Admin")) {
//                        i.putExtra("specialization", specialization);
//                    }

                    try {
                        String result = sendPostLight("light", name, mode, brightness, color1, color2, color3);
                        System.out.println("Result from POST: ");
                        System.out.println(result);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("method", "lightview");
                    startActivity(i);
                }
            }
        });

        lightview_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn(v);
            }
        });

        lightview_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconn(v);
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

    String sendPostLight(String type, String name, String mode, int brightness, int color1, int color2, int color3) throws ExecutionException, InterruptedException {
        NetworkAsyncTaskLight NAT = new NetworkAsyncTaskLight();
        NAT.setType(type);
        NAT.setName(name);
        NAT.setMode(mode);
        NAT.setBrightness(brightness);
        NAT.setColor1(color1);
        NAT.setColor2(color2);
        NAT.setColor3(color3);
        NAT.execute().get();
        String info = NAT.getInfo();
        return info;
    }

    public void pub(View v) {
        if(connected) {
            String topic = topicStr;
            String message = messageToSend;
            try {
                client.publish(topic, message.getBytes(), 0, false);
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

        response = (TextView) findViewById(R.id.lightview_response);

        try {
            IMqttToken token = client.connect();
//            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(LightView.this, "connected!! :)", Toast.LENGTH_LONG).show();
                    System.out.println("connected!! :)");
                    setSubscription();
                    connected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(LightView.this, "not connected.. :(", Toast.LENGTH_LONG).show();
                    System.out.println("not connected.. :(");
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
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void disconn(View v) {
        try {
            IMqttToken token = client.disconnect();
//            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(LightView.this, "disconnected!! :)", Toast.LENGTH_LONG).show();
                    System.out.println("disconnected!! :)");
                    connected = false;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(LightView.this, "not disconnected.. :(", Toast.LENGTH_LONG).show();
                    System.out.println("not disconnected.. :(");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription() {
        try {
            client.subscribe(topicResp, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}