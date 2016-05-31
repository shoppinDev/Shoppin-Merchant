package com.shoppin.merchant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * Created by IBU on 6/27/2015.
 */
public class ModuleClass {

    public static String SERVER_PATH = "http://www.ibizsolutions.net/dealwala/api/index.php?webmethod=";
    //public static String IMAGE_SERVER_PATH = "http://www.yourbikebuddy.com/api/bikeyourride/upload/";
    public static String LIVE_API_PATH = "http://www.ibizsolutions.net/dealwala/api/";

    public static String MERCHANT_ID = "7";
    public static String MERCHANT_NAME = "";

    public static SharedPreferences appPreferences;

    public static String APP_PREFERENCE = "app_preference";

    public static String KEY_IS_REMEMBER = "key_is_remember";
    public static String KEY_MERCHANT_ID = "key_merchant_id";
    public static String KEY_MERCHANT_NAME = "key_merchant_name";

    public static double locationLatitude = 0.0;
    public static double locationLongitude = 0.0;

    public static Typeface typefaceRThin;
    public static Typeface typefaceRLight;
    public static Typeface typefaceRRegular;
    public static Typeface typefaceRCLight;
    public static Typeface typefaceRCRegular;

    public static Typeface typefaceBariol;
    public static Typeface typefaceLatoLight;
    public static Typeface typefaceLatoThin;
    public static Typeface typefaceOpenSansLight;
    public static Typeface typefaceOstrichSansMedium;
    public static Typeface typefaceOstrichSansMediumRounded;
    public static Typeface typefaceOswald;

    public static int notification_count = 0;

    public static GPSTracker gpsTracker;

    public static boolean isInternetOn;

    public static boolean setLocationParameters(Context context) {

        /*LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //criteria object will select best service based on
        //Accuracy, power consumption, response, bearing and monetary cost
        //set false to use best service otherwise it will select the default Sim network
        //and give the location based on sim network
        //now it will first check satellite than Internet than Sim network location
        String provider = locationManager.getBestProvider(criteria, false);
        //now you have best provider
        //get location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //get latitude and longitude of the location
            Log.v("Notification", "Location is not null...");
            locationLongitude = location.getLongitude();
            locationLatitude = location.getLatitude();
        } else {
            Log.v("Notification", "Location is null...");
            locationLatitude = 0.0;
            locationLongitude = 0.0;
        }*/

        gpsTracker = new GPSTracker(context);
        //locationLatitude = gpsTracker.getLatitude();
        //locationLongitude = gpsTracker.getLongitude();
        //gpsTracker.stopUsingGPS();

        return gpsTracker.canGetLocation();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        /*Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);*/
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems;itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + 40;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }
}
