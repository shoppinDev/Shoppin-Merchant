package com.shoppin.merchant.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.shoppin.merchant.model.DealDataModel;
import com.shoppin.merchant.model.ShopModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karm on 06-May-16.
 */
public class EditDealListAdapter extends ArrayAdapter<DealDataModel> {
    LayoutInflater inflater;
    HashMap<String,Boolean> checkBoxMap = new HashMap<String,Boolean>();
    ArrayList<DealDataModel> dealModelList = new ArrayList<>();
    Activity mContext;
    FragmentManager fm;

    public EditDealListAdapter(Activity context, int resource, ArrayList<DealDataModel> dealList, FragmentManager fm) {
        super(context, resource, dealList);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dealModelList = dealList;
        this.fm = fm;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_edit_deal, parent, false);
        }

        final DealDataModel data = getItem(position);

        TextView tvDealTitle = (TextView) view.findViewById(R.id.tvDealTitle);
        TextView tvDealPrice = (TextView) view.findViewById(R.id.tvDealPrice);

        Button btnEditDeal = (Button) view.findViewById(R.id.btnEditDeal);
        btnEditDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditDealFragment();
                Bundle args = new Bundle();
                args.putSerializable("deal_data",data);
                fragment.setArguments(args);
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("");
                ft.replace(R.id.fragment_container,fragment);
                ft.commit();
            }
        });

        Button btnShowQR = (Button) view.findViewById(R.id.btnShowQr);
        btnShowQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRCodeDialog(mContext,data.getQrImagePath());
            }
        });

        tvDealTitle.setText(data.getDealTitle());
        tvDealPrice.setText(data.getDiscountValue());

        return view;
    }

    @Override
    public DealDataModel getItem(int position) {
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

    public String getSelectedId(){
        String dealId = null;
        for(int i = 0 ; i < dealModelList.size();i++){
            if(checkBoxMap.get(dealModelList.get(i).getDealId()))
                dealId = dealModelList.get(i).getShopId();
        }

        return dealId;
    }

    public void openQRCodeDialog(Context context,String imagePath){
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Scan QR code to get the Offer")
                .customView(R.layout.dialog_redeem,true)
                .titleGravity(GravityEnum.CENTER)
                .negativeText("CLOSE")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .positiveColorRes(R.color.text_blue)
                .negativeColorRes(R.color.text_blue)
                .buttonsGravity(GravityEnum.CENTER)
                .build();

        View view = dialog.getCustomView();

        String qrCodeLink = imagePath;

        final ImageView imgQR = (ImageView) view.findViewById(R.id.ivQR);

        if(qrCodeLink.equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(R.drawable.qrcode)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imgQR.setImageBitmap(bitmap);
                            int width = bitmap.getWidth(), height = bitmap.getHeight();
                            int[] pixels = new int[width * height];
                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                            MultiFormatReader reader = new MultiFormatReader();
                            try {
                                Result result = reader.decode(bBitmap);
                                Log.v("Notification", "QR image Decode Result" + result);
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else {
            Picasso.with(context)
                    //.load(R.drawable.qrcode)
                    .load(qrCodeLink)
                    .placeholder(R.drawable.icon_logo)
                    .resize(500, 500)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imgQR.setImageBitmap(bitmap);
                            int width = bitmap.getWidth(), height = bitmap.getHeight();
                            int[] pixels = new int[width * height];
                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                            MultiFormatReader reader = new MultiFormatReader();
                            try {
                                Result result = reader.decode(bBitmap);
                                Log.v("Notification", "QR image Decode Result : " + result);
                            } catch (NotFoundException e) {
                                Log.e("NotFound Exception", "decode exception", e);

                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

        dialog.show();

    }
}
