package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.net.URI;
import java.util.ArrayList;

public class StoreInfoActivity extends Activity {

    CarouselView carouselView;
    ImageView imageView;
    Context context;

    Store store;
    ArrayList<String> imageList;

   // int sampleImages [] =  {R.drawable.pic1,R.drawable.pic2, R.drawable.pic3, R.drawable.pic4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        context=this;

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
        storeThingsNum.setText(String.valueOf(store.getHit()));
        TextView storeFavoriteNum = (TextView)findViewById(R.id.storeFavoriteNum);
        storeFavoriteNum.setText(String.valueOf(store.getHit()));
        TextView storeTime = (TextView)findViewById(R.id.storeTime);
        storeTime.setText(String.valueOf(store.getHit()));
        TextView storeBreakTime = (TextView)findViewById(R.id.storeBreakTime);
        storeBreakTime.setText(String.valueOf(store.getHit()));
        TextView storePhoneNum = (TextView)findViewById(R.id.storePhoneNum);
        storePhoneNum.setText(String.valueOf(store.getHit()));

/*
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);

        Display mDisplay = getWindowManager().getDefaultDisplay();
        //Intent intent = getIntent();
        store  = (Store) getIntent().getExtras().getSerializable("item");
        imageList = getImageList(store);

        carouselView = (CarouselView) findViewById(R.id.storeImage);
        carouselView.setPageCount(imageList.size());
        ViewGroup.LayoutParams params = carouselView.getLayoutParams();

        //carousel의 높이를 가로의 길이로 받아와서 1:1로 비율잡습니다.
        params.height = mDisplay.getWidth();
        carouselView.setLayoutParams(params);
        carouselView.setImageListener(imageListener);
        */
    }

    //이미지를 뿌려줍니다.
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
           // imageView.setImageResource(sampleImages[position]);
        }
    };

    //백버튼 제어
    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
            }
        }
    };


}