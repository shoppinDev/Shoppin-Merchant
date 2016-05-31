package com.shoppin.merchant.model;

import java.io.Serializable;

/**
 * Created by divine on 02/05/16.
 */
public class DealDataModel implements Serializable{

    String dealId,merchantId,shopId,dealCategory,dealSubCategory,dealTitle,dealDesc,dealStartDate,dealEndDate,dealAmount,allDays;
    String discountValue,discountType,location,dealUsage,isActive,addedDate,categoryName,subCategoryName,merchantName,shopName;
    String qrImagePath;
    String countRedeem;

    public DealDataModel(String dealId,String merchantId, String shopId, String dealCategory, String dealSubCategory, String dealTitle,
                  String dealDesc, String dealStartDate, String dealEndDate, String dealAmount, String allDays,String discountValue,
                  String discountType, String location,String dealUsage, String isActive,String addedDate,String categoryName,String subCategoryName,
                  String merchantName,String shopName){

        this.dealId = dealId;
        this.merchantId = merchantId;
        this.shopId = shopId;
        this.dealCategory = dealCategory;
        this.dealSubCategory = dealSubCategory;
        this.dealTitle = dealTitle;
        this.dealDesc = dealDesc;
        this.dealStartDate = dealStartDate;
        this.dealEndDate = dealEndDate;
        this.dealAmount = dealAmount;
        this.allDays = allDays;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.location = location;
        this.dealUsage = dealUsage;
        this.isActive = isActive;
        this.addedDate = addedDate;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.merchantName = merchantName;
        this.shopName = shopName;
    }


    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setDealCategory(String dealCategory) {
        this.dealCategory = dealCategory;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public void setDealSubCategory(String dealSubCategory) {
        this.dealSubCategory = dealSubCategory;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setAllDays(String allDays) {
        this.allDays = allDays;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public void setDealEndDate(String dealEndDate) {
        this.dealEndDate = dealEndDate;
    }

    public void setDealStartDate(String dealStartDate) {
        this.dealStartDate = dealStartDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDealUsage(String dealUsage) {
        this.dealUsage = dealUsage;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public void setQrImagePath(String qrImagePath) {
        this.qrImagePath = qrImagePath;
    }

    public void setCountRedeem(String countRedeem) {
        this.countRedeem = countRedeem;
    }

    public String getCountRedeem() {
        return countRedeem;
    }

    public String getQrImagePath() {
        return qrImagePath;
    }

    public String getAllDays() {
        return allDays;
    }

    public String getDealCategory() {
        return dealCategory;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public String getDealId() {
        return dealId;
    }

    public String getDealEndDate() {
        return dealEndDate;
    }

    public String getDealStartDate() {
        return dealStartDate;
    }

    public String getDealSubCategory() {
        return dealSubCategory;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public String getShopId() {
        return shopId;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDealUsage() {
        return dealUsage;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getLocation() {
        return location;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getShopName() {
        return shopName;
    }
}
