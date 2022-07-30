package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class StoreInformationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView image_store;
    private TextView add_store, time_store, address_store, favorite_store, share_store, phone_store;
    private Button btnOrder;
    private boolean isFavorite = false;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_infomation);
        initUI();
        setToolBar();
        getDataIntent();
    }
    public void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Thông tin cửa hàng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getDataIntent() {
        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra("data");

        Picasso.get()
                .load(store.getUrlPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(image_store);

        String[] list = store.getAddress().split(", ");
        add_store.setText(list[0]);

        time_store.setText("Giờ mở của: " + store.getStart() + " - " + store.getEnd());
        address_store.setText(store.getAddress());
        phone_store.setText("Liên hệ: " + store.getPhone());

        favorite_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFavorite();
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.shoppingCart.setStore(store);
                Intent intent = new Intent(StoreInformationActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setFavorite();
    }
    public void initUI() {
        toolbar = findViewById(R.id.toolbar);
        image_store = findViewById(R.id.image_store);
        add_store = findViewById(R.id.add_store);
        time_store = findViewById(R.id.time_store);
        address_store = findViewById(R.id.address_store);
        favorite_store = findViewById(R.id.favorite_store);
        share_store = findViewById(R.id.share_store);
        phone_store = findViewById(R.id.phone_store);
        btnOrder = findViewById(R.id.btnOrder);
    }
    private void setFavorite() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // ton tai favorite va có id của sản phẩm
                if(snapshot.child("favoriteStore").exists()) {
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favoriteStore").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if(store.getId().equals(data.getKey())) {
                                    favorite_store.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24,
                                                                                        0, 0, 0);
                                    isFavorite = true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    isFavorite = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void actionFavorite() {
        if(isFavorite) {
            // remove
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favoriteStore").child(store.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(StoreInformationActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    favorite_store.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_no_favorite, 0, 0, 0);

                    isFavorite = false;
                }
            });
        } else {
            // add
            Map map = new HashMap();
            map.put(store.getId(), store);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favoriteStore").updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(StoreInformationActivity.this, "Thêm vào yêu thích thành công", Toast.LENGTH_SHORT).show();
                        favorite_store.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0);
                        isFavorite = true;
                    }
                }
            });
        }
    }
}