package com.sona.ktp.activity.pembuatan;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sona.ktp.R;
import com.sona.ktp.activity.jenis.JenisAdapter;
import com.sona.ktp.activity.jenis.ResponseJenis;
import com.sona.ktp.activity.jenis.SemuajenisItem;
import com.sona.ktp.activity.pembuatan.PembuatanActivity;
import com.sona.ktp.activity.pembuatan.PembuatanEditActivity;
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.RecyclerItemClickListener;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembuatanDetailActivity extends AppCompatActivity {

    @BindView(R.id.rvJenis)
    RecyclerView rvJenis;

    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvTempatLahir)
    TextView tvTempatLahir;
    @BindView(R.id.tvTanggalLahir)
    TextView tvTanggalLahir;
    @BindView(R.id.tvJenisKelamin)
    TextView tvJenisKelamin;
    @BindView(R.id.tvAlamat)
    TextView tvAlamat;
    @BindView(R.id.tvRtrw)
    TextView tvRtrw;
    @BindView(R.id.tvKeldesa)
    TextView tvKeldesa;
    @BindView(R.id.tvKecamatan)
    TextView tvKecamatan;
    @BindView(R.id.tvAgama)
    TextView tvAgama;
    @BindView(R.id.tvStatusPekerjaan)
    TextView tvStatusPekerjaan;
    @BindView(R.id.tvStatusPernikahan)
    TextView tvStatusPernikahan;
    @BindView(R.id.tvKewarganegaraan)
    TextView tvKewarganegaraan;
    @BindView(R.id.tvBerlakuHingga)
    TextView tvBerlakuHingga;
    @BindView(R.id.tvNik)
    TextView tvNik;
    @BindView(R.id.tvWaktu)
    TextView tvWaktu;
    @BindView(R.id.tvStatusPembuatan)
    TextView tvStatusPembuatan;
    @BindView(R.id.tvCatatan)
    TextView tvCatatan;

    @BindView(R.id.btnEditDetail)
    Button btnEditDetail;
    @BindView(R.id.btnDeleteDetail)
    Button btnDeleteDetail;
    @BindView(R.id.btnKirim)
    Button btnKirim;

    @BindView(R.id.cvAdmin)
    CardView cvAdmin;

//    @BindView(R.id.cvAdmin1)
//    CardView cvAdmin1;

    ProgressDialog loading, loading1;

    SharedPrefManager sharedPrefManager;

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

    String sp_username, sp_role;

    Context mContext;
    List<SemuajenisItem> semuajenisItemList = new ArrayList<>();
    JenisAdapter jenisAdapter;

    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembuatan_detail);

        ButterKnife.bind(this);

        mContext = this;

        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(this);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        jenisAdapter = new JenisAdapter(this, semuajenisItemList);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        rvJenis.setLayoutManager(mLinearLayoutManager);
        rvJenis.setItemAnimator(new DefaultItemAnimator());

        getResultListJenis();

        cvAdmin.setVisibility(View.VISIBLE);
//        cvAdmin1.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        mIdPembuatan = intent.getStringExtra(Constant.KEY_ID_PEMBUATAN);

        btnEditDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PembuatanEditActivity.class);
                intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
                startActivity(intent);
            }
        });

        btnDeleteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeletePembuatan();
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmKirimPembuatan();
            }
        });

        getPembuatanDetail(mIdPembuatan);

    }

    private void getPembuatanDetail(String id_pembuatan) {
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getPembuatanDetail(id_pembuatan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()){
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

                                    tvNama.setText("Nama : " + mNama);
                                    tvTempatLahir.setText("Tempat Lahir : " + mTempatLahir);
                                    tvTanggalLahir.setText("Tanggal Lahir : " + mTanggalLahir);
                                    tvJenisKelamin.setText("Jenis Kelamin : " + mJenisKelamin);
                                    tvAlamat.setText("Alamat : " + mAlamat);
                                    tvRtrw.setText("RT/RW : " + mRtrw);
                                    tvKeldesa.setText("Kel/Desa : " + mKeldesa);
                                    tvKecamatan.setText("Kecamatan : " + mKecamatan);
                                    tvAgama.setText("Agama : " + mAgama);
                                    tvStatusPekerjaan.setText("Status Pekerjaan : " + mStatusPekerjaan);
                                    tvStatusPernikahan.setText("Status Pernikahan : " + mStatusPernikahan);
                                    tvKewarganegaraan.setText("Kewarganegaraan : " + mKewarganegaraan);
                                    tvBerlakuHingga.setText("Berlaku Hingga : " + mBerlakuHingga);
                                    tvNik.setText("NIK : " + mNik);
                                    tvWaktu.setText("Waktu Pengajuan : " + mWaktu);
                                    tvStatusPembuatan.setText("Status Pembuatan : " + mStatusPembuatan);
                                    tvCatatan.setText("Catatan : " + mCatatan);

                                    if (mStatusPembuatan.matches("")) {
                                    } else {
                                        btnKirim.setText("Terkirim");
                                        btnKirim.setClickable(false);
                                        btnEditDetail.setVisibility(View.GONE);
                                        btnDeleteDetail.setVisibility(View.GONE);
                                    }

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

    private void confirmKirimPembuatan() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Kirim Data Pembuatan KTP");
        alert.setMessage("Kirim Data Pembuatan KTP? Data yang anda kirim tidak dapat di ubah lagi, pastikan anda sudah mengecek kelengkapan data");
        alert.setPositiveButton("Ya, Kirim Sekarang", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestKirimPembuatan();
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void requestKirimPembuatan() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.pembuatanKirimRequest(mIdPembuatan).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    Toast.makeText(mContext, "Berhasil mengirim data pembuatan KTP", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PembuatanDetailActivity.class);
                    intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
                    startActivity(intent);
                    finish();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengirim data pembuatan KTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDeletePembuatan() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Delete Data Pembuatan KTP");
        alert.setMessage("Hapus Data Pembuatan KTP?");
        alert.setPositiveButton("Ya, Hapus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestDeletePembuatan();
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void requestDeletePembuatan() {
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        mApiService.pembuatanDeleteRequest(mIdPembuatan).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    Toast.makeText(mContext, "Berhasil menghapus data pembuatan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, PembuatanActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal menghapus data pembuatan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getResultListJenis() {
        loading1 = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);

        mApiService.getSemuaJenis().enqueue(new Callback<ResponseJenis>() {
            @Override
            public void onResponse(Call<ResponseJenis> call, Response<ResponseJenis> response) {
                if (response.isSuccessful()){
                    loading1.dismiss();

                    final List<SemuajenisItem> semuaJenisItems = response.body().getSemuajenis();

                    rvJenis.setAdapter(new JenisAdapter(mContext, semuaJenisItems));
                    jenisAdapter.notifyDataSetChanged();

                    initDataIntent(semuaJenisItems);
                } else {
                    loading1.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data pembuatan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseJenis> call, Throwable t) {
                loading1.dismiss();
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDataIntent(final List<SemuajenisItem> jenisList){
        rvJenis.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String id_jenis = jenisList.get(position).getId_jenis();
                        String jenis = jenisList.get(position).getJenis();

                        Intent detailJenis = new Intent(mContext, JenisDetailActivity.class);
                        detailJenis.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
                        detailJenis.putExtra(Constant.KEY_ID_JENIS, id_jenis);
                        detailJenis.putExtra(Constant.KEY_JENIS, jenis);
                        startActivity(detailJenis);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PembuatanDetailActivity.this, PembuatanActivity.class);
        startActivity(intent);
    }
}
