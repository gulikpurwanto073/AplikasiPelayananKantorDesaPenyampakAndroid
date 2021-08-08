package com.sona.ktp.activity.pembuatan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sona.ktp.R;
import com.sona.ktp.activity.jenis.JenisAdapter;
import com.sona.ktp.activity.jenis.ResponseJenis;
import com.sona.ktp.activity.jenis.SemuajenisItem;
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.RecyclerItemClickListener;
import com.sona.ktp.util.ResponseImage;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.MultipartApiService;
import com.sona.ktp.util.api.UtilsApi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class JenisDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvJenis)
    TextView tvJenis;
    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.ivFile)
    ImageView ivFile;

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

    @BindView(R.id.btnUpload)
    Button btnUpload;

    @BindView(R.id.btnKembali)
    Button btnKembali;

    @BindView(R.id.cvAdmin)
    CardView cvAdmin;

    @BindView(R.id.cvAdmin1)
    CardView cvAdmin1;

    ProgressDialog loading, loading1;

    SharedPrefManager sharedPrefManager;

    String mIdJenis;
    String mJenis;
    String mFile;

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

    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
		}

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}

        setContentView(R.layout.activity_jenis_detail);

        ButterKnife.bind(this);

        mContext = this;

        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(this);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        cvAdmin.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        mIdPembuatan = intent.getStringExtra(Constant.KEY_ID_PEMBUATAN);
        mIdJenis = intent.getStringExtra(Constant.KEY_ID_JENIS);
        mJenis = intent.getStringExtra(Constant.KEY_JENIS);

        tvJenis.setText(mJenis);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PembuatanDetailActivity.class);
                intent.putExtra(Constant.KEY_ID_PEMBUATAN, mIdPembuatan);
                startActivity(intent);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        getPembuatanDetail(mIdPembuatan);
        getLampiranDetail(mIdPembuatan, mIdJenis);

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
        Call<ResponseImage> call = multipartApiService.uploadFile(mIdPembuatan, mIdJenis, requestFile);
        call.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                if (!response.body().error) {
                    Toast.makeText(getApplicationContext(), "File sukses di upload...", Toast.LENGTH_LONG).show();
                    getPembuatanDetail(mIdPembuatan);
                    getLampiranDetail(mIdPembuatan, mIdJenis);
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
                                        btnUpload.setVisibility(View.GONE);
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

    private void getLampiranDetail(String id_pembuatan, String id_jenis) {
        loading1 = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
        mApiService.getLampiranDetail(id_pembuatan, id_jenis)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading1.dismiss();
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    mFile = jsonRESULTS.getJSONObject("lampiran").getString("file");

                                    tvFile.setText("Nama File : " + mFile);
                                    Picasso.get().load(UtilsApi.BASE_URL_FILE_LAMPIRAN + mFile).fit().centerCrop().into(ivFile);

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
                        loading1.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(JenisDetailActivity.this, PembuatanActivity.class);
        startActivity(intent);
    }
}
