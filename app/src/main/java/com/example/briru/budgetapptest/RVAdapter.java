package com.example.briru.budgetapptest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import android.support.v4.app.Fragment;


import static android.graphics.Color.GREEN;
import static com.example.briru.budgetapptest.NavActivity.spinner;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> implements ItemTouchHelperAdapter{
    static List<Products> productsList;
    static Double cash;
    View v;
    private FragmentActivity context;


    PersonViewHolder personViewHolder;
    RVAdapter(List<Products> productsList, Double currentCash){
        this.productsList = productsList;
        cash = currentCash;
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemPrice;
        ImageView itemPhoto;
        ProgressBar progressBar;

        PersonViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemPrice = (TextView)itemView.findViewById(R.id.item_price);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
    @Override
    public int getItemCount() {
        return productsList.size();
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_card, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, final int i) {


        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        personViewHolder.itemTitle.setText(productsList.get(i).title);
        Double cost = productsList.get(i).price;
        if (cost>=cash){
            personViewHolder.itemPrice.setText(formatter.format(cost)+ " ("+formatter.format(productsList.get(i).price-cash)+") left to go!");
        } else {
            personViewHolder.itemPrice.setText(formatter.format(cost)+ " (You can afford this!!!)");
            personViewHolder.progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        }
        int progress = (int)((cash/cost)*100);
        personViewHolder.progressBar.setProgress(progress,true);

        personViewHolder.itemPhoto.setImageBitmap(SetIMageHelper.getBitmapFromURL(productsList.get(i).image_url));
        spinner.setVisibility(View.GONE);


        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Clicked " + productsList.get(i).title, Toast.LENGTH_LONG).show();
                FragmentActivity activity = (FragmentActivity)v.getContext();
                if (productsList.get(i).title.length()>=30){
                    ((AppCompatActivity) activity).getSupportActionBar().setTitle(productsList.get(i).title.substring(0,30)+"...");
                } else {
                    ((AppCompatActivity) activity).getSupportActionBar().setTitle(productsList.get(i).title);
                }

                ProdDetailsFragment pf = new ProdDetailsFragment();
                Bundle args = new Bundle();
                args.putDouble("Price", productsList.get(i).price);
                args.putString("Rating", productsList.get(i).rating);
                args.putString("ASIN", productsList.get(i).asin);
                args.putString("Description", productsList.get(i).description);
                args.putString("ImgPath", productsList.get(i).image_url);
                args.putString("Manufacturer", productsList.get(i).manufacturer);
                args.putString("Title", productsList.get(i).title);
                args.putString("URL", productsList.get(i).URL);
                args.putBoolean("isManual", false);

                pf.setArguments(args);
                FragmentTransaction fragTrans3 = activity.getSupportFragmentManager().beginTransaction();
                fragTrans3.replace(R.id.frameLayout,pf,"ProgressFragment");
                fragTrans3.commit();
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onItemDismiss(int position) {
          productsList.remove(position);
          HomeFragment.rv.getLayoutManager().removeAllViews();
          System.out.println(HomeFragment.rv.getLayoutManager().getItemCount());
    }

    @Override
    public Boolean onItemMove(int fromPosition, int toPosition) {


//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(productsList, i, i+1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(productsList, i, i-1);
//            }
//        }
//        notifyItemMoved(fromPosition, toPosition);


        View draged = HomeFragment.rv.getLayoutManager().getChildAt(fromPosition);
        View otherView = HomeFragment.rv.getLayoutManager().getChildAt(toPosition);

        System.out.println(otherView);


        return true;

    }


}