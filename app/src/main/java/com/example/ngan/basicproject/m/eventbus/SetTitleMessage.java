package com.example.ngan.basicproject.m.eventbus;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public class SetTitleMessage {
    private final String mTitleText;
    public String getmTitleText() {
        return mTitleText;
    }
    public SetTitleMessage(String mTitleText) {
        this.mTitleText = mTitleText;
    }
}
