package com.sona.ktp.activity.kritiksaran;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sona.ktp.R;
import com.sona.ktp.activity.kritiksaran.KritiksaranDetailActivity;
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KritiksaranEditActivity extends AppCompatActivity {

    @BindView(R.id.etKritiksaran) EditText etKritiksaran;

    @BindView(R.id.btnEditKritiksaran) Button btnEditKritiksaran;
    @BindView(R.id.btnKembaliEdit) Button btnKembaliEdit;

    ProgressDialog loading, loading1, loading2, loading3;

    Context mContext;

    SharedPrefManager sharedPrefManager;

    String sp_username;

    String mIdKritiksaran;
    String mKritiksaran;
    String mWaktu;

    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritiksaran_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        mContext = this;
        ButterKnife.bind(this);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        sp_username = sharedPrefManager.getSPEmail();

        Intent intent = getIntent();
        mIdKritiksaran = intent.getStringExtra(Constant.KEY_ID_KRITIKSARAN);

        btnEditKritiksaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdateKritiksaran();
            }
        });

        btnKembaliEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, KritiksaranDetailActivity.class);
                intent.putExtra(Constant.KEY_ID_KRITIKSARAN, mIdKritiksaran);
                startActivity(intent);
            }
        });

        getKritiksaranDetail();
    }

    private void requestUpdateKritiksaran() {
        loading2 = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        mApiService.kritiksaranUpdateRequest(
                mIdKritiksaran,
                etKritiksaran.getText().toString(),
                sp_username
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading2.dismiss();
                            Toast.makeText(mContext, "Data Berhasil Diedit", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, KritiksaranDetailActivity.class);
                            intent.putExtra(Constant.KEY_ID_KRITIKSARAN, mIdKritiksaran);
                            startActivity(intent);
                        } else {
                            loading2.dismiss();
                            Toast.makeText(mContext, "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading2.dismiss();
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getKritiksaranDetail() {
        loading3 = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getKritiksaranDetail(mIdKritiksaran)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading3.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    mKritiksaran = jsonRESULTS.getJSONObject("kritiksaran").getString("kritiksaran");
                                    mWaktu = jsonRESULTS.getJSONObject("kritiksaran").getString("waktu");

                                    etKritiksaran.setText(mIdKritiksaran);

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
                            loading3.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading3.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, KritiksaranDetailActivity.class);
        intent.putExtra(Constant.KEY_ID_KRITIKSARAN, mIdKritiksaran);
        startActivity(intent);
    }

}