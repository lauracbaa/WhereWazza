package com.example.aine.cardview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by aflynn on 17/04/2017.
 */


public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TvShowFavAdapter adapter;
    private List<TvShow> tvShowList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_main);
        tvShowList = new ArrayList<>();
        loadFavouritesFromDB();
        adapter = new TvShowFavAdapter(this, tvShowList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new FavouriteActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Set the adapter to be a list array
        recyclerView.setAdapter(adapter);
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config); // Do it on Application start
        try {
            // Draw the image at the top of the screen first
            Glide.with(this).load(R.drawable.tvcover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loadFavouritesFromDB() {

        List<TvShow> TvShowFavouritesList;
        // These need to be read in from the SQLitedatabase
        SQLiteDatabaseHandler db2 = SQLiteDatabaseHandler.getInstance(this);
        TvShowFavouritesList = new ArrayList<TvShow>();
        TvShowFavouritesList = db2.getAllTvShowFavourites();
        // Close the database connection
        db2.close();
        // Copy the favourites into the current TvShowList
        copyFavouritesFromDB(TvShowFavouritesList);


    }



    private void copyFavouritesFromDB(List<TvShow> TvShowFavouritesList) {

//        TvShowList= new ArrayList<>();
        // Iterate through the favourites and copy them to the TvShowlist
        Iterator<TvShow> TvShowIterator = TvShowFavouritesList.iterator();
        while (TvShowIterator.hasNext()) {
            // tempTvShow will hold a temporary version of the current favourite before is copied to the list
            TvShow tempTvShow = new TvShow();
            // Use tempfav to store the current TvShow favourite that is being iterated through
            TvShow tempfav = TvShowIterator.next();
            tempTvShow.setId(tempfav.getId());
            tempTvShow.setName(tempfav.getName());
            tempTvShow.setPopularity(tempfav.getPopularity());
            tempTvShow.setOverview(tempfav.getOverview());
            tempTvShow.setPosterPath(tempfav.getPosterPath());
            tempTvShow.setBackdropPath(tempfav.getBackdropPath());
            Log.d( "ADAPT","copyFavouritesFromDB PosterPathxxx= " + tempTvShow.getPosterPath());
            tvShowList.add(tempTvShow);
        }


    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    /*private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //               collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }*/


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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