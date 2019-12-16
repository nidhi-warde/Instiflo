package com.rohg007.android.instiflo.ui;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.rohg007.android.instiflo.R;
import com.squareup.picasso.Picasso;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        final Intent intent = new Intent(Intent.ACTION_VIEW);

        CircleImageView img1 = findViewById(R.id.about_img1);
        CircleImageView img2 = findViewById(R.id.about_img2);
        CircleImageView img3 = findViewById(R.id.about_img3);
        CircleImageView img4 = findViewById(R.id.about_img4);
        CircleImageView img5 = findViewById(R.id.about_img5);

        Picasso.get().load(R.drawable.rohan).into(img1);
        Picasso.get().load(R.drawable.jatin).into(img2);
        Picasso.get().load(R.drawable.aditi).into(img3);
        Picasso.get().load(R.drawable.nirmal).into(img4);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("https://www.linkedin.com/in/rohg007/"));
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("https://www.linkedin.com/in/jatin-kumar-7025b8151/"));
                startActivity(intent);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("https://www.linkedin.com/in/aditi-taank-b246a8153/"));
                startActivity(intent);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("https://www.linkedin.com/in/nirmal-singh-rajawat-67a194158/"));
                startActivity(intent);
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse("https://www.linkedin.com/in/nidhi-warde-635bbb156/"));
                startActivity(intent);
            }
        });
    }
}
