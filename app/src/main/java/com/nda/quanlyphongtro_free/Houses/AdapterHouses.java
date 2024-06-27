package com.nda.quanlyphongtro_free.Houses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Houses.HouseDetail.HouseDetailSystem;
import com.nda.quanlyphongtro_free.MainDetailHouseUserActivity;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHouses extends RecyclerView.Adapter<AdapterHouses.HouseHolder> {

    Context context;
    List<Houses> housesList;
    Boolean isMember;

    public AdapterHouses(Context context, List<Houses> housesList,Boolean isMember) {
        this.context = context;
        this.housesList = housesList;
        this.isMember = isMember;
    }
    @Override
    public HouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_house, parent , false);
        return new AdapterHouses.HouseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseHolder holder, int position) {
        Houses houses = housesList.get(position);

        holder.txt_houseName.setText(houses.gethName());
        holder.txt_houseAddress.setText(houses.gethAddress());
        holder.txt_houseCity.setText(houses.gethTinhThanhPho());
        String imgUrl= null;
        List<String> imageUrlList = houses.gethImageUrlList();

        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            imgUrl = imageUrlList.get(0);
        }
        Picasso.get().load(imgUrl).into(holder.imgHouse);
        holder.cv_houseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMember == false){
                    Intent intent = new Intent(context, HouseDetailSystem.class);
                    intent.putExtra("Data_House_Parcelable", houses);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, MainDetailHouseUserActivity.class);
                    intent.putExtra("Data_House_Parcelable", houses);
                    context.startActivity(intent);
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return housesList.size();
    }

    public class HouseHolder extends RecyclerView.ViewHolder {
        TextView txt_houseName, txt_houseAddress, txt_houseCity;
        ImageView imgHouse;
        CardView cv_houseItem;


        public HouseHolder(@NonNull View itemView) {
            super(itemView);
            txt_houseName        = itemView.findViewById(R.id.txt_houseName);
            txt_houseAddress     = itemView.findViewById(R.id.txt_houseAddress);
            txt_houseCity        = itemView.findViewById(R.id.txt_houseCity);
            imgHouse        = itemView.findViewById(R.id.imgHouse);

            cv_houseItem    = itemView.findViewById(R.id.cv_houseItem);
        }
    }
}
