package com.example.nhanvien.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhanvien.R;
import com.example.nhanvien.adapter.PhongBanAdapter;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.utils.AppDatabase;
import com.example.nhanvien.utils.DBConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class PhongBanActivity extends AppCompatActivity {


    private ListView lvPhongBan;
    private PhongBanAdapter adapter;
    private TextView txtThuocTinh;
    private EditText txtMaPB,txtTenPB,txtValueThuocTinh;
    private Button btnLuuPB,btnClear;
    private AppDatabase db;
    private ArrayList<PhongBan> dsPhongBan;
    private PhongBan selectPhongBan =null;
    int requestCode =1000, resultCode=10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_ban);

        DBConfigUtil.copyDatabaseFromAssets(PhongBanActivity.this);
        init();
        showListView();
        addEvents();

    }
    private void init(){
        db= AppDatabase.getAppDataBase(this);
        dsPhongBan=new ArrayList<>();
        txtMaPB=findViewById(R.id.txtMaPB);
        txtTenPB=findViewById(R.id.txtTenPB);
        txtThuocTinh = findViewById(R.id.txtEditThuocTinh);
        txtValueThuocTinh=findViewById(R.id.txtValueThuocTinh);
        btnLuuPB=findViewById(R.id.btnLuuPB);
        btnClear=findViewById(R.id.btnClear);
        lvPhongBan=findViewById(R.id.lvPhongBan);
    }
    private void showListView(){
        dsPhongBan.clear();
        dsPhongBan.addAll(db.phongBanDao().getAll());
        adapter=new PhongBanAdapter(PhongBanActivity.this,R.layout.item_phongban,dsPhongBan);
        lvPhongBan.setAdapter(adapter);
    }
    private void addEvents(){
        lvPhongBan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PhongBan phongBan=dsPhongBan.get(position);
                xulyXoa(phongBan);
                return true;
            }
        });
        lvPhongBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhongBan phongBan = dsPhongBan.get(position);
                focus(phongBan);
            }
        });
        btnLuuPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(xulyDuLieu()){

                }else {
                   if (selectPhongBan==null){
                       xulyluu();
                       Toast.makeText(PhongBanActivity.this, "th??m th??nh c??ng", Toast.LENGTH_SHORT).show();
                       clear();
                       showListView();
                   }else {
                       xulyUpdate();
                       Toast.makeText(PhongBanActivity.this, "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
                       clear();
                       showListView();
                   }
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 clear();
            }
        });
    }

    /**
     * Ki???m tra t??nh ????ng ?????n c???a d??? li???u
     * c??c tr?????ng EditText tr?????c khi th??m/c???p nh???t kh??ng ???????c r???ng.
     * Kh??ng ???????c th??m tr??ng m?? ph??ng ban
     * @return
     */
    private boolean xulyDuLieu(){
        if(txtMaPB.getText().toString().equals("")){
            Toast.makeText(PhongBanActivity.this, "Kh??ng ???????c b??? tr???ng M??", Toast.LENGTH_SHORT).show();
            txtMaPB.requestFocus();
            return true;
        }else if (txtTenPB.getText().toString().equals("")){
            Toast.makeText(PhongBanActivity.this, "Kh??ng ???????c b??? tr???ng T??n", Toast.LENGTH_SHORT).show();
            txtTenPB.requestFocus();
            return true;
        }else if (txtValueThuocTinh.getText().toString().equals("")){
            txtValueThuocTinh.setHint("Kh??ng ???????c b??? tr???ng !");
            txtValueThuocTinh.requestFocus();
            return true;
        }else if(db.phongBanDao().findPhongBan(txtMaPB.getText().toString().trim()).size()>0){
                Toast.makeText(PhongBanActivity.this, "M?? ph??ng ban ???? t???n t???i", Toast.LENGTH_SHORT).show();
                txtMaPB.setText("");
                txtMaPB.requestFocus();
                return true;
        }
        return false;
    }

    /**
     * ????a c??c editText v??? r???ng
     */
    private void clear(){
        selectPhongBan=null;
        txtMaPB.setEnabled(true);
        txtMaPB.setText("");
        txtMaPB.requestFocus();
        txtTenPB.setText("");
        txtThuocTinh.setText("tr??? c???p");
        txtValueThuocTinh.setText("5000000");
        btnLuuPB.setText("Th??m");
        btnClear.setVisibility(View.INVISIBLE);
    }

    /**
     * Truy???n th??ng tin ph??ng ban ???????c ch???n l??n editText
     * @param phongBan
     */
    private void focus(PhongBan phongBan){
        txtMaPB.setText(phongBan.getMaphongban());
        txtMaPB.setEnabled(false);
        txtTenPB.setText(phongBan.getTenphongban());
        if (phongBan.getMaphongban().equals("NS")){
            txtThuocTinh.setText("nh??n vi??n qu???n l??");
        }else if (phongBan.getMaphongban().equals("KD")){
            txtThuocTinh.setText("doanh thu");
        }else{
            txtThuocTinh.setText("tr??? c???p");
        }
        txtValueThuocTinh.setText(phongBan.getThuoctinh()+"");
        txtValueThuocTinh.requestFocus();
        btnLuuPB.setText("C???p nh???t");
        selectPhongBan=phongBan;
        btnClear.setVisibility(View.VISIBLE);
    }

    /**
     * L???y d??? li???u tr??n editText xu???ng v?? c???p nh???t l???i
     */
    private void xulyUpdate(){
        PhongBan pb =new PhongBan();
        pb.setMaphongban(txtMaPB.getText().toString().trim());
        pb.setTenphongban(txtTenPB.getText().toString().trim());
        pb.setThuoctinh(Integer.parseInt(txtValueThuocTinh.getText().toString()));
        db.phongBanDao().update(pb);
        selectPhongBan=null;
    }

    /**
     * L???y d??? li???u tr??n editText xu???ng th??m m???i
     */
    private void xulyluu() {
        PhongBan pb =new PhongBan();
        pb.setMaphongban(txtMaPB.getText().toString().trim());
        pb.setTenphongban(txtTenPB.getText().toString().trim());
        pb.setThuoctinh(Integer.parseInt(txtValueThuocTinh.getText().toString()));
        db.phongBanDao().insert(pb);
    }

    /**
     * Nh???n v??o ph??ng ban v?? x??a ph??ng ban ????:
     * N???u ph??ng ban m???c ?????nh l?? Kinh Doanh, K??? Thu???t, Nh??n s??? th?? kh??ng ??c x??a.
     * N???u ph??ng ban t???n t???i nh??n vi??n c??ng kh??ng ???????c x??a.
     * @param phongBan
     */
    private void xulyXoa(PhongBan phongBan){
        String ma =phongBan.getMaphongban();
        AlertDialog.Builder b = new AlertDialog.Builder(PhongBanActivity.this);
        if(ma.equals("NS")||ma.equals("KT")||ma.equals("KD")){
            //Thi???t l???p ti??u ?????
            b.setTitle("Th??ng b??o!");
            b.setMessage("Kh??ng ???????c x??a ph??ng "+phongBan.getTenphongban());
            //N??t Cancel
            b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)  {
                    dialog.cancel();
                }
            });
        }else {
            List ds=db.nhanVienDao().nhanVienPhongBan(phongBan.getMaphongban());
            if(ds.size()==0){
                //Thi???t l???p ti??u ?????
                b.setTitle("X??c nh???n");
                b.setMessage("B???n c?? mu???n x??a kh??ng?");
                // N??t Ok
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.phongBanDao().delete(phongBan.getMaphongban());
                        showListView();
                    }
                });
                //N??t Cancel
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)  {
                        dialog.cancel();
                    }
                });
            }else {
                //Thi???t l???p ti??u ?????
                b.setTitle("C???nh b??o!");
                b.setMessage("Ph??ng ban "+phongBan.getTenphongban()+ " c?? "+ds.size()+" nh??n vi??n! Kh??ng th??? x??a");
                //N??t Cancel
                b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)  {
                        dialog.cancel();
                    }
                });
            }
        }
        //T???o dialog
        AlertDialog al = b.create();
        //Hi???n th???
        al.show();
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
                finish();
                break;
            case R.id.item_PhongBan:
                break;
            case  R.id.item_NhanVien:
                Intent intent2= new Intent(PhongBanActivity.this,NhanVienActivity.class);
                startActivityForResult(intent2,1000);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}