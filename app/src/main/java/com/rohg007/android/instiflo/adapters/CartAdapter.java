package com.rohg007.android.instiflo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapterViewHolder> {

    private ArrayList<Product> productArrayList;

    public CartAdapter(ArrayList<Product> productArrayList){
        this.productArrayList=productArrayList;
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card,parent,false);
        return new CartAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        String price = "Rs. "+product.getProductSellPrice();
        holder.cartProductTitle.setText(product.getProductTitle());
        holder.cartProductPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}
