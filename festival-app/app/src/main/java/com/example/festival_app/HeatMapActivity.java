package com.example.festival_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HeatMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

     private GoogleMap mMap;

    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    SupportMapFragment mapFragment;

    private Marker mMark;

    Button profile, setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        profile = findViewById(R.id.button_profile);
        profile.setOnClickListener(this);
        setLocation = findViewById(R.id.button_set);
        setLocation.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new JsonTask().execute("http://10.0.2.2:8080/locationSeperate");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        float zoom = 16;
        LatLng center = new LatLng(55.618310, 12.082911);
        mMap.setOnMapClickListener(this);

        LatLng ne = new LatLng(55.610050, 12.060702);
        LatLng sw = new LatLng(55.624021, 12.096797);

        LatLngBounds bound = new LatLngBounds(ne,sw);

        mMap.setLatLngBoundsForCameraTarget(bound);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == profile.getId()){

            startActivity(new Intent(HeatMapActivity.this, ProfileActivity.class));

        } else if(view.getId() == setLocation.getId()){

            //TODO SAVE LOCATION TO DB

        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

        if(mMark == null){

            mMark = mMap.addMarker(new MarkerOptions().position(latLng));

        } else {

            mMark.remove();

            mMark = mMap.addMarker(new MarkerOptions().position(latLng));

        }

    }

    public class JsonTask extends AsyncTask<String,String, List<LatLng>>{

        @Override
        protected List<LatLng> doInBackground(String... strings) {
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

                List<LatLng> list = new ArrayList<LatLng>();

                JSONArray Coords = new JSONArray(final_Json);

                for (int i = 0; i < Coords.length(); i++) {
                    JSONObject object = Coords.getJSONObject(i);
                    double lat = object.getDouble("lat");
                    double lng = object.getDouble("lng");
                    list.add(new LatLng(lat, lng));
                }

                return list;


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

        @Override
        protected void onPostExecute(List<LatLng> s) {
            super.onPostExecute(s);

            mProvider = new HeatmapTileProvider.Builder().data(s).build();

            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        }
    }
}



