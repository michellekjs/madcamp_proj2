package com.example.camp_proj1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

        private CallbackManager callbackManager;
        LoginButton loginButton;
        public static String UserID;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            UserID = "AA";
            setContentView(R.layout.loginactivity);
            callbackManager = CallbackManager.Factory.create();
            loginButton =  findViewById(R.id.login_button);
            loginButton.setReadPermissions("email");
            //Log.v("hhhhhhh",UserID);
            Button custom_login = findViewById(R.id.custom_login);
            custom_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginButton.performClick();
                }
            });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    AccessToken accessToken = loginResult.getAccessToken();
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                    try {
                                        UserID = object.getString("id");
                                        Log.v("Loginid", UserID);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("UserID",UserID);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id");
                    request.setParameters(parameters);
                    Log.i("TAGGGGGGGGGG", parameters.toString());
                    request.executeAsync();
                }


                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);

        }


}

