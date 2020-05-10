package com.example.festival_app;

import android.content.Intent;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, back, delete;
    private FirebaseAuth mAuth;
    private UserInformation mUserInformation;

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

            //TODO delete location

        } else if(view.getId()==logout.getId()){

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            //TODO clear userclass

        }else if(view.getId()==back.getId()){

            startActivity(new Intent(ProfileActivity.this, HeatMapActivity.class));

        }
    }
}

