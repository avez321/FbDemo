package com.avez.pc.fbintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private TextView name, birthdate, location, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        findViewByIds();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Constants.FB_REQUEST_PARMS);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                registerCallBack(object, response);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString(Constants.FIELDS,Constants.FB_FIELDS);
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                name.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                name.setText("Login attempt failed.");
            }
        });
    }

    private void registerCallBack(JSONObject object, GraphResponse response) {
        Log.d("LoginActivity", response.toString());

        // Application code
        String email = object.optString("email");
        String birthday = object.optString("birthday");
        String name = object.optString("name");
        String id=object.optString("id");
        String location= null;
        try {
            JSONObject jsonObject = object.getJSONObject("location");
            location=jsonObject.optString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainActivity.this.email.setText("Email:"+email);
        birthdate.setText("Birthday:"+birthday);
        MainActivity.this.name.setText("Name:"+name);
        profilePictureView.setProfileId(id);
        MainActivity.this.location.setText("Location:"+location);
    }

    private void findViewByIds() {
        profilePictureView = (ProfilePictureView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name);
        birthdate = (TextView) findViewById(R.id.birthdate);
        email = (TextView) findViewById(R.id.email);
        location = (TextView) findViewById(R.id.location);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

