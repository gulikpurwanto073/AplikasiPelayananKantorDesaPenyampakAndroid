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
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
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

public class PembuatanEditActivity extends AppCompatActivity {

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

    @BindView(R.id.btnEditPembuatan) Button btnEditPembuatan;
    @BindView(R.id.btnKembaliEdit) Button btnKembaliEdit;

    ProgressDialog loading, loading1, loading2, loading3;

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
        setContentView(R.layout.activity_pembuatan_edit);

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

        Intent intent = getIntent();
        mIdPembuatan = intent.getStringExtra(Constant.KEY_ID_PEMBUATAN);

        btnEditPembuatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdatePembuatan();
            }
        });

        btnKembaliEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PembuatanDetailActivity.class);
                intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
                startActivity(intent);
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

        getPembuatanDetail();
    }

    private void requestUpdatePembuatan() {
        loading2 = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        mApiService.pembuatanUpdateRequest(
                mIdPembuatan,
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
                            loading2.dismiss();
                            Toast.makeText(mContext, "Data Berhasil Diedit", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, PembuatanDetailActivity.class);
                            intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
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

    private void getPembuatanDetail() {
        loading3 = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getPembuatanDetail(mIdPembuatan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading3.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    mNama = jsonRESULTS.getJSONObject("pembuatan").getString("nama");
                                    mTempatLahir = jsonRESULTS.getJSONObject("pembuatan").getString("tempat_lahir");
                                    mTanggalLahir = jsonRESULTS.getJSONObject("pembuatan").getString("tanggal_lahir");
                                    mJenisKelamin = jsonRESULTS.getJSONObject("pembuatan").getString("jenis_kelamin");
                                    mAlamat = jsonRESULTS.getJSONObject("pembuatan").getString("alamat");
                                    mRtrw = jsonRESULTS.getJSONObject("pembuatan").getString("rtrw");
                                    mKeldesa = jsonRESULTS.getJSONObject("pembuatan").getString("keldesa");
                                    mKecamatan = jsonRESULTS.getJSONObject("pembuatan").getString("kecamatan");
                                    mAgama = jsonRESULTS.getJSONObject("pembuatan").getString("agama");
                                    mStatusPekerjaan = jsonRESULTS.getJSONObject("pembuatan").getString("status_pekerjaan");
                                    mStatusPernikahan = jsonRESULTS.getJSONObject("pembuatan").getString("status_pernikahan");
                                    mKewarganegaraan = jsonRESULTS.getJSONObject("pembuatan").getString("kewarganegaraan");
                                    mBerlakuHingga = jsonRESULTS.getJSONObject("pembuatan").getString("berlaku_hingga");
                                    mNik = jsonRESULTS.getJSONObject("pembuatan").getString("nik");
                                    mWaktu = jsonRESULTS.getJSONObject("pembuatan").getString("waktu");
                                    mStatusPembuatan = jsonRESULTS.getJSONObject("pembuatan").getString("status_pembuatan");
                                    mCatatan = jsonRESULTS.getJSONObject("pembuatan").getString("catatan");

                                    etNama.setText(mNama);
                                    etTempatLahir.setText(mTempatLahir);
                                    etTanggalLahir.setText(mTanggalLahir);
                                    spJenisKelamin.setSelection(((ArrayAdapter<String>)spJenisKelamin.getAdapter()).getPosition(mJenisKelamin));
                                    etAlamat.setText(mAlamat);
                                    etRtrw.setText(mRtrw);
                                    etKeldesa.setText(mKeldesa);
                                    etKecamatan.setText(mKecamatan);
                                    spAgama.setSelection(((ArrayAdapter<String>)spAgama.getAdapter()).getPosition(mAgama));
                                    etStatusPekerjaan.setText(mStatusPekerjaan);
                                    spStatusPernikahan.setSelection(((ArrayAdapter<String>)spStatusPernikahan.getAdapter()).getPosition(mStatusPernikahan));
                                    spKewarganegaraan.setSelection(((ArrayAdapter<String>)spKewarganegaraan.getAdapter()).getPosition(mKewarganegaraan));
                                    etBerlakuHingga.setText(mBerlakuHingga);
                                    etNik.setText(mNik);

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
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, PembuatanDetailActivity.class);
        intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
        startActivity(intent);
    }

}
