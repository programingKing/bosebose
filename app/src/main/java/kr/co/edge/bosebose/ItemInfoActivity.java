package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class ItemInfoActivity extends Activity{

    Item item;
    ArrayList<String> imageList;
    CarouselView carouselView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        context=this;

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        Display mDisplay = getWindowManager().getDefaultDisplay();

        item = (Item)getIntent().getExtras().getSerializable("item");
        imageList = getImageList(item);

        TextView tv = (TextView)findViewById(R.id.thingsInfo);
        tv.setText(String.valueOf(item.getContent()));

        carouselView = (CarouselView) findViewById(R.id.thingsImage);
        carouselView.setPageCount(imageList.size());
        ViewGroup.LayoutParams params = carouselView.getLayoutParams();

        params.height = mDisplay.getWidth();
        carouselView.setLayoutParams(params);
        carouselView.setImageListener(imageListener);

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
            }
        }
    };

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
}