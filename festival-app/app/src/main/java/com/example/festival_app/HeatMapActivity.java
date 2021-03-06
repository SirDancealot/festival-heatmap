package com.example.festival_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

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
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HeatMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    SupportMapFragment mapFragment;
    private FirebaseAuth mAuth;
    private UserInformation mUserInformation;

    private Marker mMark;

    Button profile, setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mAuth = FirebaseAuth.getInstance();
        mUserInformation = UserInformation.getInstance();

        profile = findViewById(R.id.button_profile);
        profile.setOnClickListener(this);
        setLocation = findViewById(R.id.button_set);
        setLocation.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        new JsonTask().execute("http://dist.saluton.dk:18512/locationSeperate");
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){

           currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()){
                        mUserInformation.setToken(task.getResult().getToken());
                    }else
                        task.getException().printStackTrace();
                }
            });
        }
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

            if(mMark != null) {
                makePostSaveUser(mUserInformation.getToken(), "" + mMark.getPosition().latitude, "" + mMark.getPosition().longitude);
            }

            CountDownTimer time = new CountDownTimer(500,500) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    new JsonTask().execute("http://dist.saluton.dk:18512/locationSeperate");
                }
            }.start();


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

            mMap.clear();

            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void makePostSaveUser(String token, String lat, String lng) {

        OkHttpClient client = new OkHttpClient();

        String json = "{\"token\":\""+token+"\",\"latitude\":\""+lat+"\",\"longitude\":"+lng+"}\"";

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url("http://dist.saluton.dk:18512/saveUser")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Something went wrong");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}



