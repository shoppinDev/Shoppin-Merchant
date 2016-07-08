package com.shoppin.merchant.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStates;
import com.shoppin.merchant.R;
import com.shoppin.merchant.login.LoginActivity;
import com.shoppin.merchant.myaccount.DraftsFragment;
import com.shoppin.merchant.myaccount.MyPurchaseFragment;
import com.shoppin.merchant.myaccount.StarredFragment;
import com.shoppin.merchant.util.ModuleClass;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    LinearLayout linLaySearch;
    EditText etLocation;
    ImageButton imgBtnClear;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tvHeader;
    FragmentManager fm;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    public static GoogleApiClient mGoogleApiClient;
    public static Location mCurrentLocation;
    String mLastUpdateTime;

    public static int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001;
    LocationSettingsRequest.Builder builder;

    private final int REQUEST_CHECK_SETTINGS = 101;

    public static String KEY_OLD_LAT = "key_old_lat";
    public static String KEY_OLD_LONG = "key_old_long";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences.Editor editor = ModuleClass.appPreferences.edit();
        editor.putString(KEY_OLD_LAT,"0.00000");
        editor.putString(KEY_OLD_LONG,"0.00000");

        if (!ModuleClass.isLocationEnabled(this)) {
            /*new MaterialDialog.Builder(this)
                    .title("Location Service")
                    .content("Please turn on your GPS to get a great deals around your location")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();*/

            Log.v("Notification", "GPS is disable");
        } else {
            Log.v("Notification", "GPS is Enable");
        }

        //Location Access Permission above Lollipop version
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                if (!isGooglePlayServicesAvailable()) {
                    finish();
                }
                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
            }
        } else {
            if (!isGooglePlayServicesAvailable()) {
                finish();
            }
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        linLaySearch = (LinearLayout) findViewById(R.id.linLaySearch);

        etLocation = (EditText) findViewById(R.id.etLocation);

        imgBtnClear = (ImageButton) findViewById(R.id.btnClear);
        imgBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLocation.setText("");
            }
        });

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvHeader = (TextView) headerLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(ModuleClass.MERCHANT_NAME);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setCheckedItem(R.id.nav_home);

        if(toolbar != null)
            toolbar.setTitle("Home");

        fm = getSupportFragmentManager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Notification", "Location Access permission granted");
            if (!isGooglePlayServicesAvailable()) {
                finish();
            }
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        } else if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Log.v("Notification", "Location Access permission Denied");
            new MaterialDialog.Builder(MainActivity.this)
                    .title("Permission")
                    .content("Shoppin can not go ahead without you allowing this permission please allow us.")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
                            }
                        }
                    })
                    .show();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            //GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.v("Notification","Location Permission not granted");
            return;
        }

        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, MainActivity.this);

        /*PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates settingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, MainActivity.this);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });*/

        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
                Log.d(TAG, "Location update resumed .....................");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.v("Notification","Location setting changed");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.v("Notification","Location setting Cancel");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        } catch (Exception e) {
            Log.v("Error", "Error stopping location : " + e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(fm != null) {
            if (fm.getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                fm.popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setBackgroundColor(getResources().getColor(R.color.white));

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Notification","Search click");
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.v("Notification","Search close");
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.v("Notification","On expand");
                linLaySearch.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.v("Notification","On Collapse");
                linLaySearch.setVisibility(View.GONE);
                return true;
            }
        });

        setSearchTextColour(mSearchView);
        setCloseSearchIcon(mSearchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search)
        {
            Log.v("Notification","Search Selected");
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();

            if(b.getBoolean("shop_added")){
                if(navigationView != null) {
                    onNavigationItemSelected(navigationView.getMenu().getItem(5));
                    navigationView.setCheckedItem(R.id.nav_my_shops);
                    if (toolbar != null)
                        toolbar.setTitle("My Shops");
                }
            }else if(b.getBoolean("deal_added")){
                if(navigationView != null) {
                    onNavigationItemSelected(navigationView.getMenu().getItem(4));
                    navigationView.setCheckedItem(R.id.nav_my_deals);
                    if (toolbar != null)
                        toolbar.setTitle("My Deals");
                }
            }
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(toolbar != null){
                toolbar.setTitle("Home");
            }
            // Handle the camera action
            Fragment fragment = new OfferFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,fragment);
            ft.commit();

        } else if (id == R.id.nav_starred) {
            if(toolbar != null){
                toolbar.setTitle("Starred Deals");
            }

            Fragment fragment = new StarredFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();

        } else if (id == R.id.nav_add_shop) {
            if(toolbar != null){
                toolbar.setTitle("Add Shop");
            }

            ShopInfoFragment fragment = new ShopInfoFragment();
            fragment.setMessageHandler(handler);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();

        }else if (id == R.id.nav_add_deal) {

            if(toolbar != null){
                toolbar.setTitle("Add Deal");
            }

            AddDealFragment fragment = new AddDealFragment();
            fragment.setMessageHandler(handler);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();

        }else if (id == R.id.nav_my_deals) {

            if(toolbar != null){
                toolbar.setTitle("My Deals");
            }

            Fragment fragment = new MyDealFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();

        } else if (id == R.id.nav_my_shops) {

            if(toolbar != null){
                toolbar.setTitle("My Shops");
            }

            Fragment fragment = new MyShopFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();

        }else if (id == R.id.nav_draft) {
            if(toolbar != null){
                toolbar.setTitle("Draft deals");
            }

            Fragment fragment = new DraftsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();
        }else if (id == R.id.nav_all_purchases) {
            if(toolbar != null){
                toolbar.setTitle("All Purchases");
            }

            Fragment fragment = new MyPurchaseFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();
        } else if (id == R.id.nav_help) {
            if(toolbar != null){
                toolbar.setTitle("Help");
            }

            Fragment fragment = new HelpFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab",1);
            fragment.setArguments(args);
            ft.replace(R.id.container,fragment);
            ft.commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = ModuleClass.appPreferences.edit();
            editor.remove(ModuleClass.KEY_IS_REMEMBER);
            editor.remove(ModuleClass.KEY_MERCHANT_ID);
            editor.remove(ModuleClass.KEY_MERCHANT_NAME);
            ModuleClass.MERCHANT_NAME = null;
            ModuleClass.MERCHANT_ID = null;
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
            /*if(Build.VERSION.SDK_INT > 15)
                this.finishAffinity();
            else{
                System.exit(0);
            }*/
        } else if (id == R.id.nav_my_account) {
            if(toolbar != null){
                toolbar.setTitle("My Profile");
            }
            Fragment fragment = new MyAccountFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab", 1);
            fragment.setArguments(args);
            ft.replace(R.id.container, fragment);
            ft.commit();
        }else if (id == R.id.nav_notifications) {
            if(toolbar != null){
                toolbar.setTitle("Notification");
            }
            Fragment fragment = new NotificationFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab", 1);
            fragment.setArguments(args);
            ft.replace(R.id.container, fragment);
            ft.commit();
        }else if (id == R.id.nav_rewards) {
            if(toolbar != null){
                toolbar.setTitle("Reward");
            }
            Fragment fragment = new RewardFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putInt("tab", 1);
            fragment.setArguments(args);
            ft.replace(R.id.container, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setSearchTextColour(SearchView searchView) {
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.setTextColor(getResources().getColor(R.color.textColor));
        searchPlate.setHintTextColor(getResources().getColor(R.color.textColor));
        searchPlate.setHint("Search by Product,Shop etc.");
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void setCloseSearchIcon(SearchView searchView) {

        try {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(searchView);
            closeBtn.setImageResource(R.drawable.icon_cancel);

            ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            searchButton.setImageResource(R.drawable.icon_search);
        } catch (NoSuchFieldException e) {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("SearchView", e.getMessage(), e);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("Notification", "On Location changed : latitude : " + location.getLatitude() + " Longitude :" + location.getLongitude());
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }
}
