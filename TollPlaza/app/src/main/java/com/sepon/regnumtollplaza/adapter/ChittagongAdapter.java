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


public class ChittagongAdapter extends RecyclerView.Adapter<ChittagongAdapter.ToadyViewHolder> {

    private List<Tali> taliList;
    Context context;

    public ChittagongAdapter(List<Tali> taliList, Context context) {
        this.taliList = taliList;
        this.context = context;
    }


//    public TodayAdapter() {
//    }

    @NonNull
    @Override
    public ChittagongAdapter.ToadyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tali2, viewGroup, false);
        ToadyViewHolder toadyViewHolder = new ToadyViewHolder(view);



        return toadyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChittagongAdapter.ToadyViewHolder holder, int position) {

        String vichelClass = taliList.get(position).getTaliName();

        holder.vichelName.setText(vichelClass);
        holder.vichelTaka.setText(taliList.get(position).getTaliCountedTaka()+" Taka");
        holder.vichelcount.setText(taliList.get(position).getTaliCount());

        if (vichelClass.equals("rickshaw")){
            holder.vichelProfile.setImageResource(R.drawable.rickshaw);
        }else if (vichelClass.equals("saden")){
            holder.vichelProfile.setImageResource(R.drawable.sedan);
        }else if (vichelClass.equals("minibus")){
            holder.vichelProfile.setImageResource(R.drawable.minibus);
        }else if (vichelClass.equals("microbus")){
            holder.vichelProfile.setImageResource(R.drawable.microbus);
        }else if (vichelClass.equals("cng")){
            holder.vichelProfile.setImageResource(R.drawable.cng);
        }else if (vichelClass.equals("bike")){
            holder.vichelProfile.setImageResource(R.drawable.bike);
        }else {
            holder.vichelProfile.setImageResource(R.drawable.tollview);
        }

    }

    @Override
    public int getItemCount() {
        return taliList.size();
    }

    public class ToadyViewHolder extends RecyclerView.ViewHolder {

        ImageView vichelProfile;
        TextView vichelcount, vichelTaka, vichelName;

        public ToadyViewHolder(@NonNull View itemView) {
            super(itemView);

            vichelProfile = itemView.findViewById(R.id.vichelProfile);
            vichelName = itemView.findViewById(R.id.vichelName);
            vichelcount = itemView.findViewById(R.id.vichelCount);
            vichelTaka = itemView.findViewById(R.id.vichel_tk);


        }
    }
}
