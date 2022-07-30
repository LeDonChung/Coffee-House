package com.dmt.ledonchung.coffeehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.Contract;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractHolder>{
    private List<Contract> list;

    public ContractAdapter(List<Contract> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContractHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_contract, parent, false);
        return new ContractHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractHolder holder, int position) {
        Contract contract = list.get(position);
        holder.icon_contract.setImageResource(contract.getIcon());
        holder.infor_contract.setText(contract.getInfor());
        holder.method_contract.setText(contract.getMethod());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContractHolder extends RecyclerView.ViewHolder{
        private ImageView icon_contract;
        private TextView method_contract, infor_contract;
        public ContractHolder(@NonNull View itemView) {
            super(itemView);
            icon_contract = itemView.findViewById(R.id.icon_contract);
            infor_contract = itemView.findViewById(R.id.infor_contract);
            method_contract = itemView.findViewById(R.id.method_contract);
        }
    }
}
