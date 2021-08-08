package com.sona.ktp.util.api;

import com.sona.ktp.activity.jenis.ResponseJenis;
import com.sona.ktp.activity.kritiksaran.ResponseKritiksaran;
import com.sona.ktp.activity.pembuatan.ResponsePembuatan;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiService {

    //LOGIN
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                    @Field("password") String password);
    //REGISTER
    @FormUrlEncoded
    @POST("login/save")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password);

    //GET USER DETAIL
    @GET("login/all/{username}")
    Call<ResponseBody> getUserDetail(@Path("username") String username);
    //EDIT USER
    @FormUrlEncoded
    @POST("login/update/{username}")
    Call<ResponseBody> updateProfileRequest(@Path("username") String username,
                                            @Field("nama") String nama,
                                            @Field("password") String password);


    //PEMBUATAN
    //GET PEMBUATAN LIST
    @GET("pembuatan/filter/{username}")
    Call<ResponsePembuatan> getSemuaPembuatan(@Path("username") String username);
    //GET PEMBUATAN DETAIL
    @GET("pembuatan/all/{id_pembuatan}")
    Call<ResponseBody> getPembuatanDetail(@Path("id_pembuatan") String id_pembuatan);
    //POST PEMBUATAN
    @FormUrlEncoded
    @POST("pembuatan/save")
    Call<ResponseBody> pembuatanInsertRequest(@Field("nama") String nama,
                                              @Field("tempat_lahir") String tanggal,
                                              @Field("tanggal_lahir") String id_upah,
                                              @Field("jenis_kelamin") String qty,
                                              @Field("alamat") String catatan,
                                              @Field("rtrw") String rtrw,
                                              @Field("keldesa") String keldesa,
                                              @Field("kecamatan") String kecamatan,
                                              @Field("agama") String agama,
                                              @Field("status_pekerjaan") String status_pekerjaan,
                                              @Field("status_pernikahan") String status_pernikahan,
                                              @Field("kewarganegaraan") String kewarganegaraan,
                                              @Field("berlaku_hingga") String berlaku_hingga,
                                              @Field("nik") String nik,
                                              @Field("username") String username);
    //PUT PEMBUATAN
    @FormUrlEncoded
    @POST("pembuatan/update/{id_pembuatan}")
    Call<ResponseBody> pembuatanUpdateRequest(@Path("id_pembuatan") String id_pembuatan,
                                              @Field("nama") String nama,
                                              @Field("tempat_lahir") String tanggal,
                                              @Field("tanggal_lahir") String id_upah,
                                              @Field("jenis_kelamin") String qty,
                                              @Field("alamat") String catatan,
                                              @Field("rtrw") String rtrw,
                                              @Field("keldesa") String keldesa,
                                              @Field("kecamatan") String kecamatan,
                                              @Field("agama") String agama,
                                              @Field("status_pekerjaan") String status_pekerjaan,
                                              @Field("status_pernikahan") String status_pernikahan,
                                              @Field("kewarganegaraan") String kewarganegaraan,
                                              @Field("berlaku_hingga") String berlaku_hingga,
                                              @Field("nik") String nik,
                                              @Field("username") String username);
    //DELETE PEMBUATAN
    @DELETE("pembuatan/delete/{id_pembuatan}")
    Call<ResponseBody> pembuatanDeleteRequest(@Path("id_pembuatan") String id_pembuatan);
    //KIRIM PEMBUATAN
    @DELETE("pembuatan/kirim/{id_pembuatan}")
    Call<ResponseBody> pembuatanKirimRequest(@Path("id_pembuatan") String id_pembuatan);


    //KRITIKSARAN
    //GET KRITIKSARAN LIST
    @GET("kritiksaran/filter/{username}")
    Call<ResponseKritiksaran> getSemuaKritiksaran(@Path("username") String username);
    //GET KRITIKSARAN DETAIL
    @GET("kritiksaran/all/{id_kritiksaran}")
    Call<ResponseBody> getKritiksaranDetail(@Path("id_kritiksaran") String id_kritiksaran);
    //POST KRITIKSARAN
    @FormUrlEncoded
    @POST("kritiksaran/save")
    Call<ResponseBody> kritiksaranInsertRequest(@Field("kritiksaran") String kritiksaran,
                                              @Field("username") String username);
    //PUT KRITIKSARAN
    @FormUrlEncoded
    @POST("kritiksaran/update/{id_kritiksaran}")
    Call<ResponseBody> kritiksaranUpdateRequest(@Path("id_kritiksaran") String id_kritiksaran,
                                              @Field("kritiksaran") String kritiksaran,
                                              @Field("username") String username);
    //DELETE KRITIKSARAN
    @DELETE("kritiksaran/delete/{id_kritiksaran}")
    Call<ResponseBody> kritiksaranDeleteRequest(@Path("id_kritiksaran") String id_kritiksaran);


    //JENIS
    //GET JENIS LIST
    @GET("jenis")
    Call<ResponseJenis> getSemuaJenis();
    //GET JENIS DETAIL
    @GET("jenis/all/{id_jenis}")
    Call<ResponseBody> getJenisDetail(@Path("id_jenis") String id_jenis);

    //GET LAMPIRAN DETAIL
    @GET("lampiran/filterbyarray/{id_pembuatan}/{id_jenis}")
    Call<ResponseBody> getLampiranDetail(@Path("id_pembuatan") String id_pembuatan,
                                         @Path("id_jenis") String id_jenis);

}
