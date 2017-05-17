package com.hyy.payutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fancy.hyypaysdk.pay.PayUtill;
import fancy.hyypaysdk.pay.alipay.AliPayUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PayUtill.init("WX_APP_ID");
    }
}
