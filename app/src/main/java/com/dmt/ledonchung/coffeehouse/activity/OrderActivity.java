package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ProductSelectedAdapter;
import com.dmt.ledonchung.coffeehouse.model.Cart;
import com.dmt.ledonchung.coffeehouse.model.Date;
import com.dmt.ledonchung.coffeehouse.model.RandomString;
import com.dmt.ledonchung.coffeehouse.model.ShoppingCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private Button btnChange, btnAdd;
    private static TextView tvAddressFirst;
    private static TextView tvAddress;
    private TextView userName;
    private TextView phoneNumber;
    private TextView intoMoney;
    private TextView transportFee;
    private TextView paymentAmount;
    private TextView promotion;
    private TextView removeCarts;
    private static TextView publicPay;
    private static TextView deliveryWay;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private LinearLayout changeLayout;
    private ProductSelectedAdapter adapter;
    private TextView order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initUI();
        setToolbar();
        getDeliveryWay("Chọn địa điểm");
        setData();
        showPublicPay();
        eventButton();
    }

    public void initUI() {
        tvAddress = findViewById(R.id.tvAddress);
        changeLayout = findViewById(R.id.changeLayout);
        removeCarts = findViewById(R.id.removeCarts);
        toolbar = findViewById(R.id.toolbar);
        deliveryWay = findViewById(R.id.deliveryWay);
        btnChange = findViewById(R.id.btnChange);
        btnAdd = findViewById(R.id.btnAdd);
        order = findViewById(R.id.order);
        tvAddressFirst = findViewById(R.id.tvAddressFirst);
        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.phoneNumber);
        intoMoney = findViewById(R.id.intoMoney);
        transportFee = findViewById(R.id.transportFee);
        paymentAmount = findViewById(R.id.paymentAmount);
        promotion = findViewById(R.id.promotion);
        publicPay = findViewById(R.id.publicPay);
        adapter = new ProductSelectedAdapter(MainActivity.shoppingCart.getCarts());
        adapter.notifyDataSetChanged();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đặt hàng");;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setData() {
        String name = MainActivity.shoppingCart.getUser().getFirstName() + " " + MainActivity.shoppingCart.getUser().getLastName();
        userName.setText("Người nhận: "+ name);
        String phone = MainActivity.shoppingCart.getUser().getPhoneNumber();
        phoneNumber.setText("Số điện thoại: "+ phone);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        intoMoney.setText(decimalFormat.format(MainActivity.shoppingCart.getTotalPrice())+"đ");

        transportFee.setText("15,000đ");

        double pay = MainActivity.shoppingCart.getTotalPrice() + 15000;
        paymentAmount.setText(decimalFormat.format(pay) + "đ");


    }
    public static void getDeliveryWay(String df) {
        String delivery = MainActivity.shoppingCart.getDeliveryAway();
        if(MainActivity.shoppingCart.getAddressDelivery() != null) {
            df = MainActivity.shoppingCart.getAddressDelivery();
        }
        if(delivery != null) {
            if(delivery.equals("deliver")) {
                deliveryWay.setText("Giao hàng");

                tvAddressFirst.setVisibility(View.GONE);
                tvAddress.setText(df);
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_forward_ios_24, 0);

            } else {
                tvAddressFirst.setVisibility(View.VISIBLE);
                deliveryWay.setText("Đến lấy");
                String addressStore = MainActivity.shoppingCart.getStore().getAddress();
                String[] arrayAddress = addressStore.split(", ");

                tvAddress.setText(addressStore);
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAddressFirst.setText(arrayAddress[0]);
            }
        } else {
            deliveryWay.setText("Cách giao hàng");
        }
    }
    public void eventButton() {
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delivery = MainActivity.shoppingCart.getDeliveryAway();
                String address = MainActivity.shoppingCart.getAddressDelivery();

                if(delivery == null) {
                    // null
                    Toast.makeText(OrderActivity.this, "Vui lòng chọn cách giao hàng", Toast.LENGTH_SHORT).show();
                    return;
                } else if (delivery.equals("deliver")) {
                    if(address == null || address.equals("")) {
                        Toast.makeText(OrderActivity.this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                String publicPay = MainActivity.shoppingCart.getPublicPay();
                if(publicPay == null || publicPay.equals("")) {
                    Toast.makeText(OrderActivity.this, "Vui lòng chọn hình thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }


                showDialogConfirmOrder();
            }
        });
        removeCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Xóa giỏ hàng")
                        .setMessage("Bạn có muốn xóa giỏ hàng này không?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.shoppingCart.getCarts().clear();
                                MainActivity.getInformationCart();
                                MainActivity.shoppingCart.setTotalPrice(0);
                                Toast.makeText(OrderActivity.this, "Xóa giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .create()
                        .show();


            }
        });
        publicPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, PublicPayActivity.class);
                startActivity(intent);
            }
        });
        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, PromotionActivity.class);
                startActivity(intent);
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = MainActivity.shoppingCart.getDeliveryAway();
                if(a.equals("deliver")) {
                    Intent intent = new Intent(OrderActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    // Chọn cửa hàng
                    Intent intent = new Intent(OrderActivity.this, StoreChooseActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void showDialogConfirmOrder() {
        new AlertDialog.Builder(OrderActivity.this)
                .setTitle("Xác nhận mua hàng")
                .setMessage("Bạn có muốn đặt sản phẩm này")
                .setNegativeButton("Không", null)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pushInformationOrderToDatabase();
                    }
                })
                .show();
    }

    private void pushInformationOrderToDatabase() {

        ShoppingCart cart = MainActivity.shoppingCart;
        Calendar c = Calendar.getInstance();

        DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        String timeFormat = formatTime.format(c.getTime());
        String arrayTime[] = timeFormat.split(":");
        timeFormat = (Integer.parseInt(arrayTime[0]) - 1) + ":" + arrayTime[1] + ":" + arrayTime[2];

        DateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
        String stringDate = formatDate.format(c.getTime());
        String arrayDate[] = stringDate.split("/");


        Date dateOrder = new Date(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]) + 2000, timeFormat);

        MainActivity.shoppingCart.setDateOrder(dateOrder);
        cart.setId(RandomString.randomAlphaNumeric(10));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("orders")
                .child(cart.getId())
                .updateChildren(cart.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        MainActivity.shoppingCart.getCarts().clear();
                        showDialogSuccess();
                    }
                });
    }

    private void showDialogSuccess() {
        View view = LayoutInflater.from(OrderActivity.this).inflate(R.layout.layout_success,null, false);
        Dialog dialog = new Dialog(OrderActivity.this);
        dialog.setContentView(view);
        dialog.setTitle("Success");
        dialog.setCancelable(false);
        Button confirm = dialog.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.getInformationCart();
                finish();
            }
        });
        dialog.show();

    }

    public void showPopupMenu(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.menu_select_delivery_away);
        menu.show();
    }
    public static void showPublicPay() {
        String result = MainActivity.shoppingCart.getPublicPay();
        if(result != null) {
            if(result.equals("pay")) {
                publicPay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visa, 0, R.drawable.ic_baseline_arrow_forward_ios_24, 0);
                publicPay.setText("Thẻ ngân hàng");
            } else {
                publicPay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, R.drawable.ic_baseline_arrow_forward_ios_24, 0);
                publicPay.setText("Tiền mặt");
            }
            publicPay.setCompoundDrawablePadding(10);
            publicPay.setTextColor(Color.BLACK);
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delivery: {
                deliveryWay.setText("Giao hàng");
                tvAddressFirst.setVisibility(View.GONE);
                if(MainActivity.shoppingCart.getAddressDelivery() != null && !MainActivity.shoppingCart.getAddressDelivery().equals("")) {
                    tvAddress.setText(MainActivity.shoppingCart.getAddressDelivery());
                } else {
                    tvAddress.setText("Chọn địa điểm");
                }
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_forward_ios_24, 0);
                MainActivity.shoppingCart.setDeliveryAway("deliver");

                break;
            }
            case R.id.carried: {
                deliveryWay.setText("Đến lấy");
                tvAddressFirst.setVisibility(View.VISIBLE);

                MainActivity.shoppingCart.setDeliveryAway("carriedaway");
                String addressStore = MainActivity.shoppingCart.getStore().getAddress();
                String[] arrayAddress = addressStore.split(", ");

                tvAddress.setText(addressStore);
                tvAddressFirst.setText(arrayAddress[0]);
                break;
            }
        }
        return true;
    }
}