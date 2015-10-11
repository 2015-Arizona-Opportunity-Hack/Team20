package com.nagirescue;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cipher1729 on 10/10/2015.
 */
public class HomeFragment extends Fragment{
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId= R.layout.homelayout;
        rootView = inflater.inflate(layoutId, container, false);
        return rootView;
    }


}
