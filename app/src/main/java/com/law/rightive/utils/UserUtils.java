package com.law.rightive.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static List<String> primarySpecializations;
    private static List<String> secondarySpecializations;
    private final Map<String, Object> userDetailsMap = new HashMap<String, Object>();
    private final Map<String, Object> lawyersSpecializationsMap = new HashMap<String, Object>();

    private UserUtils() {
    }

    public Map<String, Object> getUserDetailsMap() {
        userDetailsMap.put("firstName", firstName);
        userDetailsMap.put("lastName", lastName);
        userDetailsMap.put("email", email);
        userDetailsMap.put("userType", userType);
        userDetailsMap.put("userTypeString", userTypeString);
        if (userType == LAWYER) {
            lawyersSpecializationsMap.put("primary", primarySpecializations);
            lawyersSpecializationsMap.put("secondary", secondarySpecializations);
            userDetailsMap.put("specializations", lawyersSpecializationsMap);
        }
        return userDetailsMap;
    }

    public Map<String, Object> getTestClientMap() {
        Map<String, Object> testClientMap = new HashMap<String, Object>();
        testClientMap.put("firstName", "Client");
        testClientMap.put("lastName", "Test");
        testClientMap.put("email", "client@rightive.com");
        testClientMap.put("userType", UserUtils.CLIENT);
        testClientMap.put("userTypeString", "CLIENT");
        return testClientMap;
    }

    public Map<String, Object> getTestLawyerMap() {
        Map<String, Object> testLawyerMap = new HashMap<String, Object>();
        testLawyerMap.put("firstName", "Lawyer");
        testLawyerMap.put("lastName", "Test");
        testLawyerMap.put("email", "lawyer@rightive.com");
        testLawyerMap.put("userType", UserUtils.LAWYER);
        testLawyerMap.put("userTypeString", "LAWYER");
        if (Integer.parseInt(Objects.requireNonNull(testLawyerMap.get("userType")).toString()) == LAWYER) {
            lawyersSpecializationsMap.put("primary", primarySpecializations);
            lawyersSpecializationsMap.put("secondary", secondarySpecializations);
            testLawyerMap.put("specializations", lawyersSpecializationsMap);
        }
        return testLawyerMap;
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

    public List<String> getPrimarySpecializations() {
        return primarySpecializations;
    }

    public void setPrimarySpecializations(List<String> primarySpecializations) {
        UserUtils.primarySpecializations = primarySpecializations;
    }

    public List<String> getSecondarySpecializations() {
        return secondarySpecializations;
    }

    public void setSecondarySpecializations(List<String> secondarySpecializations) {
        UserUtils.secondarySpecializations = secondarySpecializations;
    }
}