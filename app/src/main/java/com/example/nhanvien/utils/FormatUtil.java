package com.example.nhanvien.utils;

import java.text.DecimalFormat;

public class FormatUtil {
    static DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    public static String formatNumber(int num){
        return decimalFormat.format(num);
    }
}
