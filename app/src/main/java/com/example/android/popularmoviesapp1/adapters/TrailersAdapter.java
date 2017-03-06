package com.example.android.popularmoviesapp1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.popularmoviesapp1.R;
import com.example.android.popularmoviesapp1.data.VideoData;

import java.util.ArrayList;

/**
 * Created by Tcastrovillari on 03.03.2017.
 */

public class TrailersAdapter extends ArrayAdapter<VideoData> {
    public TrailersAdapter(Context context, ArrayList<VideoData> videos) {
        super(context, R.layout.movie_video_list_item,videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final VideoData videoData = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_video_list_item, parent, false);
        }

        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imagebutton_trailer);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+videoData.getKey())));
            }
        });

        TextView textView = (TextView) convertView.findViewById(R.id.textview_trailers_title);
        textView.setText(videoData.getName());

        return convertView;

    }
}
