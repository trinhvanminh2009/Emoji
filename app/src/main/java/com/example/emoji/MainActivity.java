package com.example.emoji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.emoji.emojiworld.EmojiContainerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmojiContainerView emojiContainerView = findViewById(R.id.emojiContainer);
        if(emojiContainerView != null){
            Button runEmoji = findViewById(R.id.btnRun);
            Button runEmojiTop   = findViewById(R.id.btnRun2);
            runEmojiTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emojiContainerView.createEmojis("\uD83D\uDE46", 0);
                }
            });
            Button btnStop = findViewById(R.id.btnStop);
            runEmoji.setOnClickListener(view -> {
                Log.e("run"," ine here");
                emojiContainerView.createEmojis("\uD83D\uDE4A", 1);

            });
            btnStop.setOnClickListener(view -> emojiContainerView.stopRunEmoji());
        }

    }
}