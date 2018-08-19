package com.training.crudmakananapp.network;

import com.training.crudmakananapp.model.ResponseKategorimakanan;
import com.training.crudmakananapp.model.ResponseLogin;
import com.training.crudmakananapp.model.ResponseRegister;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {

//todo 3 panggil endpoint untuk register user
   //untuk daftar
    @FormUrlEncoded
    //nama file atau function
    @POST("registeruser.php")
    //response sesuai dengan model dan sesuaikan parameter dengan function di webservice
    Call<ResponseRegister> registeruser(
            @Field("vsnama") String nama,
            @Field("vsalamat") String alamat,
            @Field("vsnotelp") String nohp,
            @Field("vsjenkel") String jenkel,
            @Field("vsusername") String username,
            @Field("vslevel") String level,
            @Field("vspassword") String password
            );

    //untuk login
    @FormUrlEncoded
    //nama file atau function
    @POST("loginuser.php")
    //response sesuai dengan model dan sesuaikan parameter dengan function di webservice
    Call<ResponseLogin> loginuser(
            @Field("edtpassword") String password,
            @Field("edtusername") String username,
            @Field("vslevel") String level
    );

    @GET("kategorimakanan.php")
    Call<ResponseKategorimakanan> getkategorimakanan();


}
