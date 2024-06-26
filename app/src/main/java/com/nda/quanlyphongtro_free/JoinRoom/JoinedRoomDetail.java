package com.nda.quanlyphongtro_free.JoinRoom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.JoinRoom;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JoinedRoomDetail extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    ImageView imgBack,img_huy,img_duyet;
    JoinRoom joinRoom;
    String ownerUserId,joinedHouseId, joinedRoomId;

    TextView txt_joinedRoomName,txt_hoTen,txt_soDienThoai,txt_cccd,txt_email,txt_noiCap;

    LinearLayout ll_chiTietPhong, ll_showRoomDetail, ll_optionRooms,ll_danhSachTenants,ll_showTenants;
    TextView txt_bgColor2, txt_bgColor1;

    TextView txt_roomFee, txt_area, txt_floorNumber, txt_numberOfBedRooms, txt_numberOfLivingRooms,
            txt_limitTenants, txt_deposits;
    TextView txt_genderMale, txt_genderFemale, txt_genderOther;
    TextView txt_description, txt_noteForTenants, txt_roomHouseID;

    LinearLayout lnUpdate;
    List<Service> serviceList = new ArrayList<>();
    AdapterService adapterService;
    RecyclerView rcv_servicesJoinedRoom;
    Tenants tenants = new Tenants();
    Houses house = new Houses();
    Rooms room= new Rooms();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_room_detail);

        initUI();

        init();

    }

    private void init() {
        joinRoom = getIntent().getParcelableExtra("Data_JoinedRoom_Parcelable");

        ownerUserId = joinRoom.getOwnerUserId();
        joinedHouseId = joinRoom.getHouseId();
        joinedRoomId = joinRoom.getRoomId();

        getRoom();
        getHouse();
        getTenant();
        displayDataOfJoinedRoomDetail();
        displayTenant();

        ll_chiTietPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showRoomDetail.setVisibility(View.VISIBLE);
                ll_showTenants.setVisibility(View.GONE);

                txt_bgColor2.setBackgroundColor(Color.parseColor("#0A83E8"));
                txt_bgColor1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        ll_danhSachTenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTenant();
                displayTenant();
                ll_showRoomDetail.setVisibility(View.GONE);
                ll_showTenants.setVisibility(View.VISIBLE);

                txt_bgColor2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txt_bgColor1.setBackgroundColor(Color.parseColor("#0A83E8"));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToJoinRoomSystem();
            }
        });
        img_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJoinRoom(false);
            }
        });
        img_duyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJoinRoom(true);
            }
        });
    }
    /***************************
     *
     *
     * (Related) Contract
     *
     *
     *************************** */
    private void getHouse(){
        DatabaseReference  query = myRef.child("houses").child(joinedHouseId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    house = dataSnapshot.getValue(Houses.class);
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void getRoom(){
        DatabaseReference  query = myRef.child("rooms").child(joinedHouseId).child(joinedRoomId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    room = dataSnapshot.getValue(Rooms.class);
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void getTenant(){
        DatabaseReference  query = myRef.child("tenants").child(ownerUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tenants = dataSnapshot.getValue(Tenants.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void updateJoinRoom(Boolean isDuyet){
        if(isDuyet == true){
            JoinRoom joinRoomUpdate = new JoinRoom(joinRoom.getjId(), ownerUserId, joinedHouseId, joinedRoomId,"Duyệt");
            myRef.child("joinRooms").child(joinRoom.getjId()).setValue(joinRoomUpdate);
            updateTenantWithRoom();
            Toast.makeText(this, "Duyệt thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(JoinedRoomDetail.this, JoinRoomSystem.class);
            startActivity(intent);
        }
        else{
            JoinRoom joinRoomUpdate = new JoinRoom(joinRoom.getjId(), ownerUserId, joinedHouseId, joinedRoomId,"Huỷ");
            myRef.child("joinRooms").child(joinRoom.getjId()).setValue(joinRoomUpdate);
            Toast.makeText(this, "Hủy duyệt thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(JoinedRoomDetail.this, JoinRoomSystem.class);
            startActivity(intent);
        }

    }
    private void updateTenantWithRoom(){
        getTenant();
        if(tenants.getId() != null){
            tenants.settRentRoom(room.getrName());
            tenants.settRentHouse(house.gethName());
            tenants.setRentHouseId(joinedHouseId);
            tenants.setRentRoomId(joinedRoomId);
            myRef.child("tenants").child(ownerUserId).setValue(tenants);
        }

    }
    private void displayTenant(){
        txt_hoTen.setText(tenants.gettName());
        txt_email.setText(tenants.gettEmail());
        txt_cccd.setText(tenants.gettSoCMND());
        txt_noiCap.setText(tenants.gettNoiCapCMND());
        txt_soDienThoai.setText(tenants.gettPhoneNumber());
    }
    private void displayDataOfJoinedRoomDetail() {
        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Rooms rooms = snapshot.getValue(Rooms.class);

                    txt_joinedRoomName.setText("Phòng tham gia : " + rooms.getrName());
                    txt_floorNumber.setText(rooms.getrFloorNumber());

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    Double cost = Double.parseDouble(rooms.getrPrice());
                    txt_roomFee.setText(formatter.format(cost) + " đ");

                    Double cost2 = Double.parseDouble(rooms.getrDeposit());
                    txt_deposits.setText(formatter.format(cost2) + " đ");

                    txt_area.setText(rooms.getrArea() + "m2");
                    txt_numberOfBedRooms.setText(rooms.getrBedRoomNumber());
                    txt_numberOfLivingRooms.setText(rooms.getrLivingRoomNumber());
                    txt_limitTenants.setText(rooms.getrLimitTenants());
                    txt_description.setText(rooms.getrDescription());
                    txt_noteForTenants.setText(rooms.getrNoteToTenant());


                    if(joinRoom.getStatus().equals("Duyệt") || joinRoom.getStatus().equals("Hủy"))
                    {
                        lnUpdate.setVisibility(View.GONE);
                    }
                    else lnUpdate.setVisibility(View.VISIBLE);
                    String selectedGender = rooms.getrGender();
                    String[] splitGender = selectedGender.split(",");
                    for (int i = 0 ; i < splitGender.length; i ++)
                    {
                        if (splitGender[i].equals("Nam"))
                        {
                            txt_genderMale.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                        else if (splitGender[i].equals("Nữ"))
                        {
                            txt_genderFemale.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                        else if (splitGender[i].equals("Khác"))
                        {
                            txt_genderOther.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(joinedHouseId)
                    .child(joinedRoomId);
            query.addListenerForSingleValueEvent(valueEventListener);
        }catch (Exception e)
        {
            Toast.makeText(this, "Error : Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            backToJoinRoomSystem();
        }

        adapterService = new AdapterService(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_servicesJoinedRoom.setLayoutManager(linearLayoutManager);
        rcv_servicesJoinedRoom.setAdapter(adapterService);
        displayServices();
    }

    private void displayServices() {
        serviceList.clear();

        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Service service = dataSnapshot.getValue(Service.class);

                        serviceList.add(service);
                    }
                    adapterService.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("houses")
                    .child(joinedHouseId)
                    .child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            backToJoinRoomSystem();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }

    }

    private void backToJoinRoomSystem()
    {
        Intent intent = new Intent(JoinedRoomDetail.this, JoinRoomSystem.class);
        startActivity(intent);

        JoinedRoomDetail.this.finish();

    }


    private void initUI() {
        imgBack         =  findViewById(R.id.imgBack);

        rcv_servicesJoinedRoom = findViewById(R.id.rcv_servicesJoinedRoom);

        txt_joinedRoomName        =  findViewById(R.id.txt_joinedRoomName);
        txt_roomHouseID     = findViewById(R.id.txt_roomHouseID);

        txt_bgColor2        =  findViewById(R.id.txt_bgColor2);
        txt_bgColor1        = findViewById(R.id.txt_bgColor1);

        ll_optionRooms      = findViewById(R.id.ll_optionRooms);
        ll_chiTietPhong     =  findViewById(R.id.ll_chiTietPhong);
        ll_showRoomDetail   =  findViewById(R.id.ll_showRoomDetail);

        txt_roomFee     =  findViewById(R.id.txt_roomFee);
        txt_area     =  findViewById(R.id.txt_area);
        txt_floorNumber     =  findViewById(R.id.txt_floorNumber);
        txt_numberOfBedRooms     =  findViewById(R.id.txt_numberOfBedRooms);
        txt_numberOfLivingRooms     =  findViewById(R.id.txt_numberOfLivingRooms);
        txt_limitTenants     =  findViewById(R.id.txt_limitTenants);
        txt_deposits     =  findViewById(R.id.txt_deposits);

        txt_genderMale     =  findViewById(R.id.txt_genderMale);
        txt_genderFemale     =  findViewById(R.id.txt_genderFemale);
        txt_genderOther     =  findViewById(R.id.txt_genderOther);

        txt_description     =  findViewById(R.id.txt_description);
        txt_noteForTenants     =  findViewById(R.id.txt_noteForTenants);

        img_huy = findViewById(R.id.img_huy);
        img_duyet = findViewById(R.id.img_duyet);

        txt_hoTen = findViewById(R.id.txt_hoTen);
        txt_soDienThoai = findViewById(R.id.txt_soDienThoai);
        txt_cccd = findViewById(R.id.txt_cccd);
        txt_email = findViewById(R.id.txt_email);
        txt_noiCap = findViewById(R.id.txt_noiCap);
        ll_danhSachTenants = findViewById(R.id.ll_danhSachTenants);
        ll_showTenants = findViewById(R.id.ll_showTenants);

        lnUpdate = findViewById(R.id.lnUpdate);

    }


    @Override
    public void onBackPressed() {
        backToJoinRoomSystem();

        super.onBackPressed();
    }

}