package com.example.nhanvien.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.example.nhanvien.R;
import com.example.nhanvien.adapter.MainAdapter;
import com.example.nhanvien.adapter.NhanVienAdapter;
import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.utils.AppDatabase;
import com.example.nhanvien.utils.DBConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    private Spinner spinnerNVPhongBan;
    private ArrayAdapter spinerAdapter;
    private ListView lvNhanVien;
    private NhanVienAdapter adapter;
    private NhanVien chon=null;
    private PhongBan pb;
    private AppDatabase db;
    private FloatingActionButton fabThem;
    ArrayList<NhanVien> dsNhanVien;
    ArrayList<PhongBan> dsPhongBan;
    int requestCode =1000, resultCode=10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBConfigUtil.copyDatabaseFromAssets(NhanVienActivity.this);
        setContentView(R.layout.activity_nhan_vien);
        init();
        loadSpiner();
        addEvent();


    }
    private void init(){
        db= AppDatabase.getAppDataBase(this);
        spinnerNVPhongBan=findViewById(R.id.spinnerNVPhongBan);
        dsNhanVien=new ArrayList<>();
        dsPhongBan= new ArrayList<>();
        dsPhongBan.addAll(db.phongBanDao().getAll());
        dsNhanVien.addAll(db.nhanVienDao().getAll());
        lvNhanVien=findViewById(R.id.lvNhanVien);
        fabThem=findViewById(R.id.fabThem);
    }


    /**
     *
     */
    private void showListView(String mapb){
        dsNhanVien.clear();
        if(mapb.equals("none")){
            dsNhanVien.clear();
        }else if(mapb.equals("all")){
            dsNhanVien.addAll(db.nhanVienDao().getAll());
        }else {
            dsNhanVien.addAll(db.nhanVienDao().nhanVienPhongBan(mapb));
        }
        adapter = new NhanVienAdapter(NhanVienActivity.this,R.layout.item_nhanvien_layout,dsNhanVien);
        lvNhanVien.setAdapter(adapter);
    }

    /**
     *
     */
    private void loadSpiner(){
        List<String> dsPhongBan=new ArrayList<>();
        dsPhongBan.add("All");
        dsPhongBan.addAll(db.phongBanDao().dsTenPhongBan());
        spinerAdapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,dsPhongBan);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNVPhongBan.setAdapter(spinerAdapter);
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

    /**
     *
     */
    private void addEvent(){
        /** Spiner giưới*/
        spinnerNVPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){
                    showListView("all");
                }else {
                    PhongBan pb = dsPhongBan.get(position-1);
                    if (db.nhanVienDao().nhanVienPhongBan(pb.getMaphongban()).size()!=0){
                        showListView(pb.getMaphongban());
                        Toast.makeText(NhanVienActivity.this,"Phòng ban "+ spinerAdapter.getItem(position)+" có " + dsNhanVien.size() +" nhân viên", Toast.LENGTH_SHORT).show();
                    }else {
                        showListView("none");
                        Toast.makeText(NhanVienActivity.this,"Phòng ban "+ spinerAdapter.getItem(position)+" không có nhân viên", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**Thêm Nhân viên*/
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NhanVienActivity.this, EditNhanVienActivity.class);
                startActivityForResult(intent,requestCode);
            }
        });
        /** Sửa nhân viên*/
        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NhanVien nv= dsNhanVien.get(position);
                Intent intent = new Intent(NhanVienActivity.this, EditNhanVienActivity.class);
                intent.putExtra("EDITNV",nv.getManv());
                startActivityForResult(intent,requestCode);
            }
        });
        /** Xóa nhân viên*/
        lvNhanVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(NhanVienActivity.this);
                //Thiết lập tiêu đề
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có muốn xóa không?");
                // Nút Ok
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.nhanVienDao().delete(dsNhanVien.get(position).getManv());
                        showListView(dsNhanVien.get(position).getPhongban());
                    }
                });
                //Nút Cancel
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)  {
                        dialog.cancel();
                    }
                });
                //Tạo dialog
                AlertDialog al = b.create();
                //Hiển thị
                al.show();
                return true;
            }
        });
    }

    /**
     * Nhận data bên EditNhanVien
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==this.requestCode){
            if (resultCode==this.resultCode){
                if (data.hasExtra("TRA")){
                    Toast.makeText(NhanVienActivity.this, "Lưu Thành công", Toast.LENGTH_SHORT).show();
                }
            }
        }
        showListView("all");
        chon=null;
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

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.item_Main:
                finish();
                break;
            case R.id.item_PhongBan:
                Intent intent= new Intent(NhanVienActivity.this,PhongBanActivity.class);
                startActivityForResult(intent,1000);
                finish();
                break;
            case  R.id.item_NhanVien:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}