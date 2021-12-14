package com.law.rightive.utils;

import java.util.HashMap;
import java.util.Map;

public class UserUtils {
    public static final int CLIENT = 1000;
    public static final int LAWYER = 1001;

    private static String firstName;
    private static String lastName;
    private static String email;
    private static int userType;
    private static String mobileNumber;
    private static String userTypeString;
    private static UserUtils userUtils = null;
    private final Map<String, Object> userDetailsMap = new HashMap<String, Object>();

    private UserUtils() {
    }

    public Map<String, Object> getUserDetailsMap() {
        userDetailsMap.put("firstName", UserUtils.firstName);
        userDetailsMap.put("lastName", UserUtils.lastName);
        userDetailsMap.put("email", UserUtils.email);
        userDetailsMap.put("userType", UserUtils.userType);
        userDetailsMap.put("userTypeString", UserUtils.userTypeString);
        return userDetailsMap;
    }

    public static UserUtils getInstance() {
        if (userUtils == null) {
            userUtils = new UserUtils();
        }
        return userUtils;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        UserUtils.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        UserUtils.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserUtils.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        UserUtils.mobileNumber = "+91" + mobileNumber;
    }

    public String getUserTypeString() {
        return userTypeString;
    }

    public void setUserTypeString(String userTypeString) {
        UserUtils.userTypeString = userTypeString;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        UserUtils.userType = userType;
    }
}