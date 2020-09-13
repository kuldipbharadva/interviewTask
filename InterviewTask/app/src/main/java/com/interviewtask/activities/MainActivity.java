package com.interviewtask.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.interviewtask.R;
import com.interviewtask.adapters.UserAdapter;
import com.interviewtask.models.GetUserRe;
import com.interviewtask.roomDb.UserDatabase;
import com.interviewtask.utils.Constants;
import com.interviewtask.utils.CustomDialog;
import com.interviewtask.utils.Functions;
import com.interviewtask.utils.MySharedPreference;
import com.interviewtask.utils.MySnackbar;
import com.interviewtask.webApi.ApiCalling;
import com.interviewtask.webApi.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.pbLoadMore)
    ProgressBar pbLoadMore;

    private Context mContext;

    private int pageIndex = 0;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;

    private UserAdapter userAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<GetUserRe> getUserReArrayList;

    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBasic();
    }

    private void initBasic() {
        mContext = MainActivity.this;
        userDatabase = UserDatabase.getInstance(mContext);
        setLazyLoading();
    }

    private void setLazyLoading() {
        getUserReArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mContext);
        userAdapter = new UserAdapter(mContext);
        rvList.setAdapter(userAdapter);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setHasFixedSize(true);

        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                boolean isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage;
                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                boolean isValidFirstItem = firstVisibleItemPosition >= 0;
                boolean totalIsMoreThanVisible = totalItemCount >= 10/*total item in page*/;
                // flag to know whether to load more
                boolean shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage;

                if (ApiCalling.isNetworkAvailable(mContext)) {
                    if (shouldLoadMore) apiCallResult(false);
                }  else {
                    getUserReArrayList = (ArrayList<GetUserRe>) userDatabase.getUserDao().getAllData();
                    userAdapter.setList(getUserReArrayList);
                }
            }
        });

        if (ApiCalling.isNetworkAvailable(mContext)) {
            apiCallResult(true);
        } else {
            getUserReArrayList = (ArrayList<GetUserRe>) userDatabase.getUserDao().getAllData();
            userAdapter.setList(getUserReArrayList);
            if (getUserReArrayList.size() > 0) {
                rvList.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            } else {
                tvNoData.setVisibility(View.VISIBLE);
                rvList.setVisibility(View.GONE);
            }
        }
    }

    private void apiCallResult(boolean isLoading) {
        mIsLoading = true;
        pageIndex++;
        if (!isLoading) {
            pbLoadMore.setVisibility(View.VISIBLE);
        }

        String postUrl = "per_page=10&since=" + pageIndex;
        ApiCalling.makeApiCall(mContext, RetrofitClient.apiPostUrl.getUsers + postUrl, RetrofitClient.methodType.GET, new HashMap(), new Object(), isLoading, new ApiCalling.ApiSuccessInterface() {
            @Override
            public void onSuccess(int resCode, String resMsg, String apiResponse) {
                pbLoadMore.setVisibility(View.GONE);
                if (resCode == 200 || resCode == 201) {
                    ArrayList<GetUserRe> res = new Gson().fromJson(apiResponse, new TypeToken<List<GetUserRe>>() {
                    }.getType());
                    try {
                        if (res != null) {
                            if (pageIndex == 1) {
                                if (res.size() > 0) {
                                    rvList.setVisibility(View.VISIBLE);
                                    tvNoData.setVisibility(View.GONE);
                                    userAdapter.setList((ArrayList<GetUserRe>) res);
                                    userDatabase.getUserDao().deleteUserTable();
                                    addListInTable((ArrayList<GetUserRe>) res);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    rvList.setVisibility(View.GONE);
                                }
                            } else {
                                userAdapter.addAll((ArrayList<GetUserRe>) res);
                                addListInTable((ArrayList<GetUserRe>) res);
                            }
                            mIsLoading = false;
                            mIsLastPage = (pageIndex == 11);
                        }
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                } else {
                    MySnackbar.showSnackbar(mContext, resMsg, MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pbLoadMore.setVisibility(View.GONE);
                MySnackbar.showSnackbar(mContext, t.getLocalizedMessage().isEmpty() ? mContext.getString(R.string.msg_unexpected_error) : t.getLocalizedMessage(), MySnackbar.SnackbarPosition.TOP, MySnackbar.SnackbarType.FAILED);
            }
        });
    }

    private void doLogout() {
        CustomDialog.alertDialogDoubleClick(mContext, 0, getString(R.string.lbl_alert), getString(R.string.msg_logout_alert), true
                , getString(R.string.btn_log_out), getString(R.string.btn_not_now), R.color.colorGreen, R.color.colorRed, new CustomDialog.DoubleClickDialogInterface() {
                    @Override
                    public void setPositiveClicked() {
                        MySharedPreference.setPreference(mContext, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_LOGIN, false);
                        startActivity(new Intent(mContext, LoginActivity.class));
                        Functions.nextAnim(mContext);
                        finishAffinity();
                    }

                    @Override
                    public void setNegativeClicked() {

                    }
                });
    }

    @OnClick(R.id.ivLogout)
    public void onViewClicked() {
        doLogout();
    }

    /* this function insert list in database table */
    private void addListInTable(ArrayList<GetUserRe> getList) {
        ArrayList<GetUserRe> myList = new ArrayList<>();
        myList.addAll(getList);
        userDatabase.getUserDao().insertAll(myList);
    }
}