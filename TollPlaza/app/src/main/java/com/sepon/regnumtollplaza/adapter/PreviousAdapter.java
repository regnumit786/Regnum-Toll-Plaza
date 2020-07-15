package com.sepon.regnumtollplaza.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.PreviousDetails;

import java.util.List;

public class PreviousAdapter extends RecyclerView.Adapter<PreviousAdapter.PlazaViewHolder> {

    private List<PreviousDetails> pewviousList;
    Context context;

    public PreviousAdapter(List<PreviousDetails> plazaList, Context context) {
        this.pewviousList = plazaList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.previous_details, viewGroup, false);
        PlazaViewHolder plazaViewHolder = new PlazaViewHolder(view);
        return plazaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlazaViewHolder holder, final int position) {

        holder.date.setText(pewviousList.get(position).getDate());
        holder.taka.setText(pewviousList.get(position).getDayTotalAmount());
        holder.amount.setText(pewviousList.get(position).getVichelAmount()+" Car");

    }

    @Override
    public int getItemCount() {
        return pewviousList.size();
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {

        TextView date,amount,taka;


        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.vichelAmount);
            taka = itemView.findViewById(R.id.vichelTaka);

        }
    }
}
