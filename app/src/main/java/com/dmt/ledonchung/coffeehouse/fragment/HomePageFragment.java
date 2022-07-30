package com.dmt.ledonchung.coffeehouse.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ProductSuggestAdapter;
import com.dmt.ledonchung.coffeehouse.model.Product;
import com.dmt.ledonchung.coffeehouse.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.ArrayList;


public class HomePageFragment extends Fragment {
    private View view;
    private ImageView deliver, carriedAway;
    private ViewFlipper viewFlipper;
    private RecyclerView productSuggest;
    private MainActivity mainActivity;
    private ArrayList<Product> products;
    private ProductSuggestAdapter suggestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home_page, container, false);
        initUI();
        getProductSuggest();
        setViewFlipper();
        eventButton();
        return view;
    }
    public void eventButton() {
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToOrderFragment();
                MainActivity.shoppingCart.setDeliveryAway("deliver");
            }
        });
        carriedAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToOrderFragment();
                MainActivity.shoppingCart.setDeliveryAway("carriedaway");
            }
        });
    }
    public void initUI() {
        deliver = view.findViewById(R.id.deliver);
        carriedAway = view.findViewById(R.id.carriedAway);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        productSuggest = view.findViewById(R.id.productSuggest);

        products = new ArrayList<>();

        suggestAdapter = new ProductSuggestAdapter(products, getContext());

        productSuggest.setLayoutManager(new GridLayoutManager(mainActivity, 2));
        productSuggest.setAdapter(suggestAdapter);

        mainActivity = (MainActivity) getActivity();
    }

    public void getProductSuggest() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products/coffee");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    products.add(product);
                    suggestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setViewFlipper() {
        Animation anim_in = AnimationUtils.loadAnimation(getContext(), R.anim.anim_in_viewflipper);
        viewFlipper.setInAnimation(anim_in);
        Animation anim_out = AnimationUtils.loadAnimation(getContext(), R.anim.anim_out_viewflipper);
        viewFlipper.setOutAnimation(anim_out);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("advertise");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    String url = data.getValue().toString().trim();
                    ImageView image = new ImageView(mainActivity);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.placeholder_adv)
                            .error(R.drawable.placeholder_adv)
                            .into(image);

                    viewFlipper.addView(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);


    }
}