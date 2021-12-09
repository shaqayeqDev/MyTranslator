package com.shaqayeq.mytranslator.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shaqayeq.mytranslator.model.RecentlyWord;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RecentlyWord word);

    @Delete
    Completable remove(RecentlyWord word);

    @Query("SELECT * FROM word")
    Single<List<RecentlyWord>> words();

}
