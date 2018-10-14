package com.example.briru.budgetapptest;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Collections;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    DynamoDBMapper dynamoDBMapper;
    String email = "";
    String name = "";
    static RecyclerView rv;
    static TextView cashText;
    static TextView greetText;
    static String userID;
    Double cash = 0.0;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_recycler, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
        AWSConfiguration awsConfig = AWSMobileClient.getInstance().getConfiguration();
        CognitoUserPool userPool = new CognitoUserPool(getContext(),awsConfig);
        CognitoUser currentUser = userPool.getCurrentUser();
        userID = currentUser.getUserId();

        GetDetailsHandler getDetailsHandler  = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                Map<String,String> attributeMap = list.getAttributes().getAttributes();
                email = attributeMap.get("email");
                name = attributeMap.get("given_name");
                greetText = getView().findViewById(R.id.prodTitle);
                greetText.setText("Hello "+name+", you currently have:");
            }
            @Override
            public void onFailure(final Exception exception) {
                // Failed to retrieve the user details, probe exception for the cause
            }
        };
        currentUser.getDetailsInBackground(getDetailsHandler);
        rv = getView().findViewById(R.id.rview);

        cashText = getView().findViewById(R.id.cashText);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

// Extend the Callback class
        final RVAdapter adapter = new RVAdapter(RVAdapter.productsList,cash);

//        ItemTouchHelper.Callback scb = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
//
//
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView,
//                                        RecyclerView.ViewHolder viewHolder) {
//                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//                System.out.println("drag value" + dragFlags);
//                System.out.println("swipe value" + swipeFlags);
//
//                return makeMovementFlags(dragFlags, swipeFlags);
//            }
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                // get the viewHolder's and target's positions in your adapter data, swap them
//                //Collections.swap( viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                // and notify the adapter that its dataset has changed
//                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){
//                System.out.println("Swiped");
//            }
//
//            @Override
//            public boolean isLongPressDragEnabled() {
//                return true;
//            }
//        };
//
//
//        ItemTouchHelper ith = new ItemTouchHelper(scb);
//        ith.attachToRecyclerView(rv);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

        rv.setLayoutManager(llm);
        Users u = new Users(name,email);
        u.getCashOnHand(userID);

    }

}
