package com.example.festival_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;
    private FirebaseAuth mAuth;
    private UserInformation mUserInformation;

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

        mAuth = FirebaseAuth.getInstance();
        mUserInformation = UserInformation.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            mUserInformation.setUserEmail(currentUser.getEmail());

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
    public void onClick(View view) {

        if(view.getId()==delete.getId()){

            makePost();

            //TODO instert email of user to be deleted

        } else if(view.getId()==logout.getId()){
           FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));


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
                    .add("token", mUserInformation.getToken())
                    .build();

            Request request = new Request.Builder()
                    .url(BASE_URL+"/deleteUser")
                    .post(fromBody)
                    .build();

            Call call = client.newCall(request);


            return null;
        }
    }

    private void makePost() {

        OkHttpClient client = new OkHttpClient();

        String json = "{\"token\":\""+mUserInformation.getToken()+"\",\"latitude\":\"10\",\"longitude\":10}\"";

        RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);



        RequestBody formBody = new FormBody.Builder()
                .add("token", mUserInformation.getToken())
                .add("latitude", "20")
                .add("longitude", "11")
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/saveUser")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("gayanders");
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

