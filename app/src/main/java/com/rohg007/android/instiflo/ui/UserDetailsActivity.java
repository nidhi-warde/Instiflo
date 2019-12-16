package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UserDetailsActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String userId;
    private CircleImageView userImageView;
    private Uri imageUri;
    private String imageUrl;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final String LOG_TAG = UserDetailsActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int REQUEST_CODE = 500;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Toolbar toolbar = findViewById(R.id.user_details_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("users");

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        userId = intent.getStringExtra("id");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        imageUrl = intent.getStringExtra("imageUrl");

        final EditText firstNameEdt = findViewById(R.id.first_name_edt);
        final EditText lastNameEdt = findViewById(R.id.last_name_edt);
        EditText emailEdt = findViewById(R.id.user_details_email_edt);
        final EditText addressEdt = findViewById(R.id.user_address_edt);
        final EditText phoneEdt = findViewById(R.id.phone_number_edt);
        FloatingActionButton saveFab = findViewById(R.id.save_user_fab);
        userImageView = findViewById(R.id.user_details_img);

        Picasso.get()
                .load(imageUrl)
                .into(userImageView);

        emailEdt.setText(email);
        firstNameEdt.setText(firstName);
        lastNameEdt.setText(lastName);
        addressEdt.setText(address);
        phoneEdt.setText(phone);

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = firstNameEdt.getText().toString();
                lastName = lastNameEdt.getText().toString();
                address = addressEdt.getText().toString();
                phone = phoneEdt.getText().toString();
                addDetailsToFirebase();
            }
        });

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void addDetailsToFirebase(){
        if(imageUri!=null){
            progressDialog.show();
            final StorageReference fileReference = storageReference.child(userId+(userId+System.currentTimeMillis()).hashCode()+"."+getFileExtension(imageUri));
            UploadTask uploadTask = fileReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            User user = new User(userId, firstName, lastName, email, address, phone,uri.toString());
                            databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Snackbar.make(userImageView,"User Successfully Updated",Snackbar.LENGTH_SHORT).show();
                                    else
                                        Snackbar.make(userImageView,"User could not be Updated",Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(userImageView,"Couldn't Upload Image",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Log.d(TAG, String.valueOf(bitmap))
                userImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
