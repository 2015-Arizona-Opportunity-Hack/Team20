package com.nagirescue;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by cipher1729 on 10/10/2015.
 */
public class LostAndFoundFragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId= R.layout.lostfoundlayout;
        rootView = inflater.inflate(layoutId, container, false);
        setOnClickListeners();
        return rootView;
    }

    private void setOnClickListeners() {
       //lost for found button clicks
        ((Button)rootView.findViewById(R.id.foundBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FoundFragment())
                        .commit();
            }
        });

         //listen for lost button clicks
        ((Button)rootView.findViewById(R.id.lostBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new LostFragment())
                        .commit();
            }
        });


    }
}
