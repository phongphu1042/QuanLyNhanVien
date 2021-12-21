package com.example.nhanvien.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.nhanvien.R;
import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.utils.FormatUtil;

import java.util.List;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {

    Activity context;
    int resource;
    List<NhanVien> objects;
    public NhanVienAdapter(@NonNull Activity context, int resource, @NonNull List<NhanVien> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects= objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource,null);
        TextView txtmaNV =item.findViewById(R.id.txtMaNV);
        TextView txttenNV =item.findViewById(R.id.txtTenNV);
        TextView txtSdt =item.findViewById(R.id.txtSdt);
        TextView txtLuongCB =item.findViewById(R.id.txtLuongCB);
        TextView txtLuong =item.findViewById(R.id.txtLuong);
        TextView txtPB =item.findViewById(R.id.txtPB);

        final NhanVien nv =this.objects.get(position);
        txtmaNV.setText(nv.getManv()+"");
        txtPB.setText(nv.getPhongban().toString());
        txttenNV.setText(nv.getTennv());
        txtSdt.setText(nv.getSodt()+"");
        txtLuongCB.setText(FormatUtil.formatNumber(nv.getLuongcb()));
        txtLuong.setText(FormatUtil.formatNumber(nv.getLuong()));

        return item;
    }
}

