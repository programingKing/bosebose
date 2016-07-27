package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Intent i;
    private ImageButton seeHeaderThings;
    private ImageButton seeHeaderStores;
    private ImageButton tab_store;
    private ImageButton tab_product;
    private ViewPager mPager;
    ArrayList<Item> itemList;
    ArrayList<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab_product=(ImageButton)findViewById(R.id.tab_product);
        tab_product.setImageResource(R.drawable.tap_product);
        tab_store=(ImageButton) findViewById(R.id.tab_store);

        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");

        mPager = (ViewPager)findViewById(R.id.mian_grid_pager);
        mPager.setAdapter(new MyPagerAdapter(getApplicationContext(),itemList,storeList));
        mPager.setCurrentItem(0);

        findViewById(R.id.headerMainLogoBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerFavoriteStoreBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerFavoriteThingsBtn).setOnClickListener(mClickListener);
        findViewById(R.id.headerSearchBtn).setOnClickListener(mClickListener);
        seeHeaderThings = (ImageButton) findViewById(R.id.secHeaderThings);
        seeHeaderThings.setOnClickListener(mClickListener);
        seeHeaderStores=(ImageButton)findViewById(R.id.secHeaderStores);
        seeHeaderStores.setOnClickListener(mClickListener);


        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        TextView textView=(TextView)findViewById(R.id.headerMainLogoBtn);
        textView.setTypeface(typeface);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        seeHeaderThings.setImageResource(R.drawable.product_clicked);
                        seeHeaderStores.setImageResource(R.drawable.store);
                        tab_product.setImageResource(R.drawable.tap_product);
                        tab_store.setImageResource(R.drawable.tap);
                        break;
                    case 1:
                        seeHeaderThings.setImageResource(R.drawable.product);
                        seeHeaderStores.setImageResource(R.drawable.store_clicked);
                        tab_product.setImageResource(R.drawable.tap);
                        tab_store.setImageResource(R.drawable.tap_store);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.headerMainLogoBtn:
                    break;
                case R.id.headerFavoriteStoreBtn:
                    //TODO 암시적 Intent로 전환 가능성
                    i = new Intent(MainActivity.this, FavoriteStoreListActivity.class);
                    i.putExtra("storeList",storeList);
                    startActivity(i);
                    break;
                case R.id.headerFavoriteThingsBtn:
                    i = new Intent(MainActivity.this, LikeItemListActivity.class);
                    i.putExtra("itemList", itemList);
                    i.putExtra("storeList",storeList);
                    startActivity(i);
                    break;
                case R.id.headerSearchBtn:
                    i = new Intent(MainActivity.this, SearchActivity.class);
                    i.putExtra("storeList",storeList);
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