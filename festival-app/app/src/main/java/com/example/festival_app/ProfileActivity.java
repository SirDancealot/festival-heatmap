package com.example.festival_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;
    private FirebaseAuth mAuth;
    private UserInformation mUserInformation;

    String BASE_URL = "dist.saluton.dk";

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

            makePostDeleteUser(mUserInformation.getToken());

        } else if(view.getId()==logout.getId()){
           FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finishAffinity();


        }else if(view.getId()==back.getId()){

            startActivity(new Intent(ProfileActivity.this, HeatMapActivity.class));

        }
    }

    private void makePostDeleteUser(String token) {

        OkHttpClient client = new OkHttpClient();

        String json = "{\"token\":\""+token+"}\"";

        RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);

        HttpUrl uu = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(18512)
                .addPathSegments("deleteUser")
                .addQueryParameter("token", token)
                .build();

        Request req = new Request.Builder().url(uu).delete(body).build();

        client.newCall(req).enqueue(new Callback() {
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

