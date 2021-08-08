package com.sona.ktp.activity.kritiksaran;

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
import com.sona.ktp.activity.kritiksaran.KritiksaranAdapter;
import com.sona.ktp.activity.kritiksaran.KritiksaranAddActivity;
import com.sona.ktp.activity.kritiksaran.KritiksaranDetailActivity;
import com.sona.ktp.activity.kritiksaran.ResponseKritiksaran;
import com.sona.ktp.activity.kritiksaran.SemuakritiksaranItem;
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

public class KritiksaranActivity extends AppCompatActivity {

    @BindView(R.id.rvKritiksaran)
    RecyclerView rvKritiksaran;

    @BindView(R.id.swiperefreshkritiksaran)
    SwipeRefreshLayout swiperefreshkritiksaran;

    @BindView(R.id.btnTambahKritikSaran)
    Button btnTambahKritikSaran;

    ProgressDialog loading;

    String sp_username, sp_role;

    Context mContext;
    List<SemuakritiksaranItem> semuakritiksaranItemList = new ArrayList<>();
    KritiksaranAdapter kritiksaranAdapter;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritiksaran);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        sharedPrefManager = new SharedPrefManager(mContext);
        sp_username = sharedPrefManager.getSPEmail();
        sp_role = sharedPrefManager.getSPRole();

        kritiksaranAdapter = new KritiksaranAdapter(this, semuakritiksaranItemList);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        rvKritiksaran.setLayoutManager(mLinearLayoutManager);
        rvKritiksaran.setItemAnimator(new DefaultItemAnimator());

        getResultListKritiksaran();

        swiperefreshkritiksaran.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResultListKritiksaran();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swiperefreshkritiksaran.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        swiperefreshkritiksaran.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        btnTambahKritikSaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, KritiksaranAddActivity.class));
            }
        });

    }

    private void getResultListKritiksaran(){
        loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);

        mApiService.getSemuaKritiksaran(sp_username).enqueue(new Callback<ResponseKritiksaran>() {
            @Override
            public void onResponse(Call<ResponseKritiksaran> call, Response<ResponseKritiksaran> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    final List<SemuakritiksaranItem> semuaKritiksaranItems = response.body().getSemuakritiksaran();

                    rvKritiksaran.setAdapter(new KritiksaranAdapter(mContext, semuaKritiksaranItems));
                    kritiksaranAdapter.notifyDataSetChanged();

                    initDataIntent(semuaKritiksaranItems);
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data kritiksaran", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseKritiksaran> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDataIntent(final List<SemuakritiksaranItem> kritiksaranList){
        rvKritiksaran.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String id_kritiksaran = kritiksaranList.get(position).getId_kritiksaran();
                        Intent detailKritiksaran = new Intent(mContext, KritiksaranDetailActivity.class);
                        detailKritiksaran.putExtra(Constant.KEY_ID_KRITIKSARAN, id_kritiksaran);
                        startActivity(detailKritiksaran);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}
