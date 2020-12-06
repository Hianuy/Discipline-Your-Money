package com.example.disciplineYourMoney.Activity.helper;

import android.util.Base64;

public class Base64Custom {

    public static String  encodeBase64( String text ){
        // tentar usar trim
       return Base64.encodeToString(text.getBytes(), Base64.DEFAULT ).replaceAll("(\\n|\\r)","");

    }

    public static String decodeBase64(String textEncoded ){
      return new String(Base64.decode( textEncoded, Base64.DEFAULT ));

    }

}
