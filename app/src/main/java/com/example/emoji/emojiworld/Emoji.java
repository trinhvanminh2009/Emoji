package com.example.emoji.emojiworld;

public class Emoji {
    private int  viewId;
    private int viewSize;
    private String viewText;
    private Float viewX;
    private Float viewY;
    private int playerId;

    public Emoji(int viewId, int viewSize, String viewText, Float viewX, Float viewY, int playerId) {
        this.viewId = viewId;
        this.viewSize = viewSize;
        this.viewText = viewText;
        this.viewX = viewX;
        this.viewY = viewY;
        this.playerId  = playerId;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getViewSize() {
        return viewSize;
    }

    public void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    public String getViewText() {
        return viewText;
    }

    public void setViewText(String viewText) {
        this.viewText = viewText;
    }

    public Float getViewX() {
        return viewX;
    }

    public void setViewX(Float viewX) {
        this.viewX = viewX;
    }

    public Float getViewY() {
        return viewY;
    }

    public void setViewY(Float viewY) {
        this.viewY = viewY;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
