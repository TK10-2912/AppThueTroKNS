package com.nda.quanlyphongtro_free.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Houses implements Parcelable {
    private String hId;
    public String hName, hFloorsNumber, hFee, hDescription, hAddress, hTinhThanhPho,
            hQuanHuyen,hOpenTime, hCloseTime, hBaoSoNgayChuyen, hNote,hDienTich,hNguoiThue,hPhone;

    private List<Service> serviceList;
    private List<String> hImageUrlList;

    public Houses(){}



    public Houses(String hId, String hName, String hFloorsNumber,String hPhone, String hFee, String hDescription,
                  String hAddress, String hTinhThanhPho, String hQuanHuyen, List<Service> serviceList,
                  String hOpenTime, String hCloseTime, String hBaoSoNgayChuyen, String hNote, String hDienTich, String hNguoiThue,List<String> imageUrlList) {
        this.hId = hId;
        this.hName = hName;
        this.hFloorsNumber = hFloorsNumber;
        this.hPhone = hPhone;
        this.hFee = hFee;
        this.hDescription = hDescription;
        this.hAddress = hAddress;
        this.hTinhThanhPho = hTinhThanhPho;
        this.hQuanHuyen = hQuanHuyen;
        this.hOpenTime = hOpenTime;
        this.hCloseTime = hCloseTime;
        this.hBaoSoNgayChuyen = hBaoSoNgayChuyen;
        this.hNote = hNote;
        this.serviceList = serviceList;
        this.hDienTich = hDienTich;
        this.hNguoiThue = hNguoiThue;
        this.hImageUrlList = imageUrlList;
    }

    // Related to Parcelable
    protected Houses(Parcel in) {
        hId = in.readString();
        hName = in.readString();
        hFloorsNumber = in.readString();
        hPhone = in.readString();
        hFee = in.readString();
        hDescription = in.readString();
        hAddress = in.readString();
        hTinhThanhPho = in.readString();
        hQuanHuyen = in.readString();
        hOpenTime = in.readString();
        hCloseTime = in.readString();
        hBaoSoNgayChuyen = in.readString();
        hNote = in.readString();
        hDienTich = in.readString();
        hNguoiThue = in.readString();
        hImageUrlList = in.createStringArrayList();
    }

    public static final Creator<Houses> CREATOR = new Creator<Houses>() {
        @Override
        public Houses createFromParcel(Parcel in) {
            return new Houses(in);
        }

        @Override
        public Houses[] newArray(int size) {
            return new Houses[size];
        }
    };


    public String gethId() {
        return hId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethFloorsNumber() {
        return hFloorsNumber;
    }
    public String gethPhone() {
        return hPhone;
    }

    public void sethFloorsNumber(String hFloorsNumber) {
        this.hFloorsNumber = hFloorsNumber;
    }
    public void sethPhone(String hPhone) {
        this.hPhone = hPhone;
    }

    public String gethFee() {
        return hFee;
    }

    public void sethFee(String hFee) {
        this.hFee = hFee;
    }

    public String gethDescription() {
        return hDescription;
    }

    public void sethDescription(String hDescription) {
        this.hDescription = hDescription;
    }

    public String gethAddress() {
        return hAddress;
    }

    public void sethAddress(String hAddress) {
        this.hAddress = hAddress;
    }

    public String gethTinhThanhPho() {
        return hTinhThanhPho;
    }

    public void sethTinhThanhPho(String hTinhThanhPho) {
        this.hTinhThanhPho = hTinhThanhPho;
    }

    public String gethQuanHuyen() {
        return hQuanHuyen;
    }

    public void sethQuanHuyen(String hQuanHuyen) {
        this.hQuanHuyen = hQuanHuyen;
    }

    public String gethOpenTime() {
        return hOpenTime;
    }

    public void sethOpenTime(String hOpenTime) {
        this.hOpenTime = hOpenTime;
    }

    public String gethCloseTime() {
        return hCloseTime;
    }

    public void sethCloseTime(String hCloseTime) {
        this.hCloseTime = hCloseTime;
    }

    public String gethBaoSoNgayChuyen() {
        return hBaoSoNgayChuyen;
    }

    public void sethBaoSoNgayChuyen(String hBaoSoNgayChuyen) {
        this.hBaoSoNgayChuyen = hBaoSoNgayChuyen;
    }

    public String gethNote() {
        return hNote;
    }

    public void sethNote(String hNote) {
        this.hNote = hNote;
    }

    public String gethDienTich() {return hDienTich;}

    public void sethDienTich(String hDienTich) {this.hDienTich = hDienTich;}


    public String gethNguoiThue() {return hNguoiThue;}

    public void sethNguoiThue(String hNguoiThue) {this.hNguoiThue = hNguoiThue;}
    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
    public List<String> gethImageUrlList() {
        return hImageUrlList;
    }

    public void sethImageUrlList(List<String> hImageUrlList) {
        this.hImageUrlList = hImageUrlList;
    }


    // Related to Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hId);
        parcel.writeString(hName);
        parcel.writeString(hFloorsNumber);
        parcel.writeString(hPhone);
        parcel.writeString(hFee);
        parcel.writeString(hDescription);
        parcel.writeString(hAddress);
        parcel.writeString(hTinhThanhPho);
        parcel.writeString(hQuanHuyen);
        parcel.writeString(hOpenTime);
        parcel.writeString(hCloseTime);
        parcel.writeString(hBaoSoNgayChuyen);
        parcel.writeString(hNote);
        parcel.writeString(hDienTich);
        parcel.writeString(hNguoiThue);
        parcel.writeStringList(hImageUrlList);
    }
}
