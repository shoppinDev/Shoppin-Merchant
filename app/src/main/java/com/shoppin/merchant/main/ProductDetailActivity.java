package com.shoppin.merchant.main;

import android.app.ProgressDialog;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.Result;
import com.shoppin.merchant.R;
import com.shoppin.merchant.model.DealDataModel;
import com.shoppin.merchant.util.JSONParser;
import com.shoppin.merchant.util.ModuleClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgMapView;
    MapView mapView;
    GoogleMap map;
    String dealId;
    TextView tvDealTitle, tvDealDesc, tvDealLongDesc, tvShopName, tvShopAddress, tvShopDistance, tvDealOrgValue, tvDealDiscValue;
    JSONObject resultObject;
    Button btnEditDeal;

    DealDataModel dealDataModel;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();

        if (getIntent() != null)
            dealDataModel = (DealDataModel) getIntent().getSerializableExtra("deal_data");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvDealDesc = (TextView) findViewById(R.id.tvDealDesc);
        tvDealDiscValue = (TextView) findViewById(R.id.tvDealDiscountValue);
        tvDealLongDesc = (TextView) findViewById(R.id.tvLongDesc);
        tvDealLongDesc.setVisibility(View.GONE);
        tvDealOrgValue = (TextView) findViewById(R.id.tvDealOriginalValue);
        tvDealDiscValue = (TextView) findViewById(R.id.tvDealDiscountValue);
        tvDealTitle = (TextView) findViewById(R.id.tvDealTitle);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvShopDistance = (TextView) findViewById(R.id.tvShopDistance);
        tvShopName = (TextView) findViewById(R.id.tvShopName);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        btnEditDeal = (Button) findViewById(R.id.btnEditDeal);
        btnEditDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDealFragment fragment = new EditDealFragment();
                Bundle args = new Bundle();
                args.putSerializable("deal_data",dealDataModel);
                args.putBoolean("from_detail",true);
                fragment.setHandler(handler);
                fragment.setArguments(args);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,fragment);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        Log.v("Notification", "Internet ON : " + ((ModuleClass.isInternetOn) ? "YES" : "NO"));

        //if (ModuleClass.isInternetOn) {
        new GetDealDetailTask(dealId).execute();
        //}
    }

    public String getMap(double lattitude, double longitude) {
        String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
                + lattitude
                + ","
                + longitude
                + "&sensor=false";
        return getMapURL;
    }

    public void updateDetail(final JSONObject object) {
        try {
            tvDealTitle.setText(object.getString("dealtitle"));
            tvShopName.setText(object.getString("shopname"));
            tvShopDistance.setText("1.5 km");
            tvShopAddress.setText(object.getString("shop_addres"));
            tvDealDesc.setText(object.getString("dealdescription"));
            //tvDealDiscValue.setText(object.getString("discountvalue"));
            //tvDealLongDesc.setText(object.getString(""));
            //tvDealOrgValue.setText(object.getString("dealamount"));

            final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            String orgPrice = getResources().getString(R.string.Rs) + " " + object.getString("dealamount");
            //String orgPrice = getResources().getString(R.string.Rs) + " " + object.getString("orignal_value");
            tvDealOrgValue.setText(orgPrice, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) tvDealOrgValue.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, orgPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //String discPrice = getResources().getString(R.string.Rs) + " " + object.getString("discountvalue");
            String discPrice = getResources().getString(R.string.Rs) + " " + object.getString("orignal_value");
            tvDealDiscValue.setText(discPrice);

            final double latitude = Double.parseDouble(object.getString("shop_latitude"));
            final double longitude = Double.parseDouble(object.getString("shop_longitude"));

            // Gets to GoogleMap from the MapView and does initialization stuff
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.getUiSettings().setMyLocationButtonEnabled(false);

                    // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                    MapsInitializer.initialize(ProductDetailActivity.this);

                    // Updates the location and zoom of the MapView
                    LatLng latLng = null;
                    try {
                        // latLng = new LatLng(Double.parseDouble(object.getString("shop_latitude")), Double.parseDouble(object.getString("shop_longitude")));
                        latLng = new LatLng(latitude, longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    map.animateCamera(cameraUpdate);

                    Marker perth = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true));
                }
            });

            mapView.onResume();
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("update_detail")){
                new GetDealDetailTask(dealId).execute();
            }
        }
    };


    class GetDealDetailTask extends AsyncTask<Void, Void, Void> {
        boolean success;
        String responseError;
        String dealId;
        ProgressDialog dialog;

        GetDealDetailTask(String dealId) {
            this.dealId = dealId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (success) {
                updateDetail(resultObject);
            } else {
                Toast.makeText(ProductDetailActivity.this, responseError, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> inputArray = new ArrayList<>();
            inputArray.add(new BasicNameValuePair("webmethod", "deal_detail"));
            inputArray.add(new BasicNameValuePair("id", dealDataModel.getDealId()));
            inputArray.add(new BasicNameValuePair("userid", ModuleClass.MERCHANT_ID));
            inputArray.add(new BasicNameValuePair("id", dealDataModel.getDealId()));
            inputArray.add(new BasicNameValuePair("id", dealDataModel.getDealId()));

            double latitude = 0.0000;
            double longitude = 0.0000;
            inputArray.add(new BasicNameValuePair("lat",""+latitude));
            inputArray.add(new BasicNameValuePair("long",""+longitude));

            JSONObject responseJSON = new JSONParser().makeHttpRequest(ModuleClass.LIVE_API_PATH + "index.php", "GET", inputArray);
            Log.d("Deal detail ", responseJSON.toString());

            if (responseJSON != null && !responseJSON.toString().equals("")) {
                success = true;
                try {
                    JSONArray dataArray = responseJSON.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            resultObject = dataArray.getJSONObject(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseError = "There is some problem in server connection";
                }

            } else {
                success = false;
                responseError = "There is some problem in server connection";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailActivity.this, R.style.MyThemeDialog);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
            dialog.show();
        }
    }

    public DealDataModel getDealListFromJson(JSONObject object) {
        DealDataModel dealData = null;
        try {

            dealData = new DealDataModel(
                    object.getString("deal_id"),
                    object.getString("merchant_id"),
                    object.getString("shop_id"),
                    object.getString("deal_category"),
                    object.getString("deal_subcategory"),
                    object.getString("deal_title"),
                    object.getString("deal_description"),
                    object.getString("deal_startdate"),
                    object.getString("deal_enddate"),
                    object.getString("deal_amount"),
                    object.getString("all_days"),
                    object.getString("discount_value"),
                    object.getString("discount_type"),
                    object.getString("location"),
                    object.getString("deal_usage"),
                    object.getString("is_active"),
                    object.getString("added_date"),
                    object.getString("category_name"),//category_name
                    object.getString("subcategory_name"),//subcategory_name
                    object.getString("merchant_name"),//merchant_name
                    object.getString("shop_name")
            );

            dealData.setCountRedeem(object.getString("count_redeem"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dealData;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(fm != null) {
            if (fm.getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                fm.popBackStack();
            }
        }
    }
}
