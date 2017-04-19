package com.example.aine.cardview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.aine.cardview.PopularActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laura on 18/04/2017.
 */

public class PopularTabbed extends Fragment {

    private TvShowAdapter adapter;
    private List<TvShow> tvShowList;

    final String REQUEST_POPULAR="https://api.themoviedb.org/3/discover/tv?api_key=872b836efb69a145906d151fc7e865d1&language=en-UK&sort_by=popularity.desc&page=1&timezone=UK/London&include_null_first_air_dates=false";

    public PopularTabbed(){

        //just accept this needs to be here; dont delete
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);

        //Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        //initCollapsingToolbar(rootView);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        tvShowList = new ArrayList<>();
        // Set up adapter to the RecycleView
        adapter = new TvShowAdapter(getContext(), tvShowList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config); // Do it on Application start

        new PopularShows(new PopularShows.AsyncResponse() {
            @Override
            public void processFinish(JSONArray result) {
                // This functionality is only run when the call to the external film service has been
                // completed and the film data has been stored in the tvShow List array
                Log.d("TVSHOW_RESULT", result.toString());

                // Loop through the JSON array and add entries to the TvList
                for (int i = 0; i < result.length(); i++) {
                    try {
                        makeView(result, i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Inform adapter that data has changed in the cardview
                // This will drive the drawing of the dynamic data
                adapter.notifyDataSetChanged();
            }
        }).execute(REQUEST_POPULAR);

        return rootView;


    }

    /**
     * Initializing the TV show in the List array from the JSON feed for the moviedb
     * api call
     */
    public void makeView(JSONArray popShows, int index) throws JSONException {
        TvShow tvShow = new TvShow();
        JSONObject JSONpopShow = popShows.getJSONObject(index);

        Log.d("JSON_SHOW", JSONpopShow.toString());

        tvShow.setName(JSONpopShow.getString("name"));
        tvShow.setId(JSONpopShow.getInt("id"));
        tvShow.setBackdropPath(tvShow.getIMG_BACKDROP_BASE_URL()+ JSONpopShow.getString("backdrop_path"));
        tvShow.setOverview(JSONpopShow.getString("overview"));
        tvShow.setPopularity(JSONpopShow.getDouble("popularity"));
        tvShow.setPosterPath(tvShow.getIMG_POSTER_BASE_URL()+ JSONpopShow.getString("poster_path"));
        Log.d( "MAIN","makeView: PosterPath= " + tvShow.getPosterPath());

        tvShowList.add(tvShow);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}