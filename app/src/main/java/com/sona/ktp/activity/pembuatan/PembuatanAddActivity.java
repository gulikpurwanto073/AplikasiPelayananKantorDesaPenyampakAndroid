package com.sona.ktp.activity.pembuatan;

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
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembuatanAddActivity extends AppCompatActivity {

    @BindView(R.id.etNama) EditText etNama;
    @BindView(R.id.etTempatLahir) EditText etTempatLahir;
    @BindView(R.id.etTanggalLahir) EditText etTanggalLahir;
    @BindView(R.id.spJenisKelamin) Spinner spJenisKelamin;
    @BindView(R.id.etAlamat) EditText etAlamat;
    @BindView(R.id.etRtrw) EditText etRtrw;
    @BindView(R.id.etKeldesa) EditText etKeldesa;
    @BindView(R.id.etKecamatan) EditText etKecamatan;
    @BindView(R.id.spAgama) Spinner spAgama;
    @BindView(R.id.etStatusPekerjaan) EditText etStatusPekerjaan;
    @BindView(R.id.spStatusPernikahan) Spinner spStatusPernikahan;
    @BindView(R.id.spKewarganegaraan) Spinner spKewarganegaraan;
    @BindView(R.id.etBerlakuHingga) EditText etBerlakuHingga;
    @BindView(R.id.etNik) EditText etNik;

    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.btnKembali) Button btnKembali;

    ProgressDialog loading;

    Context mContext;

    SharedPrefManager sharedPrefManager;

    String sp_username, spinner_jenis_kelamin, spinner_agama, spinner_status_pernikahan, spinner_kewarganegaraan;

    String mIdPembuatan;
    String mNama;
    String mTempatLahir;
    String mTanggalLahir;
    String mJenisKelamin;
    String mAlamat;
    String mRtrw;
    String mKeldesa;
    String mKecamatan;
    String mAgama;
    String mStatusPekerjaan;
    String mStatusPernikahan;
    String mKewarganegaraan;
    String mBerlakuHingga;
    String mNik;
    String mWaktu;
    String mStatusPembuatan;
    String mCatatan;

    BaseApiService mApiService;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembuatan_add);

        mContext = this;

        ButterKnife.bind(this);

        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(mContext);
        sp_username = sharedPrefManager.getSPEmail();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        spAgama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_agama = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spJenisKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_jenis_kelamin = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spKewarganegaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_kewarganegaraan = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStatusPernikahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_status_pernikahan = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("tanggal_lahir");
            }
        });

        etBerlakuHingga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("berlaku_hingga");
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestInsertPembuatan();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PembuatanActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showDateDialog(String tanggal) {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if (tanggal == "tanggal_lahir") {
                    etTanggalLahir.setText(dateFormatter.format(newDate.getTime()));
                } else {
                    etBerlakuHingga.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void requestInsertPembuatan() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        mApiService.pembuatanInsertRequest(
                etNama.getText().toString(),
                etTempatLahir.getText().toString(),
                etTanggalLahir.getText().toString(),
                spinner_jenis_kelamin,
                etAlamat.getText().toString(),
                etRtrw.getText().toString(),
                etKeldesa.getText().toString(),
                etKecamatan.getText().toString(),
                spinner_agama,
                etStatusPekerjaan.getText().toString(),
                spinner_status_pernikahan,
                spinner_kewarganegaraan,
                etBerlakuHingga.getText().toString(),
                etNik.getText().toString(),
                sp_username
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            Toast.makeText(mContext, "Data Berhasil Ditambah", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, PembuatanActivity.class);
                            startActivity(intent);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, PembuatanActivity.class);
        startActivity(intent);
    }

}
