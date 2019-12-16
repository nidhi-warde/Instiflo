package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Event;
import com.rohg007.android.instiflo.utils.DateSetter;
import com.rohg007.android.instiflo.utils.TimeSetter;

import java.io.IOException;

public class AddEvent extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;
    //    private static final int REQUEST_CODE = 500;
    private Uri mImageUri;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    ImageView browseImageView;
    private TextInputEditText event_title,event_date,event_time,event_location,event_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = findViewById(R.id.add_event_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Event");
        }

        //toolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));

        TextInputEditText dateEdt = findViewById(R.id.event_date_edt);
        final TextInputEditText timeEdt = findViewById(R.id.event_time_edt);

        FloatingActionButton floatingActionButton = findViewById(R.id.event_browse_fab);
        browseImageView = findViewById(R.id.event_browse_image_view);

        new TimeSetter(timeEdt);
        new DateSetter(dateEdt);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        MaterialButton add_event_button=(MaterialButton)findViewById(R.id.add_event_button);
        MaterialButton clear_event_button=(MaterialButton) findViewById(R.id.event_clear_button);

        event_title=(TextInputEditText)findViewById(R.id.event_title_edt);
        event_date=(TextInputEditText)findViewById(R.id.event_date_edt);
        event_time=(TextInputEditText)findViewById(R.id.event_time_edt);
        event_location=(TextInputEditText)findViewById(R.id.event_location_edt);
        event_description=(TextInputEditText)findViewById(R.id.product_description_edt);

        clear_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_title.getText().clear();
                event_date.getText().clear();
                event_time.getText().clear();
                event_location.getText().clear();
                event_description.getText().clear();
            }
        });

        add_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                final String title=event_title.getText().toString().trim();
                final String date=event_date.getText().toString().trim();
                final String time=event_time.getText().toString().trim();
                final String location=event_location.getText().toString().trim();
                final String description=event_description.getText().toString();

//                Toast.makeText(AddEvent.this, "Add Event clicked", Toast.LENGTH_SHORT).show();

                Boolean flag=true;

                event_title.setError(null);
                event_date.setError(null);
                event_time.setError(null);
                event_description.setError(null);
                event_location.setError(null);

                if(title.isEmpty()) {
                    event_title.setError("Event title can't be empty");
                    flag=false;
                }
////
                if(date.isEmpty()) {
                    event_date.setError("Event date can't be empty");
                    flag=false;
                }

                if(time.isEmpty()) {
                    event_time.setError("Event time can't be empty");
                    flag=false;
                }

                if(location.isEmpty()) {
                    event_location.setError("Event location can't be empty");
                    flag=false;
                }

                if(description.isEmpty()) {
                    event_description.setError("Event description can't be empty");
                    flag=false;
                }

                if(flag)
                {
                    if(mImageUri!=null)
                    {
                        final StorageReference childRef = storageReference.child("events");
                        final StorageReference childRef2=childRef.child(System.currentTimeMillis() + "." + GetFileExtension(mImageUri));
                        UploadTask uploadTask = childRef2.putFile(mImageUri);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                childRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String creator=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        String eventId=databaseReference.push().getKey();
                                        String imageId=uri.toString();
                                        Event event=new Event(creator,eventId,title, date, time, location, description,imageId);

//                                Toast.makeText(AddEvent.this, event.getEventTitle()+" "+event.getEventDate()+" "+event.getEventTime(), Toast.LENGTH_LONG).show();

                                        databaseReference.child("events").child(eventId).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent=new Intent(AddEvent.this, MainActivity.class);
                                                    startActivity(intent);
                                                    Toast.makeText(AddEvent.this, "Event Added Succesfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(AddEvent.this, "Event Adding Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddEvent.this, "Event Adding Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    else
                    {
                        String creator=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String eventId=databaseReference.push().getKey();
                        String imageId="https://firebasestorage.googleapis.com/v0/b/instiflo.appspot.com/o/instiflo_dark_bg.png?alt=media&token=3add8b05-b4ed-4c09-8b7e-ce13c0e19109";
                        Event event=new Event(creator,eventId,title, date, time, location, description,imageId);

//                                Toast.makeText(AddEvent.this, event.getEventTitle()+" "+event.getEventDate()+" "+event.getEventTime(), Toast.LENGTH_LONG).show();

                        databaseReference.child("events").child(eventId).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent=new Intent(AddEvent.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(AddEvent.this, "Event Added Succesfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddEvent.this, "Event Adding Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        });


    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode ==PICK_IMAGE_REQUEST && data!=null && data.getData()!=null)
        {
            mImageUri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                browseImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                Toast.makeText(this, "Failed to load the image ,plese retry", Toast.LENGTH_SHORT).show();
            }
        }

        else
        {
            Toast.makeText(this, "Failed to load the image ,plese retry", Toast.LENGTH_SHORT).show();
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
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