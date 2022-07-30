package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.HistoryOrderAdapter;
import com.dmt.ledonchung.coffeehouse.model.ShoppingCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public interface SendShoppingCart{
        void send(ShoppingCart shoppingCart);
    }
    private TextView cartEmpty;
    private RecyclerView recyclerView;
    private ArrayList<ShoppingCart> shoppingCarts;
    private HistoryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        initUI();
        setToolbar();
        getDataOrder();

    }
    public void isEmpty() {
        if(shoppingCarts.size() < 1) {
            recyclerView.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);
        }
    }

    public void initUI() {
        cartEmpty = findViewById(R.id.cartEmpty);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        shoppingCarts = new ArrayList<>();
        adapter = new HistoryOrderAdapter(shoppingCarts, new SendShoppingCart() {
            @Override
            public void send(ShoppingCart shoppingCart) {
                Intent intent = new Intent(HistoryOrderActivity.this, OrderReActivity.class);
                intent.putExtra("data", shoppingCart);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch sửa mua hàng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void getDataOrder() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingCarts.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    ShoppingCart shoppingCart = data.getValue(ShoppingCart.class);
                    shoppingCarts.add(shoppingCart);
                    adapter.notifyDataSetChanged();
                }
                isEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}