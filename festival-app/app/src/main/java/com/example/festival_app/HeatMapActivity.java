package com.example.festival_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HeatMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new JsonTask().execute("http://10.0.2.2:8080/locationSeperate");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng Maharashtra = new LatLng(19.169257, 73.341601);
        map.addMarker(new MarkerOptions().position(Maharashtra).title("Maharashtra"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Maharashtra));
    }

    public class JsonTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputS = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputS));

                StringBuffer buf = new StringBuffer();

                String lin = "";

                while((lin=reader.readLine())!=null){
                    buf.append(lin);
                }

                String final_Json = buf.toString();

                JSONObject parentObject = new JSONObject();
                JSONArray parentArray = parentObject.getJSONArray("");
                JSONObject finalObject = parentArray.getJSONObject(0);

                String Coords = finalObject.getString("Coords");

                return Coords+"\n";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }

                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }

            return null;
        }
    }
}



