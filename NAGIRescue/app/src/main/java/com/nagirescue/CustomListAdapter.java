package com.nagirescue;

import java.util.ArrayList;

/**
 * Created by cipher1729 on 10/11/2015.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, String[] imgid) {
        super(context, R.layout.mylistlayout, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylistlayout, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        WebView webView = (WebView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        webView.loadUrl(imgid[position]);
        extratxt.setText("Description "+itemname[position]);
        return rowView;

    };
}