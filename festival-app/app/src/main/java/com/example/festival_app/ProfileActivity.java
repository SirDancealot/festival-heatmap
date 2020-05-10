package com.example.festival_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;

    UserInformation user;

    String BASE_URL = "http://10.0.2.2:8080";

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

        user = UserInformation.getInstance();

    }


    @Override
    public void onClick(View view) {

        if(view.getId()==delete.getId()){

            new JsonTask().execute();

            //TODO instert email of user to be deleted

        } else if(view.getId()==logout.getId()){

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            //TODO clear userclass

        }else if(view.getId()==back.getId()){

            startActivity(new Intent(ProfileActivity.this, HeatMapActivity.class));

        }
    }

    public class JsonTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;

            OkHttpClient client = new OkHttpClient();

            RequestBody fromBody = new FormBody.Builder()
                    .add("token",user.getToken())
                    .build();

            Request request = new Request.Builder()
                    .url(BASE_URL+"/deleteUser")
                    .post(fromBody)
                    .build();

            Call call = client.newCall(request);


            return null;
        }
    }

}

