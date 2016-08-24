package com.ilink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.ilink.lib.DatabaseHandler;

public class ContenuTestActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private TextView textView1;
    private TextView textView2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_contenu_test);
        this.textView1.findViewById(C1558R.id.textView10);
        this.textView2.findViewById(C1558R.id.textView11);
    }
}
