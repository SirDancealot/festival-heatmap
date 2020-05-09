package com.example.festival_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;

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

            //TODO delete location

        } else if(view.getId()==logout.getId()){

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            //TODO clear userclass

        }else if(view.getId()==back.getId()){

            startActivity(new Intent(ProfileActivity.this, HeatMapActivity.class));

        }
    }
}

