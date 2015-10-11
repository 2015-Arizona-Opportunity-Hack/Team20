package com.nagirescue;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cipher1729 on 10/11/2015.
 */
public class PostsFragment extends Fragment {
    View rootView;
    ListView list;
    String[] itemname ;

    String[] url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId= R.layout.postslayout;
        rootView = inflater.inflate(layoutId, container, false);
        //setOnClickListeners();

        try {
            JSONArray array = new JSONArray(MultiPartHelper.sb.toString());
            itemname = new String[array.length()];
            url = new String[array.length()];
            for(int i=0;i < array.length();i++)
            {
                JSONObject object = array.getJSONObject(i);
                //if(object.getString("phoneNumber")!=null || !(object.has("phoneNumber")))
                //itemname[i]= object.getString("features")+","+ object.getString("phoneNumber");
                itemname[i]  = object.getString("email");
                //itemname[i]="hello";
                    if(object.getString("image")!=null)
                {
                    url[i]= object.getString("image");
                }
                else
                {
                    url[i]="N/A" ;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, url);
        list=(ListView)rootView.findViewById(R.id.postsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }


}
