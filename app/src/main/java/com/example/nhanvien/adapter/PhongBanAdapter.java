package com.example.nhanvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.nhanvien.R;
import com.example.nhanvien.model.NhanVien;
import com.example.nhanvien.model.PhongBan;
import com.example.nhanvien.utils.FormatUtil;

import java.util.List;

public class PhongBanAdapter extends ArrayAdapter<PhongBan> {
    Activity context;
    int resource;
    List<PhongBan> objects;

    public PhongBanAdapter(@NonNull Context context, int resource, @NonNull List<PhongBan> objects) {
        super(context, resource, objects);
        this.context=(Activity) context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource,null);
        TextView txtmaPB =item.findViewById(R.id.txtMaPB);
        TextView txttenPB =item.findViewById(R.id.txtTenPB);
        TextView txtThuocTinh =item.findViewById(R.id.txtThuocTinh);
        TextView thuoctinh=item.findViewById(R.id.thuoctinh);

        final PhongBan pb =this.objects.get(position);
        txtmaPB.setText(pb.getMaphongban());
        txttenPB.setText(pb.getTenphongban());
        txtThuocTinh.setText(FormatUtil.formatNumber(pb.getThuoctinh()));
        if (pb.getMaphongban().trim().equals("KD")){
            thuoctinh.setText("Doanh thu: ");
        }else if(pb.getMaphongban().trim().equals("NS")){
            thuoctinh.setText("Số nhân viên quản lý:");
        }else{
            thuoctinh.setText("Trợ cấp: ");
        }

        return item;
    }
}
