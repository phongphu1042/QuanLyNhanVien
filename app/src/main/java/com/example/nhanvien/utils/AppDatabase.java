package com.example.nhanvien.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.dao.NhanVienDao;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.dao.PhongBanDao;


@Database(entities = {PhongBan.class, NhanVien.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract PhongBanDao phongBanDao();
    public abstract NhanVienDao nhanVienDao();
    public static AppDatabase getAppDataBase(Context context){
            if(INSTANCE == null){
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DBConfigUtil.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }

        public static void destroyInstance(){
            INSTANCE = null;
        }
}
