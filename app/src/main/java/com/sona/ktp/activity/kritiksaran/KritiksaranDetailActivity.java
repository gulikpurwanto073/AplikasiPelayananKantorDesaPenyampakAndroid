package com.sona.ktp.activity.kritiksaran;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sona.ktp.R;
import com.sona.ktp.activity.kritiksaran.KritiksaranActivity;
import com.sona.ktp.activity.kritiksaran.KritiksaranEditActivity;
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.RecyclerItemClickListener;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KritiksaranDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvKritiksaran)
    TextView tvKritiksaran;
    @BindView(R.id.tvWaktu)
    TextView tvWaktu;

    @BindView(R.id.btnEditDetail)
    Button btnEditDetail;
    @BindView(R.id.btnDeleteDetail)
    Button btnDeleteDetail;

    @BindView(R.id.cvAdmin)
    CardView cvAdmin;

    ProgressDialog loading, loading1;

    SharedPrefManager sharedPrefManager;

    String mIdKritiksaran;
    String mKritiksaran;
    String mWaktu;

    String sp_username, sp_role;

    Context mContext;

    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritiksaran_detail);

        ButterKnife.bind(this);

        mContext = this;

        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(this);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        cvAdmin.setVisibility(View.GONE);

        Intent intent = getIntent();
        mIdKritiksaran = intent.getStringExtra(Constant.KEY_ID_KRITIKSARAN);

        btnEditDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, KritiksaranEditActivity.class);
                intent.putExtra(Constant.KEY_ID_KRITIKSARAN, mIdKritiksaran);
                startActivity(intent);
            }
        });

        btnDeleteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteKritiksaran();
            }
        });

        getKritiksaranDetail(mIdKritiksaran);

    }

    private void getKritiksaranDetail(String id_kritiksaran) {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getKritiksaranDetail(id_kritiksaran)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    mKritiksaran = jsonRESULTS.getJSONObject("kritiksaran").getString("kritiksaran");
                                    mWaktu = jsonRESULTS.getJSONObject("kritiksaran").getString("waktu");

                                    tvKritiksaran.setText(mKritiksaran);
                                    tvWaktu.setText("Waktu Pengajuan : " + mWaktu);
                                    tvUsername.setText("Username : " + sp_username);

                                } else {
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void confirmDeleteKritiksaran() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Delete Data Kritik dan Saran");
        alert.setMessage("Hapus Data Kritik dan Saran?");
        alert.setPositiveButton("Ya, Hapus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestDeleteKritiksaran();
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void requestDeleteKritiksaran() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.kritiksaranDeleteRequest(mIdKritiksaran).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    Toast.makeText(mContext, "Berhasil menghapus data kritik dan saran", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, KritiksaranActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal menghapus data kritik dan saran", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(KritiksaranDetailActivity.this, KritiksaranActivity.class);
        startActivity(intent);
    }
}
