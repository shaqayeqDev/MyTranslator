package com.shaqayeq.mytranslator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        logo = findViewById(R.id.logo);
        text = findViewById(R.id.textView);

        logo.post(() -> {
            logo.setAlpha(0f);
            text.setAlpha(0f);
            logo.animate().setDuration(1000)
                    .alpha(1)
                    .withEndAction(() -> {
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(this,MainActivity.class));
                            finish();
                        },3000);
                    });

            text.animate().setDuration(1000)
                    .alpha(1);
        });

    }
}