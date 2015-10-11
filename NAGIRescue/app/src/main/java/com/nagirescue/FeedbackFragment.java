package com.nagirescue;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cipher1729 on 10/10/2015.
 */
public class FeedbackFragment extends Fragment{
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId= R.layout.feedbacklayout;
        rootView = inflater.inflate(layoutId, container, false);
        //setOnClickListeners();
        return rootView;
    }
}
