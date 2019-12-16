package com.rohg007.android.instiflo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.adapters.IssueAdapter;
import com.rohg007.android.instiflo.models.Issue;

import java.util.ArrayList;
import java.util.List;

public class ApproveIssue extends AppCompatActivity {

    private ListView listViewLostFound;
    private DatabaseReference databaseReference;
    public static final int activity = 1003;
    List<Issue> issueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_issue);

        Toolbar toolbar = findViewById(R.id.approve_issues_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("issues");
        listViewLostFound = findViewById(R.id.approve_issue_list_view);
        issueList=new ArrayList<Issue>();

        TextView empty_case = findViewById(android.R.id.empty);
        listViewLostFound.setEmptyView(empty_case);

        listViewLostFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Gets the current position on List View and sends an intent to open Description Activity
                Issue currDetails = issueList.get(position);
                Intent intent = new Intent(ApproveIssue.this, IssueDetails.class);
                intent.putExtra("ISSUE", currDetails);
                intent.putExtra("calling-activity",activity);
                startActivity(intent);
            }
        });

        getData();
    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                issueList.clear();
                //Data Snapshot to get the data from Firebase Database
                for (DataSnapshot lostFoundSnapshot : dataSnapshot.getChildren()) {
                    Issue details = lostFoundSnapshot.getValue(Issue.class);
                    if(details.getmApproved().equals("NO"))
                        issueList.add(details);
                }
                //Sets the adapter to Details Adapter for our custom list
                IssueAdapter detailsAdapter = new IssueAdapter(ApproveIssue.this, issueList);
                listViewLostFound.setAdapter(detailsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getCode() + ": " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
