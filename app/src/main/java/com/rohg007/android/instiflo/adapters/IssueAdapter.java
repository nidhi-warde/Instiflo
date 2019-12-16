package com.rohg007.android.instiflo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Issue;
import com.rohg007.android.instiflo.utils.ImageRequester;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class IssueAdapter extends ArrayAdapter<Issue> {
    private Context context;
    private List<Issue> issuesList;
    private ImageRequester imageRequester;

    public IssueAdapter(Activity context, List<Issue> issuesList){
        super(context, 0, issuesList);
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.issue_item, null, false);
        }
        Issue currentIssue = getItem(position);
        TextView titleTextView = listViewItem.findViewById(R.id.issue_title);
        String title = currentIssue.getLostFound() + " " + currentIssue.getmObjectType();
        titleTextView.setText(title);

        NetworkImageView issueImage = listViewItem.findViewById(R.id.issue_image);
        imageRequester.setImageFromUrl(issueImage,currentIssue.getmImageUrl());

        TextView authorTextView = listViewItem.findViewById(R.id.issue_author);
        String author = currentIssue.getmName();
        authorTextView.setText(author);
        return listViewItem;
    }
}
