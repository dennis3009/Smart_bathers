package com.example.SmartBath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SmartBath.model.DatabaseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowerView extends AppCompatActivity{
    private TextView textView;
    private Button button;
    private EditText waterTemperature;
    private EditText waterPressure;
    private EditText showerDuration;
    private EditText showerGelQuantity;
    private EditText showerShampooQuantity;
    private EditText name;
    public SharedPreferences sp;

    public static boolean isValidWaterTemperature(final int waterTemperature) {
        if (waterTemperature < 0 || waterTemperature > 50)
            return false;

        return true;
    }

    public static boolean isValidWaterPressure(final int waterPressure) {
        if (waterPressure < 1 || waterPressure > 100)
            return false;

        return true;
    }

    public static boolean isValidShowerDuration(final int showerDuration) {
        if (showerDuration < 1 || showerDuration > 100)
            return false;

        return true;
    }

    public static boolean isValidShowerGelQuantity(final int showerGelQuantity) {
        if (showerGelQuantity < 10 || showerGelQuantity > 500)
            return false;

        return true;
    }

    public static boolean isValidShowerShampooQuantity(final int showerShampooQuantity) {
        if (showerShampooQuantity < 10 || showerShampooQuantity > 500)
            return false;

        return true;
    }

    public static boolean isValidName(final String name) {
        Pattern pattern;
        Matcher matcher;

        int len = name.length();

        final String namePattern = "^^([A-Z][-,a-z]+)";
        pattern = Pattern.compile(namePattern);
        matcher = pattern.matcher(name);

        return (len >= 3 && matcher.matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DatabaseHandler databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_shower_view);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        waterTemperature = (EditText) findViewById(R.id.waterTemperature);
        waterPressure = (EditText) findViewById(R.id.waterPressure);
        showerDuration = (EditText) findViewById(R.id.showerDuration);
        showerGelQuantity = (EditText) findViewById(R.id.showerGelQuantity);
        showerShampooQuantity = (EditText) findViewById(R.id.showerShampooQuantity);
        name = (EditText) findViewById(R.id.name);

        String hello = "Hello, " + sp.getString("username", "You are now logged in");
        textView.setText(hello);
        String role = sp.getString("role", "");

        if (role.equals("User")) {
//            profileview_condition.setVisibility(View.VISIBLE);
//            profileview_specialization.setVisibility(View.INVISIBLE);
//            Cursor cursorPatient = databaseHandler.searchUserInPatients(sp.getString("username",""));
//            while (cursorPatient.moveToNext()) {
//                lightview_username.setText(cursorPatient.getString(1));
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
//                lightview_username.setText(cursorDoctor.getString(1));
//                profileview_surname.setText(cursorDoctor.getString(2));
//                profileview_age.setText(cursorDoctor.getString(3));
//                profileview_address.setText(cursorDoctor.getString(4));
//                profileview_phoneNo.setText(cursorDoctor.getString(5));
//                profileview_specialization.setText(cursorDoctor.getString(6));
//            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowerView.this, MainActivity.class);
                String textViewCond = textView.getText().toString();
                int waterTemperatureCond = Integer.parseInt(waterTemperature.getText().toString());
                int waterPressureCond = Integer.parseInt(waterPressure.getText().toString());
                int showerDurationCond = Integer.parseInt(showerDuration.getText().toString());
                int showerGelQuantityCond = Integer.parseInt(showerGelQuantity.getText().toString());
                int showerShampooQuantityCond = Integer.parseInt(showerShampooQuantity.getText().toString());
                String nameCond = name.getText().toString();

                boolean goodWaterTemperature = false;
                boolean goodWaterPressure = false;
                boolean goodShowerDuration = false;
                boolean goodShowerGelQuantity = false;
                boolean goodShowerShampooQuantity = false;
                boolean goodName = false;

                if (!isValidWaterTemperature(waterTemperatureCond)) {
                    waterTemperature.setError("The water temperature must be between 0 Celsius degrees and 50 Celsius degrees!");
                }
                else {
                    goodWaterTemperature = true;
                }

                if (!isValidWaterPressure(waterPressureCond)) {
                    waterPressure.setError("The water pressure must be between 1% and 100%!");
                }
                else {
                    goodWaterPressure = true;
                }

                if (!isValidShowerDuration(showerDurationCond)) {
                    showerDuration.setError("The shower duration must be between 1 minute and 100 minutes!");
                }
                else {
                    goodShowerDuration = true;
                }

                if (!isValidShowerGelQuantity(showerGelQuantityCond)) {
                    showerGelQuantity.setError("The quantity of shower gel must be between 10 ml and 500 ml!");
                }
                else{
                        goodShowerGelQuantity = true;
                }

                if (!isValidShowerShampooQuantity(showerShampooQuantityCond)) {
                    showerShampooQuantity.setError("The quantity of shower shampoo must be between 10 ml and 500 ml!");
                }
                else {
                    goodShowerShampooQuantity = true;
                }

                if (!isValidName(nameCond)) {
                    name.setError("The name must have at least 5 characters! The first one should be uppercase!");
                }
                else{
                        goodName = true;
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

                if (goodWaterTemperature && goodWaterPressure && goodShowerDuration && goodShowerGelQuantity && goodShowerShampooQuantity && goodName) {
                    i.putExtra("waterTemperature", waterTemperatureCond);
                    i.putExtra("waterPressure", waterPressureCond);
                    i.putExtra("showerDuration", showerDurationCond);
                    i.putExtra("showerGelQuantity", showerGelQuantityCond);
                    i.putExtra("showerShampooQuantity", showerShampooQuantityCond);
                    i.putExtra("name", nameCond);

//                    if (role.equals("User")) {
//                        i.putExtra("condition", condition);
//                    }
//                    else if (role.equals("Admin")) {
//                        i.putExtra("specialization", specialization);
//                    }

                    i.putExtra("method", "showerview");
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        if(sp.getBoolean("logged", false)){
//            MenuItem item = menu.findItem(R.id.logout);
//            item.setVisible(true);
//            MenuItem itemReg = menu.findItem(R.id.register);
//            itemReg.setVisible(false);
//            MenuItem items = menu.findItem(R.id.login);
//            items.setVisible(false);
//        }
//        else {
//            MenuItem item = menu.findItem(R.id.login);
//            item.setVisible(true);
//            MenuItem items = menu.findItem(R.id.logout);
//            items.setVisible(false);
//            MenuItem itemReg = menu.findItem(R.id.register);
//            itemReg.setVisible(true);
//        }
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

    //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if (id == R.id.login){
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//        }
//        if (id == R.id.register) {
//            Intent intent = new Intent(this, Register.class);
//            startActivity(intent);
//        }
//        if (id == R.id.logout) {
//            Intent intent = new Intent(this, MainActivity.class);
//            sp.edit().putBoolean("logged", false).apply();
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

