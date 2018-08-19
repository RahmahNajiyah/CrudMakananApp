package com.training.crudmakananapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.training.crudmakananapp.R;
import com.training.crudmakananapp.databinding.ActivityMakananBinding;
import com.training.crudmakananapp.databinding.ContentMakananBinding;
import com.training.crudmakananapp.databinding.TambahmakananBinding;
import com.training.crudmakananapp.helper.MyConstants;
import com.training.crudmakananapp.helper.MyFuction;
import com.training.crudmakananapp.model.DataKategoriItem;
import com.training.crudmakananapp.model.ResponseKategorimakanan;
import com.training.crudmakananapp.network.InitRetrofit;
import com.training.crudmakananapp.network.RestApi;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakananActivity extends MyFuction {


    ContentMakananBinding makananBinding;
    ActivityMakananBinding makanan2Binding;
    private Dialog dialog;
    private Uri filepath;
    private Bitmap bitmap;
    TambahmakananBinding binding;
    private List<DataKategoriItem> listkategorimakanan;
    private String Strkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makananBinding = DataBindingUtil.setContentView(this,R.layout.content_makanan);
        makanan2Binding = DataBindingUtil.setContentView(this,R.layout.activity_makanan);
        setSupportActionBar(makanan2Binding.toolbar);
        permissiongalery();
        makananBinding.listmakanan.setLayoutManager(new LinearLayoutManager(c));
        makanan2Binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dialog = new Dialog(MakananActivity.this);
        dialog.setTitle("data makanan");

        binding = DataBindingUtil.inflate(LayoutInflater.from(MakananActivity.this)
               ,R.layout.tambahmakanan,null,false);
       dialog.setContentView(binding.getRoot());
       dialog.setCancelable(true);
       dialog.setCanceledOnTouchOutside(false);
       dialog.show();
       getkategorimakanan(binding.spincarikategori);
       binding.btnuploadmakanan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showfilechooser();
           }
       });

            }
        });
    }

    private void getkategorimakanan(final Spinner spincarikategori) {
        RestApi api= InitRetrofit.getInstanceRetrofit();
        Call<ResponseKategorimakanan> kategorimakananCall = api.getkategorimakanan();
        kategorimakananCall.enqueue(new Callback<ResponseKategorimakanan>() {
            @Override
            public void onResponse(Call<ResponseKategorimakanan> call, Response<ResponseKategorimakanan> response) {
                if (response.isSuccessful()){
                    listkategorimakanan = response.body().getDataKategori();
                    String [] namakategori = new String[listkategorimakanan.size()];
                    String [] idkategori = new String[listkategorimakanan.size()];
                    for (int i =0 ;i<listkategorimakanan.size();i++){
                        namakategori[i]= listkategorimakanan.get(i).getNamaKategori();
                        idkategori[i]= listkategorimakanan.get(i).getIdKategori();
                    }
                    ArrayAdapter adapter = new ArrayAdapter(MakananActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,namakategori);
                    spincarikategori.setAdapter(adapter);
                    Strkategori =idkategori[0].toString();
                    spincarikategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Strkategori =parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseKategorimakanan> call, Throwable t) {

            }
        });
    }

    private void permissiongalery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MyConstants.STORAGE_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MyConstants.STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                myToast("Permission granted now you can read the storage");
            } else {
                //Displaying another toast if permission is not granted
                myToast("Oops you just denied the permission");
            }
        }
    }

    private void showfilechooser() {
        Intent intentgalery = new Intent(Intent.ACTION_PICK);
        intentgalery.setType("image/*");
        intentgalery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentgalery, "select Pictures"), MyConstants.REGFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.REGFILE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                binding.imgupload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onBackPressed() {
        keluar();
    }
}
