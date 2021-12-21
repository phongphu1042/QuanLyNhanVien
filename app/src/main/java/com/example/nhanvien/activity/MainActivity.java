package com.example.nhanvien.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nhanvien.adapter.MainAdapter;
import com.example.nhanvien.utils.AppDatabase;
import com.example.nhanvien.utils.DBConfigUtil;
import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<NhanVien> dsNhanVien ;
    private ArrayList<PhongBan> dsPhongBan;
    private ListView lvNhanVienPhongBan;
    private Spinner spinnerMain;
    private MainAdapter adapter;
    private ArrayAdapter spinerAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBConfigUtil.copyDatabaseFromAssets(MainActivity.this);
        init();
        loadSpiner();
        addEvent();
    }


    private void init(){
        db=AppDatabase.getAppDataBase(this);
        lvNhanVienPhongBan= findViewById(R.id.lvNhanVienPhongBan);
        dsNhanVien=new ArrayList<>();
        dsPhongBan= new ArrayList<>();
        dsPhongBan.addAll(db.phongBanDao().getAll());
        dsNhanVien.addAll(db.nhanVienDao().getAll());
        spinnerMain=findViewById(R.id.spinnerMain);
    }
    private void showListView(String mapb){

        dsNhanVien.clear();
        if(mapb!="none"){
            dsNhanVien.addAll(db.nhanVienDao().nhanVienPhongBan(mapb));
        }
        adapter = new MainAdapter(MainActivity.this,R.layout.item_nv_main,dsNhanVien);
        lvNhanVienPhongBan.setAdapter(adapter);
    }


    private void loadSpiner(){
        spinerAdapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,db.phongBanDao().dsTenPhongBan());
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMain.setAdapter(spinerAdapter);
    }

    /**
     *auto load listview
     */
    protected void onResume(){
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }


    private void addEvent(){
        spinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PhongBan pb = dsPhongBan.get(position);
                if (db.nhanVienDao().nhanVienPhongBan(pb.getMaphongban()).size()!=0){
                    showListView(pb.getMaphongban());
                }else {
                    showListView("none");
                    Toast.makeText(MainActivity.this,"Phòng ban "+ spinerAdapter.getItem(position)+" không có nhân viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.item_Main:

                break;
            case R.id.item_PhongBan:
                Intent intent= new Intent(MainActivity.this,PhongBanActivity.class);
                startActivityForResult(intent,1000);
                break;
            case  R.id.item_NhanVien:
                Intent intent1 =new Intent(MainActivity.this,NhanVienActivity.class);
                startActivityForResult(intent1,1001);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}