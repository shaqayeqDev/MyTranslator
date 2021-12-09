package com.shaqayeq.mytranslator.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "word")
public class RecentlyWord {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    int id;
    String word;
    String translatedWord;

    public RecentlyWord(String word, String translatedWord) {
        this.word = word;
        this.translatedWord = translatedWord;
    }

    @Ignore
    public RecentlyWord(){

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

}
