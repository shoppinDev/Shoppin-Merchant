package com.shoppin.merchant.myaccount;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.shoppin.merchant.R;
import com.shoppin.merchant.main.EditDealFragment;
import com.shoppin.merchant.model.DealDataModel;
import com.shoppin.merchant.model.DraftsDataModel;
import com.shoppin.merchant.model.PurchaseDataModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karm on 06-May-16.
 */
public class DraftsListAdapter extends ArrayAdapter<DraftsDataModel> {
    LayoutInflater inflater;
    HashMap<String,Boolean> checkBoxMap = new HashMap<String,Boolean>();
    ArrayList<DraftsDataModel> dealModelList = new ArrayList<>();
    Activity mContext;
    FragmentManager fm;
    Handler handler;

    public DraftsListAdapter(Activity context, int resource, ArrayList<DraftsDataModel> dealList, FragmentManager fm,Handler handler) {
        super(context, resource, dealList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dealModelList = dealList;
        this.fm = fm;
        mContext = context;
        this.handler = handler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_edit_deal, parent, false);
        }

        final DraftsDataModel data = getItem(position);

        TextView tvDealTitle = (TextView) view.findViewById(R.id.tvDealTitle);
        TextView tvDealPrice = (TextView) view.findViewById(R.id.tvDealPrice);

        Button btnEditDeal = (Button) view.findViewById(R.id.btnEditDeal);
        btnEditDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDraftDealFragment fragment = new EditDraftDealFragment();
                fragment.setHandler(handler);
                Bundle args = new Bundle();
                args.putSerializable("deal_data",data);
                fragment.setArguments(args);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,fragment);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        Button btnShowQR = (Button) view.findViewById(R.id.btnShowQr);
        btnShowQR.setVisibility(View.GONE);

        tvDealTitle.setText(data.getDealTitle());
        tvDealPrice.setText(data.getDiscountValue());

        return view;
    }

    @Override
    public DraftsDataModel getItem(int position) {
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
        Log.v("Notification","Shop list size : "+dealModelList.size());
        Log.v("Notification"," Checkbox map size : "+checkBoxMap.size());
        for(int i = 0 ; i < dealModelList.size(); i++ ){
            if(checkBoxMap.get(dealModelList.get(i).getShopId()))
                count++;
        }
        return count;
    }

    public String getSelectedId() {
        String dealId = null;
        for (int i = 0; i < dealModelList.size(); i++) {
            if (checkBoxMap.get(dealModelList.get(i).getDealId()))
                dealId = dealModelList.get(i).getShopId();
        }

        return dealId;
    }

}
