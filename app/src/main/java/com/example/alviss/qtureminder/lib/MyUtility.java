package com.example.alviss.qtureminder.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Alviss on 25/03/2017.
 */

public final class MyUtility {
    public static String getCurrentTime(){
        String hour24;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();

        //Muốn xuất Giờ:Phút theo kiểu 24h
        String strDateFormat24 = "HH:mm";
        SimpleDateFormat sdf = null;

        //Tạo đối tượng SimpleDateFormat với định dạng 24
        sdf = new SimpleDateFormat(strDateFormat24);
        hour24 = sdf.format(now.getTime());
        return hour24;
    }
    public static String getCurrentDate(){
        String date;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();
        //Lấy ngày theo kiểu ngày/tháng/năm
        String strDateFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        date = sdf.format(now.getTime());
        return date;
    }
    public static String getCurrentDateTime(){
        String datetime;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();
        //Lấy ngày theo kiểu ngày/tháng/năm
        String strDateFormat = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        datetime = sdf.format(now.getTime());
        return datetime;
    }
}
