package com.sepon.regnumtollplaza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.PreviousDetails;
import com.sepon.regnumtollplaza.pojo.Previous_pojo;

import java.util.List;

public class ChittagongPreviousAdapter extends RecyclerView.Adapter<ChittagongPreviousAdapter.PlazaViewHolder> {

    private List<Previous_pojo> pewviousList;
    Context context;

    public ChittagongPreviousAdapter(List<Previous_pojo> plazaList, Context context) {
        this.pewviousList = plazaList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chittagong_previous, viewGroup, false);
        PlazaViewHolder plazaViewHolder = new PlazaViewHolder(view);
        return plazaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlazaViewHolder holder, int position) {

            String total = pewviousList.get(position).getTotal();
            String ctrl = pewviousList.get(position).getCtrl();
            String regular = pewviousList.get(position).getRegular();
            String date = pewviousList.get(position).getDate();


            holder.total.setText("Total Vehicles: "+total);
            holder.ctrl.setText("Ctrl: "+ctrl);
            holder.regular.setText("Regular: "+regular);
            holder.date.setText(""+date);

    }

    @Override
    public int getItemCount() {
        return pewviousList.size();
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {

        TextView date,regular,ctrl,total;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.previous_date);
            regular = itemView.findViewById(R.id.previous_regular);
            ctrl = itemView.findViewById(R.id.previous_ctrl);
            total = itemView.findViewById(R.id.previous_total);
        }
    }
}
