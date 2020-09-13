package com.interviewtask.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.interviewtask.R;
import com.interviewtask.utils.Constants;
import com.interviewtask.utils.Functions;
import com.interviewtask.utils.MySharedPreference;
import com.interviewtask.utils.MySnackbar;
import com.interviewtask.utils.Validations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmailAddress)
    EditText etEmailAddress;
    @BindView(R.id.etPassword)
    EditText etPassword;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initBasic();
    }

    private void initBasic() {
        mContext = LoginActivity.this;
        boolean isLogin = (boolean) MySharedPreference.getPreference(mContext, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_LOGIN, false);
        if (isLogin) {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
            Functions.nextAnim(mContext);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Functions.backAnim(mContext);
    }

    @OnClick({R.id.btnForgotPassword, R.id.tvLogin, R.id.tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnForgotPassword:
                break;
            case R.id.tvLogin:
                if (isValidateField()) {
                    manageLogin();
                }
                break;
            case R.id.tvSignUp:
                break;
        }
    }

    /* this function check field validation */
    private boolean isValidateField() {
        return Validations.isBlank(mContext, etEmailAddress, "please enter your email!", MySnackbar.SnackbarPosition.TOP)
                && Validations.isValidEmail(mContext, "please enter valid email!", MySnackbar.SnackbarPosition.TOP, etEmailAddress)
                && Validations.isBlank(mContext, etPassword, "please enter your password!", MySnackbar.SnackbarPosition.TOP);
    }

    /* this function manage login details in preference */
    private void manageLogin() {
        MySharedPreference.setPreference(mContext, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_LOGIN, true);
        MySharedPreference.setPreference(mContext, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_EMAIL_MOBILE, etEmailAddress.getText().toString().trim());
        MySharedPreference.setPreference(mContext, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_PASSWORD, etPassword.getText().toString().trim());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
        Functions.nextAnim(mContext);
    }
}
