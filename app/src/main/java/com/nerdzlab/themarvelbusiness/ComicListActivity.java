package com.nerdzlab.themarvelbusiness;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;


import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.nerdzlab.themarvelbusiness.adapters.ComicsRecyclerAdapter;
import com.nerdzlab.themarvelbusiness.utils.BranchAndBoundSolver;
import com.nerdzlab.themarvelbusiness.utils.KnapsackSolution;
import com.nerdzlab.themarvelbusiness.utils.KnapsackSolver;

import java.util.List;

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
            public void onClick(final View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                new MaterialDialog.Builder(ComicListActivity.this)
                        .title(R.string.input)
                        .content(R.string.input_content)
                        .inputRange(1,5)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                Integer budget = Integer.valueOf(input.toString());
                                Log.d(TAG, "start--------------------------------");

                                KnapsackSolver solver = new BranchAndBoundSolver(data,budget);
                                KnapsackSolution best = solver.solve();
                                data.clear();
                                data = best.getItems();
                                mAdapter.swapItems(data);

                                Snackbar.make(view,"Pages:"+best.getValue() +
                                        " Budget Spent: "+ String.format("%.02f $", best.getWeight()),
                                        Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Reset", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                data.clear();
                                                onResume();

                                            }
                                        }).show();

                                Log.d(TAG, "END------------" );
                            }
                        }).show();
            }
        });

        if (findViewById(R.id.comic_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }





    }

    @Override
    protected void onResume() {
        super.onResume();

        comicsrecyclerview = (RecyclerView) findViewById(R.id.comic_list);
        assert comicsrecyclerview != null;
        mAdapter = new ComicsRecyclerAdapter(data, this, mTwoPane);
        comicsrecyclerview.setAdapter(mAdapter);

        marvelApiConfig =
                new MarvelApiConfig.Builder(
                        getString(R.string.marvel_public), getString(R.string.marvel_private))
                        .debug()
                        .build();

        GetComicsTask getComicsTask = new GetComicsTask();
        getComicsTask.execute();
    }

    public void Reset()
    {

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



        }
    }

}
