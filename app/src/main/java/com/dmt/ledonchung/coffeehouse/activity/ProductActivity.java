package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.Cart;
import com.dmt.ledonchung.coffeehouse.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageProduct;
    private TextView nameProduct, priceProduct, describeProduct, count;
    private ImageButton favorite, remove, add;
    private RadioButton sizeLarge, sizeSmall, sizeNormal;
    private Product product;
    private boolean isFavorite = false;
    private String size = "S";
    private double priceBySize = 0;
    private int totalProduct = 1;
    private Button addCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initUI();
        setToolbar();
        getDataIntent();
        eventButton();
    }

    public void initUI() {
        addCart = findViewById(R.id.addCart);
        count = findViewById(R.id.count);
        toolbar = findViewById(R.id.toolbar);
        imageProduct = findViewById(R.id.imageProduct);
        nameProduct = findViewById(R.id.nameProduct);
        priceProduct = findViewById(R.id.priceProduct);
        describeProduct = findViewById(R.id.describeProduct);
        favorite = findViewById(R.id.favorite);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        sizeLarge = findViewById(R.id.sizeLarge);
        sizeSmall = findViewById(R.id.sizeSmall);
        sizeNormal = findViewById(R.id.sizeNormal);
    }

    private void setPriceSize() {
        DecimalFormat mFormat = new DecimalFormat("###,###");
        sizeSmall.setText("Nh???\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + mFormat.format(product.getPrice()) + "??");
        sizeNormal.setText("V???a\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + mFormat.format(product.getPrice() + 5000) + "??");
        sizeLarge.setText("L???n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + mFormat.format(product.getPrice() + 10000) + "??");
    }

    private void setFavorite() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // ton tai favorite va c?? id c???a s???n ph???m
                if(snapshot.child("favorite").exists()) {
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorite").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if(product.getId().equals(data.getKey())) {
                                    favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
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

    public void eventButton() {
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFavorite();
            }
        });
        sizeSmall.setOnCheckedChangeListener(listenerRadio);
        sizeLarge.setOnCheckedChangeListener(listenerRadio);
        sizeNormal.setOnCheckedChangeListener(listenerRadio);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalProduct < 10) {
                    totalProduct += 1;
                    count.setText(totalProduct+"");
                    double total =  totalProduct*priceBySize;
                    DecimalFormat mFormat = new DecimalFormat("###,###");
                    addCart.setText(mFormat.format(total) + "??");

                }

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalProduct > 1) {
                    totalProduct -= 1;
                    count.setText(totalProduct+"");
                    double total =  totalProduct*priceBySize;
                    DecimalFormat mFormat = new DecimalFormat("###,###");
                    addCart.setText(mFormat.format(total) + "??");
                }

            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new AlertDialog.Builder(ProductActivity.this)
                        .setTitle("Th??m v??o gi??? h??ng")
                        .setMessage("B???n c?? mu???n th??m s???n ph???m v??o gi??? h??ng")
                        .setCancelable(false)
                        .setPositiveButton("C??", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 0; i < MainActivity.shoppingCart.getCarts().size(); i++) {
                                    if(MainActivity.shoppingCart.getCarts().get(i).getSize().equals(size) && MainActivity.shoppingCart.getCarts().get(i).getProduct().equals(product)) {
                                        // g??n l???i s??? l?????ng hi???n t???i
                                        int totalCurrent = MainActivity.shoppingCart.getCarts().get(i).getTotalCount();
                                        MainActivity.shoppingCart.getCarts().get(i).setTotalCount(totalCurrent + totalProduct);
                                        // g??n l???i gi??
                                        double priceCurrent = MainActivity.shoppingCart.getCarts().get(i).getTotalPrice();
                                        MainActivity.shoppingCart.getCarts().get(i).setTotalPrice(priceCurrent + totalProduct*priceBySize);
                                        // Log.d("AAA",  MainActivity.carts.toString());
                                        Toast.makeText(ProductActivity.this, "Th??m v??o gi??? h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                                        MainActivity.getInformationCart();
                                        finish();
                                        return;
                                    }
                                }

                                Cart cart = new Cart(product, size, totalProduct*priceBySize, totalProduct);
                                MainActivity.shoppingCart.getCarts().add(cart);
                                // Log.d("AAA",  MainActivity.carts.toString());
                                Toast.makeText(ProductActivity.this, "Th??m v??o gi??? h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                                MainActivity.getInformationCart();
                                finish();
                            }
                        })
                            .setNegativeButton("Kh??ng", null)
                            .create()
                            .show();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if(compoundButton.getText().toString().indexOf("Nh???") >= 0) {
                    size = "S";
                    priceBySize = product.getPrice();

                } else if(compoundButton.getText().toString().indexOf("V???a") >= 0) {
                    size = "M";
                    priceBySize = product.getPrice() + 5000;

                } else if(compoundButton.getText().toString().indexOf("L???n") >= 0) {
                    size = "L";
                    priceBySize = product.getPrice() + 10000;

                }
                double total =  totalProduct*priceBySize;
                DecimalFormat mFormat = new DecimalFormat("###,###");
                addCart.setText(mFormat.format(total) + "??");
            }
        }
    };
    private void actionFavorite() {
        if(isFavorite) {
            // remove
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favorite").child(product.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ProductActivity.this, "???? x??a kh???i danh s??ch y??u th??ch", Toast.LENGTH_SHORT).show();
                    favorite.setImageResource(R.drawable.ic_no_favorite);
                    isFavorite = false;
                }
            });
        } else {
            // add
            Map map = new HashMap();
            map.put(product.getId(), product);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favorite").updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ProductActivity.this, "Th??m v??o y??u th??ch th??nh c??ng", Toast.LENGTH_SHORT).show();
                        favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                        isFavorite = true;
                    }
                }
            });
        }
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Th??ng tin s???n ph???m");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getDataIntent() {
        String id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");

        if(id != null && type != null) {
            if(type.equals("210")) {
                type = "coffee";
            } else if(type.equals("211")) {
                type = "milktea";

            } else if(type.equals("212")) {
                type = "cake";
            } else if(type.equals("213")) {
                type = "fruittea";
            } else if(type.equals("214")) {
                type = "snack";
            } else if(type.equals("215")) {
                type = "drinkother";
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");
            reference.child(type).child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    product = snapshot.getValue(Product.class);
                    Picasso.get()
                            .load(product.getUrlPhoto())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(imageProduct);

                    nameProduct.setText(product.getName());
                    DecimalFormat format = new DecimalFormat("###,###");
                    priceProduct.setText(format.format(product.getPrice()) + "??");
                    describeProduct.setText(product.getDescribe());

                    priceBySize = product.getPrice();
                    setFavorite();
                    setPriceSize();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}