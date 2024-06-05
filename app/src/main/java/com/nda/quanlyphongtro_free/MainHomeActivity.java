package com.nda.quanlyphongtro_free;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import java.io.IOException;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainHomeActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    androidx.appcompat.widget.SearchView searchView_houses;

    List<Houses> housesList = new ArrayList<>();
    RecyclerView rcv_houses;
    AdapterHouses adapterHouses;
    ImageView img_logout;

    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        initUI();
        setUpRCV();
        initiate();

    }

    private void setUpRCV() {
        adapterHouses = new AdapterHouses(MainHomeActivity.this, housesList, true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rcv_houses.setLayoutManager(staggeredGridLayoutManager);
        rcv_houses.setAdapter(adapterHouses);
        displayHouses();
    }

    private void displayHouses() {
        housesList.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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


    //Tìm kiếm nhà

    private void searchHouses(String searchText) {
        housesList.clear();
        Query queryByName = database.getReference("houses");
//                .orderByChild("hTinhThanhPho");
//                .startAt(searchText.toLowerCase())
//                .endAt(searchText.toLowerCase() + "\uf8ff");

        queryByName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Main", "Query result:");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Houses houses = snapshot.getValue(Houses.class);
                    if (houses != null) {
                        String houseName = removeDiacritics(houses.gethTinhThanhPho().toLowerCase());
                        Log.d("Main", "House name: " + houseName);
                        if (houseName.contains(removeDiacritics(searchText.toLowerCase()))) {
                            if (!housesList.contains(houses)) {
                                housesList.add(houses);
                            }
                        }
                    }
                }
                adapterHouses.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Main", "Query cancelled:", databaseError.toException());
            }
        });
    }
    public static String removeDiacritics(String str) {
        // Normalize the string to NFD form (Normalization Form D)
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        // Use regex to remove diacritical marks
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("");
    }
    private void initiate() {
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainHomeActivity.this, LoginSignUpActivity.class));
                finishAffinity();
            }
        });
    }

    private void initUI() {
        searchView_houses = findViewById(R.id.searchView_houses);

        rcv_houses = findViewById(R.id.rcv_houses);
        searchView_houses = findViewById(R.id.searchView_houses);
        img_logout = findViewById(R.id.img_logout);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        searchView_houses.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Main","name: " + newText );
                searchHouses(newText);
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        searchView_houses.setVisibility(View.GONE);
        rcv_houses.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        super.onStart();

    }
}
