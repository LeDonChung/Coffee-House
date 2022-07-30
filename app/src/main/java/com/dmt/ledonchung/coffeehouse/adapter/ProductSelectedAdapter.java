package com.dmt.ledonchung.coffeehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.Cart;

import java.text.DecimalFormat;
import java.util.List;

public class ProductSelectedAdapter extends RecyclerView.Adapter<ProductSelectedAdapter.ProductSelectedHolder>{
    private List<Cart> list;

    public ProductSelectedAdapter(List<Cart> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProductSelectedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product_selected, parent, false);
        return new ProductSelectedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSelectedHolder holder, int position) {
        Cart cart = list.get(position);

        holder.nameProduct.setText(cart.getTotalCount() + "x " + cart.getProduct().getName());

        String typeSize = cart.getSize();
        if(typeSize.equals("S")) {
            typeSize = "Nhỏ";
        } else if(typeSize.equals("N")) {
            typeSize = "Vừa";
        } else {
            typeSize = "Lớn";
        }
        holder.sizeProduct.setText(typeSize);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        holder.totalPrice.setText(decimalFormat.format(cart.getTotalPrice())+"đ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductSelectedHolder extends RecyclerView .ViewHolder{
        private TextView nameProduct, sizeProduct, totalPrice;
        public ProductSelectedHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            sizeProduct = itemView.findViewById(R.id.sizeProduct);
            totalPrice = itemView.findViewById(R.id.totalPrice);

        }
    }
}
