package com.nda.quanlyphongtro_free;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.AdapterHouses;
import com.nda.quanlyphongtro_free.Model.Houses;

import java.util.ArrayList;
import java.util.List;

public class MainHomeActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    androidx.appcompat.widget.SearchView searchView_houses;

    List<Houses> housesList = new ArrayList<>();
    RecyclerView rcv_houses;
    AdapterHouses adapterHouses;

    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        initUI();
        init();
        setUpRCV();
    }
    private void setUpRCV() {
        adapterHouses = new AdapterHouses(MainHomeActivity.this, housesList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rcv_houses.setLayoutManager(staggeredGridLayoutManager);
        rcv_houses.setAdapter(adapterHouses);
        displayHouses();
    }
    private void init() {
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HousesSystem.this, MainActivity.class));
//
//            }
//        });
//
//        imgAddHouses.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HousesSystem.this, AddHouse.class));
//
//            }
//        });
    }
    private void displayHouses() {
        housesList.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Houses houses = dataSnapshot.getValue(Houses.class); // Snap Key here is City
                    housesList.add(houses);
                }
                adapterHouses.notifyDataSetChanged();
                searchView_houses.setVisibility(View.VISIBLE);
                rcv_houses.setVisibility(View.VISIBLE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        Query query = myRef.child("houses");
        query.addListenerForSingleValueEvent(valueEventListener);
    }


    private void initUI() {
        searchView_houses   = findViewById(R.id.searchView_houses);

        rcv_houses     = findViewById(R.id.rcv_houses);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);

    }


    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
        searchView_houses.setVisibility(View.GONE);
        rcv_houses.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        super.onStart();

    }
}
