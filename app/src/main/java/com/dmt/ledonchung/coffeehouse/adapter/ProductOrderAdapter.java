package com.dmt.ledonchung.coffeehouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.activity.ProductActivity;
import com.dmt.ledonchung.coffeehouse.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderHolder> implements Filterable {
    private Context context;
    private List<Product> productList;
    private List<Product> productListOld;
    public ProductOrderAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.productListOld = this.productList;

    }

    @NonNull
    @Override
    public ProductOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product_order, parent, false);
        return new ProductOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderHolder holder, int position) {
        Product product = productList.get(position);

        holder.name_product_order.setText(product.getName().trim());

        DecimalFormat format = new DecimalFormat("###,###");
        holder.price_product_order.setText(format.format(product.getPrice()) + "đ");

        Picasso.get()
                .load(product.getUrlPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_suggest_error)
                .into(holder.image_product_order);
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ProductActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("type", product.getType());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()) {
                    productList = productListOld;
                } else {
                    List<Product> list = new ArrayList<>();
                    for(Product product : productListOld) {
                        if(product.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(product);
                        }
                    }
                    productList = list;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductOrderHolder extends RecyclerView.ViewHolder{
        private ImageView image_product_order;
        private TextView name_product_order, price_product_order;
        private ImageButton btnAddOrder;
        private LinearLayout main;

        public ProductOrderHolder(@NonNull View itemView) {
            super(itemView);
            image_product_order = itemView.findViewById(R.id.image_product_order);
            name_product_order = itemView.findViewById(R.id.name_product_order);
            price_product_order = itemView.findViewById(R.id.price_product_order);
            btnAddOrder = itemView.findViewById(R.id.btnAddOrder);
            main = itemView.findViewById(R.id.main);

        }
    }
}
