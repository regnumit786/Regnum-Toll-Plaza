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
import com.sepon.regnumtollplaza.admin.Report;
import com.sepon.regnumtollplaza.pojo.Tali;

import java.util.List;


public class AxelAdapter extends RecyclerView.Adapter<AxelAdapter.ToadyViewHolder> {

    private List<Report> axellist;
    Context context;

    public AxelAdapter(List<Report> axellist, Context context) {
        this.axellist = axellist;
        this.context = context;
    }


//    public TodayAdapter() {
//    }

    @NonNull
    @Override
    public AxelAdapter.ToadyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carview, viewGroup, false);
        ToadyViewHolder toadyViewHolder = new ToadyViewHolder(view);



        return toadyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AxelAdapter.ToadyViewHolder holder, int position) {

            holder.lane.setText(axellist.get(position).getLane());
            holder.totalwise.setText(axellist.get(position).getDateTime());
            holder.vichelNumber.setText(axellist.get(position).getVehicleNumber());
            holder.tr.setText(axellist.get(position).getTransactionNumber());

    }

    @Override
    public int getItemCount() {
        return axellist.size();
    }

    public class ToadyViewHolder extends RecyclerView.ViewHolder {

        TextView tr, totalwise, vichelNumber,lane;

        public ToadyViewHolder(@NonNull View itemView) {
            super(itemView);

            tr = itemView.findViewById(R.id.transaction);
            totalwise = itemView.findViewById(R.id.totalWeight);
            vichelNumber = itemView.findViewById(R.id.vehicleNumber);
            lane = itemView.findViewById(R.id.lane);


        }
    }
}
