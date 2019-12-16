package com.rohg007.android.instiflo.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.adapters.IssueAdapter;
import com.rohg007.android.instiflo.models.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IssueFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<Issue> issuesList;
    private ListView listViewLostFound;
    public static final int activity = 1001;

    public IssueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_issue, container, false);

        final FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.add));
        databaseReference = FirebaseDatabase.getInstance().getReference("issues");
        issuesList = new ArrayList<>();
        listViewLostFound = v.findViewById(R.id.issue_list_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddIssue.class);
                startActivity(intent);
            }
        });

        //If the list view is empty
        TextView empty_case = v.findViewById(android.R.id.empty);
        listViewLostFound.setEmptyView(empty_case);

        listViewLostFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Issue currIssue = issuesList.get(position);
                Intent intent = new Intent(getActivity(),IssueDetails.class);
                intent.putExtra("ISSUE",currIssue);
                intent.putExtra("calling-activity",activity);
                startActivity(intent);
            }
        });

        getIssueDetailsFromFirebase();
        return v;
    }

    private void getIssueDetailsFromFirebase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                issuesList.clear();
                //Data Snapshot to get the data from Firebase Database
                for (DataSnapshot lostFoundSnapshot : dataSnapshot.getChildren()) {
                    Issue details = lostFoundSnapshot.getValue(Issue.class);
                    if (details.getmVisibililty().equals("YES"))
                        issuesList.add(details);
                }
                //Sets the adapter to Details Adapter for our custom list
                IssueAdapter detailsAdapter = new IssueAdapter(getActivity(), issuesList);
                listViewLostFound.setAdapter(detailsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode() + ": " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
