package com.example.disciplineYourMoney.Activity.helper;

import java.text.SimpleDateFormat;

import static java.lang.System.currentTimeMillis;

public class DateUtil {
//
    public static String dateAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr  = simpleDateFormat.format( date );
        return dateStr;

    }

    public static String MonthYearDateChoice(  String date ){
        String returnDate [] = date.split("/");
        String day    = returnDate[ 0 ];
        String month  = returnDate[ 1 ];
        String year   = returnDate[ 2 ];

        String monthYear = month + year;

        return monthYear;
    }
}
