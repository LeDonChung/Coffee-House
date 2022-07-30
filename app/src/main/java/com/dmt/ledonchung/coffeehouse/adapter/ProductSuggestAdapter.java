package com.dmt.ledonchung.coffeehouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

public class ProductSuggestAdapter extends RecyclerView.Adapter<ProductSuggestAdapter.ProductSuggestHolder> {

    private List<Product> products;
    private Context context;

    public ProductSuggestAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductSuggestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product_suggest, parent, false);
        return new ProductSuggestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSuggestHolder holder, int position) {
        Product product = products.get(position);

        holder.name_product_suggest.setText(product.getName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String price = decimalFormat.format(product.getPrice()) + "đ";
        holder.price_product_suggest.setText(price);

        Picasso.get()
                .load(product.getUrlPhoto())
                .error(R.drawable.image_suggest_error)
                .placeholder(R.drawable.placeholder)
                .into(holder.image_product_suggest);

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
        return products.size();
    }

    public class ProductSuggestHolder extends RecyclerView.ViewHolder {
        private ImageView image_product_suggest;
        private TextView name_product_suggest, price_product_suggest;
        private Button btnSelectProduct;
        private LinearLayout main;
        public ProductSuggestHolder(@NonNull View itemView) {
            super(itemView);
            image_product_suggest = itemView.findViewById(R.id.image_product_suggest);
            name_product_suggest = itemView.findViewById(R.id.name_product_suggest);
            price_product_suggest = itemView.findViewById(R.id.price_product_suggest);
            btnSelectProduct = itemView.findViewById(R.id.btnSelectProduct);
            main = itemView.findViewById(R.id.main);

        }
    }
}
