package com.shaqayeq.mytranslator;

import com.shaqayeq.mytranslator.model.Root;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search")
    Single<Root> translate(@Query("token") String token, @Query("q") String text, @Query("type") String type, @Query("filter") String filter);
}
