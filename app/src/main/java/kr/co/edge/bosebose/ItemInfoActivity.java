package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class ItemInfoActivity extends Activity{

    ArrayList<Store> storeList;
    ArrayList<Item> itemList;
    Item item;
    Store store;
    ArrayList<String> imageList;
    CarouselView carouselView;
    Context context;
    ImageButton thingsLIke;

    Animation animScale;
    Animation animAlpha;
    Animation animAlpha2;
    NetworkService service;

    Display mDisplay;

    TextView thingsTitle;
    TextView thingsLikeValue;
    TextView thingsPrice;
    TextView thingsContent;
    LinearLayout itemTagContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        animAlpha2 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha2);

        context=this;

        thingsTitle = (TextView)findViewById(R.id.thingsTitle);
        thingsLikeValue = (TextView)findViewById(R.id.thingsLikeValue);
        thingsPrice = (TextView)findViewById(R.id.thingsPrice);
        thingsContent = (TextView)findViewById(R.id.thingsContent);
        carouselView = (CarouselView) findViewById(R.id.thingsImage);
        thingsLIke = (ImageButton)findViewById(R.id.thingsLIke);
        itemTagContent = (LinearLayout)findViewById(R.id.itemTagContent);

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        findViewById(R.id.btn_move_store).setOnClickListener(mClickListener);
        mDisplay = getWindowManager().getDefaultDisplay();

        item = (Item)getIntent().getExtras().getSerializable("item");
        service = ServiceGenerator.createService(NetworkService.class);

        getItem(item.getId());

    }

    @Override
    protected  void onStart() {
        super.onStart();

        final AnimationSet sets2 = new AnimationSet(false);
        sets2.addAnimation(animAlpha);
        final AnimationSet sets3 = new AnimationSet(false);
        sets3.addAnimation(animAlpha2);
        LinearLayout itemTitleWrapper = (LinearLayout)findViewById(R.id.itemTitleWrapper);
        final LinearLayout itemPriceWrapper = (LinearLayout)findViewById(R.id.itemPriceWrapper);
        final LinearLayout itemTagWrapper = (LinearLayout)findViewById(R.id.itemTagWrapper);
        final LinearLayout itemContentWrapper = (LinearLayout)findViewById(R.id.itemContentWrapper);
        itemTitleWrapper.startAnimation(sets2);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        itemPriceWrapper.startAnimation(sets3);
                        itemTagWrapper.startAnimation(sets3);
                        itemContentWrapper.startAnimation(sets3);
                        itemPriceWrapper.setAlpha(1);
                        itemTagWrapper.setAlpha(1);
                        itemContentWrapper.setAlpha(1);
                    }
                }, 500);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int _position, ImageView imageView) {
            final int position = _position;

            Picasso.with(context)
                    .load(imageList.get(position))
                    .resize(500,500)
                    .centerCrop()
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ItemInfoActivity.this, ItemInfoImage.class);
                    i.putExtra("imageUri",imageList.get(position));
                    startActivity(i);
                }
            });
        }
    };

    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.btn_move_store:
                    Intent intent = new Intent(ItemInfoActivity.this, StoreInfoActivity.class);
                    intent.putExtra("storeID",item.getStoreID());
                    startActivity(intent);
                    break;
                case R.id.thingsLIke:
                    AnimationSet sets = new AnimationSet(false);
                    sets.addAnimation(animScale);
                    //TODO 즐겨찾기 추가
                    if (item.likeCheck==0) {
                        thingsLIke.startAnimation(sets);
                        addLike(item.getId(),LoadingActivity.uuid);
                    } else {
                        thingsLIke.startAnimation(sets);
                        subLike(item.getId(),LoadingActivity.uuid);
                    }
                 //   sharedPreferencesHelper.setStringArrayPref(context, "likeItems", likeItems);
                    break;
            }
        }
    };

    public void getItem(int id){

        Call<Item> callback = service.getClothe(Integer.toString(id),LoadingActivity.uuid);
        callback.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.isSuccessful()){

                    item = response.body();

                    ViewGroup.LayoutParams params = carouselView.getLayoutParams();
                    params.height = mDisplay.getWidth();
                    carouselView.setLayoutParams(params);
                    imageList = getImageList(item);
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(imageList.size());;

                    thingsTitle.setText(String.valueOf(item.getName()));
                    thingsLikeValue.setText(String.valueOf(item.getLikeCount()));
                    thingsPrice.setText(String.valueOf(item.getPrice()));

                    final String[] itemTag = item.getTag().split("#");
                    for(int i = 1, ii = itemTag.length ; i < ii ; i++) {
                        final String value = itemTag[i];
                        TextView itemTagStr = new TextView(context);
                        itemTagStr.setText("#" + value);
                        itemTagStr.setTextColor(Color.parseColor("#FF101093"));
                        itemTagStr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goToSearch(value);
                            }
                        });
                        itemTagContent.addView(itemTagStr);
                    }

                    thingsContent.setText(String.valueOf(item.getContent()));

                    Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");

                    if (item.likeCheck==1 ) {
                        thingsLIke.setImageResource(R.drawable.like_clicked);
                    } else {
                        thingsLIke.setImageResource(R.drawable.like);
                    }
                    thingsLIke.setOnClickListener(mClickListener);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }

    public void addHit(int id){

        Call<ResponseBody> callback = service.addHit(Integer.toString(id));
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("lsw","response success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }

    public void addLike(int id, String uuid){

        Call<ResponseBody> callback = service.addLike(Integer.toString(id), uuid);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    thingsLIke.setImageResource(R.drawable.like_clicked);
                    item.likeCheck = 1;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }

    public void subLike(int id, String uuid){

        Call<ResponseBody> callback = service.subLike(Integer.toString(id), uuid);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    thingsLIke.setImageResource(R.drawable.like);
                    item.likeCheck = 0;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("lsw","error:"+t.getMessage());
            }
        });
    }


     public static ArrayList<String> getImageList(Item item){

        ArrayList<String> imageList = new ArrayList<>();

        if(item.getImage1() != null){
            imageList.add(item.getImage1());
        }
        if(item.getImage2() != null){
            imageList.add(item.getImage2());
        }
        if(item.getImage3() != null){
            imageList.add(item.getImage3());
        }
        if(item.getImage4() != null){
            imageList.add(item.getImage4());
        }
        return imageList;
    }

    public void goToSearch(String searchItem) {
        Intent intent = new Intent(ItemInfoActivity.this, SearchActivity.class);
        intent.putExtra("searchItem", searchItem);
        intent.putExtra("storeList",storeList);
        intent.putExtra("itemList",itemList);
        startActivity(intent);
    }

}