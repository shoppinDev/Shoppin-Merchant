package com.shoppin.merchant.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shoppin.merchant.R;

public class AddShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ShopInfoFragment();
        ft.replace(R.id.fragment_container,fragment);
        ft.commit();
    }
}
