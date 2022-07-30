package com.dmt.ledonchung.coffeehouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.activity.StoreChooseActivity;
import com.dmt.ledonchung.coffeehouse.activity.StoreInformationActivity;
import com.dmt.ledonchung.coffeehouse.model.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreHolder> {
    private List<Store> list;
    private Context context;

    public StoreAdapter(List<Store> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_store, parent,  false);
        return new StoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        Store store = list.get(position);

        String[] address = store.getAddress().split(", ");
        holder.address_store.setText(address[0]);

        Picasso.get()
                .load(store.getUrlPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.image_store);
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StoreInformationActivity.class);
                intent.putExtra("data", store);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StoreHolder extends RecyclerView.ViewHolder{
        private ImageView image_store;
        private TextView address_store;
        private LinearLayout main;


        public StoreHolder(@NonNull View itemView) {
            super(itemView);
            image_store = itemView.findViewById(R.id.image_store);
            address_store = itemView.findViewById(R.id.address_store);
            main = itemView.findViewById(R.id.main);
        }
    }
}
