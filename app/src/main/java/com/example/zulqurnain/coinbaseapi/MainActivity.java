package com.example.zulqurnain.coinbaseapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.coinbase.android.sdk.OAuth;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.OAuthTokensResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;
import com.example.zulqurnain.coinbaseapi.R;

import java.util.concurrent.Semaphore;

import roboguice.activity.RoboActivity;
import roboguice.util.RoboAsyncTask;

public class MainActivity extends RoboActivity {

    private static final String CLIENT_ID = "37ccb7e87145ca56b06554b50a6f54b9f39d9c3cecc798038fd767d2e903f6fe";
    private static final String CLIENT_SECRET = "bdd4f8fa882bd2a2e054567779d3ba08645b4b98c5bc255ae00f3e0888935587";
    private static final String REDIRECT_URI = "coinbase-android-example://coinbase-oauth";
    private String token ="";
    private TextView mTextView;

    public class DisplayEmailTask extends RoboAsyncTask<String> {
        private OAuthTokensResponse mTokens;

        public DisplayEmailTask(OAuthTokensResponse tokens) {
            super(MainActivity.this);
            mTokens = tokens;


        }

        public String call() throws Exception {
           Coinbase  cb = new CoinbaseBuilder().withAccessToken(mTokens.getAccessToken()).build();
            return cb.getUser().getEmail();
        }

        @Override
        public void onException(Exception ex) {
            mTextView.setText("There was an error fetching the user's email address: " + ex.getMessage());
        }

        @Override
        public void onSuccess(String email) {
            mTextView.setText("Success! The user's email address is: " + email);

        }
    }

    public class CompleteAuthorizationTask extends RoboAsyncTask<OAuthTokensResponse> {
        private Intent mIntent;

        public CompleteAuthorizationTask(Intent intent) {
            super(MainActivity.this);
            mIntent = intent;
        }

        @Override
        public OAuthTokensResponse call() throws Exception {
            return OAuth.completeAuthorization(MainActivity.this, CLIENT_ID, CLIENT_SECRET, mIntent.getData());
        }

        @Override
        public void onSuccess(OAuthTokensResponse tokens) {
            token= tokens.toString();
            new DisplayEmailTask(tokens).execute();
        }

        @Override
        public void onException(Exception ex) {
            mTextView.setText("There was an error fetching access tokens using the auth code: " + ex.getMessage());
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW")) {
            new CompleteAuthorizationTask(intent).execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.the_text_view);

        try {
            OAuth.beginAuthorization(this, CLIENT_ID, "user", REDIRECT_URI, null);
      //      OAuth.completeAuthorization(this,CLIENT_ID,CLIENT_SECRET,Uri.parse(REDIRECT_URI));
            token = OAuth.getLoginCSRFToken(this);

        } catch (Exception ex) {
            mTextView.setText("There was an error redirecting to Coinbase: " + ex.getMessage());
        }


    }

}
