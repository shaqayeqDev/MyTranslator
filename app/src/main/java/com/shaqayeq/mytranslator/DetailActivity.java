package com.shaqayeq.mytranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView wordTv, translatedTv, pronTv;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bundle = getIntent().getExtras();

        init();
        fillItem(bundle.getString("text", ""), bundle.getString("translated", ""), bundle.getString("pron", ""));
    }

    private void fillItem(String text, String translated, String pron) {
        wordTv.setText(text);
        translatedTv.setText(translated);
        pronTv.setText(pron);
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.darkRed));
        }
        wordTv = findViewById(R.id.wordTv);
        translatedTv = findViewById(R.id.translatedTv);
        pronTv = findViewById(R.id.pronTv);
    }
}