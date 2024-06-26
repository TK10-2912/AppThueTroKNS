package com.nda.quanlyphongtro_free;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.AdapterServiceOfRoom;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.JoinRoom;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RoomUserDetailActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ImageView imgBack;
    TextView txt_roomName;


    LinearLayout ll_chiTietPhong;

    Houses houses;
    Rooms rooms;

    TextView txt_roomFee, txt_area, txt_floorNumber, txt_numberOfBedRooms, txt_numberOfLivingRooms,
            txt_limitTenants, txt_deposits;
    TextView txt_genderMale, txt_genderFemale, txt_genderOther;
    TextView txt_description, txt_noteForTenants, txt_roomHouseID;

    Button btn_lienHe,btn_datPhong;

    List<Service> serviceList = new ArrayList<>();
    AdapterServiceOfRoom adapterServiceOfRoom;
    RecyclerView rcv_servicesRoomDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_room_user);

        initUI();
        init();
        relatedRoomDetail();
    }


    private void init() {
        houses = getIntent().getParcelableExtra("Data_RoomOfHouse_Parcelable");
        rooms  = getIntent().getParcelableExtra("Data_Room_Parcelable");

        //txt_roomHouseID.setText(houses.gethId() + "_splitHere_" + rooms.getId()+ "_splitHere_" + firebaseUser.getUid());

        txt_roomName.setText(rooms.getrName());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHouseDetailSystem();
            }
        });
        btn_lienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNumber(view);
            }
        });
        btn_datPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String houseId = houses.gethId();
                String roomId = rooms.getId();
                String ownerUserId = firebaseUser.getUid();

//                if (firebaseUser.getUid().equals(ownerUserId))
//                {
//                    Toast.makeText(JoinRoomSystem.this, "Error : Không thể tham gia phòng của chính bạn", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());
                String joinRoomId = "joinRoom_" + currentDateAndTime + "_" + firebaseUser.getUid();
                JoinRoom joinRoom = new JoinRoom(joinRoomId, ownerUserId, houseId, roomId,"Khởi tạo");

                // Add joinRoom to Database
                myRef.child("joinRooms").child(joinRoomId).setValue(joinRoom);
                Toast.makeText(RoomUserDetailActivity.this, "Bạn đã đặt phòng thành công!", Toast.LENGTH_SHORT).show();
            }
        });
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
    public void formatMoneyType(EditText edtCostInput)
    {
        edtCostInput.addTextChangedListener( new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void afterTextChanged(Editable s) {
                edtCostInput.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    double doubleVal;

                    /**
                     * Kiếm tra xem data users nhập vào đã chứa "," chưa ?
                     * Nếu có thì sẽ thay thế = ""
                     * */
                    if (originalString.contains(","))
                        originalString = originalString.replaceAll(",","");

                    doubleVal = Double.parseDouble(originalString);


                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(doubleVal);

                    //setting text after format to EditText
                    edtCostInput.setText(formattedString);
                    edtCostInput.setSelection(edtCostInput.getText().length());

                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                edtCostInput.addTextChangedListener(this);

            }
        });
    }

    /***************************
     *
     *
     * (Related) room
     *
     *
     *************************** */
    private void relatedRoomDetail() {

        txt_area.setText(rooms.getrArea() + " m2");

        txt_floorNumber.setText(rooms.getrFloorNumber());

        txt_numberOfBedRooms.setText(rooms.getrBedRoomNumber());
        txt_numberOfLivingRooms.setText(rooms.getrLivingRoomNumber());
        txt_limitTenants.setText(rooms.getrLimitTenants());

        if (rooms.getrPrice().equals(""))
        {
            txt_roomFee.setText("0 đ");
        } else {
            /**
             * Format cost lấy về từ firebase
             * theo định dạng money
             * */
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            Double cost = Double.parseDouble(rooms.getrPrice());
            txt_roomFee.setText(formatter.format(cost) + " đ");
        }

        if (rooms.getrDeposit().equals(""))
        {
            txt_deposits.setText("0 đ");
        } else {
            /**
             * Format cost lấy về từ firebase
             * theo định dạng money
             * */
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            Double cost = Double.parseDouble(rooms.getrDeposit());
            txt_deposits.setText(formatter.format(cost) + " đ");
        }

        txt_description.setText(rooms.getrDescription());
        txt_noteForTenants.setText(rooms.getrNoteToTenant());


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

//        adapterServiceOfRoom = new AdapterServiceOfRoom(this, serviceList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
//                RecyclerView.HORIZONTAL,false);
//        rcv_servicesRoomDetail.setLayoutManager(linearLayoutManager);
//        rcv_servicesRoomDetail.setAdapter(adapterServiceOfRoom);

//        displayServices();
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

                    // When get data successfully, hide the shimmer and show all function field

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(houses.gethId()).child(rooms.getId()).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            startActivity(new Intent(RoomUserDetailActivity.this, MainActivity.class));
            RoomUserDetailActivity.this.finish();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }

    }

    private void backToHouseDetailSystem()
    {
        Intent intent = new Intent(RoomUserDetailActivity.this, MainDetailHouseUserActivity.class);

        intent.putExtra("Data_House_Parcelable", houses);
        startActivity(intent);

        RoomUserDetailActivity.this.finish();
    }

    private void initUI() {
        imgBack         =  findViewById(R.id.imgBack);

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

        rcv_servicesRoomDetail     =  findViewById(R.id.rcv_servicesRoomDetail);

        txt_roomName = findViewById(R.id.txt_roomName);

        btn_lienHe = findViewById(R.id.btn_lienHe);
        btn_datPhong = findViewById(R.id.btn_datPhong);

    }
    @Override
    public void onBackPressed() {
        backToHouseDetailSystem();

        super.onBackPressed();
    }


    @Override
    protected void onStart() {

        //displayServices();

        super.onStart();

    }
}
