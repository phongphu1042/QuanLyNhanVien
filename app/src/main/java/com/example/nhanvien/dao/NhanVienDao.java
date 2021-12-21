package com.example.nhanvien.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;

import java.util.List;

@Dao
public interface NhanVienDao {
    @Query("select * from NhanVien")
    List<NhanVien> getAll();

    @Query("select * from NhanVien where phongban like :phongban")
    List<NhanVien> nhanVienPhongBan(String phongban);

    @Query("select * from NhanVien where manv like :manv")
    List<NhanVien> findNhanVien(int manv);

    @Query("delete from NhanVien where manv like :ma ")
    int delete( int ma);

    @Insert
    void insert(NhanVien nhanVien);

    @Update
    int update(NhanVien nhanVien);
}
