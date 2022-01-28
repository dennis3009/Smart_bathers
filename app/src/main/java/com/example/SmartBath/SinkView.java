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

import com.example.SmartBath.model.DatabaseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinkView extends AppCompatActivity {

    private TextView sinkview_username;
    private EditText sinkview_flow;
    private EditText sinkview_temperature;
    private EditText sinkview_isSoap;
    private EditText sinkview_soap;
    private Button sinkview_submit;
    public SharedPreferences sp;

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

    public static boolean isValidIsSoap(final Boolean isSoap) {
        if(isSoap.equals("true") || isSoap.equals("false")) return true;
        return false;
    }

    public static boolean isValidTemperature(final Double temperature) {
        if(temperature < 49 || -10 < temperature) return true;
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

//        String hello = "Hello, " + sp.getString("username","You are not logged in");
//        sinkview_username.setText(hello);
        String role = sp.getString("role","");

        if (role.equals("User")) {
//            profileview_condition.setVisibility(View.VISIBLE);
//            profileview_specialization.setVisibility(View.INVISIBLE);
//            Cursor cursorPatient = databaseHandler.searchUserInPatients(sp.getString("username",""));
//            while (cursorPatient.moveToNext()) {
//                sinkview_username.setText(cursorPatient.getString(1));
//                profileview_surname.setText(cursorPatient.getString(2));
//                profileview_age.setText(cursorPatient.getString(3));
//                profileview_address.setText(cursorPatient.getString(4));
//                profileview_phoneNo.setText(cursorPatient.getString(5));
//                profileview_condition.setText(cursorPatient.getString(6));
//            }
        }
        else if (role.equals("Admin")) {
//            profileview_condition.setVisibility(View.INVISIBLE);
//            profileview_specialization.setVisibility(View.VISIBLE);
//            Cursor cursorDoctor = databaseHandler.searchUserInDoctors(sp.getString("username",""));
//            while (cursorDoctor.moveToNext()) {
//                sinkview_username.setText(cursorDoctor.getString(1));
//                profileview_surname.setText(cursorDoctor.getString(2));
//                profileview_age.setText(cursorDoctor.getString(3));
//                profileview_address.setText(cursorDoctor.getString(4));
//                profileview_phoneNo.setText(cursorDoctor.getString(5));
//                profileview_specialization.setText(cursorDoctor.getString(6));
//            }
        }


        sinkview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinkView.this, MainActivity.class);
                String username = sinkview_username.getText().toString();
                String flow = sinkview_flow.getText().toString();
                Double temperature = Double.parseDouble(sinkview_temperature.getText().toString());
                Boolean isSoap = Boolean.parseBoolean(sinkview_isSoap.getText().toString());
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
}
