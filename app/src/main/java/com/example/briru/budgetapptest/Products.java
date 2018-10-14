package com.example.briru.budgetapptest;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.models.nosql.ProductsDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.example.briru.budgetapptest.HomeFragment.rv;

public class Products {
    String asin, title, manufacturer, image_url, userID, description, rating, URL;
    Double price;
    DynamoDBMapper dynamoDBMapper;
    ProgressBar pbar;
    final com.amazonaws.models.nosql.ProductsDO product = new com.amazonaws.models.nosql.ProductsDO();


    Products(String pUser, String pTitle, Double pPrice, String pDescription, String pRating, String pASIN, String pImgPath, String pURL, String pManufacturer, ProgressBar pb){
        asin = pASIN;
        title = pTitle;
        manufacturer = pManufacturer;
        price = pPrice;
        image_url = pImgPath;
        userID = pUser;
        description = pDescription;
        rating = pRating;
        URL = pURL;
        pbar = pb;
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

    Products(String pUserID){
        userID = pUserID;

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

     public void addToDB(){
        product.setUserId(userID);
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setManufacturer(manufacturer);
        product.setRating(rating);
        product.setASIN(asin);
        product.setImagePath(image_url);
        product.setURL(URL);
        product.setRanking(RVAdapter.productsList.size()+1.0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(product);
                runOnUiThread (new Thread(new Runnable() {
                    public void run() {
                        pbar.setVisibility(View.GONE);
                    }
                }));
                // Item saved
            }
        }).start();
    }

    public ArrayList<Products> getFromDB(final Double cash){
        final ArrayList<Products> allProducts = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void run() {
                product.setUserId(userID);




                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(product)
                        .withRangeKeyCondition("Ranking",null)
                        .withConsistentRead(false);

                PaginatedList<ProductsDO> result = dynamoDBMapper.query(com.amazonaws.models.nosql.ProductsDO.class, queryExpression);

                Gson gson = new Gson();
                StringBuilder stringBuilder = new StringBuilder();

                // Loop through query results
                for (int i = 0; i < result.size(); i++) {
                    String jsonFormOfItem = gson.toJson(result.get(i));
                    stringBuilder.append(jsonFormOfItem + "\n\n");
                    Products prod = new Products( result.get(i).getUserId(), result.get(i).getTitle(), result.get(i).getPrice(), result.get(i).getDescription(), result.get(i).getRating(),result.get(i).getASIN(),result.get(i).getImagePath(), result.get(i).getURL(),result.get(i).getManufacturer(),null );
                    allProducts.add(prod);
                }

                // Add your code here to deal with the data result
                //Log.d("Query results: ", stringBuilder.toString());

                if (result.isEmpty()) {
                    Log.d("No results: ", stringBuilder.toString());
                }
               // System.out.println("all products is: "+allProducts.get(0).price);
                final RVAdapter adapter = new RVAdapter(allProducts,cash);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (allProducts.isEmpty()){
                            NavActivity.spinner.setVisibility(View.GONE);
                        }

                        System.out.println(rv);
                        rv.setAdapter(adapter);

                        HomeFragment.cashText.setText("");
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();
                        HomeFragment.cashText.setText(formatter.format(cash).toString()+" saved up!");

                    }
                });
//                for (Products products: productsArray){
//                    // Log.d("Title", products.title);
//
//                }
            }
        }).start();
        return allProducts;
    }
}
