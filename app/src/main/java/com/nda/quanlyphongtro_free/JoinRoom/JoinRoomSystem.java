package com.nda.quanlyphongtro_free.JoinRoom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.JoinRoom;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JoinRoomSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ShimmerFrameLayout shimmer_view_container;

    ImageView imgBack;
   
    androidx.appcompat.widget.SearchView searchView_searchJoinedRoom;


    ProgressDialog progressDialog;

    RecyclerView rcv_joinedRooms;
    List<JoinRoom> joinRoomList = new ArrayList<>();
    AdapterJoinRoom adapterJoinRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_system);

        initUI();

        setUpRCV();
        init();
    }

    private void setUpRCV() {
        adapterJoinRoom = new AdapterJoinRoom(JoinRoomSystem.this, joinRoomList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_joinedRooms.setLayoutManager(linearLayoutManager);
        rcv_joinedRooms.setAdapter(adapterJoinRoom);

        displayJoinRoom();
    }


    private void init() {
        progressDialog.setMessage("Đang truy vấn !");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });
    }

    private void displayJoinRoom() {
        joinRoomList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    JoinRoom joinRoom = dataSnapshot.getValue(JoinRoom.class); // Snap Key here is City
                    joinRoomList.add(joinRoom);
                }
                adapterJoinRoom.notifyDataSetChanged();

                // When get data successfully, hide the shimmer and show all function field
                searchView_searchJoinedRoom.setVisibility(View.VISIBLE);
                rcv_joinedRooms.setVisibility(View.VISIBLE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("joinRooms");
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    public void getInformationOfJoinedRoom(JoinRoom joinRoom, TextView txt_joinRoomName,
                                                 TextView txt_joinRoomFloor, TextView txt_joinRoomFee)
    {
        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Rooms rooms = snapshot.getValue(Rooms.class);

                    txt_joinRoomName.setText(rooms.getrName());
                    txt_joinRoomFloor.setText("Tầng : " + rooms.getrFloorNumber());

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    Double cost = Double.parseDouble(rooms.getrPrice());
                    txt_joinRoomFee.setText("Giá Phòng : " + formatter.format(cost));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(joinRoom.getHouseId())
                    .child(joinRoom.getRoomId());
            query.addListenerForSingleValueEvent(valueEventListener);
        }catch (Exception e)
        {
            Toast.makeText(this, "Error : Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            backToMain();
        }



    }


    private void initUI() {
        progressDialog = new ProgressDialog(JoinRoomSystem.this);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        imgBack         =  findViewById(R.id.imgBack);

        rcv_joinedRooms = findViewById(R.id.rcv_joinedRooms);

        searchView_searchJoinedRoom = findViewById(R.id.searchView_searchJoinedRoom);
    }


    private void backToMain() {
        startActivity(new Intent(JoinRoomSystem.this, MainActivity.class));
        JoinRoomSystem.this.finish();

    }
    @Override
    public void onBackPressed() {
        backToMain();

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
//        searchView_searchJoinedRoom.setVisibility(View.GONE);
//        rcv_joinedRooms.setVisibility(View.GONE);
//        img_joinRoom.setVisibility(View.GONE);
//
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        shimmer_view_container.startShimmerAnimation();

        //displayServices();

        super.onStart();

    }


}