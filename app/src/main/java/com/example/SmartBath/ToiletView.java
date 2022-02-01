package com.example.SmartBath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SmartBath.model.DatabaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NetworkAsyncTaskToilet extends AsyncTask<Void, Void, Void> {

    String type;
    String name;
    String isOccupied;
    String isNightLightOn;
    String info;

    protected void setType(String t) {
        this.type = t;
    }
    protected void setName(String n) { this.name = n;}
    protected void setIsOccupied(String isOccupied) {
        this.isOccupied = isOccupied;
    }
    protected void setIsNightLightOn(String isNightLightOn) {
        this.isNightLightOn = isNightLightOn;
    }
    protected String getInfo() {return info;}

    @Override
    protected Void doInBackground(Void... params) {
        String type = this.type;
        String name = this.name;
        String isOccupied = this.isOccupied;
        String isNightLightOn = this.isNightLightOn;
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
                    + isOccupied;

            data += "&" + URLEncoder.encode("brightness", "UTF-8") + "="
                    + isNightLightOn;

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

public class ToiletView extends AppCompatActivity {

    private TextView toiletview_username;
    private EditText toiletview_name;
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

    public static boolean isValidOccupied(final String isOccupied) {
        if (isOccupied.equals("Yes") || isOccupied.equals("No")) return true;
        return false;
    }

    public static boolean isValidNightLight(final String isNightLightOn) {
        if (isNightLightOn.equals("Yes") || isNightLightOn.equals("No")) return true;
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
        toiletview_isOccupied = (EditText) findViewById(R.id.toiletview_isOccupied);
        toiletview_isNightLightOn = (EditText) findViewById(R.id.toiletview_isNightLightOn);
        toiletview_submit = (Button) findViewById(R.id.button);

//        String hello = "Hello, " + sp.getString("username","You are not logged in");
//        toiletview_username.setText(hello);
        String role = sp.getString("role","");


//        Cursor cursorLight = databaseHandler.searchUserInLights(sp.getString("username",""));
//        while (cursorLight.moveToNext()) {
//            lightview_name.setText(cursorLight.getString(1));
//            lightview_mode.setText(cursorLight.getString(2));
//            lightview_brightness.setText(cursorLight.getString(3));
//            toiletview_weight.setText(cursorLight.getString(4));
//            toiletview_isOccupied.setText(cursorLight.getString(5));
//            toiletview_isNightLightOn.setText(cursorLight.getString(6));
//        }

        toiletview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ToiletView.this, MainActivity.class);
                //String username = cursorLight.getString(7);
                String name = toiletview_name.getText().toString();
                String isOccupied = toiletview_isOccupied.getText().toString();
                String isNightLightOn = toiletview_isNightLightOn.getText().toString();

                boolean goodName = false;
                boolean goodOccupied = false;
                boolean goodNightLight = false;

                if ( !isValidName(name) ) {
                    toiletview_name.setError("The name must have at least 5 characters! The first one should be uppercase!");
                }
                else {
                    goodName = true;
                }

                if ( !isValidOccupied(isOccupied) ) {
                    toiletview_isOccupied.setError("Yes/No only");
                }
                else {
                    goodOccupied = true;
                }

                if ( !isValidNightLight(isNightLightOn) ) {
                    toiletview_isNightLightOn.setError("Yes/No only");
                }
                else {
                    goodNightLight = true;
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

                if( goodName && goodOccupied && goodNightLight) {
                    i.putExtra("name", name);
                    i.putExtra("isOccupied", isOccupied);
                    i.putExtra("isNightLightOn", isNightLightOn);

//                    if (role.equals("User")) {
//                        i.putExtra("condition", condition);
//                    }
//                    else if (role.equals("Admin")) {
//                        i.putExtra("specialization", specialization);
//                    }

                    try {
                        String result = sendPostToilet("toilet", name, isOccupied, isNightLightOn);
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

    String sendPostToilet (String type, String name, String isOccupied, String isNightLightOn) throws ExecutionException, InterruptedException {
        NetworkAsyncTaskToilet NAT = new NetworkAsyncTaskToilet();
        NAT.setType(type);
        NAT.setName(name);
        NAT.setIsOccupied(isOccupied);
        NAT.setIsNightLightOn(isNightLightOn);
        NAT.execute().get();
        String info = NAT.getInfo();
        return info;
    }
}