package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ProductSelectedAdapter;
import com.dmt.ledonchung.coffeehouse.model.ShoppingCart;
import com.dmt.ledonchung.coffeehouse.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class OrderReActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvAddress, tvAddressFirst, userName, phoneNumber, time, reOrder, intoMoney, transportFee, paymentAmount, publicPay;
    private RecyclerView recyclerView;
    private ProductSelectedAdapter adapter;
    private ShoppingCart shoppingCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_re);
        getDataIntent();
        initUI();
        setToolbar();
        getDataShoppingCart();
        onClickListener();
    }
    public void onClickListener() {
        reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmOrder();
            }
        });
    }
    private void showDialogConfirmOrder() {
        new AlertDialog.Builder(OrderReActivity.this)
                .setTitle("Đặt lại đơn hàng")
                .setMessage("Bạn có muốn đặt lại đơn hàng này")
                .setNegativeButton("Không", null)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToOrderActivity();
                    }
                })
                .show();
    }

    private void goToOrderActivity() {
        // set data shoppingCart
        double totalPrice = shoppingCart.getTotalPrice();
        MainActivity.shoppingCart.setTotalPrice(totalPrice);

        MainActivity.shoppingCart.setStore(shoppingCart.getStore());

        MainActivity.shoppingCart.setDeliveryAway(shoppingCart.getDeliveryAway());

        MainActivity.shoppingCart.setAddressDelivery(shoppingCart.getAddressDelivery());

        MainActivity.shoppingCart.setUser(shoppingCart.getUser());

        MainActivity.shoppingCart.setPublicPay(shoppingCart.getPublicPay());

        MainActivity.shoppingCart.setCarts(shoppingCart.getCarts());

        Intent intent = new Intent(OrderReActivity.this, OrderActivity.class);
        startActivity(intent);
    }

    private void getDataShoppingCart() {
        String delivery = shoppingCart.getDeliveryAway();

        if(delivery != null) {
            if(delivery.equals("deliver")) {
                tvAddressFirst.setVisibility(View.GONE);
                tvAddress.setText(shoppingCart.getAddressDelivery());
            } else {
                tvAddressFirst.setVisibility(View.VISIBLE);
                String addressStore = shoppingCart.getStore().getAddress();
                String[] arrayAddress = addressStore.split(", ");

                tvAddressFirst.setText(arrayAddress[0]);
                tvAddress.setText(addressStore);
            }
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String name = user.getFirstName() + " " + user.getLastName();
                userName.setText("Người nhận: "+ name);
                String phone = user.getPhoneNumber();
                phoneNumber.setText("Số điện thoại: "+ phone);
                shoppingCart.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        time.setText("Thời gian: " + shoppingCart.getDateOrder().getHour());


        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        intoMoney.setText(decimalFormat.format(shoppingCart.getTotalPrice())+"đ");
        transportFee.setText("15,000đ");
        double pay = shoppingCart.getTotalPrice() + 15000;
        paymentAmount.setText(decimalFormat.format(pay) + "đ");


        String result = shoppingCart.getPublicPay();
        if(result != null) {
            if(result.equals("pay")) {
                publicPay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visa, 0, 0, 0);
                publicPay.setText("Thẻ ngân hàng");
            } else {
                publicPay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
                publicPay.setText("Tiền mặt");
            }
            publicPay.setCompoundDrawablePadding(10);
            publicPay.setTextColor(Color.BLACK);
        }


    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            shoppingCart = (ShoppingCart) intent.getSerializableExtra("data");
        }
    }

    public void initUI() {
        reOrder = findViewById(R.id.reOrder);
        time = findViewById(R.id.time);
        tvAddress = findViewById(R.id.tvAddress);
        toolbar = findViewById(R.id.toolbar);
        tvAddressFirst = findViewById(R.id.tvAddressFirst);
        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        intoMoney = findViewById(R.id.intoMoney);
        transportFee = findViewById(R.id.transportFee);
        paymentAmount = findViewById(R.id.paymentAmount);
        publicPay = findViewById(R.id.publicPay);

        adapter = new ProductSelectedAdapter(shoppingCart.getCarts());
        adapter.notifyDataSetChanged();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin đơn hàng");;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}