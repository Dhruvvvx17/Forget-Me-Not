package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MEEEE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String uid = prefs.getString("uid", "");

        Log.d("MEEEEE", uid);

        if (uid.equals("")) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else {
            callPatientMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = user.getUid();
                SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("uid", uid);
                editor.apply();

                callPatientMenu();

            } else {
                Toast.makeText(this, "Couldn't sign you in. Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the interestsMap canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void callPatientMenu() {
        Intent in = new Intent(this, PatientMenu.class);
        startActivity(in);
    }


    private void reLaunchActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void reLaunchActivity(View view) {
        reLaunchActivity();
    }
}
