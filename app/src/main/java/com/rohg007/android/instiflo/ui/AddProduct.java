package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Product;
import com.rohg007.android.instiflo.models.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.rohg007.android.instiflo.R.id.product_duration_textinput_layout;
import static com.rohg007.android.instiflo.R.id.product_rent_price_textinput_layout;
import static com.rohg007.android.instiflo.R.id.product_sell_price_textinput_layout;

public class AddProduct extends AppCompatActivity {

    EditText title;
    CheckBox rent;
    CheckBox buy;
    TextInputLayout rentDurationLayout;
    TextInputLayout rentPriceLayout;
    TextInputLayout sellPriceLayout;
    EditText rentDuration;
    EditText rentprice;
    EditText sellprice;
    EditText units;
    EditText description;
    Button clear;
    Button add;
    ImageView browseImageView;

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int REQUEST_CODE = 500;
    Uri filePath;
    ProgressDialog pd;
    Task<Uri> uri;
    String productImgeUrl;
    private DatabaseReference databaseReference;
    private StorageReference StorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        StorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        Toolbar toolbar = findViewById(R.id.add_product_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Product");
        }


        FloatingActionButton floatingActionButton = findViewById(R.id.product_browse_fab);
        browseImageView = findViewById(R.id.product_browse_image_view);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        title=findViewById(R.id.product_title_edt);
        rent=findViewById(R.id.rent_checkbox);
        rentDuration=(EditText)findViewById(R.id.product_rent_duration_edt);
        rentDurationLayout=(TextInputLayout)findViewById(product_duration_textinput_layout);
        rentDurationLayout.setVisibility(View.GONE);
        //rentDuration.setVisibility(View.GONE);
        rentprice=(EditText)findViewById(R.id.product_rent_price_edt);
        rentPriceLayout=(TextInputLayout)findViewById(product_rent_price_textinput_layout);
        rentPriceLayout.setVisibility(View.GONE);
        //rentprice.setVisibility(View.GONE);
        sellprice=(EditText)findViewById(R.id.product_sell_price_edt);
        sellPriceLayout=(TextInputLayout)findViewById(product_sell_price_textinput_layout);
        sellPriceLayout.setVisibility(View.GONE);
        //sellprice.setVisibility(View.GONE);
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rent.isChecked())
                {
                    rentDurationLayout.setVisibility(View.VISIBLE);
                    rentPriceLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    rentDurationLayout.setVisibility(View.GONE);
                    rentPriceLayout.setVisibility(View.GONE);
                }
            }
        });
        buy=(CheckBox)findViewById(R.id.buy_checkbox);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buy.isChecked())
                {
                    sellPriceLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    sellPriceLayout.setVisibility(View.GONE);
                }
            }
        });
        units=(EditText)findViewById(R.id.product_unit_edt);
        description=(EditText)findViewById(R.id.product_description_edt);
        add=(Button)findViewById(R.id.add_product_button);
        //rentDuration.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(browseImageView.getDrawable()==null)
                {
                    Toast.makeText(AddProduct.this, "Upload image for your Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String productTitle=title.getText().toString();
                if(productTitle.equals(""))
                {
                    Toast.makeText(AddProduct.this, "Give a title to your product.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int productCategory;
                final int productrentDuration,productSellPrice,productRentPrice;
                if(buy.isChecked() && rent.isChecked())
                {
                    productCategory=3;
                    String s=rentprice.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter price for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productRentPrice=Integer.parseInt(s);
                    if(productRentPrice<0)
                    {
                        Toast.makeText(AddProduct.this, "Enter valid price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    s=sellprice.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter price for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productSellPrice=Integer.parseInt(s);
                    if(productSellPrice<0)
                    {
                        Toast.makeText(AddProduct.this, "Enter valid price", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    s=rentDuration.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter rent duration (in days) for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productrentDuration=Integer.parseInt(s);
                    if(productrentDuration<0)
                    {
                        Toast.makeText(AddProduct.this, "Enter valid rent duration", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(buy.isChecked())
                {
                    productCategory=1;
                    productrentDuration=0;
                    productRentPrice=0;
                    String s=sellprice.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter price for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productSellPrice=Integer.parseInt(s);
                    if(productSellPrice<0)
                    {
                        Toast.makeText(AddProduct.this, "Enter valid price", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(rent.isChecked())
                {
                    productCategory=2;
                    productSellPrice=0;
                    String s=rentprice.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter price for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productRentPrice=Integer.parseInt(s);
                    if(productRentPrice<0) {
                        Toast.makeText(AddProduct.this, "Enter valid price", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    s=rentDuration.getText().toString();
                    if(s.equals(""))
                    {
                        Toast.makeText(AddProduct.this, "Enter rent duration (in days) for your product", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    productrentDuration=Integer.parseInt(s);
                    if(productrentDuration<0)
                    {
                        Toast.makeText(AddProduct.this, "Enter valid rent duration", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else
                {
                    Toast.makeText(AddProduct.this, "Check atleast one check box", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s=units.getText().toString();
                if(s.equals(""))
                {
                    {
                        Toast.makeText(AddProduct.this, "Enter number of units for your product", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final int productCount=Integer.parseInt(s);
                if(productCount<=0)
                {
                    {
                        Toast.makeText(AddProduct.this, "Enter atleast one unit for your product", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final String productDescription=description.getText().toString();
                if(productDescription.equals(""))
                {
                    Toast.makeText(AddProduct.this, "Give a description for your Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (filePath != null) {
                    pd.show();
                    final StorageReference childRef = StorageRef.child(System.currentTimeMillis() + "." + GetFileExtension(filePath));
                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            //Toast.makeText(AddProduct.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    productImgeUrl=uri.toString();
                                    String ownerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String productId=databaseReference.push().getKey();
                                    Product product=new Product(productId,ownerId,productTitle,productCategory,productrentDuration,productRentPrice,productSellPrice,productCount,productDescription,productImgeUrl);
                                    databaseReference.child("products").child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AddProduct.this,MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(AddProduct.this, "Product Adding Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });


                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddProduct.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        clear=(Button)findViewById(R.id.product_clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentDuration.setVisibility(View.GONE);
                rentprice.setVisibility(View.GONE);
                sellprice.setVisibility(View.GONE);
                browseImageView.setImageResource(0);
                title.getText().clear();
                rentDuration.getText().clear();
                sellprice.getText().clear();
                rentprice.getText().clear();
                buy.setChecked(false);
                rent.setChecked(false);
                units.getText().clear();
                description.getText().clear();

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap))
                browseImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}