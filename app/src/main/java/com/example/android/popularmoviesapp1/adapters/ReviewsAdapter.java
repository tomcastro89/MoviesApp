package com.example.android.popularmoviesapp1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesapp1.R;
import com.example.android.popularmoviesapp1.data.ReviewData;

import java.util.ArrayList;

/**
 * Created by Tcastrovillari on 03.03.2017.
 */

public class ReviewsAdapter extends ArrayAdapter<ReviewData> {
    public ReviewsAdapter(Context context, ArrayList<ReviewData> reviews) {
        super(context, R.layout.movie_review_list_item, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReviewData reviewData = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_review_list_item, parent, false);
        }

        TextView textViewAuthor = (TextView) convertView.findViewById(R.id.textview_review_author);
        textViewAuthor.setText(reviewData.getAuthor());

        TextView textViewContent = (TextView) convertView.findViewById(R.id.textview_review_content);
        textViewContent.setText(reviewData.getContent());

        return convertView;

    }
}
