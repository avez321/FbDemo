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
    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private ProfilePictureView mProfilePictureView;
    private TextView mName,mBirthdate,mLocation,mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        findViewByIds();
        mLoginButton = (LoginButton)findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Constants.FB_REQUEST_PARMS);
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
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
                                mEmail.setText("Email:"+email);
                                mBirthdate.setText("Birthday:"+birthday);
                                mName.setText("Name:"+name);
                                mProfilePictureView.setProfileId(id);
                                mLocation.setText("Location:"+location);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString(Constants.FIELDS,Constants.FB_FIELDS);
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                mName.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                mName.setText("Login attempt failed.");
            }
        });
    }

    private void findViewByIds() {
        mProfilePictureView= (ProfilePictureView) findViewById(R.id.profile_pic);
        mName= (TextView) findViewById(R.id.name);
        mBirthdate= (TextView) findViewById(R.id.birthdate);
        mEmail= (TextView) findViewById(R.id.email);
        mLocation= (TextView) findViewById(R.id.location);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

