package com.shoppin.merchant.model;

/**
 * Created by Karm on 13-May-16.
 */
public class CategoryDataModel {

    String categoryId;
    String categoryName;

    public CategoryDataModel(String categoryId,String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
