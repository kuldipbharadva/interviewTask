package com.interviewtask.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.snackbar.Snackbar;
import com.interviewtask.R;

public class MySnackbar {

    public interface SnackbarPosition {
        int TOP=0;
        int BOTTOM=1;
    }

    public interface SnackbarType {
        int FAILED=0;
        int SUCCESS=1;
    }

    public static void showSnackbar(Context context, String msg, int snackPosition, int snackbarType) {
        View myView=((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        if (snackPosition == SnackbarPosition.TOP) {
            TSnackbar snackbar= TSnackbar.make(myView, msg, TSnackbar.LENGTH_LONG);
            setTSnackbar(context, snackbar, snackbarType);
        } else {
            Snackbar snackBar= Snackbar.make(myView, msg, Snackbar.LENGTH_LONG);
            setSnackbar(snackBar, snackbarType);
        }
    }

    /* this function set TSnackbar on top of screen */
    private static void setTSnackbar(Context context, TSnackbar snackbar, int snackbarType) {
        View snackbarView=snackbar.getView();
        if (snackbarType == SnackbarType.FAILED) {
            snackbarView.setBackgroundColor(Color.RED);
        } else if (snackbarType == SnackbarType.SUCCESS){
            snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        }
        TextView textView=snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /* this function show default Snackbar bottom of screen */
    private static void setSnackbar(Snackbar snackbar, int snackbarType) {
        View snackbarView=snackbar.getView();
        if (snackbarType == SnackbarType.FAILED) {
            snackbarView.setBackgroundColor(Color.RED);
        } else if (snackbarType == SnackbarType.SUCCESS){
            snackbarView.setBackgroundColor(Color.GREEN);
        }
        TextView textView=snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}