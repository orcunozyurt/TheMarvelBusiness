package com.nerdzlab.themarvelbusiness.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.nerdzlab.themarvelbusiness.ComicDetailActivity;
import com.nerdzlab.themarvelbusiness.ComicDetailFragment;
import com.nerdzlab.themarvelbusiness.ComicListActivity;
import com.nerdzlab.themarvelbusiness.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by orcun on 09.02.2017.
 */
public class ComicsRecyclerAdapter extends RecyclerView.Adapter<ComicsRecyclerAdapter.ViewHolder> {
    private final Context context;
    private List<ComicDto> mDataSet;
    private Boolean mTwoPane;

    public ComicsRecyclerAdapter(List<ComicDto> data, Context context, Boolean mTwoPane) {
        this.mDataSet = data;
        this.context = context;
        this.mTwoPane = mTwoPane;
    }

    public void swapItems(List<ComicDto>  data) {
        this.mDataSet = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ComicDto item = mDataSet.get(position);

        holder.getTitle().setText(item.getTitle());
        holder.getDescription().setText(item.getDescription());

        getComicThumbnail(holder.getThumbnail(), item.getThumbnail().getImageUrl(MarvelImage.Size.FULLSIZE));

        Gson gson = new Gson();
        final String itemInString = gson.toJson(item);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ComicDetailFragment.ARG_ITEM_STRING, itemInString);
                    ComicDetailFragment fragment = new ComicDetailFragment();
                    fragment.setArguments(arguments);
                    ((ComicListActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.comic_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ComicDetailActivity.class);
                    intent.putExtra(ComicDetailFragment.ARG_ITEM_STRING, itemInString);

                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView thumbnail;
        private TextView title;
        private TextView description;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }
    }

    public void getComicThumbnail(ImageView thumbview, String url) {

        try {
            URL imageURL = new URL(url);
            Picasso.with(context)
                    .load(String.valueOf(imageURL))
                    .fit()
                    .into(thumbview);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}