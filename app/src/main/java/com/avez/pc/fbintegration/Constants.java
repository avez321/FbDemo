package com.avez.pc.fbintegration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 12/30/2016.
 */

public class Constants {
    public  static List<String> FB_REQUEST_PARMS=Arrays.asList("public_profile","email", "user_friends","user_location","user_birthday");
    public  static  String FB_FIELDS="id,name,email,first_name,last_name,location,gender,picture,birthday,age_range";
    public  static  String FIELDS="fields";
}
