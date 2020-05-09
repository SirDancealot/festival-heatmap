package com.example.festival_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;

    String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        delete = findViewById(R.id.button_delete);
        delete.setOnClickListener(this);

        logout = findViewById(R.id.button_logout);
        logout.setOnClickListener(this);

        back = findViewById(R.id.button_back);
        back.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if(view.getId()==delete.getId()){

            new JsonTask().execute("http://10.0.2.2:8080/deleteUser");

            //TODO instert email of user to be deleted

        } else if(view.getId()==logout.getId()){

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            //TODO clear userclass

        }else if(view.getId()==back.getId()){

            startActivity(new Intent(ProfileActivity.this, HeatMapActivity.class));

        }

    }

    public class JsonTask extends AsyncTask<String,String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;


            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }

            return null;

        }
    }
}

