package com.example.briru.budgetapptest;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.NumberFormat;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdDetailsFragment extends Fragment {
    // TextView titleBox,priceBox,descBox,manuBox,ratingBox,asinBox;
    ImageView prodImage;
    Button saveBtn,rBtn,webBtn;
    String URL, imgPath, Title, ASIN,Description,Manufacturer,Rating;
    Double Price;
    NumberFormat formatter;
    Boolean fromAdder = false;
    public ProdDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fromAdder = getArguments().getBoolean("isManual");
        if (!fromAdder){
            Rating = getArguments().getString("Rating");
            ASIN = getArguments().getString("ASIN");
            Description = getArguments().getString("Description");
            imgPath = getArguments().getString("ImgPath");
            Manufacturer = getArguments().getString("Manufacturer");
            Title = getArguments().getString("Title");
            URL = getArguments().getString("URL");
            Price = getArguments().getDouble("Price");
        }





        return inflater.inflate(R.layout.activity_product_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final WebView myWebView = getView().findViewById(R.id.webview);
        final Button amzBtn = getView().findViewById(R.id.toAmazon);
        final Button backBtn = getView().findViewById(R.id.backBtn);
        final Button saveBtn = getView().findViewById(R.id.saveBtn);
        final Button homeBtn = getView().findViewById(R.id.backHome);

        final ImageView prodImage =  getView().findViewById(R.id.imageView);
        final TextView titleBox = getView().findViewById(R.id.titleBox);
        final TextView priceBox = getView().findViewById(R.id.priceBox);
        final TextView descBox = getView().findViewById(R.id.descriptionBox);
        final TextView manuBox = getView().findViewById(R.id.manuBox);
        final TextView ratingBox = getView().findViewById(R.id.ratingBox);
        final TextView asinBox = getView().findViewById(R.id.asinBox);
        final ProgressBar progressBar = getView().findViewById(R.id.progressBar2);
        myWebView.setVisibility(View.GONE);

        if (!fromAdder){
            prodImage.setImageBitmap(SetIMageHelper.getBitmapFromURL(imgPath));
            titleBox.setText(Title);
            formatter = NumberFormat.getCurrencyInstance();
            priceBox.setText(formatter.format(Price));
            descBox.setText(Description);
            manuBox.setText(Manufacturer);
            ratingBox.setText(Rating);
            asinBox.setText(ASIN);
            ratingBox.setEnabled(false);
            manuBox.setEnabled(false);
            ratingBox.setEnabled(false);
            asinBox.setEnabled(false);
        } else {
            imgPath = "https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg";
            prodImage.setVisibility(View.GONE);
            amzBtn.setVisibility(View.GONE);
            saveBtn.setText("Add Item");
        }



        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)getView().getContext();
                HomeFragment pf = new HomeFragment();
                ((AppCompatActivity) activity).getSupportActionBar().setTitle("Progress");
                FragmentTransaction fragTrans3 = activity.getSupportFragmentManager().beginTransaction();
                fragTrans3.replace(R.id.frameLayout,pf,"ProgressFragment");
                fragTrans3.commit();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
            }
        });
        amzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl(URL);
                myWebView.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                backBtn.setText("Back");

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = "";
                if (priceBox.getText().toString().contains("$")){
                    price = priceBox.getText().toString().substring(1);
                    price = price.replaceAll(",", "");

                } else {
                    price = priceBox.getText().toString();
                }

            Products newProduct = new Products(HomeFragment.userID,titleBox.getText().toString(),Double.parseDouble(price) ,descBox.getText().toString(),ratingBox.getText().toString(),asinBox.getText().toString(),imgPath,URL,manuBox.getText().toString(),progressBar);
            newProduct.addToDB();
            progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

}
