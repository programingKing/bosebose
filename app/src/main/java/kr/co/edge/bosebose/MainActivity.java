package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Intent i;
    private ImageButton seeHeaderThings;
    private ImageButton seeHeaderStores;
    private ViewPager mPager;
    ArrayList<Item> itemList;
    ArrayList<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        setContentView(R.layout.activity_main);
        final LinearLayout indicator = (LinearLayout)findViewById(R.id.indicator);
        indicator.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;

        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");

        mPager = (ViewPager)findViewById(R.id.mian_grid_pager);
        mPager.setAdapter(new MyPagerAdapter(getApplicationContext(),itemList,storeList));
        mPager.setCurrentItem(0);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 1 && positionOffset != 0) {
                    int setLeftMargin = (int) (getResources().getDisplayMetrics().widthPixels * positionOffset / 2);
                    setMargins(indicator, setLeftMargin, 0, 0, 0);
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
                        break;
                    case 1:
                        seeHeaderThings.setImageResource(R.drawable.product);
                        seeHeaderStores.setImageResource(R.drawable.store_clicked);
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
                    i.putExtra("itemList", itemList);
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
                    i.putExtra("itemList", itemList);
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
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
}