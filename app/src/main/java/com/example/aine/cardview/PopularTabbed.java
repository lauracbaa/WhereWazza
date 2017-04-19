package com.example.aine.cardview;

/**
 * Created by Laura on 18/04/2017.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class PopularTabbed extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popular_main, container, false);

        return rootView;
    }
}
