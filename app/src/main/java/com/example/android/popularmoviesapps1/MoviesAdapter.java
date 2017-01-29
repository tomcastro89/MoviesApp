/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapps1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{


    private ListItemClickListener mListItemClickListener;
    private ArrayList<MovieData> mMovieDataList;
    private Context mContext;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(ListItemClickListener listener){
        mListItemClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieData movieData = mMovieDataList.get(position);
        String movieImageUrl = movieData.getMovieImageURL();
        Picasso.with(mContext).load(movieImageUrl).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieDataList==null ? 0 : mMovieDataList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImageView;

        public MovieViewHolder(View itemView){
            super(itemView);

            movieImageView = (ImageView) itemView.findViewById(R.id.imageView_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setMovieDataList(ArrayList<MovieData> movieDataList) {
        this.mMovieDataList = movieDataList;
        notifyDataSetChanged();
    }

    public ArrayList<MovieData> getMovieDataList() {
        return mMovieDataList;
    }
}
