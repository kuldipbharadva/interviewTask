package com.interviewtask.utils;

import android.app.Activity;
import android.content.Context;

import com.interviewtask.R;

@SuppressWarnings("CatchMayIgnoreException")
public class Functions {

    public static void nextAnim(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public static void backAnim(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}