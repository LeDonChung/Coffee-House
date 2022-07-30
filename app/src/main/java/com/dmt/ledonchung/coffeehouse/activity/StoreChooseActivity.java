package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.StoreAdapter;
import com.dmt.ledonchung.coffeehouse.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreChooseActivity extends AppCompatActivity {
    private TextView tvStoreFavorite, tvStoreOther;
    private RecyclerView storesFavorite, storesOther;
    private StoreAdapter favoritesAdapter;
    private StoreAdapter othersAdapter;
    private ArrayList<Store> favorites;
    private ArrayList<Store> others;
    private MainActivity mainActivity;
    public boolean isOther = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_choose);
        initUI();
        getDataOtherStore();
        getDataFavoriteStore();
    }
    public void initUI() {
        tvStoreFavorite = findViewById(R.id.tvStoreFavorite);
        tvStoreOther = findViewById(R.id.tvStoreOther);

        storesFavorite = findViewById(R.id.storesFavorite);
        favorites = new ArrayList<>();
        favoritesAdapter = new StoreAdapter(favorites, mainActivity);

        storesFavorite.setLayoutManager(new LinearLayoutManager(mainActivity));
        storesFavorite.setAdapter(favoritesAdapter);

        storesOther = findViewById(R.id.storesOther);
        others = new ArrayList<>();
        othersAdapter = new StoreAdapter(others, this);
        storesOther.setLayoutManager(new LinearLayoutManager(mainActivity));
        storesOther.setAdapter(othersAdapter);
    }
    public void getDataFavoriteStore() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("favoriteStore").exists()) {
                    storesFavorite.setVisibility(View.VISIBLE);
                    tvStoreFavorite.setVisibility(View.VISIBLE);
                    showProductFavorite();

                } else {
                    storesFavorite.setVisibility(View.GONE);
                    tvStoreFavorite.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showProductFavorite() {
        favorites.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favoriteStore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    Store store = data.getValue(Store.class);
                    favorites.add(store);
                    favoritesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDataOtherStore() {
        others.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    Store store = data.getValue(Store.class);
                    Log.d("AAA", store.toString());
                    others.add(store);
                    isOther = true;
                }
                othersAdapter.notifyDataSetChanged();
                Log.d("AAA", ""+othersAdapter.getItemCount());
                if(!isOther) {
                    tvStoreOther.setVisibility(View.GONE);
                    storesOther.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}