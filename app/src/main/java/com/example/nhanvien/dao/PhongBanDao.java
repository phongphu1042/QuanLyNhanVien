package com.example.nhanvien.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;

import java.util.List;

@Dao
public interface PhongBanDao {
    @Query("select * from PhongBan")
    List<PhongBan> getAll();

    @Query("select tenphongban from PhongBan ")
    List<String> dsTenPhongBan();

    @Query("select * from PhongBan where maphongban like :ma")
    List<PhongBan> findPhongBan (String ma);

    @Query("delete from PhongBan where maphongban like :ma ")
    int delete( String ma);

    @Insert
    void insert(PhongBan phongBan);

    @Update
    int update(PhongBan phongBan);
}
