package com.nda.quanlyphongtro_free.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JoinRoom implements Parcelable {
    private String jId;
    private String ownerUserId, houseId,roomId,status;

    public JoinRoom(){}
    public JoinRoom(String jId, String ownerUserId, String houseId, String roomId,String status) {
        this.jId = jId;
        this.ownerUserId = ownerUserId;
        this.houseId = houseId;
        this.roomId = roomId;
        this.status = status;


    }

    protected JoinRoom(Parcel in) {
        jId = in.readString();
        ownerUserId = in.readString();
        houseId = in.readString();
        roomId = in.readString();
        status = in.readString();
    }

    public static final Creator<JoinRoom> CREATOR = new Creator<JoinRoom>() {
        @Override
        public JoinRoom createFromParcel(Parcel in) {
            return new JoinRoom(in);
        }

        @Override
        public JoinRoom[] newArray(int size) {
            return new JoinRoom[size];
        }
    };

    public String getjId() {
        return jId;
    }


    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(jId);
        parcel.writeString(ownerUserId);
        parcel.writeString(houseId);
        parcel.writeString(roomId);
        parcel.writeString(status);
    }
}
