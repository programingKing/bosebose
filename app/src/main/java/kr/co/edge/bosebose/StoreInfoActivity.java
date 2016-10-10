package kr.co.edge.bosebose;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInfoActivity extends Activity {

    Context context;
    Store store;
    ArrayList<Item> storeItemList;

    NetworkService service;
    WebView webView;
    Animation animScale;

    ImageView imageView;
    TextView storeTitle;
    TextView tabName;
    TextView storesContent;
    TextView storeTime;
    TextView storeBreakTime;
    TextView storePhoneNum;
    ImageButton storeLike;

    MyAdapter thingsAdapter;
    GridViewWithHeaderAndFooter gvThings;

    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        final ScrollView scrollViewId = (ScrollView)findViewById(R.id.scrollViewId);
        final Display mDisplay = getWindowManager().getDefaultDisplay();
        width = mDisplay.getWidth();
        context = this;

        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        int storeID = getIntent().getIntExtra("storeID",0);

        imageView = (ImageView)findViewById(R.id.storeImage);
        storeTitle = (TextView)findViewById(R.id.storeTitle);
        tabName = (TextView)findViewById(R.id.tabName);
        storesContent = (TextView)findViewById(R.id.storesContent);
        storeTime = (TextView)findViewById(R.id.storeTime);
        storeBreakTime = (TextView)findViewById(R.id.storeBreakTime);
        storePhoneNum = (TextView)findViewById(R.id.storePhoneNum);
        storeLike = (ImageButton)findViewById(R.id.storesLIke);

        storeItemList = new ArrayList<>();
        service = ServiceGenerator.createService(NetworkService.class);

        LinearLayout storeInfo = (LinearLayout)findViewById(R.id.storeInfo);
        final LinearLayout storeInfoContext = (LinearLayout)findViewById(R.id.storeInfoContext);
        ViewGroup.MarginLayoutParams storeInfoMarginParams = (ViewGroup.MarginLayoutParams) storeInfo.getLayoutParams();
        storeInfoMarginParams.setMargins(0,width, 0, 0);
        final FrameLayout frameLayoutId = (FrameLayout)findViewById(R.id.frameLayoutId);
        final ViewGroup.MarginLayoutParams frameLayoutIdParams = (ViewGroup.MarginLayoutParams) frameLayoutId.getLayoutParams();
        frameLayoutId.bringToFront();
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);


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

        thingsAdapter = new MyAdapter (context, R.layout.things_item, storeItemList, context.getResources().getDisplayMetrics().widthPixels);
        gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.storeGridViewThings);


        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item sItem = storeItemList.get(position);
                Intent i = new Intent(context, ItemInfoActivity.class);
                i.putExtra("item", sItem);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


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

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollViewId.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Log.i("lsw","ssssss"+storeID);
        getStore(storeID);
        getItems(storeID);
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
                    if (store.likeCheck == 0) {
                        storeLike.startAnimation(sets);
                        addLike(store.getId(),LoadingActivity.uuid);
                    } else {
                        storeLike.startAnimation(sets);
                        subLike(store.getId(),LoadingActivity.uuid);
                    }

                    break;
            }
        }
    };

    private void getStore(int id ){

        Call<Store> callback = service.getStore(Integer.toString(id),LoadingActivity.uuid);

        callback.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if(response.isSuccessful()) {

                    store = response.body();

                    storeTitle.setText(String.valueOf(store.getName()));
                    storesContent.setText(String.valueOf(store.getIntroduction()));
                    storeTime.setText(String.valueOf(store.getBusinessHour()));
                    storeBreakTime.setText(String.valueOf(store.getHoliday()));
                    storePhoneNum.setText(String.valueOf(store.getPhoneNumber()));
                    Picasso.with(context)
                            .load(store.getImage())
                            .resize(width, width)
                            .centerCrop()
                            .into(imageView);

                    Log.i("lsw","like+" + store.likeCheck);

                    if (store.likeCheck == 1 ) {
                        storeLike.setImageResource(R.drawable.favorite_click);
                    } else {
                        storeLike.setImageResource(R.drawable.favorite);
                    }

                    String url =
                            "http://52.78.112.226:8888/App/Store/GetMap"
                                    +"?longitude="+store.getLongitude()+"&latitude="+store.getLatitude()+"&name="+store.getName();
                    webView.loadUrl(url);
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });

    }

    private void getItems(int id){

        Call<List<Item>> callback = service.getStoreItems(Integer.toString(id));
        callback.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.isSuccessful()){

                    storeItemList.addAll(response.body());
                    gvThings.setAdapter(thingsAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.i("lsw","가게 아이템 얻어오기 실패");
            }
        });


    }



    public void addLike(int id, String uuid){

        Call<ResponseBody> callback = service.addStoreLike(Integer.toString(id), uuid);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    storeLike.setImageResource(R.drawable.favorite_click);
                    store.likeCheck = 1;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }

    public void subLike(int id, String uuid){

        Call<ResponseBody> callback = service.subStoreLike(Integer.toString(id), uuid);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    storeLike.setImageResource(R.drawable.favorite);
                    store.likeCheck = 0;
                }else{
                    Log.i("lsw","!!!!!!!");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }

}