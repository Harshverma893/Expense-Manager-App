package com.example.expensemanager.Utills;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String DateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return simpleDateFormat.format(date);
    }
    public static String DateFormatbyMonth(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, yyyy");
        return simpleDateFormat.format(date);
    }
}
