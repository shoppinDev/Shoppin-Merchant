package com.shoppin.merchant.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class ShopListAdapter extends ArrayAdapter<ShopModel> {

    LayoutInflater inflater;
    HashMap<String,String> checkedData = new HashMap<>();
    ArrayList<ShopModel> shopModelList;
    String selectedShopId;

    public ShopListAdapter(Context context, int resource, ArrayList<ShopModel> shopList) {

        super(context, resource, shopList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        shopModelList = shopList;
    }

    public void setSelectedShopId(String selectedShopId) {
        this.selectedShopId = selectedShopId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_shop, parent, false);
        }

        final ShopModel data = getItem(position);

        TextView tvShopName = (TextView) view.findViewById(R.id.tvShopName);
        TextView tvShopAddress = (TextView) view.findViewById(R.id.tvShopAddress);



        CheckBox cbItem = (CheckBox) view.findViewById(R.id.cbShop);

        if(selectedShopId != null && !selectedShopId.equals("")){
            if(data.getShopId().equals(selectedShopId)){
                cbItem.setChecked(true);
                checkedData.put(selectedShopId,selectedShopId);
            }
        }

        cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkedData.put(data.getShopId(),data.getShopId());
                }else{
                    checkedData.remove(data.getShopId());
                }
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
        return checkedData;
    }

    public int getCheckedCount(){
        return checkedData.size();
    }

    public String getSelectedId(){
        String shopId = null;
        Log.v("Notification","Shop model list size : "+shopModelList.size());
        for(int i = 0 ; i < shopModelList.size();i++){
            if(checkedData.containsKey(shopModelList.get(i).getShopId())) {
                if (!checkedData.get(shopModelList.get(i).getShopId()).equals(""))
                    shopId = checkedData.get(shopModelList.get(i).getShopId());
            }
        }
        return shopId;
    }
}
