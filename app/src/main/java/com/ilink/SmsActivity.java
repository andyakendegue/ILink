package com.ilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.wearable.MessageApi;
import com.google.firebase.messaging.SendException;
import com.ilink.app.AppController;
import com.ilink.helper.PrefManager;
import com.ilink.service.HttpService;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsActivity extends AppCompatActivity implements OnClickListener {
    private static String TAG;
    private ViewPagerAdapter adapter;
    private ImageButton btnEditMobile;
    private Button btnRequestSms;
    private Button btnVerifyOtp;
    private EditText inputEmail;
    private EditText inputMobile;
    private EditText inputName;
    private EditText inputOtp;
    private LinearLayout layoutEditMobile;
    private PrefManager pref;
    private ProgressBar progressBar;
    private TextView txtEditMobile;
    private ViewPager viewPager;

    /* renamed from: com.ilink.SmsActivity.1 */
    class C15721 implements OnPageChangeListener {
        C15721() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.ilink.SmsActivity.2 */
    class C15732 implements Listener<String> {
        C15732() {
        }

        public void onResponse(String response) {
            Log.d(SmsActivity.TAG, response.toString());
            try {
                JSONObject responseObj = new JSONObject(response);
                boolean error = responseObj.getBoolean(MediaRouteProviderProtocol.SERVICE_DATA_ERROR);
                String message = responseObj.getString("message");
                if (error) {
                    Toast.makeText(SmsActivity.this.getApplicationContext(), "Error: " + message, 1).show();
                } else {
                    SmsActivity.this.pref.setIsWaitingForSms(true);
                    SmsActivity.this.viewPager.setCurrentItem(1);
                    SmsActivity.this.txtEditMobile.setText(SmsActivity.this.pref.getMobileNumber());
                    SmsActivity.this.layoutEditMobile.setVisibility(0);
                    Toast.makeText(SmsActivity.this.getApplicationContext(), message, 0).show();
                }
                SmsActivity.this.progressBar.setVisibility(8);
            } catch (JSONException e) {
                Toast.makeText(SmsActivity.this.getApplicationContext(), "Error: " + e.getMessage(), 1).show();
                SmsActivity.this.progressBar.setVisibility(8);
            }
        }
    }

    /* renamed from: com.ilink.SmsActivity.3 */
    class C15743 implements ErrorListener {
        C15743() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.e(SmsActivity.TAG, "Error: " + error.getMessage());
            Toast.makeText(SmsActivity.this.getApplicationContext(), error.getMessage(), 0).show();
            SmsActivity.this.progressBar.setVisibility(8);
        }
    }

    /* renamed from: com.ilink.SmsActivity.4 */
    class C15754 extends StringRequest {
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$mobile;
        final /* synthetic */ String val$name;

        C15754(int x0, String x1, Listener x2, ErrorListener x3, String str, String str2, String str3) {
            this.val$name = str;
            this.val$email = str2;
            this.val$mobile = str3;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put("name", this.val$name);
            params.put(TamponGeolocatedActivity.KEY_EMAIL, this.val$email);
            params.put("mobile", this.val$mobile);
            Log.e(SmsActivity.TAG, "Posting params: " + params.toString());
            return params;
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        ViewPagerAdapter() {
        }

        public int getCount() {
            return 2;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {
            int resId = 0;
            switch (position) {
                case MessageApi.FILTER_LITERAL /*0*/:
                    resId = C1558R.id.layout_sms;
                    break;
                case SendException.ERROR_INVALID_PARAMETERS /*1*/:
                    resId = C1558R.id.layout_otp;
                    break;
            }
            return SmsActivity.this.findViewById(resId);
        }
    }

    static {
        TAG = SmsActivity.class.getSimpleName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_sms);
        this.viewPager = (ViewPager) findViewById(C1558R.id.viewPagerVertical);
        this.inputName = (EditText) findViewById(C1558R.id.inputName);
        this.inputEmail = (EditText) findViewById(C1558R.id.inputEmail);
        this.inputMobile = (EditText) findViewById(C1558R.id.inputMobile);
        this.inputOtp = (EditText) findViewById(C1558R.id.inputOtp);
        this.btnRequestSms = (Button) findViewById(C1558R.id.btn_request_sms);
        this.btnVerifyOtp = (Button) findViewById(C1558R.id.btn_verify_otp);
        this.progressBar = (ProgressBar) findViewById(C1558R.id.progressBar);
        this.btnEditMobile = (ImageButton) findViewById(C1558R.id.btn_edit_mobile);
        this.txtEditMobile = (TextView) findViewById(C1558R.id.txt_edit_mobile);
        this.layoutEditMobile = (LinearLayout) findViewById(C1558R.id.layout_edit_mobile);
        this.btnEditMobile.setOnClickListener(this);
        this.btnRequestSms.setOnClickListener(this);
        this.btnVerifyOtp.setOnClickListener(this);
        this.layoutEditMobile.setVisibility(8);
        this.pref = new PrefManager(this);
        if (this.pref.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(335544320);
            startActivity(intent);
            finish();
        }
        this.adapter = new ViewPagerAdapter();
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setOnPageChangeListener(new C15721());
        if (this.pref.isWaitingForSms()) {
            this.viewPager.setCurrentItem(1);
            this.layoutEditMobile.setVisibility(0);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C1558R.id.btn_request_sms /*2131689719*/:
                validateForm();
            case C1558R.id.btn_verify_otp /*2131689722*/:
                verifyOtp();
            case C1558R.id.btn_edit_mobile /*2131689726*/:
                this.viewPager.setCurrentItem(0);
                this.layoutEditMobile.setVisibility(8);
                this.pref.setIsWaitingForSms(false);
            default:
        }
    }

    private void validateForm() {
        String name = this.inputName.getText().toString().trim();
        String email = this.inputEmail.getText().toString().trim();
        String mobile = this.inputMobile.getText().toString().trim();
        if (name.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", 0).show();
        } else if (isValidPhoneNumber(mobile)) {
            this.progressBar.setVisibility(0);
            this.pref.setMobileNumber(mobile);
            requestForSMS(name, email, mobile);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", 0).show();
        }
    }

    private void requestForSMS(String name, String email, String mobile) {
        StringRequest strReq = new C15754(1, Config.URL_REQUEST_SMS, new C15732(), new C15743(), name, email, mobile);
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000, 1, TextTrackStyle.DEFAULT_FONT_SCALE));
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void verifyOtp() {
        String otp = this.inputOtp.getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", 0).show();
            return;
        }
        Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
        grapprIntent.putExtra("otp", otp);
        startService(grapprIntent);
    }

    private static boolean isValidPhoneNumber(String mobile) {
        return mobile.matches("^[0-9]{10}$");
    }
}
