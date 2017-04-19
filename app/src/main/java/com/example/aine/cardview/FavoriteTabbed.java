package com.example.aine.cardview;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Laura on 18/04/2017.
 */

public class FavoriteTabbed extends Fragment {
    private RecyclerView recyclerView;
    private TvShowFavAdapter adapter;
    private List<TvShow> tvShowList;

    public FavoriteTabbed(){

        //just accept this needs to be here; dont delete
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        tvShowList = new ArrayList<>();
        loadFavouritesFromDB();
        adapter = new TvShowFavAdapter(getContext(), tvShowList);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Set the adapter to be a list array
        recyclerView.setAdapter(adapter);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);

        return rootView;
    }

    private void loadFavouritesFromDB() {

        List<TvShow> TvShowFavouritesList;
        // These need to be read in from the SQLitedatabase
        SQLiteDatabaseHandler db2 = SQLiteDatabaseHandler.getInstance(getContext());
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
