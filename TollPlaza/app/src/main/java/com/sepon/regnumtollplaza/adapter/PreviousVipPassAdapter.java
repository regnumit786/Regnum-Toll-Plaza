package com.sepon.regnumtollplaza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sepon.regnumtollplaza.R;

public class PreviousVipPassAdapter extends RecyclerView.Adapter<PreviousVipPassAdapter.VipHolder> {
    String date, value;
    Context context;

    public PreviousVipPassAdapter(Context context, String date, String value) {
        this.context = context;
        this.date = date;
        this.value = value;
    }

    @NonNull
    @Override
    public VipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_vip_pass_item, parent, false);
        return new VipHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VipHolder holder, int position) {
        holder.txtDate.setText(date);
        holder.txtDate.setText(value);
    }

    @Override
    public int getItemCount() {
        return date.length();
    }

    public static class VipHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtValue;
        public VipHolder(@NonNull View itemView) {
            super(itemView);
            txtDate= itemView.findViewById(R.id.txt_vip_pass_recycler_item_date);
            txtValue= itemView.findViewById(R.id.txt_vip_pass_recycler_item_value);
        }
    }
}
