package com.shoppin.merchant.model;

import java.io.Serializable;

/**
 * Created by Karm on 16-May-16.
 */
public class DraftsDataModel implements Serializable{

    String dealId,merchantId,saveId,shopId,dealCategory,dealSubCategory,dealTitle,dealDesc,dealStartDate,dealEndDate,dealAmount,allDays;
    String discountValue,discountType,location,dealUsage,isActive,addedDate,merchantName;
    String customerId,customerName,customerEmail,customerPassword,customerMobile,isMobileVerify;

    public DraftsDataModel(String saveId,String customerId,String customerName,
                             String customerEmail,String customerPassword,String customerMobile,String isMobileVerify,String dealId,
                             String merchantId,String shopId,String dealCategory,String dealSubCategory,String dealTitle,String dealDesc,
                             String dealStartDate,String dealEndDate,String dealAmount,String allDays,String discountValue,String discountType,
                             String location,String dealUsage,String isActive,String addedDate,String merchantName){
        this.saveId = saveId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerMobile = customerMobile;
        this.isMobileVerify = isMobileVerify;
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
        this.merchantName = merchantName;
    }

    public String getDealCategory() {
        return dealCategory;
    }

    public String getDealId() {
        return dealId;
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

    public String getShopId() {
        return shopId;
    }

    public String getAllDays() {
        return allDays;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public String getDealEndDate() {
        return dealEndDate;
    }

    public String getDealStartDate() {
        return dealStartDate;
    }

    public String getDealUsage() {
        return dealUsage;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getLocation() {
        return location;
    }


    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getSaveId() {
        return saveId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public String getIsMobileVerify() {
        return isMobileVerify;
    }
}
