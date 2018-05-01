package com.example.kellihe_emil.traveldiary;

//Michael Augello, McKenna Buck, Emily Kelliher, Rachid Macer
//CS-480 Term Project
//May 1, 2018
//Review Activity

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Reviews extends TravelLog {

    private EditText urlText;
    private Button goButton;
    private WebView webView;
    private TextView textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // Get a handle to all user interface elements
        urlText = (EditText) findViewById(R.id.url);
        goButton = (Button) findViewById(R.id.navigate);
        webView = (WebView) findViewById(R.id.webview);
        textview = (TextView) findViewById(R.id.text2);

        //urlText.setText(TravelLog.url);
        webView.loadUrl(TravelLog.url);

        //intercept URL loading and load in widget
        webView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

        });

        // Set button to open browser
        goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                webView.loadUrl(urlText.getText().toString());
            }
        });

        urlText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    webView.loadUrl(urlText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }


    //the back key navigates back to the previous web page
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
