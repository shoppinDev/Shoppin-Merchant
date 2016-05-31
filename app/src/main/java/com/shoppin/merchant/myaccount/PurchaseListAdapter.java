package com.shoppin.merchant.myaccount;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shoppin.merchant.R;
import com.shoppin.merchant.main.EditDealFragment;
import com.shoppin.merchant.model.DealDataModel;
import com.shoppin.merchant.model.PurchaseDataModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karm on 06-May-16.
 */
public class PurchaseListAdapter extends ArrayAdapter<PurchaseDataModel> {
    LayoutInflater inflater;
    HashMap<String,Boolean> checkBoxMap = new HashMap<String,Boolean>();
    ArrayList<PurchaseDataModel> purchaseModelList = new ArrayList<>();
    Activity mContext;
    FragmentManager fm;

    public PurchaseListAdapter(Context context, int resource, ArrayList<PurchaseDataModel> purchaseList, FragmentManager fm) {
        super(context, resource, purchaseList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        purchaseModelList = purchaseList;
        this.fm = fm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.item_list_purchases, parent, false);
        }

        final PurchaseDataModel data = getItem(position);

        TextView tvDealTitle = (TextView) view.findViewById(R.id.tvDealTitle);
        TextView tvDealPrice = (TextView) view.findViewById(R.id.tvDealDiscValue);
        TextView tvOrderDate = (TextView) view.findViewById(R.id.tvOrderDate);
        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        TextView tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
        TextView tvCustomerEmail = (TextView) view.findViewById(R.id.tvCustomerEmail);


        tvDealTitle.setText(data.getDealTitle());
        tvDealPrice.setText(data.getDiscountValue());
        tvCustomerName.setText(data.getCustomerName());
        tvCustomerEmail.setText(data.getCustomerEmail());
        tvOrderDate.setText(data.getOrderDate());
        tvAmount.setText(data.getAmount());

        return view;
    }

    @Override
    public PurchaseDataModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public HashMap getCheckedHashMap(){
        return checkBoxMap;
    }

    public int getCheckedCount(){
        int count = 0;
        Log.v("Notification","Shop list size : "+purchaseModelList.size());
        Log.v("Notification"," Checkbox map size : "+checkBoxMap.size());
        for(int i = 0 ; i < purchaseModelList.size(); i++ ){
            if(checkBoxMap.get(purchaseModelList.get(i).getShopId()))
                count++;
        }
        return count;
    }

    public String getSelectedId(){
        String dealId = null;
        for(int i = 0 ; i < purchaseModelList.size();i++){
            if(checkBoxMap.get(purchaseModelList.get(i).getDealId()))
                dealId = purchaseModelList.get(i).getShopId();
        }

        return dealId;
    }
}
