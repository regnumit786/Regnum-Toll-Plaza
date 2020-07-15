package com.sepon.regnumtollplaza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.Tali;

import java.util.List;


public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ToadyViewHolder> {

    private List<Tali> taliList;
    Context context;

    public TodayAdapter(List<Tali> taliList, Context context) {
        this.taliList = taliList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodayAdapter.ToadyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tali2, viewGroup, false);
        ToadyViewHolder toadyViewHolder = new ToadyViewHolder(view);

        return toadyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodayAdapter.ToadyViewHolder holder, int position) {

        String vichelClass = taliList.get(position).getTaliName();

        holder.vichelName.setText(vichelClass);
        holder.vichelTaka.setText(taliList.get(position).getTaliCountedTaka()+" Taka");
        holder.vichelcount.setText(taliList.get(position).getTaliCount());
        holder.vichelProfile.setImageResource(taliList.get(position).getImage());
        holder.rate.setText(taliList.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return taliList.size();
    }

    public class ToadyViewHolder extends RecyclerView.ViewHolder {

        ImageView vichelProfile;
        TextView vichelcount, vichelTaka, vichelName, rate;

        public ToadyViewHolder(@NonNull View itemView) {
            super(itemView);

            vichelProfile = itemView.findViewById(R.id.vichelProfile);
            vichelName = itemView.findViewById(R.id.vichelName);
            vichelcount = itemView.findViewById(R.id.vichelCount);
            vichelTaka = itemView.findViewById(R.id.vichel_tk);
            rate = itemView.findViewById(R.id.rate);

        }
    }
}
