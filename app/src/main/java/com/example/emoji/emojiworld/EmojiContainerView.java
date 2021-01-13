package com.example.emoji.emojiworld;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.example.emoji.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmojiContainerView extends FrameLayout implements EmojiWorld.Listener {


    /**
     * The size (width and height) of the Emoji view, in pixels.
     */
    private int EMOJI_SIZE = 200;

    /**
     * The amount of Emojis to create.
     */
    private long EMOJI_COUNT = 20L;

    /**
     * The interval at which the Emojis are created one after the other.
     */

    private long EMOJI_TEXT_SIZE = 40;

    private long EMOJI_CREATION_INTERVAL = 1000L;
    private int NUMBER_EMOJI_PER_CLICK = 5;

    private final EmojiWorld world = new EmojiWorld(this);
    private Disposable emojiCreationDisposable;
    private String componentID;

    public EmojiContainerView(@NonNull Context context) {
        super(context);
    }

    public EmojiContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmojiContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onRunEmoji();
    }

    public void onRunEmoji(){
        this.post(() -> {
            if (world.state == EmojiWorld.State.IDLE) {
                world.create(getMeasuredWidth() +200 , getMeasuredHeight() +200);
            }
            // Start simulation.
            world.startSimulation();
        });
    }

    public void stopRunEmoji(){
        world.stopSimulation();
        this.removeAllViews();
        onRunEmoji();
    }

    public void createEmojis(String emojiName, int playerID) {
        Disposable emojiCreationDisposable = this.emojiCreationDisposable;
        if (emojiCreationDisposable != null) {
            emojiCreationDisposable.dispose();
        }
        for (int i = 0; i < NUMBER_EMOJI_PER_CLICK; i++) {
            Emoji emoji = new Emoji(FrameLayout.generateViewId(), EMOJI_SIZE, emojiName, Float.valueOf(i), Float.valueOf(i), playerID);
            addEmojiView(emoji);
        }
    }

    /**
     * Adds a new [EmojiView1] and tells the 2D world to add a body that corresponds to this view.
     */
    private void addEmojiView(Emoji emoji) {
        EmojiView emojiView = new EmojiView(this.getContext());
        emojiView.setLayoutParams(new LayoutParams(emoji.getViewSize(), emoji.getViewSize()));
        emojiView.setId(emoji.getViewId());
        emojiView.setText(emoji.getViewText());
        emojiView.setTextSize(EMOJI_TEXT_SIZE);
        world.createEmoji(emoji);
        addView(emojiView);
    }

    @Override
    public void onSimulationUpdate(Emoji emoji) {
        EmojiView emojiView = this.findViewById(emoji.getViewId());
        if (emojiView != null) {
//            if(emoji.getViewX() < -60 || emoji.getViewY() < 60){
//                removeView(emojiView);
//            }
            emojiView.setX(emoji.getViewX());
            emojiView.setY(emoji.getViewY());
            Log.e("onSimulationUpdate", getChildCount() + " ");


        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.world.stopSimulation();
        Disposable emojiCreationDisposable = this.emojiCreationDisposable;
        if (emojiCreationDisposable != null) {
            emojiCreationDisposable.dispose();
        }
    }
}
