package com.example.zulqurnain.coinbaseapi;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.coinbase.android.sdk.OAuth;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Account;
import com.coinbase.api.exception.CoinbaseException;

import org.joda.money.Money;

import java.io.IOException;

public class Blank extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);




        Coinbase cb = new CoinbaseBuilder()
                .withAccessToken("7690cfa026636504598606f1e0124bbf1713f599368fb9235ba60c14f088bc84")
                .build();
//        try {
//       //   token =   cb.getTokens("37ccb7e87145ca56b06554b50a6f54b9f39d9c3cecc798038fd767d2e903f6fe","bdd4f8fa882bd2a2e054567779d3ba08645b4b98c5bc255ae00f3e0888935587","7690cfa026636504598606f1e0124bbf1713f599368fb9235ba60c14f088bc84","http://www.redcodetechnologies.com").toString();
//
//        } catch (CoinbaseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            String c = cb.getBalance().getAmount().toString();
            Toast.makeText(this,c,Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoinbaseException e) {
            e.printStackTrace();
        }
        System.out.println(token);


       // Toast.makeText(this,"asdasd",Toast.LENGTH_LONG).show();
    }
}
