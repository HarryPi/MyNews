package com.example.harry.mynews.DebugInfo;

/**
 * Created by harry on 12/03/2018.
 * Created to hold all debug related strings
 */

public class DebugConstants {
    private static final DebugConstants ourInstance = new DebugConstants();

    public static DebugConstants getInstance() {
        return ourInstance;
    }
    public static final String FACEBOOK_DEBUG_TAG = "FACEBOOK_LOGIN";
    public static final String AUTHUI_DEBUG_TAG = "AUTH_UI";
    public static final String SUCCESS = "SUCCESS";
    public static final String INTERRUPTED = "INTERRUPTED";
    public static final String ERROR = "ERROR";
    private DebugConstants() {
    }
}
