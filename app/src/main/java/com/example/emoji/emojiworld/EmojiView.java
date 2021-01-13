package com.example.emoji.emojiworld;

import android.content.Context;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.emoji.R;


public class EmojiView extends AppCompatTextView {
    public EmojiView(@NonNull Context context) {
        super(context);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.drawable.bg_emoji);
    }
}
