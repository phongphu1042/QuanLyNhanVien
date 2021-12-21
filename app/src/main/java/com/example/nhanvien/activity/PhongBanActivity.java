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
                       Toast.makeText(PhongBanActivity.this, "thêm thành công", Toast.LENGTH_SHORT).show();
                       clear();
                       showListView();
                   }else {
                       xulyUpdate();
                       Toast.makeText(PhongBanActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
     * Kiểm tra tính đúng đắn của dữ liệu
     * các trường EditText trước khi thêm/cập nhật không được rỗng.
     * Không được thêm trùng mã phòng ban
     * @return
     */
    private boolean xulyDuLieu(){
        if(txtMaPB.getText().toString().equals("")){
            Toast.makeText(PhongBanActivity.this, "Không được bỏ trống Mã", Toast.LENGTH_SHORT).show();
            txtMaPB.requestFocus();
            return true;
        }else if (txtTenPB.getText().toString().equals("")){
            Toast.makeText(PhongBanActivity.this, "Không được bỏ trống Tên", Toast.LENGTH_SHORT).show();
            txtTenPB.requestFocus();
            return true;
        }else if (txtValueThuocTinh.getText().toString().equals("")){
            txtValueThuocTinh.setHint("Không được bỏ trống !");
            txtValueThuocTinh.requestFocus();
            return true;
        }else if(db.phongBanDao().findPhongBan(txtMaPB.getText().toString().trim()).size()>0){
                Toast.makeText(PhongBanActivity.this, "Mã phòng ban đã tồn tại", Toast.LENGTH_SHORT).show();
                txtMaPB.setText("");
                txtMaPB.requestFocus();
                return true;
        }
        return false;
    }

    /**
     * đưa các editText về rỗng
     */
    private void clear(){
        selectPhongBan=null;
        txtMaPB.setEnabled(true);
        txtMaPB.setText("");
        txtMaPB.requestFocus();
        txtTenPB.setText("");
        txtThuocTinh.setText("trợ cấp");
        txtValueThuocTinh.setText("5000000");
        btnLuuPB.setText("Thêm");
        btnClear.setVisibility(View.INVISIBLE);
    }

    /**
     * Truyền thông tin phòng ban được chọn lên editText
     * @param phongBan
     */
    private void focus(PhongBan phongBan){
        txtMaPB.setText(phongBan.getMaphongban());
        txtMaPB.setEnabled(false);
        txtTenPB.setText(phongBan.getTenphongban());
        if (phongBan.getMaphongban().equals("NS")){
            txtThuocTinh.setText("nhân viên quản lý");
        }else if (phongBan.getMaphongban().equals("KD")){
            txtThuocTinh.setText("doanh thu");
        }else{
            txtThuocTinh.setText("trợ cấp");
        }
        txtValueThuocTinh.setText(phongBan.getThuoctinh()+"");
        txtValueThuocTinh.requestFocus();
        btnLuuPB.setText("Cập nhật");
        selectPhongBan=phongBan;
        btnClear.setVisibility(View.VISIBLE);
    }

    /**
     * Lấy dữ liệu trên editText xuống và cập nhật lại
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
     * Lấy dữ liệu trên editText xuống thêm mới
     */
    private void xulyluu() {
        PhongBan pb =new PhongBan();
        pb.setMaphongban(txtMaPB.getText().toString().trim());
        pb.setTenphongban(txtTenPB.getText().toString().trim());
        pb.setThuoctinh(Integer.parseInt(txtValueThuocTinh.getText().toString()));
        db.phongBanDao().insert(pb);
    }

    /**
     * Nhận vào phòng ban và xóa phòng ban đó:
     * Nếu phòng ban mặc định là Kinh Doanh, Kỹ Thuật, Nhân sự thì không đc xóa.
     * Nếu phòng ban tồn tại nhân viên cũng không được xóa.
     * @param phongBan
     */
    private void xulyXoa(PhongBan phongBan){
        String ma =phongBan.getMaphongban();
        AlertDialog.Builder b = new AlertDialog.Builder(PhongBanActivity.this);
        if(ma.equals("NS")||ma.equals("KT")||ma.equals("KD")){
            //Thiết lập tiêu đề
            b.setTitle("Thông báo!");
            b.setMessage("Không được xóa phòng "+phongBan.getTenphongban());
            //Nút Cancel
            b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)  {
                    dialog.cancel();
                }
            });
        }else {
            List ds=db.nhanVienDao().nhanVienPhongBan(phongBan.getMaphongban());
            if(ds.size()==0){
                //Thiết lập tiêu đề
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có muốn xóa không?");
                // Nút Ok
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.phongBanDao().delete(phongBan.getMaphongban());
                        showListView();
                    }
                });
                //Nút Cancel
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)  {
                        dialog.cancel();
                    }
                });
            }else {
                //Thiết lập tiêu đề
                b.setTitle("Cảnh báo!");
                b.setMessage("Phòng ban "+phongBan.getTenphongban()+ " có "+ds.size()+" nhân viên! Không thể xóa");
                //Nút Cancel
                b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)  {
                        dialog.cancel();
                    }
                });
            }
        }
        //Tạo dialog
        AlertDialog al = b.create();
        //Hiển thị
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