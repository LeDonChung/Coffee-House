package com.dmt.ledonchung.coffeehouse.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ProductOrderAdapter;
import com.dmt.ledonchung.coffeehouse.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DrinkOtherOrderFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ProductOrderAdapter adapter;
    private ArrayList<Product> products;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drink_other_order, container, false);
        initUI();
        getProductCoffee();
        return view;
    }
    public void initUI() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mainActivity = (MainActivity) getActivity();
        products = new ArrayList<>();
        adapter = new ProductOrderAdapter(mainActivity, products);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity.getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
    public void getProductCoffee() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products/drinkother");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    products.add(product);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}