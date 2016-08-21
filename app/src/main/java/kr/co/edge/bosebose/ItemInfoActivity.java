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

public class ItemInfoActivity extends Activity{

    ArrayList<Item> itemList;
    Item item;
    Store store;
    ArrayList<String> imageList;
    CarouselView carouselView;
    Context context;
    ImageButton thingsLIke;
    ArrayList<String> likeItems;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean checkAddLike;
    Animation animScale;
    Animation animAlpha;
    Animation animAlpha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        context=this;

        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        animAlpha2 = AnimationUtils.loadAnimation(this, R.anim.anim_alpha2);

        // 즐겨찾기목록을 가져옴
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        likeItems = sharedPreferencesHelper.getStringArrayPref(this, "likeItems");

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        findViewById(R.id.btn_move_store).setOnClickListener(mClickListener);
        Display mDisplay = getWindowManager().getDefaultDisplay();

        store = (Store)getIntent().getExtras().getSerializable("store");
        item = (Item)getIntent().getExtras().getSerializable("item");
        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        imageList = getImageList(item);
        addHit(item.getId()); // 조회수 증가

        TextView thingsStoreName = (TextView)findViewById(R.id.thingsStoreName);
        TextView thingsTitle = (TextView)findViewById(R.id.thingsTitle);
        thingsTitle.setText(String.valueOf(item.getName()));
        TextView thingsLikeValue = (TextView)findViewById(R.id.thingsLikeValue);
        thingsLikeValue.setText(String.valueOf(item.getHit()));
        TextView thingsPrice = (TextView)findViewById(R.id.thingsPrice);
        thingsPrice.setText(String.valueOf(item.getPrice()));

        LinearLayout itemTagContent = (LinearLayout)findViewById(R.id.itemTagContent);
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

        TextView thingsContent = (TextView)findViewById(R.id.thingsContent);
        thingsContent.setText(String.valueOf(item.getContent()));
        thingsLIke = (ImageButton)findViewById(R.id.thingsLIke);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
       // Typeface godic = Typeface.createFromAsset(getAssets(),"NotoSans-Regular.ttf");
        //Typeface boldgodic = Typeface.createFromAsset(getAssets(),"NotoSans-Bold.ttf");
        thingsStoreName.setTypeface(typeface);
        //thingsTitle.setTypeface(boldgodic);
        //thingsPrice.setTypeface(godic);

        if (likeItems.contains(String.valueOf(item.getId()))) {
            checkAddLike = true;
            thingsLIke.setImageResource(R.drawable.like_clicked);
        } else {
            checkAddLike = false;
            thingsLIke.setImageResource(R.drawable.like);
        }
        thingsLIke.setOnClickListener(mClickListener);

        carouselView = (CarouselView) findViewById(R.id.thingsImage);
        carouselView.setPageCount(imageList.size());
        ViewGroup.LayoutParams params = carouselView.getLayoutParams();

        params.height = mDisplay.getWidth();
        carouselView.setLayoutParams(params);
        carouselView.setImageListener(imageListener);
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
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.with(context)
                    .load(imageList.get(position))
                    .resize(500,500)
                    .centerCrop()
                    .into(imageView);
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
                    intent.putExtra("store",store);
                    intent.putExtra("itemList",itemList);
                    startActivity(intent);
                    break;
                case R.id.thingsLIke:
                    AnimationSet sets = new AnimationSet(false);
                    sets.addAnimation(animScale);
                    //TODO 즐겨찾기 추가
                    if (!checkAddLike) {
                        checkAddLike = true;
                        likeItems.add(String.valueOf(item.getId()));
                        thingsLIke.setImageResource(R.drawable.like_clicked);
                        thingsLIke.startAnimation(sets);
                    } else {
                        checkAddLike = false;
                        likeItems.remove(String.valueOf(item.getId()));
                        thingsLIke.setImageResource(R.drawable.like);
                        thingsLIke.startAnimation(sets);
                    }
                    sharedPreferencesHelper.setStringArrayPref(context, "likeItems", likeItems);
                    break;
            }
        }
    };

    public void addHit(int id){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkService.SERVICE_URL)
                .build();

        final NetworkService service = retrofit.create(NetworkService.class);
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

    public static ArrayList<String> getImageList(Item item){

        ArrayList<String> imageList = new ArrayList<>();

        if(item.getImage1().compareTo("") != 0){
            imageList.add(item.getImage1());
        }
        if(item.getImage2().compareTo("") != 0){
            imageList.add(item.getImage2());
        }
        if(item.getImage3().compareTo("") != 0){
            imageList.add(item.getImage3());
        }
        if(item.getImage4().compareTo("") != 0){
            imageList.add(item.getImage4());
        }
        return imageList;
    }

    public void goToSearch(String searchItem) {
        Intent intent = new Intent(ItemInfoActivity.this, SearchActivity.class);
        intent.putExtra("searchItem", searchItem);
        startActivity(intent);
    }

}