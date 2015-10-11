package com.nagirescue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by cipher1729 on 10/10/2015.
 */
public class LostFragment extends Fragment {
    View rootView;
    Uri selectedImage;
    double latitude, longitude;
    String type,sex,color,breed, height,collared,tagged,location,time,email,
            firstName,lastName,phone,other,issueType;
    final int TAKE_PICTURE=0;
    Uri imageUri;
    Date today;
    String fileTime;
    public static JSONArray retArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        today = new Date();
        int layoutId= R.layout.findlayout;
        rootView = inflater.inflate(layoutId, container, false);
        setOnClickListeners();
        return rootView;
    }

    private void setOnClickListeners() {
        //lost for found button clicks
        ((Button) rootView.findViewById(R.id.submitBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSharingIntent(getActivity());
                doPostRequest();
                Toast.makeText(getActivity(), "Submitted to database", Toast.LENGTH_LONG).show();

            }
        });

        ((Button) rootView.findViewById(R.id.cameraBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                fileTime = String.valueOf(today.getHours()) + String.valueOf(today.getMinutes()) + String.valueOf(today.getSeconds());
                File photo = new File(Environment.getExternalStorageDirectory(), fileTime + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        /*((Button) rootView.findViewById(R.id.shareBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSharingIntent(getActivity());
            }
        });*/


        }




    public void createSharingIntent( final Context context) {
        //final String res = r;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Would you like to share to a social networking app?"); // display a dialog on failure.
        alertDialogBuilder.setTitle("NAGI FOUNDATION");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                URI uri = null;
                /*try {
                    uri = new URI("http://bebusinessed.com/wp-content/uploads/2014/03/734899052_13956580111.jpg");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*/
               /* androiduri = Uri.parse(uri.toString());*/
               // Log.e("Path", uri.getPath());
                Intent share = ShareCompat.IntentBuilder.from((Activity) context).setText("Lost Dog Found").setType("image/*").setStream(selectedImage).getIntent();
                startActivity(share);
            }
        });

        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();

    }

    private void doPostRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                /*Read values from form*/

                    type = ((EditText) (rootView.findViewById(R.id.type))).getText().toString();
                    sex = ((EditText) (rootView.findViewById(R.id.sex))).getText().toString();
                    color = ((EditText) (rootView.findViewById(R.id.color))).getText().toString();
                    breed = ((EditText) (rootView.findViewById(R.id.breed))).getText().toString();
                    height = ((EditText) (rootView.findViewById(R.id.height))).getText().toString();
                    collared = ((EditText) (rootView.findViewById(R.id.collared))).getText().toString();
                    tagged = ((EditText) (rootView.findViewById(R.id.tagged))).getText().toString();
                    location = ((EditText) (rootView.findViewById(R.id.location))).getText().toString();
                    time = ((EditText) (rootView.findViewById(R.id.time))).getText().toString();
                    email = ((EditText) (rootView.findViewById(R.id.email))).getText().toString();
                    firstName = ((EditText) (rootView.findViewById(R.id.firstName))).getText().toString();
                    lastName = ((EditText) (rootView.findViewById(R.id.lastName))).getText().toString();
                    phone = ((EditText) (rootView.findViewById(R.id.phone))).getText().toString();
                    other = ((EditText) (rootView.findViewById(R.id.other))).getText().toString();
                    issueType = "lost";

                    String urlStr = "http://nagifound-pcqzft2why.elasticbeanstalk.com/nagi/postlostfound";
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    MultiPartHelper multipart = new MultiPartHelper(connection);
                    updateFields(multipart);
                    File imgFile = new File(Environment.getExternalStorageDirectory(),  fileTime+ ".jpg");
                    multipart.addFilePart(imgFile,"image/jpeg","fileObject");
                    multipart.addStringPart(fileTime + ".jpg", "fileName");
                    multipart.makeRequest();

                    //get other results for display
                    urlStr = "http://nagifound-pcqzft2why.elasticbeanstalk.com/nagi/getlostfound";
                    url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    multipart = new MultiPartHelper(connection);
                    updateFields(multipart);
                    multipart.addStringPart("lost", "requestType");
                    multipart.makeRequest();


                     /*I will start a new fragment here with the string in sb*/
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new PostsFragment())
                            .commit();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateFields(MultiPartHelper multipart) {
        multipart.addStringPart(type, "type");
        multipart.addStringPart(sex, "sex");
        multipart.addStringPart(color, "color");
        multipart.addStringPart(breed, "breed");
        multipart.addStringPart(height, "height");
        multipart.addStringPart(collared, "collared");
        multipart.addStringPart(tagged, "tagged");
        //converting location to lat/lng
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 3);
            if (addresses.size() != 0) {
                latitude = (float) addresses.get(0).getLatitude();
                longitude = (float) addresses.get(0).getLongitude();
            } else {
                latitude = 0.0f;
                longitude = 0.0f;
            }
        } catch (Exception e) {

        }
        location= String.valueOf(latitude)+","+ String.valueOf(longitude);
        multipart.addStringPart(location, "location");
        multipart.addStringPart(fileTime, "time");
        multipart.addStringPart(email, "email");
        multipart.addStringPart(firstName, "firstName");
        multipart.addStringPart(lastName, "lastName");
        multipart.addStringPart(phone, "phone");
        multipart.addStringPart(other, "other");
        multipart.addStringPart(issueType, "issueType");


        /*multipart.addStringPart("Cat", "type");
        multipart.addStringPart("male", "sex");*/
        /*multipart.addStringPart("red", "color");
        multipart.addStringPart("gumpy", "breed");
        multipart.addStringPart("1.3", "height");
        multipart.addStringPart("true", "collared");
        multipart.addStringPart("true", "tagged");
        multipart.addStringPart("mesa", "location");
        multipart.addStringPart(fileTime, "time");*/
        // multipart.addStringPart("cipher17292@gmail.com", "email");
        /*multipart.addStringPart("Joe", "firstName");
        multipart.addStringPart("Doe", "lastName");
        multipart.addStringPart("11111111", "phone");
        multipart.addStringPart("none", "other");*/
        // multipart.addStringPart(issueType, "issueType");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);
                        imageView.setScaleType(ImageView.ScaleType.FIT_START);
                        imageView.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.cameraBtn).setVisibility(View.GONE);

                        imageView.setImageBitmap(bitmap);
                        Toast.makeText(getActivity(), selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }


}
