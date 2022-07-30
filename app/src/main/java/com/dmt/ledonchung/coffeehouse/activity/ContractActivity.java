package com.dmt.ledonchung.coffeehouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ContractAdapter;
import com.dmt.ledonchung.coffeehouse.model.Contract;

import java.util.ArrayList;

public class ContractActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView comment;
    private ArrayList<Contract> contracts;
    private ContractAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        initUI();
        setToolBar();
    }
    public void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("Liên hệ và góp ý");
    }
    public void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        comment = findViewById(R.id.comment);
        contracts = new ArrayList<>();
        contracts.add(new Contract("Tổng đài", "18006936", R.drawable.ic_phone));
        contracts.add(new Contract("Email", "hi@thecoffeehouse.com", R.drawable.ic_email));
        contracts.add(new Contract("Website", "https://thecoffeehouse.com", R.drawable.ic_world));
        contracts.add(new Contract("Facebook", "facebook.com/The.Coffee.House.2014", R.drawable.ic_facebook_contract));
        adapter = new ContractAdapter(contracts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}