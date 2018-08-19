package com.training.crudmakananapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.training.crudmakananapp.R;
import com.training.crudmakananapp.databinding.ActivityRegisterBinding;
import com.training.crudmakananapp.helper.MyFuction;
import com.training.crudmakananapp.model.ResponseRegister;
import com.training.crudmakananapp.network.InitRetrofit;
import com.training.crudmakananapp.network.RestApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends MyFuction implements View.OnClickListener {

    ActivityRegisterBinding registerBinding;
    String[] jenkel = {"laki - laki", "perempuan"};
    private String strjenkel;
    private String strlevel;
    private String strpasswordconfirm;
    private String strpassword;
    private String strusername;
    private String strnohp;
    private String stralamat;
    private String strnama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setjenkel();
        sethakakses();
        //aksi klik radiobutton dan button
        registerBinding.regAdmin.setOnClickListener(this);
        registerBinding.regUserbiasa.setOnClickListener(this);
        registerBinding.btnregister.setOnClickListener(this);
    }

    private void sethakakses() {
        if (registerBinding.regAdmin.isChecked()) {
            strlevel = "admin";
        } else {
            strlevel = "user biasa";
        }
    }

    private void setjenkel() {
        ArrayAdapter adapter = new ArrayAdapter(
                RegisterActivity.this, android.R.layout.simple_spinner_item, jenkel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //setdata ke view (spinner)
        registerBinding.spinjenkel.setAdapter(adapter);
        registerBinding.spinjenkel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strjenkel = jenkel[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regAdmin:
                strlevel = "admin";
                break;
            case R.id.regUserbiasa:
                strlevel = "user biasa";
                break;
            case R.id.btnregister:
                strnama = registerBinding.edtnama.getText().toString();
                stralamat = registerBinding.edtalamat.getText().toString();
                strnohp = registerBinding.edtnotelp.getText().toString();
                strusername = registerBinding.edtusername.getText().toString();
                strpassword = registerBinding.edtpassword.getText().toString();
                strpasswordconfirm = registerBinding.edtpasswordconfirm.getText().toString();

                if (TextUtils.isEmpty(strnama)) {
                    registerBinding.edtnama.setError(getString(R.string.namawarning));
                    registerBinding.edtnama.requestFocus();
                    myanimation(registerBinding.edtnama);
                } else if (TextUtils.isEmpty(stralamat)) {
                    registerBinding.edtalamat.requestFocus();
                    registerBinding.edtalamat.setError(getString(R.string.alamatwarning));
                    myanimation(registerBinding.edtalamat);
                } else if (TextUtils.isEmpty(strnohp)) {
                    registerBinding.edtnotelp.requestFocus();
                    myanimation(registerBinding.edtnotelp);
                    registerBinding.edtnotelp.setError(getString(R.string.nohpwarning));
                } else if (TextUtils.isEmpty(strusername)) {
                    registerBinding.edtusername.requestFocus();
                    myanimation(registerBinding.edtusername);
                    registerBinding.edtusername.setError("username tidak boleh kosong");
                } else if (TextUtils.isEmpty(strpassword)) {
                    registerBinding.edtpassword.requestFocus();
                    myanimation(registerBinding.edtpassword);
                    registerBinding.edtpassword.setError("password tidak boleh kosong");
                } else if (strpassword.length() < 6) {
                    myanimation(registerBinding.edtpassword);
                    registerBinding.edtpassword.setError("password minimal 6 karakter");
                } else if (TextUtils.isEmpty(strpasswordconfirm)) {
                    registerBinding.edtpasswordconfirm.requestFocus();
                    myanimation(registerBinding.edtpasswordconfirm);
                    registerBinding.edtpasswordconfirm.setError("password confirm tidak boleh kosong");
                } else if (!strpassword.equals(strpasswordconfirm)) {
                    registerBinding.edtpasswordconfirm.requestFocus();
                    myanimation(registerBinding.edtpasswordconfirm);
                    registerBinding.edtpasswordconfirm.setError("password tidak sama");
                } else {
                    registeruser();
                }
                break;

        }
    }

    private void registeruser() {
        //todo 4 panggil interface dan instance retrofit
        RestApi api = InitRetrofit.getInstanceRetrofit();
        //todo 5 request ke endpoint yang ada di webservice sesuai dengan urutan parameter
        Call<ResponseRegister> registerCall = api.registeruser(
                strnama,
                stralamat,
                strnohp,
                strjenkel,
                strusername,
                strlevel,
                strpassword
        );
        //todo 6 menangkap response dari webser (berhasil atau gagal)
        registerCall.enqueue(new Callback<ResponseRegister>() {
            //todo 6.1 jika response dari webservice berhasil / menampilkan data json
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    if (result.equals("1")) {
                        myIntent(LoginActivity.class);
                        finish();
                        myToast(msg);
                    } else {
                        myToast(msg);
                    }
                } else {
                    myToast("gagal mengambil data json");
                }
            }
            //todo 6.2 jika response dari webservice gagal ex masalah koneksi

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                myToast("gagal koneksi" + t.getMessage());
            }
        });
    }
}
