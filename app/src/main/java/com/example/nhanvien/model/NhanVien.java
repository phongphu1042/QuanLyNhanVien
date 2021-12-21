package com.example.nhanvien.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "NhanVien")
public class NhanVien {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int manv;

    @ColumnInfo(name = "tennv")
    private String tennv;

    @ColumnInfo(name = "sodt")
    private String sodt;

    @ColumnInfo(name = "luong")
    private int luong;

    @ColumnInfo(name = "luongcb")
    private int luongcb;

    @ColumnInfo(name = "phongban")
    private String phongban;

    public NhanVien() {
    }

    public NhanVien(int manv, String tennv, String sodt, int luong, int luongcb, String phongban) {
        this.manv = manv;
        this.tennv = tennv;
        this.sodt = sodt;
        this.luong = luong;
        this.luongcb = luongcb;
        this.phongban = phongban;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getSodt() {
        return sodt;
    }

    public void setSodt(String sodt) {
        this.sodt = sodt;
    }

    public int getLuong() {
        return luong;
    }

    public void setLuong(int luong) {
        this.luong = luong;
    }

    public int getLuongcb() {
        return luongcb;
    }

    public void setLuongcb(int luongcb) {
        this.luongcb = luongcb;
    }

    public String getPhongban() {
        return phongban;
    }

    public void setPhongban(String phongban) {
        this.phongban = phongban;
    }
}
