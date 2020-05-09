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

public class HeatMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new JsonTask().execute("localhost");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng Maharashtra = new LatLng(19.169257, 73.341601);
        map.addMarker(new MarkerOptions().position(Maharashtra).title("Maharashtra"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Maharashtra));
    }

    public class JsonTask extends AsyncTask<String,String, List<LatLng>>{

        @Override
        protected List<LatLng> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("localhost");

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

            mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        }
    }

}



