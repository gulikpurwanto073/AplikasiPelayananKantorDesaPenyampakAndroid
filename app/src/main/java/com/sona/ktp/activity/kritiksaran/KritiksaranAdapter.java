package com.sona.ktp.activity.kritiksaran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sona.ktp.R;
import com.sona.ktp.activity.kritiksaran.SemuakritiksaranItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KritiksaranAdapter extends RecyclerView.Adapter<KritiksaranAdapter.KritiksaranHolder>{

    List<SemuakritiksaranItem> semuakritiksaranItemList;
    Context mContext;

    public KritiksaranAdapter(Context context, List<SemuakritiksaranItem> kritiksaranList){
        this.mContext = context;
        semuakritiksaranItemList = kritiksaranList;
    }

    @Override
    public KritiksaranHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kritiksaran, parent, false);
        return new KritiksaranHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KritiksaranHolder holder, int position) {
        final SemuakritiksaranItem semuakritiksaranitem = semuakritiksaranItemList.get(position);

        holder.tvKritiksaran.setText(semuakritiksaranitem.getKritiksaran());
        holder.tvWaktu.setText(semuakritiksaranitem.getWaktu());
    }

    @Override
    public int getItemCount() {
        int itemsize = 0;
        return itemsize = (semuakritiksaranItemList == null) ? 0 : semuakritiksaranItemList.size();
    }

    public class KritiksaranHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvKritiksaran)
        TextView tvKritiksaran;
        @BindView(R.id.tvWaktu)
        TextView tvWaktu;

        public KritiksaranHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
