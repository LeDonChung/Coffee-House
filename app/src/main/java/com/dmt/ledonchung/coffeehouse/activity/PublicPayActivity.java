package com.dmt.ledonchung.coffeehouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PublicPayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioButton money, pay;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_pay);
        initUI();
        setToolbar();
        setChecked();
        eventButton();
    }
    public void setChecked() {
        String result = MainActivity.shoppingCart.getPublicPay();
        if(result != null) {
            if(result.equals("pay")) {
                pay.setChecked(true);
                money.setChecked(false);
            } else {
                pay.setChecked(false);
                money.setChecked(true);
            }

        }
    }
    public void eventButton() {
        money.setOnCheckedChangeListener(listenerRadio);
        pay.setOnCheckedChangeListener(listenerRadio);
    }
    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if(compoundButton.getText().toString().indexOf("Tiền mặt") >= 0) {
                    MainActivity.shoppingCart.setPublicPay("money");
                } else {
                    MainActivity.shoppingCart.setPublicPay("pay");
                }
                OrderActivity.showPublicPay();
                finish();
            }
        }
    };
    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initUI() {
        toolbar = findViewById(R.id.toolbar);

        money = findViewById(R.id.money);
        pay = findViewById(R.id.pay);
        radioGroup = findViewById(R.id.radioGroup);


    }
}