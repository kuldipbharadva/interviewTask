package com.interviewtask.utils;

import android.content.Context;
import android.widget.EditText;

public class Validations {

    public static boolean isShowSnackBar = true;

    public static boolean isBlank(Context context, EditText editText, String emptyMsg, int snackBarPosition) {
        String _strEditTextVal = editText.getText().toString().trim();
        if (_strEditTextVal.length() == 0) {
            showSnackBar(context, emptyMsg, snackBarPosition);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isBlank(Context context, EditText editText, String emptyMsg, String errorMsg, int snackBarPosition, int minVal, int maxVal) {
        String _strEditTextVal = editText.getText().toString().trim();
        boolean b = _strEditTextVal.length() < minVal || _strEditTextVal.length() > maxVal;
        if (_strEditTextVal.length() == 0) {
            showSnackBar(context, emptyMsg, snackBarPosition);
            editText.requestFocus();
            return false;
        }
        if (b) {
            showSnackBar(context, errorMsg, snackBarPosition);
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidEmail(Context context, String msg, int snackBarPosition, EditText editText) {
        if (!(editText.getText().toString().trim().toLowerCase()
                .matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
            showSnackBar(context, msg, snackBarPosition);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPasswordMatch(Context context, EditText edtPassword1, EditText edtPassword2, String msg, int snackbarPosition) {
        String strPassword1 = edtPassword1.getText().toString().trim();
        String strPassword2 = edtPassword2.getText().toString().trim();
        if (!strPassword1.equals(strPassword2)) {
            showSnackBar(context, msg, snackbarPosition);
            edtPassword2.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private static void showSnackBar(Context context, String msg, int snackBarPosition) {
        if (isShowSnackBar) {
            if (snackBarPosition == MySnackbar.SnackbarPosition.TOP) {
                MySnackbar.showSnackbar(context, msg, MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
            } else {
                MySnackbar.showSnackbar(context, msg, MySnackbar.SnackbarPosition.BOTTOM, MySnackbar.SnackbarType.FAILED);
            }
        }
    }
}