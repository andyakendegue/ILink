package com.ilink.service;

import android.app.IntentService;
import android.content.Intent;
import com.ilink.Config;
import com.ilink.app.AppController;

public class HttpService extends IntentService {
    private static String TAG;

    static {
        TAG = HttpService.class.getSimpleName();
    }

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            verifyOtp(intent.getStringExtra("otp"));
        }
    }

    private void verifyOtp(String otp) {
        AppController.getInstance().addToRequestQueue(new 3(this, 1, Config.URL_VERIFY_OTP, new 1(this), new 2(this), otp));
    }
}
