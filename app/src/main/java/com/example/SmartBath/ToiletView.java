package com.example.SmartBath;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.SmartBath.model.DatabaseHandler;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToiletView extends AppCompatActivity {

    private TextView toiletview_username;
    private EditText toiletview_name;
    private EditText toiletview_weight;
    private EditText toiletview_isOccupied;
    private EditText toiletview_isNightLightOn;
    private Button toiletview_submit;
    public SharedPreferences sp;

    public static boolean isValidName(final String name) {
        Pattern pattern;
        Matcher matcher;

        int len = name.length();

        final String NAME_PATTERN = "^^([A-Z][-,a-z]+)";
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);

        return (len >= 3 && matcher.matches());
    }

    public static boolean isValidWeight(final int weight) {
        if (weight > 0) {
            return true;
        }
        return false;
    }

    public static boolean isValidOccupied(final int isOccupied) {
        if (isOccupied == 0 || isOccupied == 1) return true;
        return false;
    }

    public static boolean isValidNightLight(final int isNightLightOn) {
        if (isNightLightOn == 0 || isNightLightOn == 1) return true;
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_toilet_view);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        toiletview_username = (TextView) findViewById(R.id.lightview_username);
       toiletview_name = (EditText) findViewById(R.id.toiletview_name);
        lightview_mode = (EditText) findViewById(R.id.lightview_mode);
        toiletview_weight = (EditText) findViewById(R.id.lightview_color1);
        toiletview_isOccupied = (EditText) findViewById(R.id.lightview_color2);
        toiletview_isNightLightOn = (EditText) findViewById(R.id.lightview_color3);
        lightview_brightness = (EditText) findViewById(R.id.lightview_brightness);
        lightview_submit = (Button) findViewById(R.id.lightview_submit);

        String hello = "Hello, " + sp.getString("username","You are not logged in");
        toiletview_username.setText(hello);
        String role = sp.getString("role","");


        Cursor cursorLight = databaseHandler.searchUserInLights(sp.getString("username",""));
        while (cursorLight.moveToNext()) {
            lightview_name.setText(cursorLight.getString(1));
            lightview_mode.setText(cursorLight.getString(2));
            lightview_brightness.setText(cursorLight.getString(3));
            toiletview_weight.setText(cursorLight.getString(4));
            toiletview_isOccupied.setText(cursorLight.getString(5));
            toiletview_isNightLightOn.setText(cursorLight.getString(6));
        }

        lightview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LightView.this, MainActivity.class);
                //String username = cursorLight.getString(7);
                String name = lightview_name.getText().toString();
                int brightness = Integer.parseInt(lightview_brightness.getText().toString());
                int color1 = Integer.parseInt(toiletview_weight.getText().toString());
                int color2 = Integer.parseInt(toiletview_isOccupied.getText().toString());
                int color3 = Integer.parseInt(toiletview_isNightLightOn.getText().toString());
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
                    toiletview_weight.setError("The color must be between 0 and 255!");
                    toiletview_isOccupied.setError("The color must be between 0 and 255!");
                    toiletview_isNightLightOn.setError("The color must be between 0 and 255!");
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
                        String result = sendPost3("light", name, mode, brightness, color1, color2, color3);
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

    String sendPost3 (String type, String name, String mode, int brightness, int color1, int color2, int color3) throws ExecutionException, InterruptedException {
        NetworkAsyncTask NAT = new NetworkAsyncTask();
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
}