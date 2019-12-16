package com.rohg007.android.instiflo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Product;
import com.rohg007.android.instiflo.utils.ImageRequester;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StaggeredRentProductCardAdapter extends RecyclerView.Adapter<StaggeredRentProductCardAdapter.StaggeredProductViewHolder> {

    private List<Product> productList;
    private View.OnClickListener onItemClickListener;
    private Context context;
    private ImageRequester imageRequester;

    public StaggeredRentProductCardAdapter(List<Product> productList){
        this.productList=productList;
        imageRequester = ImageRequester.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return position%3;
    }

    @NonNull
    @Override
    public StaggeredProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.staggered_product_card_first;
        if(viewType==1)
            layoutId = R.layout.staggered_product_card_second;
        else if(viewType==2)
            layoutId = R.layout.staggered_product_card_third;
        context=parent.getContext();
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredProductViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            Product product = productList.get(position);
            String rentprice = "Rs. "+String.valueOf(product.getProductRentPrice());
            String url = product.getProductImageUrl();
            holder.productTitle.setText(product.getProductTitle());
            holder.productPrice.setText(rentprice);
            //imageView.setImageBitmap(getBitmapFromURL(url));
            //Glide.with(context).load(product.getProductImageUrl()).into(holder.productimage);
            imageRequester.setImageFromUrl(holder.productimage,url);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        onItemClickListener=itemClickListener;
    }

    public class StaggeredProductViewHolder extends RecyclerView.ViewHolder {

        TextView productTitle;
        TextView productPrice;
        NetworkImageView productimage;
        StaggeredProductViewHolder(@NonNull View itemView){
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productimage = itemView.findViewById(R.id.product_image);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
