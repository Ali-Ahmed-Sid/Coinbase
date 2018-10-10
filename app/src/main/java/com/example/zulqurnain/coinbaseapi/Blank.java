package com.example.zulqurnain.coinbaseapi;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.coinbase.android.sdk.OAuth;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Account;
import com.coinbase.api.exception.CoinbaseException;

import org.joda.money.Money;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Blank extends AppCompatActivity {

    private String token;
    private TextView tv_Amount;
    private TextView tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        String code = getIntent().getData().getQueryParameter("code");
        tv_Amount = findViewById(R.id.tv_Amount);
        tv_email = findViewById(R.id.tv_email);

        String url = "https://www.coinbase.com/oauth/token?grant_type=authorization_code&" +
                "code=" + code + "&client_id=37ccb7e87145ca56b06554b50a6f54b9f39d9c3cecc798038fd767d2e903f6fe" +
                "&client_secret=bdd4f8fa882bd2a2e054567779d3ba08645b4b98c5bc255ae00f3e0888935587&" +
                "redirect_uri=coinbase-android-example://coinbase-oauth";


        new RetrieveFeedTask().execute(url);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;


        protected void onPreExecute() {
        }

        protected String doInBackground(String... strings) {
            // Do some validation here

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {
                    String token = new JSONObject(response).getString("access_token");
                    new MyAsyn().execute(token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    class MyAsyn extends AsyncTask<String, Void, String> {
        String val = "";

        @Override
        protected String doInBackground(String... strings) {

            Coinbase cb = new CoinbaseBuilder()
                    .withAccessToken(strings[0])
                    .build();

            try {
                val = cb.getUser().getEmail();
                System.out.println(val);
                tv_Amount.setText(cb.getAccounts().getAccounts().get(0).getBalance().toString());

                tv_email.setText(val);

            } catch (Exception e) {
                tv_email.setText("Error "+e);
                e.printStackTrace();
            }

            return val;
        }

    }
}
