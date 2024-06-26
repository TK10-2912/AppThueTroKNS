package com.nda.quanlyphongtro_free.Houses.HouseDetail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.R;
import com.nda.quanlyphongtro_free.RoomUserDetailActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.HolderRooms> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    Context context;
    List<Rooms> roomsList;
    Houses houses;
    Boolean isMember;
    public AdapterRoom(Context context, List<Rooms> roomsList, Houses houses,Boolean isMember) {
        this.roomsList = roomsList;
        this.houses = houses;
        this.context = context;
        this.isMember = isMember;
    }

    @NonNull
    @Override
    public HolderRooms onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_rooms, parent, false);
        return new AdapterRoom.HolderRooms(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRooms holder, int position) {
        Rooms rooms = roomsList.get(position);

        holder.txt_roomName.setText(rooms.getrName());
        holder.txt_roomMembers.setText("Số người : 0/" + rooms.getrLimitTenants());
        holder.txt_roomFloor.setText("Tầng : " + rooms.getrFloorNumber());


        countTenants(rooms, holder.txt_roomMembers);
        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(rooms.getrPrice());
        holder.txt_roomFee.setText("Giá Phòng : " + formatter.format(cost) + " đ");

        holder.cv_roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMember == false){
                    Intent intent = new Intent(context, RoomDetailSystem.class);

                    intent.putExtra("Data_Room_Parcelable", rooms);
                    intent.putExtra("Data_RoomOfHouse_Parcelable", houses);

                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, RoomUserDetailActivity.class);

                    intent.putExtra("Data_Room_Parcelable", rooms);
                    intent.putExtra("Data_RoomOfHouse_Parcelable", houses);

                    context.startActivity(intent);
                }
            }
        });
    }
    public void countTenants(Rooms rooms, TextView txtShowNumTenantsWithLimit)
    {
        List<Tenants> tenantsList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Tenants tenants = dataSnapshot.getValue(Tenants.class);
                    tenantsList.add(tenants);
                }
                if (Integer.parseInt(rooms.getrLimitTenants()) < tenantsList.size())
                {
                    txtShowNumTenantsWithLimit.setText("Số người : " + tenantsList.size() +
                            "/" + rooms.getrLimitTenants() + " (Số lượng vượt giới hạn) ");
                }
                else {
                    txtShowNumTenantsWithLimit.setText("Số người : " + tenantsList.size() + "/" + rooms.getrLimitTenants());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("tenants").orderByChild("rentRoomId").equalTo(rooms.getId());
        query.addListenerForSingleValueEvent(valueEventListener);


    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public class HolderRooms extends RecyclerView.ViewHolder {
        CardView cv_roomItem;
        TextView txt_roomName, txt_roomMembers, txt_roomFloor, txt_roomFee;
        public HolderRooms(@NonNull View itemView) {
            super(itemView);

            txt_roomName       = itemView.findViewById(R.id.txt_roomName);
            txt_roomMembers    = itemView.findViewById(R.id.txt_roomMembers);
            txt_roomFloor      = itemView.findViewById(R.id.txt_roomFloor);
            txt_roomFee        = itemView.findViewById(R.id.txt_roomFee);

            cv_roomItem        = itemView.findViewById(R.id.cv_roomItem);

        }
    }
}
