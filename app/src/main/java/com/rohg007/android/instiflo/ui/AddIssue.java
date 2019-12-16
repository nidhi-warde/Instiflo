package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Issue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddIssue extends AppCompatActivity {

    private ImageView issueImageView;
    private RadioButton LostRadioButton, FoundRadioButton;
    private EditText NameEditText, ContactNumberEditText, EmailEditText, ObjectTypeEditText, DescriptionEditText;

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int REQUEST_CODE = 500;
    Uri filePath;
    ProgressDialog pd;
    String productImgeUrl;
    private DatabaseReference databaseReference;
    private StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageRef = FirebaseStorage.getInstance().getReference("issues");

        FloatingActionButton floatingActionButton = findViewById(R.id.issue_image_browse_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        issueImageView = findViewById(R.id.add_issue_image_view);
        MaterialButton submitButton = findViewById(R.id.submit_button);
        LostRadioButton = findViewById(R.id.lost_radio_button);
        FoundRadioButton = findViewById(R.id.found_radio_button);
        Button createMailButton = findViewById(R.id.create_mail_button);
        NameEditText = findViewById(R.id.name_edit_text);
        ContactNumberEditText = findViewById(R.id.contact_number_edit_text);
        EmailEditText = findViewById(R.id.email_edit_text);
        ObjectTypeEditText = findViewById(R.id.object_type_edit_text);
        DescriptionEditText = findViewById(R.id.description_edit_text);
        MaterialButton resetButton = findViewById(R.id.reset_text_view);
        EmailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetailsToFirebase(getDetails());
            }
        });

        createMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderMail();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LostRadioButton.setChecked(false);
                FoundRadioButton.setChecked(false);
                NameEditText.setText("");
                ContactNumberEditText.setText("");
                ObjectTypeEditText.setText("");
                DescriptionEditText.setText("");
                issueImageView.setImageResource(0);
            }
        });
    }

    private void addDetailsToFirebase(final Issue issue){
        if(filePath!=null){
            pd.show();
            final StorageReference fileReference = StorageRef.child((issue.getmEmail()+System.currentTimeMillis()).hashCode()+"."+GetFileExtension(filePath));
            UploadTask uploadTask = fileReference.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            issue.setmImageUrl(uri.toString());
                            issue.setmId(databaseReference.push().getKey());
                            databaseReference.child("issues").child(issue.getmId()).setValue(issue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Snackbar.make(NameEditText,"Issue Added Successfully",Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        Snackbar.make(NameEditText,"Problem Adding Issue",Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(NameEditText,"Problem Uploading Image",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap))
                issueImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    A function to get all details in appropriate format
     */
    private Issue getDetails() {
        Issue details = new Issue();
        String name = NameEditText.getText().toString().trim();
        String contactNumber = ContactNumberEditText.getText().toString().trim();
        String email = EmailEditText.getText().toString().trim();
        String objectType = ObjectTypeEditText.getText().toString().trim();
        String description = DescriptionEditText.getText().toString().trim();
        RadioButtonStatus();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //Binding all details
        details.setmName(name);
        details.setmContactNumber(contactNumber);
        details.setmEmail(email);
        details.setmObjectType(objectType);
        details.setmDescription(description);
        details.setDatePosted(date);
        details.setmVisibililty("NO");
        details.setmApproved("NO");
        details.setLostFound(RadioButtonStatus());
        details.setmResolved("NO");
        return details;
    }

    /*
    Gives the Status of which Radio Button is selected
     */
    private String RadioButtonStatus() {
        String LostFound = "";
        //on Radio Button Selected Status
        if (LostRadioButton.isChecked()) {
            LostFound = "Lost";
        } else if (FoundRadioButton.isChecked()) {
            LostFound = "Found";
        }
        return LostFound;
    }

    /*
    Creates and Email Intent when Create Email Button is clicked
     */
    private void orderMail() {
        String body = "";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:"));
        if (RadioButtonStatus().equals("Lost")) {
            body = "Sir,\n Please forward this to all UG & PG Groups.\n\nI've lost my " + ObjectTypeEditText.getText().toString().trim() + ". It is a " + DescriptionEditText.getText().toString().trim() + "\n\n Anyone who finds it may contact the undersigned\n\n" + NameEditText.getText().toString().trim() + "\n" + ContactNumberEditText.getText().toString().trim();
        } else if (RadioButtonStatus().equals("Found")) {
            body = "Sir,\n Please forward this to all UG & PG Groups.\n\nI've Found a " + ObjectTypeEditText.getText().toString().trim() + ". It is a " + DescriptionEditText.getText().toString().trim() + "\n\n To collect it you may contact the undersigned\n\n" + NameEditText.getText().toString().trim() + "\n" + ContactNumberEditText.getText().toString().trim();
        }
        //Setting the subject of mail
        intent.putExtra(Intent.EXTRA_SUBJECT, ObjectTypeEditText.getText().toString().trim() + " " + RadioButtonStatus());
        //Setting the body of mail
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
