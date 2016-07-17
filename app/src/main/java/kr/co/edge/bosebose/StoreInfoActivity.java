package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class StoreInfoActivity extends Activity {
    CarouselView carouselView;

    Store store;
    ArrayList<String> imageList;

   // int sampleImages [] =  {R.drawable.pic1,R.drawable.pic2, R.drawable.pic3, R.drawable.pic4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        store  = (Store) getIntent().getExtras().getSerializable("store");

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