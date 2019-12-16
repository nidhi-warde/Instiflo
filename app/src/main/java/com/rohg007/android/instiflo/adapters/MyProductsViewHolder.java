package com.rohg007.android.instiflo.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.ui.AddProduct;
import com.rohg007.android.instiflo.ui.ProductDetails;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyProductsViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout parentLayout;
    TextView titleTextView;
    TextView sellPriceTextView;
    TextView rentPriceTextView;
    ImageView productImageView;

    public MyProductsViewHolder(@NonNull final View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.product_title);
        sellPriceTextView = itemView.findViewById(R.id.product_sell_price);
        rentPriceTextView = itemView.findViewById(R.id.product_rent_price);
        productImageView = itemView.findViewById(R.id.product_title_image);
        parentLayout = itemView.findViewById(R.id.my_product_id);
    }
}


