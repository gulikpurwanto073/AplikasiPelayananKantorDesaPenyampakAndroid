package com.sona.ktp.activity.jenis;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sona.ktp.R;
import com.sona.ktp.activity.jenis.SemuajenisItem;
import com.sona.ktp.util.SharedPrefManager;
import com.sona.ktp.util.api.BaseApiService;
import com.sona.ktp.util.api.UtilsApi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JenisAdapter extends RecyclerView.Adapter<JenisAdapter.BarangHolder>{

    Context mContext;
    List<SemuajenisItem> semuajenisItemList;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    public JenisAdapter(Context context, List<SemuajenisItem> jenisList){
        this.mContext = context;
        semuajenisItemList = jenisList;
    }

    @Override
    public BarangHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jenis, parent, false);
        return new BarangHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BarangHolder holder, int position) {
        final SemuajenisItem semuajenisitem = semuajenisItemList.get(position);
        sharedPrefManager = new SharedPrefManager(mContext);
        mApiService = UtilsApi.getAPIService();
        mApiService.getLampiranDetail(sharedPrefManager.getSpIdpembuatan(), semuajenisitem.getId_jenis())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    String file = jsonRESULTS.getJSONObject("lampiran").getString("file");
                                    if (file=="null" || file=="" || file.matches("")) {
                                        //holder.ivCheck.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.ivCheck.setVisibility(View.VISIBLE);
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
                    }
                });

        holder.tvIdJenis.setText(semuajenisitem.getId_jenis());
        holder.tvJenis.setText(semuajenisitem.getJenis());
        holder.tvTipe.setText(semuajenisitem.getTipe());
    }

    @Override
    public int getItemCount() {
        int itemsize = 0;
        return itemsize = (semuajenisItemList == null) ? 0 : semuajenisItemList.size();
    }

    public class BarangHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvIdJenis) TextView tvIdJenis;
        @BindView(R.id.tvJenis) TextView tvJenis;
        @BindView(R.id.tvTipe) TextView tvTipe;
        @BindView(R.id.ivCheck) ImageView ivCheck;

        public BarangHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
