package com.shaqayeq.mytranslator;

import com.shaqayeq.mytranslator.model.Root;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search")
    Single<Root> translate(@Query("token") String token, @Query("q") String text, @Query("type") String type, @Query("filter") String filter);

    @GET("meaning")
    Single<Root> meaning(@Query("token") String token, @Query("title") String text, @Query("db") String db, @Query("num") int number);
}
