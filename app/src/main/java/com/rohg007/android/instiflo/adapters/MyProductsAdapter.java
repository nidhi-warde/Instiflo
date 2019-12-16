package com.rohg007.android.instiflo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Product;
import com.rohg007.android.instiflo.ui.ProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsViewHolder> {

    private ArrayList<Product> productArrayList;
    private Context mContext;


    public MyProductsAdapter(ArrayList<Product> productArrayList,Context context){
        this.productArrayList=productArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_products_view,parent,false);
        return new MyProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsViewHolder holder, final int position){
            final Product product = productArrayList.get(position);
            String title = product.getProductTitle();
            String imageUrl = product.getProductImageUrl();
            if(product.getProductSellPrice()!= 0) {
                String price = "Selling price - Rs. " + product.getProductSellPrice();
                holder.sellPriceTextView.setText(price);
            }
            if(product.getProductRentPrice()!= 0) {
                String price = "Rent price - Rs. " + product.getProductRentPrice() +"/day";
                holder.rentPriceTextView.setText(price);
            }
            if(imageUrl!=null) {
                Picasso.get()
                        .load(imageUrl)
                        .into(holder.productImageView);
            }
            holder.titleTextView.setText(title);
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                Product product = productArrayList.get(position);
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetails.class);
                    intent.putExtra("productId",product.getProductId());
                    if(product.getProductSellPrice()!= 0) {
                        intent.putExtra("from", "buyFragment");
                    }
                    else if(product.getProductRentPrice()!= 0) {
                        intent.putExtra("from", "rentFragment");
                    }
                    mContext.startActivity(intent);
                }
            });
    }


    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}