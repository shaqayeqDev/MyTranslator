package com.shaqayeq.mytranslator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChooseLangFragment extends BottomSheetDialogFragment {

    private TextView farsiTv, englishTv, arabicTv;
    private LangCallBack langCallBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        langCallBack = (LangCallBack) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.choose_lang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        handleClick();

    }

    private void handleClick() {
        int type = requireArguments().getInt("type");
        farsiTv.setOnClickListener(view -> {
            if (type == 0)
                langCallBack.firstChanged("FARSI");
            else
                langCallBack.secondChanged("FARSI");
            dismiss();
        });

        arabicTv.setOnClickListener(view -> {
            if (type == 0)
                langCallBack.firstChanged("ARABIC");
            else
                langCallBack.secondChanged("ARABIC");
            dismiss();
        });

        englishTv.setOnClickListener(view -> {
            if (type == 0)
                langCallBack.firstChanged("ENGLISH");
            else
                langCallBack.secondChanged("ENGLISH");
            dismiss();
        });
    }

    private void init(View view) {
        farsiTv = view.findViewById(R.id.farsiTv);
        englishTv = view.findViewById(R.id.englishTv);
        arabicTv = view.findViewById(R.id.arabicTv);
    }

    public static ChooseLangFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        ChooseLangFragment fragment = new ChooseLangFragment();
        fragment.setArguments(args);
        return fragment;
    }

    interface LangCallBack {
        void firstChanged(String text);

        void secondChanged(String text);
    }

}
