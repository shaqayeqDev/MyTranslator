package com.shaqayeq.mytranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaqayeq.mytranslator.model.Root;
import com.shaqayeq.mytranslator.utils.Const;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity.class";
    private TextView firstLanguageIv, secondLanguageIv;
    private ImageView swapIv, searchIv;
    private EditText searchEt;
    private ApiService service = RetrofitClient.getClient().create(ApiService.class);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        swapLanguage();
        searchClicked();
    }

    private void searchClicked() {
        searchIv.setOnClickListener(view -> {
            search(firstLanguageIv.getText().toString().equals("FARSI"), searchEt.getText().toString());
        });
    }

    private void swapLanguage() {
        swapIv.setOnClickListener(view -> {
            String first = firstLanguageIv.getText().toString();
            String second = secondLanguageIv.getText().toString();
            String temp = "";

            temp = first;
            first = second;
            second = temp;

            firstLanguageIv.setText(first);
            secondLanguageIv.setText(second);

            if (firstLanguageIv.getText().toString().equals("FARSI"))
                searchEt.setHint("enter the FARSI word you want to translate");
            else
                searchEt.setHint("enter the ENGLISH word you want to translate");

        });
    }

    private void init() {
        firstLanguageIv = findViewById(R.id.firstLanguageTv);
        secondLanguageIv = findViewById(R.id.secondLanguageTv);
        swapIv = findViewById(R.id.swapIv);
        searchIv = findViewById(R.id.searchIv);
        searchEt = findViewById(R.id.searchEt);
    }

    private void search(boolean fa2en, String text) {
        service.translate(Const.TOKEN, text, "exact", (fa2en) ? "fa2en" : "en2fa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Root>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Root root) {
                        if (root.response.code == 200) {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("text", text);
                            intent.putExtra("translated", root.data.results.get(0).text);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

}