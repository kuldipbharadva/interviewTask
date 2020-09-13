package com.interviewtask.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.interviewtask.R;

import java.lang.ref.WeakReference;

@SuppressWarnings("ALL")
public class CustomDialog {

    private static android.app.ProgressDialog progressDialog;
    private static WeakReference<Activity> weakActivity;

    public static void displayProgress(Context context) {
        weakActivity = new WeakReference<>((Activity) context);

        if (weakActivity.get() != null && !weakActivity.get().isFinishing()) {
            if (progressDialog == null) {
                progressDialog = new android.app.ProgressDialog(context);

            } else {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
            progressDialog.setMessage(context.getString(R.string.please_wait));
            if (!((Activity) context).isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setCancelable(false);
        }
    }

    public static void dismissProgress(Context context) {
        weakActivity = new WeakReference<>((Activity) context);

        if (weakActivity.get() != null && !weakActivity.get().isFinishing()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static void alertDialogDoubleClick(Context context, int icon, String title, String message, boolean cancelable, String positiveText, String negativeText, int positiveTextColor, int negativeTextColor, final DoubleClickDialogInterface doubleClickDialogInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(positiveText, (dialog, which) -> doubleClickDialogInterface.setPositiveClicked());
        builder.setNegativeButton(negativeText, (dialog, which) -> doubleClickDialogInterface.setNegativeClicked());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(positiveTextColor));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(negativeTextColor));
    }

    public static void customDialog(Context context, Dialog dialog, final int view, int positiveButtonId, boolean cancelable, int height, final NoInterNetDialogInterface customDialogInterface) {
        final View viewLayout = LayoutInflater.from(context).inflate(view, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewLayout);
        if (height == CustomDialogInterface.wrap_content) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (height == CustomDialogInterface.match_parent) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        dialog.setCancelable(cancelable);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.show();
        final Dialog finalDialog = dialog;
        dialog.findViewById(positiveButtonId).setOnClickListener(v -> customDialogInterface.onOkClicked(finalDialog, viewLayout));
    }

    public static void customDialog(Context context, Dialog dialog, final int view, int positiveButtonId, int negativeButtonId, boolean cancelable, int height, final CustomDialogInterface customDialogInterface) {
        final View viewLayout = LayoutInflater.from(context).inflate(view, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewLayout);
        if (height == CustomDialogInterface.wrap_content) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (height == CustomDialogInterface.match_parent) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        dialog.setCancelable(cancelable);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.show();
        final Dialog finalDialog = dialog;
        dialog.findViewById(positiveButtonId).setOnClickListener(v -> customDialogInterface.onOkClicked(finalDialog, viewLayout));
        dialog.findViewById(negativeButtonId).setOnClickListener(v -> customDialogInterface.onCancelClicked(finalDialog, viewLayout));
    }

    /**
     * this function show no internet dialog with custom layout
     */
    public static void noInternetDialog(Context context, Dialog dialog, final int view, int positiveButtonId, boolean cancelable, int height, final NoInterNetDialogInterface noInterNetDialogInterface) {
        final View viewLayout = LayoutInflater.from(context).inflate(view, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewLayout);
        if (height == CustomDialogInterface.wrap_content) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (height == CustomDialogInterface.match_parent) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        dialog.setCancelable(cancelable);
        dialog.show();
        final Dialog finalDialog = dialog;
        dialog.findViewById(positiveButtonId).setOnClickListener(v -> noInterNetDialogInterface.onOkClicked(finalDialog, viewLayout));
    }

    /**
     * this function open setting dialog
     */
    public static void openSettingDialog(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public interface CustomDialogInterface {

        int wrap_content = 0;
        int match_parent = 1;

        void onOkClicked(Dialog dialog, View view);

        void onCancelClicked(Dialog dialog, View view);

        void onNeutralClicked(Dialog dialog);
    }

    public interface NoInterNetDialogInterface {

        int wrap_content = 0;
        int match_parent = 1;

        void onOkClicked(Dialog d, View view);
    }

    public interface DoubleClickDialogInterface {

        void setPositiveClicked();

        void setNegativeClicked();
    }
}