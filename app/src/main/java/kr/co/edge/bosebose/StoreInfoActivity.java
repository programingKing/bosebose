package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        context=this;

        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        // 즐겨찾기목록을 가져옴
        likeStores = sharedPreferencesHelper.getStringArrayPref(this, "likeStores");

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        Display mDisplay = getWindowManager().getDefaultDisplay();

        store  = (Store) getIntent().getExtras().getSerializable("store");

        imageView = (ImageView)findViewById(R.id.storeImage);
        System.out.println(store.getImage());

        Picasso.with(context)
                .load(store.getImage())
                .resize(mDisplay.getWidth(),mDisplay.getWidth())
                .centerCrop()
                .into(imageView);

        TextView storeName = (TextView)findViewById(R.id.storeName);
        storeName.setText(String.valueOf(store.getName()));
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

        webView= (WebView)findViewById(R.id.webview);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClientClass());
        String url =
                NetworkService.SERVICE_URL+"GetStoreMap.php"
                        +"?lon="+store.getLocationX()+"&lat="+store.getLocationY()+"&storeName="+store.getName();
        webView.loadUrl(url);


        storeLike = (ImageButton)findViewById(R.id.storesLIke);
        if (likeStores.contains(String.valueOf(store.getId()))) {
            checkAddLike = true;
            storeLike.setImageResource(R.drawable.favorite_click);
        } else {
            checkAddLike = false;
            storeLike.setImageResource(R.drawable.favorite);
        }
        storeLike.setOnClickListener(mClickListener);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //백버튼 제어
    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.storesLIke:
                    //TODO 즐겨찾기 추가
                    if (!checkAddLike) {
                        checkAddLike = true;
                        likeStores.add(String.valueOf(store.getId()));
                        storeLike.setImageResource(R.drawable.favorite);
                    } else {
                        checkAddLike = false;
                        likeStores.remove(String.valueOf(store.getId()));
                        storeLike.setImageResource(R.drawable.favorite_click);
                    }
                    sharedPreferencesHelper.setStringArrayPref(context, "likeStores", likeStores);
                    break;
            }
        }
    };



}