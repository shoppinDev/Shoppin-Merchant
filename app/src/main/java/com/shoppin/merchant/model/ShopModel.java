package com.shoppin.merchant.model;

import java.io.Serializable;

/**
 * Created by Karm on 06-May-16.
 */
public class ShopModel implements Serializable{

    String shopName,shopAddress,shopLatitude,shopLongitude;
    String shopId,merchantId,shopEmail,shopMobile,qrImage,shopAdd,shopCity,shopState,shopZip,shopCountry,isActive,addedDate;

    public ShopModel(String shopId,String merchantId,String shopName,String shopAddress,String shopLatitude,String shopLongitude,String shopEmail,
                     String shopMobile,String qrImage,String shopAdd,String shopCity,String shopState,String shopZip,String shopCountry,String isActive,String addedDate){
        this.shopId = shopId;
        this.merchantId = merchantId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopLatitude = shopLatitude;
        this.shopLongitude = shopLongitude;
        this.shopEmail = shopEmail;
        this.shopMobile = shopMobile;
        this.qrImage = qrImage;
        this.shopAdd = shopAdd;
        this.shopCity = shopCity;
        this.shopState = shopState;
        this.shopZip = shopZip;
        this.shopCountry = shopCountry;
        this.isActive = isActive;
        this.addedDate = addedDate;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopLatitude() {
        return shopLatitude;
    }

    public String getShopLongitude() {
        return shopLongitude;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getQrImage() {
        return qrImage;
    }

    public String getShopAdd() {
        return shopAdd;
    }

    public String getShopCity() {
        return shopCity;
    }

    public String getShopCountry() {
        return shopCountry;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public String getShopId() {
        return shopId;
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public String getShopState() {
        return shopState;
    }

    public String getShopZip() {
        return shopZip;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }

    public void setShopZip(String shopZip) {
        this.shopZip = shopZip;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public void setShopAdd(String shopAdd) {
        this.shopAdd = shopAdd;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public void setShopCountry(String shopCountry) {
        this.shopCountry = shopCountry;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setShopLatitude(String shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopLongitude(String shopLongitude) {
        this.shopLongitude = shopLongitude;
    }
}
