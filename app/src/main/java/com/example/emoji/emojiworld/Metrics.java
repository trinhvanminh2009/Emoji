package com.example.emoji.emojiworld;

 public class Metrics {
    /**
     * This value defines how many pixels correspond to 1 meter.
     */
     private static final float SCREEN_TO_WORLD_RATIO = 2000.0f;

    /**
     * Coverts pixels to meters.
     */
    public static float pixelsToMeters(float value){
        return value / SCREEN_TO_WORLD_RATIO;
    }

    /**
     * Coverts meters to pixels.
     */
    public static float metersToPixels(float value){
        return value * SCREEN_TO_WORLD_RATIO;
    }
}
