package com.sona.ktp.activity.pembuatan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sona.ktp.R;
import com.sona.ktp.activity.pembuatan.SemuapembuatanItem;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PembuatanAdapter extends RecyclerView.Adapter<PembuatanAdapter.PembuatanHolder>{

    List<SemuapembuatanItem> semuapembuatanItemList;
    Context mContext;

    public PembuatanAdapter(Context context, List<SemuapembuatanItem> pembuatanList){
        this.mContext = context;
        semuapembuatanItemList = pembuatanList;
    }

    @Override
    public PembuatanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pembuatan, parent, false);
        return new PembuatanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PembuatanHolder holder, int position) {
        final SemuapembuatanItem semuapembuatanitem = semuapembuatanItemList.get(position);

//        String date = "2021-06-06";
//        String date1 = "2021-06-06";
//        String hari = "";
//        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy-MM-dd");
//        Date newDate= null;
//        Date newDate1= null;
//
//        try {
//            newDate = spf.parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            newDate1 = spf1.parse(date1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        spf = new SimpleDateFormat("dd MMM yyyy");
//        date = spf.format(newDate);
//
//        spf1 = new SimpleDateFormat("E");
//        date1 = spf1.format(newDate1);

//        if (date1.matches("Sun")) {
//            hari="Minggu";
//        } else if (date1.matches("Mon")) {
//            hari="Senin";
//        } else if (date1.matches("Tue")) {
//            hari="Selasa";
//        } else if (date1.matches("Wed")) {
//            hari="Rabu";
//        } else if (date1.matches("Thu")) {
//            hari="Kamis";
//        } else if (date1.matches("Fri")) {
//            hari="Jumat";
//        } else if (date1.matches("Sat")) {
//            hari="Sabtu";
//        }

        holder.tvNik.setText(semuapembuatanitem.getNik());
        holder.tvNama.setText(semuapembuatanitem.getNama());
        holder.tvStatusPembuatan.setText(semuapembuatanitem.getStatus_pembuatan());
        holder.tvWaktu.setText(semuapembuatanitem.getWaktu());
    }

    @Override
    public int getItemCount() {
        int itemsize = 0;
        return itemsize = (semuapembuatanItemList == null) ? 0 : semuapembuatanItemList.size();
    }

    public class PembuatanHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvNik)
        TextView tvNik;
        @BindView(R.id.tvNama)
        TextView tvNama;
        @BindView(R.id.tvStatusPembuatan)
        TextView tvStatusPembuatan;
        @BindView(R.id.tvWaktu)
        TextView tvWaktu;

        public PembuatanHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
