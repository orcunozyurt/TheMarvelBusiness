package com.nerdzlab.themarvelbusiness;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.CreatorResourceDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResourceDto;
import com.karumi.marvelapiclient.model.MarvelResources;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import android.support.v7.graphics.Palette;


import static com.nerdzlab.themarvelbusiness.utils.MyApplication.TAG;


/**
 * A fragment representing a single Comic detail screen.
 * This fragment is either contained in a {@link ComicListActivity}
 * in two-pane mode (on tablets) or a {@link ComicDetailActivity}
 * on handsets.
 */
public class ComicDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item String that this fragment
     * represents.
     */
    public static final String ARG_ITEM_STRING = "item_string";
    private ComicDto mItem;
    private CollapsingToolbarLayout appBarLayout;
    private TextView title;
    private TextView description;
    private LinearLayout paymentTypeLayout;
    private TextView price;
    private TextView optional;
    private LinearLayout timeLayout;
    private TextView optionalOne;
    private TextView optionalTwo;
    protected RecyclerView.LayoutManager mLayoutManager;
    private MarvelApiConfig marvelApiConfig;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComicDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivityTransitions();

        if (getArguments().containsKey(ARG_ITEM_STRING)) {

            Gson gson = new Gson();
            mItem = gson.fromJson(getArguments().getString(ARG_ITEM_STRING), ComicDto.class);

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            final ImageView image = (ImageView) getActivity().findViewById(R.id.image);
            if (appBarLayout != null) {
                appBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
                appBarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                appBarLayout.setTitle(mItem.getTitle());

                Picasso.with(getContext())
                        .load(mItem.getThumbnail().getImageUrl(MarvelImage.Size.LANDSCAPE_LARGE))
                        .fit()
                        .into(image, new Callback() {
                    @Override public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                    }

                    @Override public void onError() {

                    }
                });
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_detail, container, false);

        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        paymentTypeLayout = (LinearLayout) view.findViewById(R.id.payment_type_layout);
        price = (TextView) view.findViewById(R.id.price);
        optional = (TextView) view.findViewById(R.id.optional);
        timeLayout = (LinearLayout) view.findViewById(R.id.time_layout);
        optionalOne = (TextView) view.findViewById(R.id.optional_one);
        optionalTwo = (TextView) view.findViewById(R.id.optional_two);


        title.setText(mItem.getTitle());
        description.setText(mItem.getDescription());
        price.setText(mItem.getPrices().get(0).getPrice() + " $");
        optionalOne.setText(mItem.getPageCount() + " Pages");

        String creators_names = "";

        for (CreatorResourceDto creator : mItem.getCreators().getItems())
        {
            creators_names += creator.getName()+ " ";
        }
        optionalTwo.setText(creators_names);

        return view;
    }



    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getActivity().getWindow().setEnterTransition(transition);
            getActivity().getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        appBarLayout.setContentScrimColor(palette.getMutedColor(primary));
        appBarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) getActivity().findViewById(R.id.fab), palette);
        getActivity().supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorAccent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

}
