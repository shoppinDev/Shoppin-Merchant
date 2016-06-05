package com.shoppin.merchant.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.shoppin.merchant.R;
import com.shoppin.merchant.login.LoginActivity;
import com.shoppin.merchant.myaccount.DraftsFragment;
import com.shoppin.merchant.myaccount.MyPurchaseFragment;
import com.shoppin.merchant.myaccount.StarredFragment;
import com.shoppin.merchant.util.ModuleClass;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    LinearLayout linLaySearch;
    EditText etLocation;
    ImageButton imgBtnClear;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("Notification","On Activity result called");
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
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
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
}
