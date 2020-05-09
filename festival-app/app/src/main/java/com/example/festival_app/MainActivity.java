package com.example.festival_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = this.findViewById(R.id.ButtonLogin1);
        login.setOnClickListener(this);

        username = findViewById(R.id.EditTextUsername);
        username = findViewById(R.id.EditTextPassword);

    }


    @Override
    public void onClick(View v) {

        if(login.getId()==v.getId()){

            //TODO Startloading();

            //TODO LOGIN

            if(true){

                startActivity(new Intent(MainActivity.this, HomepageActivity.class));

            }

        }

    }
}
