package com.nda.quanlyphongtro_free;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.AdapterHouses;
import com.nda.quanlyphongtro_free.Houses.AdapterSlideImagePicasso;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.AdapterRoom;
import com.nda.quanlyphongtro_free.JoinRoom.AdapterService;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainDetailHouseUserActivity extends AppCompatActivity {

    private TextView tvHouseName,tvDiaChi,tvSdt,tvDienTich,tvMota,tvMoney,quantityService;
    private ImageView imgBack,imgContact;
    private RecyclerView rcv_room;
    ViewPager viewPager ;
    private int currentPage = 0;
    final long DELAY_MS = 1500; // Độ trễ giữa mỗi lần chuyển đổi ảnh
    final long PERIOD_MS = 4000;
    private Timer timer;
    private String[] imageUrls;
    LinearLayout lnContact;
    Intent intent = getIntent();
    Button btnCall,btnMap,btnClose;
    Houses houses;
    List<Rooms> roomList = new ArrayList<>();
    AdapterRoom adapterRooms;
    List<Service> serviceList = new ArrayList<>();
    AdapterService adapterService;
    RecyclerView rcv_service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_houses_user);
        initUI();
        setUpRCV();
        getService();
    }

    private void setUpRCV() {
        if (getIntent() != null) {
            houses = getIntent().getParcelableExtra("Data_House_Parcelable");
        }
        if (houses != null) {
            adapterRooms = new AdapterRoom(MainDetailHouseUserActivity.this, roomList, houses,true);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rcv_room.setLayoutManager(staggeredGridLayoutManager);
            rcv_room.setAdapter(adapterRooms);
            displayRooms(houses.gethId());
        } else {

        }
    }
    private void displayServices() {
        serviceList.clear();
        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        long count = snapshot.getChildrenCount();
                        Service service = dataSnapshot.getValue(Service.class);
                        serviceList.add(service);
                    }
                    int count = serviceList.size();
                    quantityService.setText("Tiện ích phòng(" + count + ")");
                    adapterService.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = FirebaseDatabase.getInstance().getReference().child("houses")
                    .child(houses.gethId())
                    .child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();
        }

    }
    private void getService() {
        adapterService = new AdapterService(MainDetailHouseUserActivity.this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_service.setLayoutManager(linearLayoutManager);
        rcv_service.setAdapter(adapterService);
        displayServices();

    }

    private void displayRooms(String houseId) {
        roomList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Rooms rooms = dataSnapshot.getValue(Rooms.class);
                    roomList.add(rooms);
                }
                adapterRooms.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = FirebaseDatabase.getInstance().getReference().child("rooms").child(houses.gethId());
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    public void callPhoneNumber(View view) {
        String phoneNumber="";
        if(houses!= null)
        {
             phoneNumber = houses.gethPhone();
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
    protected void onStart() {
        super.onStart();
        startSlider();
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopSlider();
    }
    private void startSlider() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == imageUrls.length) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void stopSlider() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private String formatMoneyVND(double money) {
        // Tạo một đối tượng NumberFormat cho Locale của Việt Nam
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Định dạng số tiền và trả về chuỗi đã định dạng
        return formatter.format(money);
    }
    private String formatMonney(String tien) {
        double money = 0;
        if (!tien.isEmpty()) {
            try {
                money = Double.parseDouble(tien);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(money);
    }

    private void initUI() {
        tvHouseName   = findViewById(R.id.tvHouseName);
        tvDiaChi     = findViewById(R.id.tvDiaChi);
        tvSdt     = findViewById(R.id.tvSdt);
        tvDienTich     = findViewById(R.id.tvDienTich);
        tvMota     = findViewById(R.id.tvMota);
        imgBack     = findViewById(R.id.imgBack);
        viewPager     = findViewById(R.id.viewPager);
        btnCall     = findViewById(R.id.btnCall);
        btnMap     = findViewById(R.id.btnMap);
        btnClose     = findViewById(R.id.btnClose);
        imgContact     = findViewById(R.id.imgContact);
        lnContact     = findViewById(R.id.lnContact);
        tvMoney     = findViewById(R.id.tvMoney);
        rcv_room     = findViewById(R.id.rcv_room);
        rcv_service     = findViewById(R.id.rcv_service);
        quantityService     = findViewById(R.id.quantityService);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDetailHouseUserActivity.this, MainHomeActivity.class));
                MainDetailHouseUserActivity.this.finish();
            }
        });
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnContact.setVisibility(View.VISIBLE);
                imgContact.setVisibility(View.GONE);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnContact.setVisibility(View.GONE);
                imgContact.setVisibility(View.VISIBLE);

            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   callPhoneNumber(view);
               }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("google.navigation:q=" + houses.gethAddress()));

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;
                Log.d("Main","error: " +activities + ";" + "Intent" + intent + ";Package"+ packageManager);
                if (isIntentSafe) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainDetailHouseUserActivity.this, "Vui lòng cài đặt ứng dụng Google Maps để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (getIntent() != null) {
            houses = getIntent().getParcelableExtra("Data_House_Parcelable");
            if (houses != null) {
               tvHouseName.setText(houses.gethName()) ;
                tvDiaChi.setText(houses.gethAddress()) ;
                tvDienTich.setText("Diện tích: " + houses.gethDienTich() + "m2") ;
                tvMota.setText(houses.gethDescription()) ;
                tvSdt.setText("Số điện thoại: " + houses.gethPhone() );
                tvMoney.setText(formatMonney(houses.gethFee()));
                imageUrls = houses.gethImageUrlList().toArray(new String[0]); // Thay thế bằng URL thực sự của các ảnh
                AdapterSlideImagePicasso adapter = new AdapterSlideImagePicasso(this, imageUrls);
                viewPager.setAdapter(adapter);
            }
        }
    }
}
