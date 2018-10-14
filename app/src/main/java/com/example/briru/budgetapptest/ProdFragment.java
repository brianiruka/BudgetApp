package com.example.briru.budgetapptest;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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
import java.io.PrintWriter;
import java.text.NumberFormat;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdFragment extends Fragment {
    String chosenURL;
    // TextView titleBox,priceBox,descBox,manuBox,ratingBox,asinBox;
    ImageView prodImage;
    Button saveBtn,rBtn,webBtn;
    String URL, imgPath;

    public ProdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_webview, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final WebView myWebView = getView().findViewById(R.id.webview);
        final Button rBtn = getView().findViewById(R.id.returnBtn);
        final Button wBtn = getView().findViewById(R.id.webBtn);

        final Button saveBtn = getView().findViewById(R.id.saveBtn);
        final ImageView prodImage =  getView().findViewById(R.id.imageView);
        final TextView titleBox = getView().findViewById(R.id.titleBox);
        final TextView priceBox = getView().findViewById(R.id.priceBox);
        final TextView descBox = getView().findViewById(R.id.descriptionBox);
        final TextView manuBox = getView().findViewById(R.id.manuBox);
        final TextView ratingBox = getView().findViewById(R.id.ratingBox);
        final TextView asinBox = getView().findViewById(R.id.asinBox);
        final ProgressBar progressBar = getView().findViewById(R.id.progressBar2);

        myWebView.loadUrl("http://www.amazon.com");
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                if (url.contains("/dp/")){
                    wBtn.setVisibility(View.VISIBLE);
                    String title = myWebView.getTitle();
                    wBtn.setText("ADD "+title.substring(title.indexOf(":") + 1).substring(0,30)+"...");
                } else {
                    wBtn.setVisibility(View.GONE);
                }
                chosenURL = url;
                Log.d("URL:", url);
            }
        });

        wBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                getWebsite(prodImage,titleBox,priceBox,descBox,manuBox,ratingBox,asinBox,progressBar);
                wBtn.setVisibility(View.GONE);
            }
        });
        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.setVisibility(View.VISIBLE);
                wBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = "";
                if (priceBox.getText().toString().contains("$")){
                    price= priceBox.getText().toString().substring(1);
                } else {
                    price = priceBox.getText().toString();
                }

            Products newProduct = new Products(HomeFragment.userID,titleBox.getText().toString(),Double.parseDouble(price) ,descBox.getText().toString(),ratingBox.getText().toString(),asinBox.getText().toString(),imgPath,chosenURL,manuBox.getText().toString(),progressBar);
            newProduct.addToDB();
            progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getWebsite(final ImageView i, final TextView t, final TextView p, final TextView d, final TextView m, final TextView r, final TextView a, final ProgressBar progressBar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(chosenURL).get();

                    String title = doc.title();
                    final String parsedTitle = title.substring(title.indexOf(":") + 2);
//                    System.out.println(doc.body());

                    //Elements links = doc.select("a[href]");
                    //Element asin = doc.getElementById("ASIN");
                    final Element asin = doc.body().select("[data-fling-asin").first();
                    final Element asin2 = doc.body().select("[data-asin").first();
                    final Element price3= doc.body().select("[data-asin-price").first();
                    final Element price = doc.body().getElementById("priceblock_ourprice");
                    final Element price2 = doc.body().getElementById("olp");
                    final Element manu = doc.body().getElementById("productDetails_techSpec_section_1");
                    final Element desc = doc.body().getElementById("productDescription");
                    final Element rating = doc.body().getElementById("averageCustomerReviews_feature_div");
                    final Element manu2 =doc.body().select("th:contains(Manufacturer)").first();
                    final Element title2 = doc.body().getElementById("title");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if(title2!=null){
                                t.setText(title2.text());
                            } else {
                                t.setText(parsedTitle);
                            }
                            if(asin!=null){
                                a.setText(asin.attr("data-fling-asin"));
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                Bitmap bm = SetIMageHelper.getBitmapFromURL(asin.attr("data-midres-replacement"));
                                imgPath = asin.attr("data-midres-replacement");
                                i.setImageBitmap(bm);
                                //System.out.println("Image : "+);
                            } else if (asin2 !=null){
                                a.setText(asin2.attr("data-asin"));

                            }
                            if (price3!=null){
                                p.setText(price3.attr("data-asin-price"));
                            }
                            else if (price!=null){
                                p.setText(price.text());
                            } else if(price2!=null){
                                p.setText(price2.child(0).child(0).html());
                            }
                            if (manu2!=null){
                                m.setText(manu2.nextElementSibling().text());
                            } else if (manu!=null){
                                m.setText(manu.text());
                            }
                            if (desc!=null){
                                d.setText(desc.child(0).text());
                            }
                            if (rating!=null){
                                r.setText(rating.text());
                            }


                        }
                    });

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }


            }
        }).start();
    }
}
