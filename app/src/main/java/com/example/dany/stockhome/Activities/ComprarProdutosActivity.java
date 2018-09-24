package com.example.dany.stockhome.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.dany.stockhome.R;

public class ComprarProdutosActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_produtos);

        myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl("https://www.pingodoce.pt/produtos/");
    }
}
