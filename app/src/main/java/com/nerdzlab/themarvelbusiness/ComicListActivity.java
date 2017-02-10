package com.nerdzlab.themarvelbusiness;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.nerdzlab.themarvelbusiness.adapters.ComicsRecyclerAdapter;
import com.nerdzlab.themarvelbusiness.utils.MyApplication;
import com.nerdzlab.themarvelbusiness.utils.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nerdzlab.themarvelbusiness.utils.Knapsack.knapsack;

/**
 * An activity representing a list of Comics. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ComicDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ComicListActivity extends AppCompatActivity {

    private static final String TAG = "ComicListActivity";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private MarvelApiConfig marvelApiConfig;
    private ComicsRecyclerAdapter mAdapter;
    private List<ComicDto> data;
    private RecyclerView comicsrecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        comicsrecyclerview = (RecyclerView) findViewById(R.id.comic_list);
        assert comicsrecyclerview != null;
        mAdapter = new ComicsRecyclerAdapter(data, this);
        comicsrecyclerview.setAdapter(mAdapter);


        if (findViewById(R.id.comic_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        marvelApiConfig =
                new MarvelApiConfig.Builder(
                        getString(R.string.marvel_public), getString(R.string.marvel_private))
                        .debug()
                        .build();

        GetComicsTask getComicsTask = new GetComicsTask();
        getComicsTask.execute();




    }


    public class GetComicsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "Attempting to get comics");

            ComicApiClient comicApiClient = new ComicApiClient(marvelApiConfig);
            ComicsQuery query = ComicsQuery.Builder.create().withOffset(0).withLimit(100).build();
            try {
                MarvelResponse<ComicsDto> comics = comicApiClient.getAll(query);
                data = comics.getResponse().getComics();


            } catch (MarvelApiException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d(TAG, "notify data set changed");
            mAdapter.swapItems(data);
            Log.d(TAG, "start--------------------------------");
            //Package.printOptimalChoice(data,Float.parseFloat("100.0"));
            int val[] = {10, 40, 30, 50};
            int wt[] = {5, 4, 6, 3};
            int W = 10;
            System.out.println(knapsack(val, wt, W));


        }
    }

}
