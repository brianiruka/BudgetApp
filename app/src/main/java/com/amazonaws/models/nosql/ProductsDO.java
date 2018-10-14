package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "budgetapp-mobilehub-2022599069-Products")

public class ProductsDO {
    private String _userId;
    private Double _ranking;
    private String _aSIN;
    private String _description;
    private String _imagePath;
    private String _manufacturer;
    private Double _price;
    private String _rating;
    private String _title;
    private String _uRL;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "Ranking")
    @DynamoDBAttribute(attributeName = "Ranking")
    public Double getRanking() {
        return _ranking;
    }

    public void setRanking(final Double _ranking) {
        this._ranking = _ranking;
    }
    @DynamoDBAttribute(attributeName = "ASIN")
    public String getASIN() {
        return _aSIN;
    }

    public void setASIN(final String _aSIN) {
        this._aSIN = _aSIN;
    }
    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return _description;
    }

    public void setDescription(final String _description) {
        this._description = _description;
    }
    @DynamoDBAttribute(attributeName = "ImagePath")
    public String getImagePath() {
        return _imagePath;
    }

    public void setImagePath(final String _imagePath) {
        this._imagePath = _imagePath;
    }
    @DynamoDBAttribute(attributeName = "Manufacturer")
    public String getManufacturer() {
        return _manufacturer;
    }

    public void setManufacturer(final String _manufacturer) {
        this._manufacturer = _manufacturer;
    }
    @DynamoDBAttribute(attributeName = "Price")
    public Double getPrice() {
        return _price;
    }

    public void setPrice(final Double _price) {
        this._price = _price;
    }
    @DynamoDBAttribute(attributeName = "Rating")
    public String getRating() {
        return _rating;
    }

    public void setRating(final String _rating) {
        this._rating = _rating;
    }
    @DynamoDBAttribute(attributeName = "Title")
    public String getTitle() {
        return _title;
    }

    public void setTitle(final String _title) {
        this._title = _title;
    }
    @DynamoDBAttribute(attributeName = "URL")
    public String getURL() {
        return _uRL;
    }

    public void setURL(final String _uRL) {
        this._uRL = _uRL;
    }

}
