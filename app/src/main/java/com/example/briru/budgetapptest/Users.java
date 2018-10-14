package com.example.briru.budgetapptest;

import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.models.nosql.ProductsDO;
import com.amazonaws.models.nosql.UserInfoDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Users {
    DynamoDBMapper dynamoDBMapper;
    Double cash;
    String uName;
    String uEmail;
    final UserInfoDO user = new UserInfoDO();


    Users(String name, String email){
        uName=name;
        uEmail=email;
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
    }
    public void createUser(String userID,String name, String email){

        user.setUserId(userID);
        user.setName(name);
        user.setEmail(email);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(user);
                // Item saved
            }
        }).start();
    }
    public void setCashOnHand(String userID,Double currentAmt, Double addAmt){

        user.setUserId(userID);
        user.setCashOnHand(currentAmt+addAmt);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(user);
                // Item saved
            }
        }).start();
    }

    public Double getCashOnHand(final String userID){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ( dynamoDBMapper.load(UserInfoDO.class,userID) == null){
                    System.out.println("No such user");
                    createUser(userID,uName,uEmail);
                } else {
                    cash =  dynamoDBMapper.load(UserInfoDO.class,userID).getCashOnHand();
                    Products p = new Products(userID);
                    p.getFromDB(cash);
                }


            }
        }).start();
        return cash;
    }

        // Get a book - Id=101
    }

