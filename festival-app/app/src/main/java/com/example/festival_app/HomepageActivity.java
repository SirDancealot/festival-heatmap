package com.example.festival_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView SeeHeatMap, RegisterOnHeatMap;
    TextView tvSee, tvregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SeeHeatMap = findViewById(R.id.imageView1);
        SeeHeatMap.setImageResource(R.drawable.unnamed);
        SeeHeatMap.setOnClickListener(this);

        RegisterOnHeatMap = findViewById(R.id.imageView2);
        RegisterOnHeatMap.setImageResource(R.drawable.unnamed);
        RegisterOnHeatMap.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        if(v.getId()==SeeHeatMap.getId()){

            startActivity(new Intent(HomepageActivity.this, HeatMapActivity.class));

        } else if (v.getId()==RegisterOnHeatMap.getId()){



        }
    }
}
