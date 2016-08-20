package kr.co.edge.bosebose;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoreInfoActivity extends Activity {

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
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        likeStores = sharedPreferencesHelper.getStringArrayPref(this, "likeStores");
        store  = (Store) getIntent().getExtras().getSerializable("store");
        imageView = (ImageView)findViewById(R.id.storeImage);

        LinearLayout storeInfo = (LinearLayout)findViewById(R.id.storeInfo);
        ViewGroup.MarginLayoutParams storeInfoMarginParams = (ViewGroup.MarginLayoutParams) storeInfo.getLayoutParams();
        storeInfoMarginParams.setMargins(0,width, 0, 0);
        final FrameLayout frameLayoutId = (FrameLayout)findViewById(R.id.frameLayoutId);
        frameLayoutId.bringToFront();
        final ViewGroup.MarginLayoutParams frameLayoutIdParams = (ViewGroup.MarginLayoutParams) frameLayoutId.getLayoutParams();


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
        blackWrapper.setAlpha(0);
        scrollViewId.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollViewId.getScrollY(); // For ScrollView
                if (scrollY > 0 && scrollY <= 600) {
                    blackWrapper.setAlpha(scrollY / 3);
                }
                if (width - scrollY >= width / 4) {
                    frameLayoutIdParams.height = width - scrollY;
                    frameLayoutId.requestLayout();
                    marginParams.setMargins(width - 80,  width - scrollY - 80, 0, 0);
                    storeLike.requestLayout();
                }
            }
        });

        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClientClass());
       // webView.loadUrl("http://192.168.43.102/daumapi.php");


        TextView storeTitle = (TextView)findViewById(R.id.storeTitle);
        storeTitle.setText(String.valueOf(store.getName()));
        TextView tabName = (TextView)findViewById(R.id.tabName);
        TextView storesContent = (TextView)findViewById(R.id.storesContent);
        storesContent.setText(String.valueOf(store.getIntroduction()));
        TextView storeThingsNum = (TextView)findViewById(R.id.storeThingsNum);
        storeThingsNum.setText(String.valueOf(store.getItemCount()));
        TextView storeFavoriteNum = (TextView)findViewById(R.id.storeFavoriteNum);
        storeFavoriteNum.setText(String.valueOf(store.getFavoriteCount()));
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
        webView= (WebView)findViewById(R.id.webview);
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClientClass());
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