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

        /** L??u nh??n vi??n sau khi s???a */
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pb==null){
                    Toast.makeText(EditNhanVienActivity.this, "Vui l??ng ch???n 'Ph??ng Ban' cho nh??n vi??n", Toast.LENGTH_SHORT).show();
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

        /** Spiner tr??n*/
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
     * ki???m tra s??? ??i???n tho???i ph???i ????? 10 s???
     * @return
     */
    private Boolean kiemtraSdt(){
        if(txtEditSdt.getText().toString().trim().equals("")){
            Toast.makeText(EditNhanVienActivity.this, "S??? ??i???n tho???i kh??ng ???????c b??? tr???ng", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(txtEditSdt.getText().toString().length()==10){
                return true;
            }else {
                Toast.makeText(EditNhanVienActivity.this, "S??? ??i???n tho???i ph???i 10 ????? s???", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     *Ki???m tra l????ng kh??ng ???????c tr???ng
     * L????ng ph???i s??? chia h???t cho 1000 ????? c?? th??? chi tr??? ???????c l????ng cho nh??n vi??n
     * @return
     */
    private Boolean kiemtraLuongcb(){
        if(txtEditLuongCB.getText().toString().trim().equals("")){
            Toast.makeText(EditNhanVienActivity.this, "L????ng kh??ng ???????c b??? tr???ng", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            int luongcb =Integer.parseInt(txtEditLuongCB.getText().toString().trim());
            if((luongcb%1000)==0){
                return true;
            }else {
                Toast.makeText(EditNhanVienActivity.this, "L????ngCB ph???i chia h???t cho 1000", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * ki???m tra T??n tr?????c khi l??u xu???ng SQlite
     * T??n kh??ng ???????c c?? k?? t??? ?????c bi???t ho???c k?? t??? s???
     * T??n kh??ng ???????c r???ng
     * */
    private Boolean kiemtraTen() {

        String s=txtEditTenNV.getText().toString().trim();
        if (s == null || s.trim().isEmpty()) {
            Toast.makeText(EditNhanVienActivity.this, "T??n kh??ng ???????c b??? tr???ng", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z]");
        s=s.replaceAll(" ", "");
        for (int i=0;i<s.length();i++){
            String n =s.charAt(i)+"";
            Matcher m = p.matcher(n);
            boolean b = m.matches();
            if (b == true) {
                Toast.makeText(EditNhanVienActivity.this, "T??n ch???a k?? t??? s??? ho???c k?? t??? ?????c bi???t", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    /**
     * Reset C??c EditText
     * */
    private void resetView() {
        txtEditTenNV.setText("");
        txtEditSdt.setText("");
        txtEditLuongCB.setText("");
        pb=null;
        chon=null;
    }

    /**
     * L???y d??? li???u nh???n ???????c truy???n l??n EditText
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
     * l???y Nh??n vi??n t??? tr??n EditText xu???ng Insert v??o SQLite
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
     * l???y Nh??n vi??n t??? tr??n EditText xu???ng Insert v??o SQLite
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