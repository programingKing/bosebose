package kr.co.edge.bosebose;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class StoreInfoActivity extends Activity {

    ArrayList<Item> itemList;
    ArrayList<Store> storeList;
    ArrayList<Item> storeItemList;
    ImageView imageView;
    Context context;
    Store store;
    ImageButton storeLike;
    ArrayList<String> likeStores;
    boolean checkAddLike;
    SharedPreferencesHelper sharedPreferencesHelper;
    WebView webView;
    Animation animScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        final ScrollView scrollViewId = (ScrollView)findViewById(R.id.scrollViewId);
        final Display mDisplay = getWindowManager().getDefaultDisplay();
        final int width = mDisplay.getWidth();
        context = this;
        final ArrayList<Item> storeItemList = new ArrayList<Item>();
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        likeStores = sharedPreferencesHelper.getStringArrayPref(this, "likeStores");
        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");
        store  = (Store) getIntent().getExtras().getSerializable("store");
        imageView = (ImageView)findViewById(R.id.storeImage);

        for (int i = 0, ii = itemList.size() ; i < ii ; i++) {
            if (itemList.get(i).getStoreName().equals(store.getName())) {
                storeItemList.add(itemList.get(i));
            }
        }

        LinearLayout storeInfo = (LinearLayout)findViewById(R.id.storeInfo);
        final LinearLayout storeInfoContext = (LinearLayout)findViewById(R.id.storeInfoContext);
        ViewGroup.MarginLayoutParams storeInfoMarginParams = (ViewGroup.MarginLayoutParams) storeInfo.getLayoutParams();
        storeInfoMarginParams.setMargins(0,width, 0, 0);
        final FrameLayout frameLayoutId = (FrameLayout)findViewById(R.id.frameLayoutId);
        final ViewGroup.MarginLayoutParams frameLayoutIdParams = (ViewGroup.MarginLayoutParams) frameLayoutId.getLayoutParams();
        frameLayoutId.bringToFront();
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        Picasso.with(context)
                .load(store.getImage())
                .resize(width, width)
                .centerCrop()
                .into(imageView);

        storeLike = (ImageButton)findViewById(R.id.storesLIke);
        final ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) storeLike.getLayoutParams();
        marginParams.setMargins(width - 80, width - 80, 0, 0);
        final Drawable blackWrapper = ((ImageView)findViewById(R.id.blackWrapper)).getBackground();
        blackWrapper.setAlpha(80);
        scrollViewId.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollViewId.getScrollY(); // For ScrollView
                if (scrollY > 0 && scrollY <= 600) {
                    blackWrapper.setAlpha(80 + scrollY / 5);
                }
                if (width - scrollY >= width / 4) {
                    frameLayoutIdParams.height = width - scrollY;
                    frameLayoutId.requestLayout();
                    marginParams.setMargins(width - 80,  width - scrollY - 80, 0, 0);
                    storeLike.requestLayout();
                    storeInfoContext.setAlpha((float)( 1 - scrollY / 400.0));
                }
            }
        });

        MyAdapter thingsAdapter = new MyAdapter (context, R.layout.things_item, storeItemList, context.getResources().getDisplayMetrics().widthPixels);
        GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.storeGridViewThings);
        gvThings.setAdapter(thingsAdapter);
        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Store sStore =null;
                Item sItem = storeItemList.get(position);
                if (store.id == sItem.storeID) {
                    sStore = store;
                }
                Intent i = new Intent(context, ItemInfoActivity.class);
                i.putExtra("item", sItem);
                i.putExtra("store",sStore);
                i.putExtra("itemList", itemList);
                i.putExtra("storeList", storeList);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        TextView storeTitle = (TextView)findViewById(R.id.storeTitle);
        storeTitle.setText(String.valueOf(store.getName()));
        TextView tabName = (TextView)findViewById(R.id.tabName);
        TextView storesContent = (TextView)findViewById(R.id.storesContent);
        storesContent.setText(String.valueOf(store.getIntroduction()));
        TextView storeTime = (TextView)findViewById(R.id.storeTime);
        storeTime.setText(String.valueOf(store.getBusinessHour()));
        TextView storeBreakTime = (TextView)findViewById(R.id.storeBreakTime);
        storeBreakTime.setText(String.valueOf(store.getHoliday()));
        TextView storePhoneNum = (TextView)findViewById(R.id.storePhoneNum);
        storePhoneNum.setText(String.valueOf(store.getHit()));

        if (likeStores.contains(String.valueOf(store.getId()))) {
            checkAddLike = true;
            storeLike.setImageResource(R.drawable.favorite_click);
        } else {
            checkAddLike = false;
            storeLike.setImageResource(R.drawable.favorite);
        }
        storeLike.setOnClickListener(mClickListener);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        tabName.setTypeface(typeface);
        storeTitle.setTypeface(typeface);

        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClientClass());
        // webView.loadUrl("http://192.168.43.102/daumapi.php");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollViewId.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        String url =
                NetworkService.SERVICE_URL+"GetStoreMap.php"
                        +"?lon="+store.getLocationX()+"&lat="+store.getLocationY()+"&storeName="+store.getName();
        webView.loadUrl(url);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.storesLIke:
                    AnimationSet sets = new AnimationSet(false);
                    sets.addAnimation(animScale);
                    //TODO 즐겨찾기 추가
                    if (!checkAddLike) {
                        checkAddLike = true;
                        likeStores.add(String.valueOf(store.getId()));
                        storeLike.setImageResource(R.drawable.favorite_click);
                        storeLike.startAnimation(sets);
                    } else {
                        checkAddLike = false;
                        likeStores.remove(String.valueOf(store.getId()));
                        storeLike.setImageResource(R.drawable.favorite);
                        storeLike.startAnimation(sets);
                    }
                    sharedPreferencesHelper.setStringArrayPref(context, "likeStores", likeStores);
                    break;
            }
        }
    };



}