package com.sona.ktp.activity.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sona.ktp.R;
import com.sona.ktp.activity.login.LoginActivity;
import com.sona.ktp.util.ResponseImage;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.MultipartApiService;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivFotoUser) ImageView ivFotoCustomer;

    @BindView(R.id.etNama) EditText etNama;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;

    @BindView(R.id.btnEditProfile) Button btnEditProfile;
    @BindView(R.id.btnLogout) Button btnLogOut;

    ProgressDialog loading;

    Context mContext;

    SharedPrefManager sharedPrefManager;

    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;
        ButterKnife.bind(this);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdateProfile(sharedPrefManager.getSPEmail());
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogout();
            }
        });

        ivFotoCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        getUserProfile(sharedPrefManager.getSPEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            uploadFile(selectedImage);
        }
    }

    private void uploadFile(Uri fileUri) {
        File file = new File(getRealPathFromURI(fileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MultipartApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        MultipartApiService multipartApiService = retrofit.create(MultipartApiService.class);
        Call<ResponseImage> call = multipartApiService.uploadImage(sharedPrefManager.getSPEmail(), requestFile);
        call.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                if (!response.body().error) {
                    Toast.makeText(getApplicationContext(), "Foto sukses di upload...", Toast.LENGTH_LONG).show();
                    getUserProfile(sharedPrefManager.getSPEmail());
                } else {
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseImage> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void requestUpdateProfile(String spUsername) {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        mApiService.updateProfileRequest(
                spUsername,
                etNama.getText().toString(),
                etPassword.getText().toString()
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            Toast.makeText(mContext, "Data Berhasil Diedit", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        } else {
                            loading.dismiss();
                            Toast.makeText(mContext, "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserProfile(String spUsername) {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getUserDetail(spUsername)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String email = jsonRESULTS.getJSONObject("user").getString("username");
                                    String password = jsonRESULTS.getJSONObject("user").getString("password");
                                    String foto = jsonRESULTS.getJSONObject("user").getString("foto");

                                    etNama.setText(nama);
                                    etEmail.setText(email);
                                    etPassword.setText(password);

                                    Picasso.get().load(UtilsApi.BASE_URL_FILE_USER+foto).fit().centerCrop().into(ivFotoCustomer);

                                } else {
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void confirmLogout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Log Out");
        alert.setMessage("Keluar dari aplikasi?");
        alert.setPositiveButton("Ya, Log Out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void logout() {
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
    }

}
