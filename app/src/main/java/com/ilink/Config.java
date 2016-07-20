package com.ilink;

/**
 * Created by capp on 19/02/16.
 */
public class Config {

    //URL to our login.php file
    public static final String LOGIN_URL = "http://ilink-app.com/app/index.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TAG = "tag";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String LASTNAME_SHARED_PREF = "lastname";
    public static final String FIRSTNAME_SHARED_PREF = "firstname";
    public static final String PHONE_SHARED_PREF = "phone";
    public static final String CATEGORY_SHARED_PREF = "category";
    public static final String COUNTRY_CODE_SHARED_PREF = "country_code";
    public static final String NETWORK_SHARED_PREF = "network";
    public static final String MEMBER_CODE_SHARED_PREF = "member_code";
    public static final String CODE_PARRAIN_SHARED_PREF = "member_code";
    public static final String VALIDATION_SHARED_PREF = "validate";
    public static final String VALIDATION_CODE_SHARED_PREF = "validation_code";
    public static final String LATITUDE_SHARED_PREF = "latitude";
    public static final String LONGITUDE_SHARED_PREF = "longitude";
    public static final String MBRE_RESEAU_SHARED_PREF = "member_code";
    public static final String MBRE_SS_RESEAU_SHARED_PREF = "member_code";
    public static final String BALANCE_SHARED_PREF = "balance";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String REGISTERED_SHARED_PREF = "registered";
    public static final String CHECK_BOX_ADVICE_PREF = "checkboxadvice";

    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://192.168.0.101:8888/android_sms/msg91/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://192.168.0.101:8888/android_sms/msg91/verify_otp.php";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "ANHIVE";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}
