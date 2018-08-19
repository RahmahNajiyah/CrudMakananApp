package com.training.crudmakananapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.training.crudmakananapp.R;
import com.training.crudmakananapp.databinding.ActivityLoginBinding;
import com.training.crudmakananapp.helper.MyFuction;
import com.training.crudmakananapp.helper.SessionManager;
import com.training.crudmakananapp.model.ResponseLogin;
import com.training.crudmakananapp.model.User;
import com.training.crudmakananapp.network.InitRetrofit;
import com.training.crudmakananapp.network.RestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyFuction implements View.OnClickListener {


    ActivityLoginBinding loginBinding;
    private String strlevel;
    private String strusername;
    private String strpassword;
    private User user;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        sethakakses();

        //aksi klik
        loginBinding.regAdmin.setOnClickListener(this);
        loginBinding.regUserbiasa.setOnClickListener(this);
        loginBinding.regBtnLogin.setOnClickListener(this);
        loginBinding.regBtnRegister.setOnClickListener(this);
    }


    private void sethakakses() {
        if (loginBinding.regAdmin.isChecked()) {
            strlevel = "admin";
        } else {
            strlevel = "user biasa";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regAdmin:
                break;
            case R.id.regUserbiasa:
                break;
            case R.id.regBtnLogin:
                strusername = loginBinding.regUsername.getText().toString();
                strpassword = loginBinding.regPass.getText().toString();
                if (TextUtils.isEmpty(strusername)) {
                    loginBinding.regUsername.requestFocus();
                    myanimation(loginBinding.regUsername);
                    loginBinding.regUsername.setError("username tidak boleh kosong");
                } else if (TextUtils.isEmpty(strpassword)) {
                    loginBinding.regPass.requestFocus();
                    myanimation(loginBinding.regPass);
                    loginBinding.regPass.setError("password tidak boleh kosong");
                } else if (strpassword.length() < 6) {
                    myanimation(loginBinding.regPass);
                    loginBinding.regPass.setError("password minimal 6 karakter");
                } else {
                    RestApi api = InitRetrofit.getInstanceRetrofit();
                    Call<ResponseLogin> loginCall = api.loginuser(
                            strpassword,
                            strusername,
                            strlevel
                    );

                    loginCall.enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if (response.isSuccessful()) {
                                String result = response.body().getResult();
                                String msg = response.body().getMsg();
                                if (result.equals("1")) {
                                    user = response.body().getUser();
                                    myIntent(MakananActivity.class);
                                    myToast(msg);
                                manager =new SessionManager(LoginActivity.this);
                                manager.createSession(user.getUsername());
                                manager.setIdUser(user.getIdUser());
                                    finish();
                                } else {
                                    myToast(msg);
                                }
                            } else {
                                myToast("gagal");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            myToast("masalah koneksi" + t.getMessage());
                        }
                    });

                }
                break;
            case R.id.regBtnRegister:
                break;
        }
    }

}
