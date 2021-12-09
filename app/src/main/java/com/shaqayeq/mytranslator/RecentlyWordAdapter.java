package com.shaqayeq.mytranslator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaqayeq.mytranslator.model.RecentlyWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentlyWordAdapter extends RecyclerView.Adapter<RecentlyWordAdapter.RecentlyWordViewHolder> {

    public List<RecentlyWord> words = new ArrayList<>();
    private WordCallBack wordCallBack;

    public RecentlyWordAdapter(WordCallBack wordCallBack) {
        this.wordCallBack = wordCallBack;
    }

    public void setWords(List<RecentlyWord> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public void addWord(RecentlyWord word) {
        words.add(0, word);
        notifyItemInserted(0);
    }

    public void removeWord(RecentlyWord word) {
        int index = words.indexOf(word);
        words.remove(index);
        notifyItemRemoved(index);
    }

    @NonNull
    @Override
    public RecentlyWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentlyWordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.word, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyWordViewHolder holder, int position) {
        holder.bind(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class RecentlyWordViewHolder extends RecyclerView.ViewHolder {
        private TextView wordTv, translatedTv;
        private ImageView deleteIv;

        public RecentlyWordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
            translatedTv = itemView.findViewById(R.id.translatedWordTv);
            deleteIv = itemView.findViewById(R.id.removeWordIv);
        }

        public void bind(RecentlyWord recentlyWord) {
            wordTv.setText(recentlyWord.getWord());
            translatedTv.setText(getFirstMeaning(recentlyWord.getTranslatedWord()));
            deleteIv.setOnClickListener(view ->{
                wordCallBack.removeWord(recentlyWord);
            });
        }
    }

    private String getFirstMeaning(String text){
        if (!text.isEmpty()){
            List<String> list= Arrays.asList(text.split("،"));
            return list.get(0);
        }else
            return "";
    }

    interface WordCallBack{
        void removeWord(RecentlyWord word);
    }

}
