package com.dmt.ledonchung.coffeehouse.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dmt.ledonchung.coffeehouse.fragment.CakeOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.CoffeeOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.DrinkOtherOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.FruitTeaOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.HomePageFragment;
import com.dmt.ledonchung.coffeehouse.fragment.MilkTeaOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.OrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.OtherFragment;
import com.dmt.ledonchung.coffeehouse.fragment.SearchFragment;
import com.dmt.ledonchung.coffeehouse.fragment.SnackOrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.StoreFragment;

public class ViewPager2OrderAdapter extends FragmentStateAdapter {
    public ViewPager2OrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new CoffeeOrderFragment();
            }
            case 1: {
                return new MilkTeaOrderFragment();
            }
            case 2: {
                return new CakeOrderFragment();
            }
            case 3: {
                return new FruitTeaOrderFragment();
            }
            case 4: {
                return new SnackOrderFragment();
            }
            case 5: {
                return new DrinkOtherOrderFragment();
            }
            default:
                return new HomePageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
