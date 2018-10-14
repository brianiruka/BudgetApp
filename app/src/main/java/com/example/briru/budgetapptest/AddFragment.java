package com.example.briru.budgetapptest;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    RadioButton addTo,subFrom, newAmount;
    RadioGroup rg;
    Button btnUpdate, manualBtn,amaznBtn;
    EditText amountBox;
    Double cashOld = RVAdapter.cash;
    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_adder, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final NumberFormat formatter = NumberFormat.getCurrencyInstance();

        addTo = getView().findViewById(R.id.radio_add);
        addTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountBox.setHint(formatter.format(cashOld) +" + [amount]");
            }
        });
        subFrom = getView().findViewById(R.id.radio_remove);
        subFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountBox.setHint(formatter.format(cashOld) +" - [amount]");

            }
        });
        newAmount = getView().findViewById(R.id.radio_new);
        newAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountBox.setHint("[enter new amount]");

            }
        });
        amountBox = getView().findViewById(R.id.amount_text);
        rg = getView().findViewById(R.id.radioGroup);

        final Users usr = new Users(null,null);
        final String userID = HomeFragment.userID;
        btnUpdate = getView().findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double cashNew = Double.parseDouble(amountBox.getText().toString());




                switch (rg.getCheckedRadioButtonId()){
                    case R.id.radio_add:
                        usr.setCashOnHand(userID,cashOld,cashNew);
                        amountBox.setHint(formatter.format(cashOld+cashNew) +" + (amount)");
                        cashNew = cashOld+cashNew;
                        break;
                    case R.id.radio_remove:
                        usr.setCashOnHand(userID,cashOld,-cashNew);
                        amountBox.setHint(formatter.format(cashOld-cashNew) +" + (amount)");
                        cashNew = cashOld-cashNew;

                        break;
                    case R.id.radio_new:
                        usr.setCashOnHand(userID,0.0,cashNew);
                        amountBox.setHint(formatter.format(cashNew) +" + (amount)");
                        break;
                }
                amountBox.setText("");
                List<Products> ProdList = RVAdapter.productsList;
                System.out.println("cash old: "+cashOld);
                System.out.println("cash new: "+cashNew);
                if (ProdList!=null){
                    for (Products prod : ProdList) {
                        System.out.println("price: "+prod.price);

                        if (prod.price > cashOld && prod.price < cashNew){
                            System.out.println("product price: "+prod.price);
                            Toast.makeText(getContext()," You can now afford "+ prod.title + "!",Toast.LENGTH_LONG).show();
                            Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibe.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                            }else{
                                //deprecated in API 26
                                vibe.vibrate(500);
                            }                    }
                    }
                }

                cashOld = cashNew;
            }
        });
        amountBox = getView().findViewById(R.id.amount_text);
        amountBox.setHint(formatter.format(cashOld) +" + (amount)");
        amaznBtn = getView().findViewById(R.id.btnAmazon);
        amaznBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        ProdFragment pf = new ProdFragment();
        FragmentTransaction fragTrans3 = getFragmentManager().beginTransaction();
        fragTrans3.replace(R.id.frameLayout,pf,"ProductFragment");
        fragTrans3.commit();

    }
        });
        manualBtn = getView().findViewById(R.id.buttonAddManual);
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdDetailsFragment pf = new ProdDetailsFragment();
                FragmentTransaction fragTrans4 = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putBoolean("isManual", true);
                pf.setArguments(args);
                fragTrans4.replace(R.id.frameLayout,pf,"ProductFragment");
                fragTrans4.commit();

            }
        });
    }
}