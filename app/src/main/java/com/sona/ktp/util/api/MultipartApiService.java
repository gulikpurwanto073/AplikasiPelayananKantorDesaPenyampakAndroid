package com.sona.ktp.util.api;

import com.sona.ktp.util.ResponseImage;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MultipartApiService {
    String BASE_URL = UtilsApi.BASE_URL_API;

    @Multipart
    @POST("login/upload/{username}")
    Call<ResponseImage> uploadImage(@Path("username") String username,
                                    @Part("foto\"; filename=\"myfile.jpg\" ") RequestBody foto);

    @Multipart
    @POST("lampiran/save/{id_pembuatan}/{id_jenis}")
    Call<ResponseImage> uploadFile(@Path("id_pembuatan") String id_pembuatan,
                                   @Path("id_jenis") String id_jenis,
                                    @Part("file\"; filename=\"myfile.jpg\" ") RequestBody file);
    
}
