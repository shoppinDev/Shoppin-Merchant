package com.shoppin.merchant.model;

/**
 * Created by Karm on 13-May-16.
 */
public class SubCategoryDataModel {

    String subCategoryId;
    String categoryId;
    String subCategoryName;

    public SubCategoryDataModel(String subCategoryId,String subCategoryName,String categoryId){
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
