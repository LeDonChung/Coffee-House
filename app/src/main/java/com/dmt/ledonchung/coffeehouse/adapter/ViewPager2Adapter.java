package com.dmt.ledonchung.coffeehouse.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.fragment.HomePageFragment;
import com.dmt.ledonchung.coffeehouse.fragment.OrderFragment;
import com.dmt.ledonchung.coffeehouse.fragment.OtherFragment;
import com.dmt.ledonchung.coffeehouse.fragment.SearchFragment;
import com.dmt.ledonchung.coffeehouse.fragment.StoreFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new HomePageFragment();
            }
            case 1: {
                return new OrderFragment();

            }
            case 2: {
                return new StoreFragment();
            }
            case 3: {
                return new SearchFragment();
            }
            case 4: {
                return new OtherFragment();
            }
            default:
                return new HomePageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
