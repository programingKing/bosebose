package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;


public class MainActivity extends Activity {
    Intent i;
    private ViewPager mPager;
    ArrayList<Item> itemList;
    ArrayList<Store> storeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");

        mPager = (ViewPager)findViewById(R.id.mian_grid_pager);
        mPager.setAdapter(new MyPagerAdapter(getApplicationContext(),itemList,storeList));
        mPager.setCurrentItem(0);

        findViewById(R.id.headerMainLogoBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerFavoriteStoreBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerFavoriteThingsBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerSearchBtn).setOnClickListener(mClickListener);
        findViewById(R.id.secHeaderThings).setOnClickListener(mClickListener);
        findViewById(R.id.secHeaderStores).setOnClickListener(mClickListener);
    }

    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.headerMainLogoBtn:
                    break;
                case R.id.headerFavoriteStoreBtn:
                    //TODO 암시적 Intent로 전환 가능성
                    i = new Intent(MainActivity.this, FavoriteStoreListActivity.class);
                    startActivity(i);
                    break;
                case R.id.headerFavoriteThingsBtn:
                    i = new Intent(MainActivity.this, LikeItemListActivity.class);
                    startActivity(i);
                    break;
                case R.id.headerSearchBtn:
                    i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);
                    break;
                case R.id.secHeaderThings:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.secHeaderStores:
                    mPager.setCurrentItem(1);
                    break;
            }
        }
    };

}