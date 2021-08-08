package com.sona.ktp.activity.pembuatan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sona.ktp.R;
import com.sona.ktp.activity.home.MainActivity;
import com.sona.ktp.activity.pembuatan.PembuatanAdapter;
import com.sona.ktp.activity.pembuatan.PembuatanAddActivity;
import com.sona.ktp.activity.pembuatan.PembuatanDetailActivity;
import com.sona.ktp.activity.pembuatan.ResponsePembuatan;
import com.sona.ktp.activity.pembuatan.SemuapembuatanItem;
import com.sona.ktp.util.Constant;
import com.sona.ktp.util.RecyclerItemClickListener;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembuatanActivity extends AppCompatActivity {

    @BindView(R.id.rvPembuatan)
    RecyclerView rvPembuatan;

    @BindView(R.id.swiperefreshpembuatan)
    SwipeRefreshLayout swiperefreshpembuatan;

    @BindView(R.id.btnTambahPembuatan)
    Button btnTambahPembuatan;

    ProgressDialog loading;

    String sp_username, sp_role;

    Context mContext;
    List<SemuapembuatanItem> semuapembuatanItemList = new ArrayList<>();
    PembuatanAdapter pembuatanAdapter;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembuatan);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(mContext);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        pembuatanAdapter = new PembuatanAdapter(this, semuapembuatanItemList);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        rvPembuatan.setLayoutManager(mLinearLayoutManager);
        rvPembuatan.setItemAnimator(new DefaultItemAnimator());

        getResultListPembuatan();

        swiperefreshpembuatan.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResultListPembuatan();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swiperefreshpembuatan.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        swiperefreshpembuatan.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        btnTambahPembuatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PembuatanAddActivity.class));
            }
        });

    }

    private void getResultListPembuatan(){
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);

        mApiService.getSemuaPembuatan(sp_username).enqueue(new Callback<ResponsePembuatan>() {
            @Override
            public void onResponse(Call<ResponsePembuatan> call, Response<ResponsePembuatan> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    final List<SemuapembuatanItem> semuaPembuatanItems = response.body().getSemuapembuatan();

                    rvPembuatan.setAdapter(new PembuatanAdapter(mContext, semuaPembuatanItems));
                    pembuatanAdapter.notifyDataSetChanged();

                    initDataIntent(semuaPembuatanItems);
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data pembuatan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePembuatan> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDataIntent(final List<SemuapembuatanItem> pembuatanList){
        rvPembuatan.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String id_pembuatan = pembuatanList.get(position).getId_pembuatan();
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_IDPEMBUATAN, id_pembuatan);

                        Intent detailPembuatan = new Intent(mContext, PembuatanDetailActivity.class);
                        detailPembuatan.putExtra(Constant.KEY_ID_PEMBUATAN, id_pembuatan);
                        startActivity(detailPembuatan);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}
