package com.example.nhanvien.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nhanvien.R;
import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.utils.AppDatabase;
import com.example.nhanvien.utils.DBConfigUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditNhanVienActivity extends AppCompatActivity {

    private EditText txtEditTenNV,txtEditSdt,txtEditLuongCB;
    private Button btnLuu, btnThoat;
    private Spinner spinnerPhongBan;
    private ArrayAdapter<PhongBan> spinerAdapter;
    private NhanVien chon;
    private PhongBan pb;
    private ArrayList<PhongBan> dsPhongBan;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nhan_vien);

        DBConfigUtil.copyDatabaseFromAssets(EditNhanVienActivity.this);

        init();
        loadSpiner();
        getIntentData();
        addEvent();


    }
    private void init(){
        db= AppDatabase.getAppDataBase(this);
        dsPhongBan= new ArrayList<>();
        dsPhongBan.addAll(db.phongBanDao().getAll());
        txtEditTenNV=findViewById(R.id.txtEditTenNV);
        txtEditSdt=findViewById(R.id.txtEditSdt);
        txtEditLuongCB=findViewById(R.id.txtEditLuongCB);
        btnLuu=findViewById(R.id.btnLuuNV);
        btnThoat= findViewById(R.id.btnThoat);
        spinnerPhongBan=findViewById(R.id.spinnerPhongBan);
        chon=null;
        pb=null;
    }

    private void loadSpiner(){
        spinerAdapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,dsPhongBan);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhongBan.setAdapter(spinerAdapter);
    }
    private void addEvent(){

        /** Lưu nhân viên sau khi sửa */
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pb==null){
                    Toast.makeText(EditNhanVienActivity.this, "Vui lòng chọn 'Phòng Ban' cho nhân viên", Toast.LENGTH_SHORT).show();
                }
                else if (!kiemtraTen()){
                    txtEditTenNV.requestFocus();
                }
                else if(!kiemtraSdt()){
                    txtEditSdt.requestFocus();
                }
                else if(!kiemtraLuongcb()){
                    txtEditLuongCB.requestFocus();
                }else if(chon==null){
                    luuThongTin();
                }else {
                    update();
                }
            }
        });

        /** Spiner trên*/
        spinnerPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pb=dsPhongBan.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView();
                finish();
            }
        });

    }

    /**
     * kiểm tra số điện thoại phải đủ 10 số
     * @return
     */
    private Boolean kiemtraSdt(){
        if(txtEditSdt.getText().toString().trim().equals("")){
            Toast.makeText(EditNhanVienActivity.this, "Số điện thoại không được bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(txtEditSdt.getText().toString().length()==10){
                return true;
            }else {
                Toast.makeText(EditNhanVienActivity.this, "Số điện thoại phải 10 đủ số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     *Kiểm tra lương không được trống
     * Lương phải số chia hết cho 1000 để có thể chi trả được lương cho nhân viên
     * @return
     */
    private Boolean kiemtraLuongcb(){
        if(txtEditLuongCB.getText().toString().trim().equals("")){
            Toast.makeText(EditNhanVienActivity.this, "Lương không được bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            int luongcb =Integer.parseInt(txtEditLuongCB.getText().toString().trim());
            if((luongcb%1000)==0){
                return true;
            }else {
                Toast.makeText(EditNhanVienActivity.this, "LươngCB phải chia hết cho 1000", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * kiểm tra Tên trước khi lưu xuống SQlite
     * Tên không được có ký tự đặc biệt hoặc ký tự số
     * Tên không được rỗng
     * */
    private Boolean kiemtraTen() {

        String s=txtEditTenNV.getText().toString().trim();
        if (s == null || s.trim().isEmpty()) {
            Toast.makeText(EditNhanVienActivity.this, "Tên không được bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z]");
        s=s.replaceAll(" ", "");
        for (int i=0;i<s.length();i++){
            String n =s.charAt(i)+"";
            Matcher m = p.matcher(n);
            boolean b = m.matches();
            if (b == true) {
                Toast.makeText(EditNhanVienActivity.this, "Tên chứa ký tự số hoặc ký tự đặc biệt", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    /**
     * Reset Các EditText
     * */
    private void resetView() {
        txtEditTenNV.setText("");
        txtEditSdt.setText("");
        txtEditLuongCB.setText("");
        pb=null;
        chon=null;
    }

    /**
     * Lấy dữ liệu nhận được truyền lên EditText
     */
    private void getIntentData(){
        Intent intent = getIntent();
        int manv =intent.getIntExtra("EDITNV",-1);
        if (intent.hasExtra("EDITNV")){
            chon= db.nhanVienDao().findNhanVien(manv).get(0);
            if (chon!=null){
                txtEditTenNV.setText(chon.getTennv());
                txtEditSdt.setText(chon.getSodt()+"");
                txtEditLuongCB.setText(chon.getLuongcb()+"");
                for (int i=0;i<spinerAdapter.getCount();i++){
                    if(dsPhongBan.get(i).getMaphongban().equals(chon.getPhongban())){
                        spinnerPhongBan.setSelection(i);
                        return;
                    }
                }
            }else{
                resetView();
            }
        }else{
            resetView();
        }
    }

    /**
     * lấy Nhân viên từ trên EditText xuống Insert vào SQLite
     */
    private void luuThongTin(){
        if(chon==null) {
            chon = new NhanVien();
        }
        chon.setTennv(txtEditTenNV.getText().toString().trim());
        chon.setSodt(txtEditSdt.getText().toString().trim());
        chon.setLuongcb(Integer.parseInt(txtEditLuongCB.getText().toString().trim()));
        chon.setPhongban(pb.getMaphongban());
        int thuoctinh =pb.getThuoctinh();
        if(chon.getPhongban()=="NS"){
            double luong=(chon.getLuongcb()*0.4)+(thuoctinh*0.1);
            chon.setLuong((int) luong);
        }else if(chon.getPhongban()=="KD"){
            double luong=(chon.getLuongcb()*0.5)+(thuoctinh*0.25);
            chon.setLuong((int) luong);
        }else if(chon.getPhongban()=="KT"){
            double luong=(chon.getLuongcb()*0.75)+thuoctinh;
            chon.setLuong((int) luong);
        }else{
            double luong=chon.getLuongcb()+5000000;
            chon.setLuong((int) luong);
        }
        Intent intent =getIntent();
        db.nhanVienDao().insert(chon);
        intent.putExtra("TRA",1);
        setResult(10001,intent);
        finish();
    }

    /**
     * lấy Nhân viên từ trên EditText xuống Insert vào SQLite
     */
    private void update(){
        chon.setTennv(txtEditTenNV.getText().toString().trim());
        chon.setSodt(txtEditSdt.getText().toString().trim());
        chon.setLuongcb(Integer.parseInt(txtEditLuongCB.getText().toString().trim()));
        chon.setPhongban(pb.getMaphongban());
        int thuoctinh =pb.getThuoctinh();
        if(chon.getPhongban()=="NS"){
            double luong=(chon.getLuongcb()*0.4)+(thuoctinh*0.1);
            chon.setLuong((int) luong);
        }else if(chon.getPhongban()=="KD"){
            double luong=(chon.getLuongcb()*0.5)+(thuoctinh*0.25);
            chon.setLuong((int) luong);
        }else if(chon.getPhongban()=="KT"){
            double luong=(chon.getLuongcb()*0.75)+thuoctinh;
            chon.setLuong((int) luong);
        }else{
            double luong=chon.getLuongcb()+5000000;
            chon.setLuong((int) luong);
        }
        Intent intent =getIntent();
        db.nhanVienDao().update(chon);
        intent.putExtra("TRA",1);
        setResult(10001,intent);
        finish();
    }
}