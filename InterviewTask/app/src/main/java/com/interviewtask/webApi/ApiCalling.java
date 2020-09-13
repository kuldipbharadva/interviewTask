package com.interviewtask.webApi;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.interviewtask.R;
import com.interviewtask.utils.CustomDialog;
import com.interviewtask.utils.MySnackbar;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class ApiCalling {

    private static Call call;
    private static Dialog dialog;

    public static void makeApiCall(Context context, String url, String method, Map headerMap, Object requestBody, boolean isProgressDialogNeeded, ApiSuccessInterface apiSuccessInterface) {
        Log.d("apiReq", "makeApiCall: request :: " + new Gson().toJson(requestBody));
        Log.d("apiReq", "makeApiCall: header  :: " + new Gson().toJson(headerMap));
        Log.d("apiReq", "makeApiCall: req url :: " + url);
        try {
            if (isNetworkAvailable(context)) {
                try {
                    if (isProgressDialogNeeded) {
                        CustomDialog.displayProgress(context);
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }

                if (method.equalsIgnoreCase(RetrofitClient.methodType.GET)) {
                    call = RetrofitClient.getInstanceNew().getServiceCall(url, headerMap);
                } else if (method.equalsIgnoreCase(RetrofitClient.methodType.POST)) {
                    call = RetrofitClient.getInstanceNew().postServiceCall(url, headerMap, requestBody);
                } else if (method.equalsIgnoreCase(RetrofitClient.methodType.PUT)) {
                    call = RetrofitClient.getInstanceNew().putServiceCall(url, headerMap, requestBody);
                } else if (method.equalsIgnoreCase(RetrofitClient.methodType.DELETE)) {
                    call = RetrofitClient.getInstanceNew().deleteServiceCall(url, headerMap, requestBody);
                }

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        Log.d("apiRes", "onResponse: " + response.message());
                        try {
                            if (isProgressDialogNeeded)
                                CustomDialog.dismissProgress(context);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                        if (response.code() == 200 || response.code() == 201 || response.code() == 403) {
                            try {
                                String res = null;
                                if (response.body() != null) {
                                    res = new String(response.body().bytes());
                                }
                                apiSuccessInterface.onSuccess(response.code(), response.message(), res);
                            } catch (IOException e) {
                                e.printStackTrace();
                                MySnackbar.showSnackbar(context, e.getLocalizedMessage().isEmpty() ? context.getString(R.string.msg_unexpected_error) : e.getLocalizedMessage(), MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
                            }
                        } else {
                            MySnackbar.showSnackbar(context, response.message().isEmpty() ? context.getString(R.string.msg_unexpected_error) : response.message(), MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.d("apiRes", "onFailure: " + t.getLocalizedMessage());
                        try {
                            if (isProgressDialogNeeded) CustomDialog.dismissProgress(context);
                            apiSuccessInterface.onFailure(t);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                            MySnackbar.showSnackbar(context, e.getLocalizedMessage().isEmpty() ? context.getString(R.string.msg_unexpected_error) : e.getLocalizedMessage(), MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
                        }
                    }
                });
            } else {
                if (dialog == null) {
                    dialog = new Dialog(context, R.style.dialog_full_screen);
                    CustomDialog.noInternetDialog(context, dialog, R.layout.dialog_no_internet, R.id.ok, false, CustomDialog.CustomDialogInterface.match_parent, new CustomDialog.NoInterNetDialogInterface() {
                        @Override
                        public void onOkClicked(Dialog d, View view) {
                            if (isNetworkAvailable(context))
                                if (dialog.isShowing()) dialog.dismiss();
                            ApiCalling.makeApiCall(context, url, method, headerMap, requestBody, true, new ApiSuccessInterface() {
                                @Override
                                public void onSuccess(int resCode, String resMsg, String apiResponse) {
                                    dialog = null;
                                    apiSuccessInterface.onSuccess(resCode, resMsg, apiResponse);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    dialog = null;
                                    apiSuccessInterface.onFailure(t);
                                }
                            });
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            MySnackbar.showSnackbar(context, e.getLocalizedMessage().isEmpty() ? context.getString(R.string.msg_unexpected_error) : e.getLocalizedMessage(), MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
        }
    }

    public interface ApiSuccessInterface {
        void onSuccess(int resCode, String resMsg, String apiResponse);

        void onFailure(Throwable t);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityMgr != null;
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
