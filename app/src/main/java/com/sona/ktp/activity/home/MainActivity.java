package com.sona.ktp.activity.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sona.ktp.R;
import com.sona.ktp.activity.about.AboutActivity;
import com.sona.ktp.activity.kritiksaran.KritiksaranActivity;
import com.sona.ktp.activity.login.LoginActivity;
import com.sona.ktp.activity.pembuatan.PembuatanActivity;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ivFotoUser) ImageView ivFotoUser;

    @BindView(R.id.btnAbout) Button btnAbout;
    @BindView(R.id.btnPengajuanKtp) ImageButton btnIsiPekerjaan;
    @BindView(R.id.btnKritikSaran) ImageButton btnKritikSaran;
    @BindView(R.id.btnLogout) ImageButton btnLogout;

    ProgressDialog loading;

    @BindView(R.id.swiperefreshmain)
    SwipeRefreshLayout swiperefreshmain;

    Context mContext;

    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    String sp_username, sp_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mContext = this;

        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(this);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        ivFotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ProfileActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        btnIsiPekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PembuatanActivity.class));
            }
        });

        btnKritikSaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KritiksaranActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogout();
            }
        });

        getUserProfile(sharedPrefManager.getSPEmail());

        swiperefreshmain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserProfile(sharedPrefManager.getSPEmail());
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swiperefreshmain.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        swiperefreshmain.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

    private void getUserProfile(String spIdCustomer) {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getUserDetail(spIdCustomer)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    String username = jsonRESULTS.getJSONObject("user").getString("username");
                                    String password = jsonRESULTS.getJSONObject("user").getString("password");
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String role = jsonRESULTS.getJSONObject("user").getString("role");
                                    String foto = jsonRESULTS.getJSONObject("user").getString("foto");

                                    Picasso.get().load(UtilsApi.BASE_URL_FILE_USER+foto).fit().centerCrop().into(ivFotoUser);

                                    //finish();
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
        startActivity(new Intent(mContext, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}