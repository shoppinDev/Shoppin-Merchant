package com.shoppin.merchant.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shoppin.merchant.R;
import com.shoppin.merchant.model.ShopModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karm on 06-May-16.
 */
public class EditShopListAdapter extends ArrayAdapter<ShopModel> {

    LayoutInflater inflater;
    HashMap<String,Boolean> checkBoxMap = new HashMap<String,Boolean>();
    ArrayList<ShopModel> shopModelList = new ArrayList<>();
    Activity mContext;
    FragmentManager fm;

    public EditShopListAdapter(Context context, int resource, ArrayList<ShopModel> shopList, FragmentManager fm) {
        super(context, resource, shopList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        shopModelList = shopList;
        this.fm = fm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_edit_shop, parent, false);
        }

        final ShopModel data = getItem(position);

        TextView tvShopName = (TextView) view.findViewById(R.id.tvShopName);
        TextView tvShopAddress = (TextView) view.findViewById(R.id.tvShopAddress);

        Button btnEditShop = (Button) view.findViewById(R.id.btnEditShop);
        btnEditShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditShopInfoFragment();
                Bundle args = new Bundle();
                args.putSerializable("shop_data",data);
                fragment.setArguments(args);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,fragment);
                ft.commit();
            }
        });

        tvShopName.setText(data.getShopName());
        tvShopAddress.setText(data.getShopAddress());

        return view;
    }

    @Override
    public ShopModel getItem(int position) {
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
        Log.v("Notification","Shop list size : "+shopModelList.size());
        Log.v("Notification"," Checkbox map size : "+checkBoxMap.size());
        for(int i = 0 ; i < shopModelList.size(); i++ ){
            if(checkBoxMap.get(shopModelList.get(i).getShopId()))
                count++;
        }
        return count;
    }

    public String getSelectedId(){
        String shopId = null;
        for(int i = 0 ; i < shopModelList.size();i++){
            if(checkBoxMap.get(shopModelList.get(i).getShopId()))
                shopId = shopModelList.get(i).getShopId();
        }

        return shopId;
    }
}
