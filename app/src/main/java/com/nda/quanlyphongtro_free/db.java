package com.nda.quanlyphongtro_free;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class db extends SQLiteOpenHelper {
    public db(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void QueryDate(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        return;
    }
    public Cursor GetData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public void INSERT_HOUSES(String housesName, String housesLocation)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Houses VALUES (null,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, housesName);
        statement.bindString(2,housesLocation);

        statement.executeInsert();

    }

    public void INSERT_ROOMS( int housesId, String roomPrice,String roomsNote, String roomName)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Rooms VALUES (null,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,housesId);
        statement.bindString(2,roomPrice);
        statement.bindString(3,roomsNote);
        statement.bindString(4,roomName);

        statement.executeInsert();

    }

    public void INSERT_TENANTS(int roomID,String tenantsName, String tenantsPhonenumber,String tenantsNote,int houseID)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Tenants VALUES (null,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,roomID);
        statement.bindString(2,tenantsName);
        statement.bindString(3,tenantsPhonenumber);
        statement.bindString(4,tenantsNote);
        statement.bindLong(5,houseID);

        statement.executeInsert();

    }

    public void INSERT_SERVICES(String serviceName, String servicePrice, String serviceNote)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Services VALUES (null,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,serviceName);
        statement.bindString(2,servicePrice);
        statement.bindString(3,serviceNote);

        statement.executeInsert();
    }

    public void INSERT_CONTRACT(byte[] img)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Contracts VALUES (null,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindBlob(1,img);


        statement.executeInsert();
    }

    public void INSERT_PAYMENT(int roomId, String money, String status, String date, String note,int houseID)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Payments VALUES (null,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,roomId);
        statement.bindString(2,money);
        statement.bindString(3,status);
        statement.bindString(4,date);
        statement.bindString(5,note);
        statement.bindLong(6,houseID);

        statement.executeInsert();

    }
}
