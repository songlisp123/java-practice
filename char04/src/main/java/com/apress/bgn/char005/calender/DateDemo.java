package com.apress.bgn.char005.calender;


//
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateDemo {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy//MM//dd HH:mm:ss");
        Date currentDate = new Date();
        System.out.println("今天"+sdf.format(currentDate));

        Date brith = new Date(01,9,16);
        System.out.println("生日是"+sdf.format(brith));

        int day = brith.getDay();
        System.out.println(day);

        int month = brith.getMonth();
        System.out.println(month);

        int year = brith.getYear();
        System.out.println(year);

        try {
            Date test2 = sdf.parse("2024//5//23 12:30:36");
        } catch(ParseException e) {
            e.printStackTrace();
        }





    }
}
