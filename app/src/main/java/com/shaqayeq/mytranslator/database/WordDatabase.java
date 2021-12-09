package com.shaqayeq.mytranslator.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shaqayeq.mytranslator.model.RecentlyWord;

@Database(entities = {RecentlyWord.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static WordDatabase INSTANCE;

    public static WordDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (WordDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordDatabase.class,"app_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}