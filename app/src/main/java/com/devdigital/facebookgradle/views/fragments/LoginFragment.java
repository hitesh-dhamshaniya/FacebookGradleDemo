package com.devdigital.facebookgradle.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devdigital.facebookgradle.R;
import com.devdigital.facebookgradle.core.CoreFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends CoreFragment implements View.OnClickListener {


    private View view;
    private LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;
    private TextView txtUsername;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI(view);
        return view;
    }

    @Override
    protected void initUI(View view) {
        txtUsername = (TextView) view.findViewById(R.id.txt_username);
        btnFacebookLogin = (LoginButton) view.findViewById(R.id.btnFbLogin);
        callbackManager = CallbackManager.Factory.create();

        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ON Success", "********* SUCCESS ****************");
                loginResult.getAccessToken();

                Toast.makeText(getActivity(), "Hello Success", Toast.LENGTH_SHORT).show();
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    profile.getFirstName();
                    txtUsername.setText(profile.getFirstName());
                } else {
                    txtUsername.setText("Error found");
                }


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("ON EROORR", "********* ERROR ****************");
                Toast.makeText(getActivity(), "Errorororororor", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View v) {

    }

    public GraphRequest.Callback onGraphResponse = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            Log.e("==> ", "===> " + response.toString());
        }
    };
}
