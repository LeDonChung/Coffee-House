package com.dmt.ledonchung.coffeehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.dmt.ledonchung.coffeehouse.activity.FavoriteActivity;
import com.dmt.ledonchung.coffeehouse.activity.OrderActivity;
import com.dmt.ledonchung.coffeehouse.adapter.ViewPager2Adapter;
import com.dmt.ledonchung.coffeehouse.fragment.CreateUserFragment;
import com.dmt.ledonchung.coffeehouse.model.Cart;
import com.dmt.ledonchung.coffeehouse.model.ShoppingCart;
import com.dmt.ledonchung.coffeehouse.model.Store;
import com.dmt.ledonchung.coffeehouse.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static ShoppingCart shoppingCart;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    public static Button btnPay;
    private ViewPager2 viewPager2;
    private ViewPager2Adapter adapter;
    private static LinearLayout goShoppingCart;
    public BottomNavigationView navigationBottom;
    private User user;
    private Store store;
    static final public int FRAGMENT_HOME = 0;
    static final public int FRAGMENT_ORDER = 1;
    static final public int FRAGMENT_STORE = 2;
    static final public int FRAGMENT_SEARCH = 3;
    static final public int FRAGMENT_OTHER = 4;
    static public  int FRAGMENT_CURRENT = 0;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        getUserToDatabase();
        setToolBarHomePageFragment();
        onClickNavigationBottom();
        eventButton();
        getInformationCart();
    }

    public void goToOrderFragment() {
        viewPager2.setCurrentItem(1);
    }
    public void eventButton() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalPrice = 0;
                for(int i = 0; i < shoppingCart.getCarts().size(); i++) {
                    totalPrice += shoppingCart.getCarts().get(i).getTotalPrice();
                }
                shoppingCart.setTotalPrice(totalPrice);
                if(shoppingCart.getTotalPrice() > 0) {
                    if(shoppingCart.getStore() == null ) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stores");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data: snapshot.getChildren()) {
                                    store = data.getValue(Store.class);
                                    shoppingCart.setStore(store);
                                    break;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClickNavigationBottom() {
        navigationBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home: {
                        if(FRAGMENT_CURRENT != FRAGMENT_HOME) {
                            FRAGMENT_CURRENT = FRAGMENT_HOME;
                            viewPager2.setCurrentItem(0);
                            setToolBarHomePageFragment();
                        }
                        break;
                    }
                    case R.id.nav_order: {

                        if(FRAGMENT_CURRENT != FRAGMENT_ORDER) {
                            FRAGMENT_CURRENT = FRAGMENT_ORDER;
                            viewPager2.setCurrentItem(1);
                            setToolBar("Đặt hàng");
                        }
                        break;
                    }
                    case R.id.nav_store: {
                        if(FRAGMENT_CURRENT != FRAGMENT_STORE) {
                            FRAGMENT_CURRENT = FRAGMENT_STORE;
                            viewPager2.setCurrentItem(2);
                            setToolBar("Cửa hàng");
                        }
                        break;
                    }
                    case R.id.nav_search: {
                        if(FRAGMENT_CURRENT != FRAGMENT_SEARCH) {
                            FRAGMENT_CURRENT = FRAGMENT_SEARCH;
                            viewPager2.setCurrentItem(3);
                            setToolBar("Tìm kiếm");

                        }
                        break;
                    }
                    case R.id.nav_other: {
                        if(FRAGMENT_CURRENT != FRAGMENT_OTHER) {
                            viewPager2.setCurrentItem(4);
                            FRAGMENT_CURRENT = FRAGMENT_OTHER;
                            setToolBar("Khác");
                        }
                        break;
                    }
                }
                setVisibleShoppingCart();
                return true;
            }
        });
    }
    public static void setVisibleShoppingCart() {
        if(FRAGMENT_CURRENT == FRAGMENT_HOME) {
            goShoppingCart.setVisibility(View.VISIBLE);
        } else {
            goShoppingCart.setVisibility(View.GONE);
        }
    }
    public void setToolBarHomePageFragment() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setLogo(R.drawable.ic_cafe);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = "Hi, Chào ";
                Log.d("AAA", snapshot.getKey());
                if(snapshot.child("lastName").exists()) {
                    title += snapshot.child("lastName").getValue().toString();
                    viewPager2.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    viewPager2.setCurrentItem(0);
                } else {
                    title += "bạn";
                    viewPager2.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    replaceFragment(new CreateUserFragment(new CreateUserFragment.SendUserFragment() {
                        @Override
                        public void send(User user) {
                            updateUser(user);
                        }
                    }));


                }

                getSupportActionBar().setTitle(title);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void setToolBar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setLogo(R.drawable.ic_cafe);
        toolbar.setTitle(title);

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();

    }
    public void getUserToDatabase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                shoppingCart.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void initUI() {
        goShoppingCart = findViewById(R.id.goShoppingCart);
        shoppingCart = new ShoppingCart();
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        btnPay = (Button)findViewById(R.id.btnPay);
        frameLayout = findViewById(R.id.frameLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0: {
                        navigationBottom.getMenu().findItem(R.id.nav_home).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_HOME;
                        setToolBarHomePageFragment();
                        break;
                    }
                    case 1: {
                        navigationBottom.getMenu().findItem(R.id.nav_order).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_ORDER;

                        setToolBar("Đặt hàng");
                        break;
                    }
                    case 2: {
                        navigationBottom.getMenu().findItem(R.id.nav_store).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_STORE;

                        setToolBar("Cửa hàng");
                        break;
                    }
                    case 3: {
                        navigationBottom.getMenu().findItem(R.id.nav_search).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_SEARCH;

                        setToolBar("Tìm kiếm");
                        break;
                    }
                    case 4: {
                        navigationBottom.getMenu().findItem(R.id.nav_other).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_OTHER;
                        setToolBar("Khác");
                        break;
                    }
                    default: {
                        navigationBottom.getMenu().findItem(R.id.nav_home).setChecked(true);
                        FRAGMENT_CURRENT = FRAGMENT_HOME;
                    }
                }
                setVisibleShoppingCart();
            }
        });
        navigationBottom = findViewById(R.id.navigationBottom);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite: {
                Intent intent = new Intent(getApplicationContext(),  FavoriteActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUser(User user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");


        // Đưa file image vào firebase store
        if(user.getUrlPhoto() != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                    .child("profile images").child(mAuth.getCurrentUser().getUid());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(user.getUrlPhoto()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map map = new HashMap();
                                String url = uri.toString().trim();
                                map.put("urlPhoto", url);
                                reference.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                            }
                        });
                    }
                }
            });
        }

        reference.child(mAuth.getCurrentUser().getUid()).updateChildren(user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    showDialogSuccess();
                } else {

                }
            }
        });
    }

    private void showDialogSuccess() {
        Dialog dialog = new Dialog(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.create_user_success, viewGroup, false);
        dialog.setContentView(view);
        Button close = view.findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                viewPager2.setCurrentItem(0);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public static void getInformationCart() {
        double totalPrice = 0;
        for(int i = 0; i < shoppingCart.getCarts().size(); i++) {
            totalPrice += shoppingCart.getCarts().get(i).getTotalPrice();
        }
        shoppingCart.setTotalPrice(totalPrice);
        DecimalFormat mFormat = new DecimalFormat("###,###");
        String price = mFormat.format(shoppingCart.getTotalPrice()).trim() + " đ";
        btnPay.setText(price);
    }
}