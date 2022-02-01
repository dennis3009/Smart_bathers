package com.example.SmartBath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import com.example.SmartBath.model.DatabaseHandler;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private Button mylight_button;
    private Button myshower_button;
    private Button mysink_button;
    private Button mytoilet_button;

    public DatabaseHandler databaseHandler;
    public SharedPreferences sp;
    public SharedPreferences sp_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NavigationView navigationView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        sp_settings = getSharedPreferences("settings", MODE_PRIVATE);

        databaseHandler = new DatabaseHandler(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String method = extras.getString("method", "");
            if (method.equals("Register")) {
                Intent i = new Intent(MainActivity.this, LightView.class);
                String username = extras.getString("username");
                String password = extras.getString("password");
                String role = extras.getString("role");
                databaseHandler.insertUser(username, password, role);
                startActivity(i);
            }
            if (method.equals("lightview")) {
                String username = sp.getString("username", "");
                String name = extras.getString("name");
                String mode = extras.getString("mode");
                int brightness = extras.getInt("brightness");
                int color1 = extras.getInt("color1");
                int color2 = extras.getInt("color2");
                int color3 = extras.getInt("color3");

                Cursor cursorPatient = databaseHandler.searchUserInLights(username);
                boolean found = false;
                while(cursorPatient.moveToNext()) {
                found = true;
                }
                if (found) {
                    databaseHandler.editLight(name, mode, brightness, color1, color2, color3, username);
                }
                else {
                    databaseHandler.insertLight(name, mode, brightness, color1, color2, color3, username);
                }
            }
            if (method.equals("showerview")) {

            }
        }



        mylight_button = (Button) findViewById(R.id.lightview);
        myshower_button = (Button) findViewById(R.id.showerview);
        mysink_button = (Button) findViewById(R.id.sinkview);
        mytoilet_button = (Button) findViewById(R.id.toiletview);


        if (sp.getBoolean("logged", false) == false) {
            mylight_button.setVisibility(View.INVISIBLE);
            myshower_button.setVisibility(View.INVISIBLE);
            mysink_button.setVisibility(View.INVISIBLE);
            mytoilet_button.setVisibility(View.INVISIBLE);
        } else {
            mylight_button.setVisibility(View.VISIBLE);
            mylight_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LightView.class);
                    startActivity(i);
                }
            });

            myshower_button.setVisibility(View.VISIBLE);
            myshower_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ShowerView.class);
                    startActivity(i);
                }
            });

            mysink_button.setVisibility(View.VISIBLE);
            mysink_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SinkView.class);
                    startActivity(i);
                }
            });

            mytoilet_button.setVisibility(View.VISIBLE);
            mytoilet_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ToiletView.class);
                    startActivity(i);
                }
            });

            String role = sp.getString("role", "");

            if (role.equals("Guest")) {

            } else if (role.equals("Admin")) {

            }
        }

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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed () {
        if (sp.getBoolean("logged", false) && sp.getString("role","").equals("Patient")) {
            String username = sp.getString("username", "user");
            Cursor idUsername = databaseHandler.getPatientIdByUsername(username);

            int id = -1;
            while(idUsername.moveToNext()){
                id = idUsername.getInt(0);
            }
            if (id != -1) {
                LocalDate localDate = java.time.LocalDate.now();
                Cursor cursor = databaseHandler.checkAppointment(id, localDate);
                if (cursor.getCount() != 0 && sp_settings.getBoolean("send_notifications", false) == true) {
                    startService(new Intent(this, NotificationService.class));
                }
            }
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        //finish();
    }

}