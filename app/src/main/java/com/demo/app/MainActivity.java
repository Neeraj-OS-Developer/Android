package com.demo.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private double brainWeight = 0.1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme set karna
        setTheme(R.style.Theme_DemoApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. FULLSCREEN & HIDE NAVIGATION (Immersive Mode)
        hideSystemUI();

        statusText = findViewById(R.id.statusText);
        Button runBtn = findViewById(R.id.runBtn);

        runBtn.setOnClickListener(v -> {
            // AI Logic: Simple Adjustment
            brainWeight += 0.05;
            statusText.setText("LOGIC UPDATED\nBRAIN FREQUENCY: " + String.format("%.2f", brainWeight));
        });
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Niche ke button gayab
                | View.SYSTEM_UI_FLAG_FULLSCREEN      // Upar ki patti gayab
        );
    }

    // Jab user screen touch kare to UI wapas hide ho jaye
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
