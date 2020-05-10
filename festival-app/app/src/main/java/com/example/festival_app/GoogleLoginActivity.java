package com.example.festival_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleLoginActivity extends AppCompatActivity {

    private UserInformation mUserInformation;
    private FirebaseAuth mAuth;
    private Task<GoogleSignInAccount> loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        // Singleton class to store user information across different activities
        mUserInformation = UserInformation.getInstance();

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Google Login-popup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_id)) // client_id from obtained from Google developer console
                                                                      // used get the client-secret which is used for hashing the access tokens
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loginTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = loginTask.getResult(ApiException.class);

                Log.d("SUCCESS", "firebaseAuthWithGoogle:" + account.getId());

                // method used for validating the user and extracting the access token
                firebaseAuthWithGoogle(account.getIdToken());
                startActivity(new Intent(getApplicationContext(), HeatMapActivity.class));

            } catch (ApiException e) {
                // Google Sign In failed
                Log.w("ERROR", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("SUCCESS", "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()){
                                        // Save the users access token
                                        mUserInformation.setToken(task.getResult().getToken());
                                        System.out.println(task.getResult().getToken());
                                    }else {
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

}
