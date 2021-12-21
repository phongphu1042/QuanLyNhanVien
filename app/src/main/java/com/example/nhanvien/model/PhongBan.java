package com.example.nhanvien.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "PhongBan")
public class PhongBan {
    @PrimaryKey
    @NonNull
    private String maphongban;

    @ColumnInfo(name = "tenphongban")
    private String tenphongban;

    @ColumnInfo(name = "thuoctinh")
    private int thuoctinh;

    public PhongBan() {
    }

    public PhongBan(@NonNull String maphongban, String tenphongban, int thuoctinh) {
        this.maphongban = maphongban;
        this.tenphongban = tenphongban;
        this.thuoctinh = thuoctinh;
    }

    @NonNull
    public String getMaphongban() {
        return maphongban;
    }

    public void setMaphongban(@NonNull String maphongban) {
        this.maphongban = maphongban;
    }

    public String getTenphongban() {
        return tenphongban;
    }

    public void setTenphongban(String tenphongban) {
        this.tenphongban = tenphongban;
    }

    public int getThuoctinh() {
        return thuoctinh;
    }

    public void setThuoctinh(int thuoctinh) {
        this.thuoctinh = thuoctinh;
    }

    @Override
    public String toString() {
        return  tenphongban;
    }
}
