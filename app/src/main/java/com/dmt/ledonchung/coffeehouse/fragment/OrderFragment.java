package com.dmt.ledonchung.coffeehouse.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.adapter.ViewPager2Adapter;
import com.dmt.ledonchung.coffeehouse.adapter.ViewPager2OrderAdapter;


public class OrderFragment extends Fragment {
    private View view;
    private ImageButton btnCoffee, btnMilkTea, btnCake, btnFruitTea, btnSnack, btnDrinkOther;
    private ViewPager2 viewPager2;
    private MainActivity mainActivity;
    private ViewPager2OrderAdapter viewPager2OrderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order, container, false);
        initUI();
        setEventButton();
        return view;
    }

    public void initUI() {
        btnCoffee = view.findViewById(R.id.btnCoffee);
        btnMilkTea = view.findViewById(R.id.btnMilkTea);
        btnCake = view.findViewById(R.id.btnCake);
        btnFruitTea = view.findViewById(R.id.btnFruitTea);
        btnSnack = view.findViewById(R.id.btnSnack);
        btnDrinkOther = view.findViewById(R.id.btnDrinkOther);
        viewPager2 = view.findViewById(R.id.viewPager2);
        mainActivity = (MainActivity) getActivity();
        viewPager2OrderAdapter = new ViewPager2OrderAdapter((FragmentActivity) getContext());
        viewPager2.setAdapter(viewPager2OrderAdapter);
    }

    public void setEventButton() {
        btnCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(0);
            }
        });

        btnMilkTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(1);
            }
        });

        btnCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(2);
            }
        });

        btnFruitTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(3);
            }
        });

        btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(4);
            }
        });

        btnDrinkOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(5);
            }
        });
    }
}