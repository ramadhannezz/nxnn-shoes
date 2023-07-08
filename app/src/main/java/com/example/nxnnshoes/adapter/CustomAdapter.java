package com.example.nxnnshoes.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nxnnshoes.view.EditPesananActivity;
import com.example.nxnnshoes.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> no_id, nama_pemesan, tlp_pemesan, merek_sepatu, warna_sepatu, ukuran_sepatu;
    private ArrayList<String> imagePathList;
    private int position;

    public CustomAdapter(Activity activity, Context context, ArrayList<String> no_id, ArrayList<String> nama_pemesan,
                         ArrayList<String> tlp_pemesan, ArrayList<String> merek_sepatu, ArrayList<String> warna_sepatu,
                         ArrayList<String> ukuran_sepatu, ArrayList<String> imagePathList) {
        this.activity = activity;
        this.context = context;
        this.no_id = no_id;
        this.nama_pemesan = nama_pemesan;
        this.tlp_pemesan = tlp_pemesan;
        this.merek_sepatu = merek_sepatu;
        this.warna_sepatu = warna_sepatu;
        this.ukuran_sepatu = ukuran_sepatu;
        this.imagePathList = imagePathList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_list, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        this.position = position;
        holder.recycleNo.setText(String.valueOf(no_id.get(position)));
        holder.recycleNama.setText(String.valueOf(nama_pemesan.get(position)));
        holder.recycleTlp.setText(String.valueOf(tlp_pemesan.get(position)));
        holder.recycleMerk.setText(String.valueOf(merek_sepatu.get(position)));
        holder.recycleWarna.setText(String.valueOf(warna_sepatu.get(position)));
        holder.recycleUkuran.setText(String.valueOf(ukuran_sepatu.get(position)));

        String imagePath = imagePathList.get(position);
        if (imagePath != null && !imagePath.isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            // Load and display image using your preferred image loading library
            // For example, using Glide:
            Glide.with(context)
                    .load(imagePath)
                    .apply(RequestOptions.overrideOf(150, 150))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .transform(new RoundedCornersTransformation(
                            20,
                            5,
                            RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.imageView);

        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        // RecyclerView onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditPesananActivity.class);
                intent.putExtra("id", String.valueOf(no_id.get(position)));
                intent.putExtra("nama", String.valueOf(nama_pemesan.get(position)));
                intent.putExtra("tlp", String.valueOf(tlp_pemesan.get(position)));
                intent.putExtra("merek", String.valueOf(merek_sepatu.get(position)));
                intent.putExtra("warna", String.valueOf(warna_sepatu.get(position)));
                intent.putExtra("ukuran", String.valueOf(ukuran_sepatu.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return no_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView recycleNo, recycleNama, recycleTlp, recycleMerk, recycleWarna, recycleUkuran;
        LinearLayout mainLayout;
        ImageView imageView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recycleNo = itemView.findViewById(R.id.recycleNo);
            recycleNama = itemView.findViewById(R.id.recycleNama);
            recycleTlp = itemView.findViewById(R.id.recycleTlp);
            recycleMerk = itemView.findViewById(R.id.recycleMerk);
            recycleWarna = itemView.findViewById(R.id.recycleWarna);
            recycleUkuran = itemView.findViewById(R.id.recycleUkuran);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageView = itemView.findViewById(R.id.imageView);

            // Animate RecyclerView
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
