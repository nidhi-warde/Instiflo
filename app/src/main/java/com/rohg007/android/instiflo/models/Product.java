package com.rohg007.android.instiflo.models;

import java.util.ArrayList;

public class Product {

    private static final String LOG_TAG = Product.class.getSimpleName();
    private String productId;
    private String productTitle;
    private String productImageUrl;
    private int productCategory;
    private int productRentPrice;
    private int productSellPrice;
    private int productCount;
    private String productDescription;
    private int rentDuration;
    private String ownerId;
    private ArrayList<String> buyerId;
    private float productRating;
    private int noOfUsersRated;

    public Product()
    {
        this.productTitle = "";
        this.productCategory = 0;
        this.productSellPrice=0;
        this.productRentPrice=0;
        this.productCount=0;
        this.productDescription="";
        this.productImageUrl="";
    }
    public Product(String productTitle, String productImageUrl){
        this.productTitle=productTitle;
        this.productImageUrl=productImageUrl;
    }


    public int getProductRentPrice() {
        return productRentPrice;
    }

    public void setProductRentPrice(int productRentPrice) {
        this.productRentPrice = productRentPrice;
    }

    public int getProductSellPrice() {
        return productSellPrice;
    }

    public void setProductSellPrice(int productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public Product(String productid, String ownerId, String productTitle, int productCategory, int rentDuration, int productRentPrice, int productSellPrice, int productCount, String productDescription, String productImageUrl) {
        this.productId = productid;
        this.ownerId=ownerId;
        this.productTitle = productTitle;
        this.productCategory = productCategory;
        this.rentDuration= rentDuration;
        this.productSellPrice=productSellPrice;
        this.productRentPrice=productRentPrice;
        this.productCount=productCount;
        this.productDescription=productDescription;
        this.productImageUrl=productImageUrl;
        this.noOfUsersRated=0;
        this.productRating=Float.parseFloat("0");
    }
    public static String getLogTag() {
        return LOG_TAG;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }


    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getRentDuration() {
        return rentDuration;
    }

    public void setRentDuration(int rentDuration) {
        this.rentDuration = rentDuration;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getBuyerId() {
        return buyerId;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public float getProductRating() {
        return productRating;
    }

    public void setProductRating(float productRating) {
        this.productRating = productRating;
    }

    public int getNoOfUsersRated() {
        return noOfUsersRated;
    }

    public void setNoOfUsersRated(int noOfUsersRated) {
        this.noOfUsersRated = noOfUsersRated;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}