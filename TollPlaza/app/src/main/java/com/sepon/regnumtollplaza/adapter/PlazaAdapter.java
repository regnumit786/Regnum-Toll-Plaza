package com.sepon.regnumtollplaza.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sepon.regnumtollplaza.ChittagongActivity;
import com.sepon.regnumtollplaza.Chittagong_Axel;
import com.sepon.regnumtollplaza.ChorshindduActivity;
import com.sepon.regnumtollplaza.ManikGong_Axel;
import com.sepon.regnumtollplaza.Plaza;
import com.sepon.regnumtollplaza.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<PlazaAdapter.PlazaViewHolder> {

    private int[] mImageView;
    private List<Plaza> plazaList;
    Context context;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public PlazaAdapter(int[] mImageView, List<Plaza> plazaList, Context context) {
        this.mImageView = mImageView;
        this.plazaList = plazaList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plazaview, viewGroup, false);
        return new PlazaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlazaViewHolder holder, final int position) {
        holder.plazaname.setText(plazaList.get(position).getName());
        holder.mPlazaimage.setImageResource(mImageView[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plaza = plazaList.get(position).getName();
                if (plaza.equals("Chittagong")) {
                    Intent intent = new Intent(context, ChittagongActivity.class);
                    intent.putExtra("plazaName", plaza);
                    context.startActivity(intent);

                } else if (plaza.equals("Manikganj")) {
                    Intent intent = new Intent(context, ManikGong_Axel.class);
                    intent.putExtra("plazaName", plaza);
                    context.startActivity(intent);

                } else if (plaza.equals("Charsindur")) {
                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();
                    String email = currentUser.getEmail();

                    if (email.equals("mamuntushi@gmail.com")) { //here block the user
                        Toast.makeText(context, "Sorry Chorshindu not Live!", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(context, ChorshindduActivity.class);
                        intent.putExtra("plazaName", plaza);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plazaList.size();
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {
        ImageView mPlazaimage;
        ImageView circleImageView;
        TextView plazaname;
        CardView view;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlazaimage = itemView.findViewById(R.id.plazaimage);
            plazaname = itemView.findViewById(R.id.plazaname);
            view = itemView.findViewById(R.id.view);
        }
    }
}
