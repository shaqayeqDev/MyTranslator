package com.shaqayeq.mytranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shaqayeq.mytranslator.database.WordDao;
import com.shaqayeq.mytranslator.database.WordDatabase;
import com.shaqayeq.mytranslator.model.RecentlyWord;
import com.shaqayeq.mytranslator.model.Root;
import com.shaqayeq.mytranslator.utils.Const;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecentlyWordAdapter.WordCallBack, ChooseLangFragment.LangCallBack {

    private static final String TAG = "MainActivity.class";
    private TextView firstLanguageTv, secondLanguageTv, emptyListTv;
    private ImageView swapIv, searchIv;
    private EditText searchEt;
    private ProgressBar progressBar, wordProgress;
    private ApiService service = RetrofitClient.getClient().create(ApiService.class);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WordDao wordDao;
    private RecyclerView listRv;
    private Intent intent;
    private RecentlyWordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        swapLanguage();
        chooseLang();
        searchClicked();
    }

    private void chooseLang() {
        firstLanguageTv.setOnClickListener(view -> {
            ChooseLangFragment chooseLangFragment = ChooseLangFragment.newInstance(0);
            chooseLangFragment.show(getSupportFragmentManager(), null);
        });
        secondLanguageTv.setOnClickListener(view -> {
            ChooseLangFragment chooseLangFragment = ChooseLangFragment.newInstance(1);
            chooseLangFragment.show(getSupportFragmentManager(), null);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillList();
    }


    private void searchClicked() {
        searchIv.setOnClickListener(view -> {
            String first = firstLanguageTv.getText().toString();
            String second = secondLanguageTv.getText().toString();

            String pair = "";
            if (first.equals("FARSI") && second.equals("ENGLISH"))
                pair = "fa2en";
            else if (first.equals("FARSI") && second.equals("ARABIC"))
                pair = "fa2ar";
            else if (first.equals("ENGLISH") && second.equals("FARSI"))
                pair = "en2fa";
            else if (first.equals("ARABIC") && second.equals("FARSI"))
                pair = "ar2fa";
            else
                pair = null;

            if (pair != null)
                search(pair, searchEt.getText().toString());
            else
                Toast.makeText(this, "can not translate because you have chosen the wrong language", Toast.LENGTH_SHORT).show();
        });
    }

    private void swapLanguage() {
        swapIv.setOnClickListener(view -> {
            String first = firstLanguageTv.getText().toString();
            String second = secondLanguageTv.getText().toString();
            String temp = "";

            temp = first;
            first = second;
            second = temp;

            firstLanguageTv.setText(first);
            secondLanguageTv.setText(second);

            if (firstLanguageTv.getText().toString().equals("FARSI"))
                searchEt.setHint("enter the FARSI word you want to translate");
            else if (firstLanguageTv.getText().toString().equals("FARSI"))
                searchEt.setHint("enter the ENGLISH word you want to translate");
            else
                searchEt.setHint("enter the ARABIC word you want to translate");
        });
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.light_black));
        }

        adapter = new RecentlyWordAdapter(this);

        wordDao = WordDatabase.getDatabase(this).wordDao();

        firstLanguageTv = findViewById(R.id.firstLanguageTv);
        secondLanguageTv = findViewById(R.id.secondLanguageTv);
        swapIv = findViewById(R.id.swapIv);
        searchIv = findViewById(R.id.searchIv);
        searchEt = findViewById(R.id.searchEt);
        progressBar = findViewById(R.id.progressBar);
        wordProgress = findViewById(R.id.wordProgress);
        listRv = findViewById(R.id.recentlyWord);
        emptyListTv = findViewById(R.id.emptyListTv);

        listRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        listRv.setAdapter(adapter);
    }

    private void handleVisibilitySearch(Boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        searchIv.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    private void handleVisibilityWord(Boolean loading) {
        wordProgress.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        listRv.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    private void search(String pair, String text) {
        service.translate(Const.TOKEN, text, "exact", pair)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Root>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        handleVisibilitySearch(true);
                    }

                    @Override
                    public void onSuccess(@NonNull Root root) {
                        handleVisibilitySearch(false);
                        if (root.response.code == 200) {
                            intent = new Intent(MainActivity.this, DetailActivity.class);

                            if (root.data.results.isEmpty()) {
                                Toast.makeText(MainActivity.this, "nothing to show", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            intent.putExtra("text", text);
                            intent.putExtra("translated", root.data.results.get(0).text);
                            intent.putExtra("pron", root.data.results.get(0).pron);

                            addWord(new RecentlyWord(text, root.data.results.get(0).text));

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleVisibilitySearch(false);
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    private void addWord(RecentlyWord word) {
        wordDao.insert(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    private void fillList() {
        wordDao.words()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RecentlyWord>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        handleVisibilityWord(true);
                    }

                    @Override
                    public void onSuccess(@NonNull List<RecentlyWord> recentlyWords) {
                        handleVisibilityWord(false);
                        adapter.setWords(recentlyWords);
                        emptyListTv.setVisibility(recentlyWords.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleVisibilityWord(false);
                    }
                });
    }

    private void removeRecentlyWord(RecentlyWord word) {
        wordDao.remove(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        adapter.removeWord(word);
                        emptyListTv.setVisibility(adapter.words.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void removeWord(RecentlyWord word) {
        removeRecentlyWord(word);
    }

    @Override
    public void firstChanged(String text) {
        firstLanguageTv.setText(text);
    }

    @Override
    public void secondChanged(String text) {
        secondLanguageTv.setText(text);
    }
}