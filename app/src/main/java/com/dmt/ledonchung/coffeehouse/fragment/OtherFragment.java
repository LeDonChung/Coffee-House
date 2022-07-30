package com.dmt.ledonchung.coffeehouse.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.activity.ContractActivity;
import com.dmt.ledonchung.coffeehouse.activity.HistoryOrderActivity;
import com.dmt.ledonchung.coffeehouse.activity.LoginActivity;
import com.dmt.ledonchung.coffeehouse.activity.UpdateUserActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OtherFragment extends Fragment {
    private View view;
    private TextView logout, informationUser, contract;
    private MainActivity mainActivity;
    private CardView historyOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, container, false);
        unitUI();
        eventButton();
        return view;
    }
    public void unitUI() {
        logout = view.findViewById(R.id.logoutOther);
        informationUser = view.findViewById(R.id.informationUserOther);
        mainActivity = (MainActivity) getActivity();
        contract = view.findViewById(R.id.contractOther);
        historyOrder = view.findViewById(R.id.historyOrder);
    }
    public void eventButton() {
        historyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, HistoryOrderActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                mainActivity.finish();
            }
        });
        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),  ContractActivity.class);
                startActivity(intent);
            }
        });
        informationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),  UpdateUserActivity.class);
                startActivity(intent);
            }
        });
    }
}