package com.example.covidapp;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LinearLayout signInButton;

    FirebaseAuth auth;
    FirebaseUser user;
    Dialog google_dialog;
    GoogleSignInClient agooglesigninclient;
    DatabaseReference userReference;
    private static final int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        agooglesigninclient = GoogleSignIn.getClient(this,gso);

        signInButton = findViewById(R.id.imageView);
        signInButton.setOnClickListener(view -> {
            Intent SignInIntent = agooglesigninclient.getSignInIntent();
            startActivityForResult(SignInIntent,RC_SIGN_IN);
            google_dialog = new Dialog(MainActivity.this);
            google_dialog.setCancelable(true);
            google_dialog.setContentView(R.layout.loading);
            google_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            google_dialog.show();
        });
    }


   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if(requestCode==RC_SIGN_IN){

           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           try {

               GoogleSignInAccount account = task.getResult(ApiException.class);
               assert account != null;
               firebaseAuthWithGoogle(account);


           } catch (ApiException e) {
               Log.e("errorrrrr",e+"");
               Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
           }
       }
   }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if(task.isSuccessful()){

                        google_dialog.dismiss();
                        user = auth.getCurrentUser();
                        userReference = FirebaseDatabase.getInstance().getReference().child("users");
                        userReference.child(user.getUid()).child("alert").setValue("Safe");

                        DatabaseReference tokenRef = userReference.child(user.getUid()).child("Token");

                        assert user != null;


                        goToInstructions();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void gotoProfile(){

        Intent intent = new Intent(MainActivity.this, dashboardDataAnalysis.class);
        startActivity(intent);
        finish();
    }

    private void goToInstructions()
    {
        Intent intent = new Intent(MainActivity.this, instructionsMACaddress.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user!=null){
            gotoProfile();
        }
    }
}