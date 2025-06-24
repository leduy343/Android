package com.example.tlunavigator;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewDocumentActivity extends AppCompatActivity {

    private WebView webView;
    private TextView txtname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_document);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtname = findViewById(R.id.txtname);
        webView = findViewById(R.id.webView);
        String link = getIntent().getStringExtra("link");
        String name = getIntent().getStringExtra("name");
        txtname.setText(name);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        String videoId = "";
        if (link.contains("watch?v=")) {
            videoId = link.substring(link.indexOf("v=") + 2);
            int ampersandIndex = videoId.indexOf("&");
            if (ampersandIndex != -1) {
                videoId = videoId.substring(0, ampersandIndex);
            }
        }

        String newlink = "https://www.youtube.com/embed/"+videoId;
        webView.loadUrl(newlink);


    }
}