package com.example.alviss.qtureminder.lib;

/**
 * Created by Alviss on 01/04/2017.
 */

public class Service_Url {

    public static String hostname = "http://192.168.1.221";
    public static String login = hostname+"/json/login.php";
    public static String createtask = hostname+"/json/createtask.php";
    public static String listtask = hostname+"/json/listitem.php";
    public static String register = hostname+"/json/register.php";
    public static String deletetask = hostname+"/json/deletetask.php";
    public static String updatetask = hostname+"/json/update.php";
    public static String singletask = hostname+"/json/singletask.php";

    public String Format_Date(String date){
        String ngay, thang, nam, result;
        String arr_tach[] = date.split("-");
        nam = arr_tach[0];
        thang = arr_tach[1];
        ngay = arr_tach[2];
        result = ngay+"-"+thang+"-"+nam;
        return result;
    }
    public String Format_time(String time){
        String gio, phut, result;
        String arr_tach[] = time.split(":");
        gio = arr_tach[0];
        phut = arr_tach[1];
        result = gio+":"+phut;
        return result;
    }
}
