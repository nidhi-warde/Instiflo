package com.rohg007.android.instiflo.adapters;

import android.view.View;
import android.widget.TextView;

import com.rohg007.android.instiflo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapterViewHolder extends RecyclerView.ViewHolder {

    TextView cartProductTitle;
    TextView cartProductPrice;

    public CartAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProductTitle = itemView.findViewById(R.id.cart_product_title);
        cartProductPrice = itemView.findViewById(R.id.cart_product_price);
    }
}
