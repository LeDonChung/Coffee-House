package com.dmt.ledonchung.coffeehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.activity.HistoryOrderActivity;
import com.dmt.ledonchung.coffeehouse.model.ShoppingCart;

import java.text.DecimalFormat;
import java.util.List;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderHolder>{
    private List<ShoppingCart> shoppingCarts;
    public HistoryOrderActivity.SendShoppingCart sendShoppingCart;
    public HistoryOrderAdapter(List<ShoppingCart> shoppingCarts, HistoryOrderActivity.SendShoppingCart sendShoppingCart) {
        this.shoppingCarts = shoppingCarts;
        this.sendShoppingCart = sendShoppingCart;
    }


    @NonNull
    @Override
    public HistoryOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_history_order, parent, false);

        return new HistoryOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderHolder holder, int position) {
        ShoppingCart carts = shoppingCarts.get(position);

        // add address
        String address = carts.getStore().getAddress().trim();
        holder.address.setText(address);

        // add time
        String time = carts.getDateOrder().toString();
        holder.time.setText(time);

        // add total
        String total = "Gồm " + carts.getCarts().size() + " món";
        holder.total.setText(total);

        // add public pay and price
        String pb = carts.getPublicPay();
        String pay = "";
        if(pb.equals("pay")) {
            pay = "Thẻ ATM";
        } else {
            pay = "Tiền mặt";
        }
        DecimalFormat priceFormat = new DecimalFormat("###,###");
        String price = priceFormat.format(carts.getTotalPrice()) + "đ" + "(" + pay + ")";
        holder.priceAndPublicPay.setText(price);

        // click
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShoppingCart.send(carts);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public class HistoryOrderHolder extends RecyclerView.ViewHolder{
        private LinearLayout main;
        private TextView time, address, priceAndPublicPay, total;
        public HistoryOrderHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            time = itemView.findViewById(R.id.time);
            address = itemView.findViewById(R.id.address);
            priceAndPublicPay = itemView.findViewById(R.id.priceAndPublicPay);
            total = itemView.findViewById(R.id.total);

        }
    }
}
