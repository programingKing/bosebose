package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemInfoActivity extends Activity{

    Item item;
    Store store;
    ArrayList<String> imageList;
    CarouselView carouselView;
    Context context;
    ImageButton thingsLIke;
    ArrayList<String> likeItems;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean checkAddLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        context=this;

        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        // 즐겨찾기목록을 가져옴
        likeItems = sharedPreferencesHelper.getStringArrayPref(this, "likeItems");

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        findViewById(R.id.btn_move_store).setOnClickListener(mClickListener);
        Display mDisplay = getWindowManager().getDefaultDisplay();

        store = (Store)getIntent().getExtras().getSerializable("store");
        item = (Item)getIntent().getExtras().getSerializable("item");
        imageList = getImageList(item);
        addHit(item.getId()); // 조회수 증가

        TextView thingsStoreName = (TextView)findViewById(R.id.thingsStoreName);
        thingsStoreName.setText(String.valueOf(item.getStoreName()));
        TextView thingsTitle = (TextView)findViewById(R.id.thingsTitle);
        thingsTitle.setText(String.valueOf(item.getName()));
        TextView thingsContent = (TextView)findViewById(R.id.thingsContent);
        thingsContent.setText(String.valueOf(item.getContent()));
        TextView thingsPrice = (TextView)findViewById(R.id.thingsPrice);
        thingsPrice.setText(String.valueOf(item.getPrice()));
        TextView thingsTag = (TextView)findViewById(R.id.thingsTag);
        thingsTag.setText(String.valueOf(item.getTag()));
        thingsLIke = (ImageButton)findViewById(R.id.thingsLIke);
        //TODO 즐겨찾기에 추가유무에 따라서 토글 되야됨. 이아디가 추가되있으면

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
                    startActivity(intent);
                    break;
                case R.id.thingsLIke:
                    //TODO 즐겨찾기 추가
                    if (!checkAddLike) {
                        checkAddLike = true;
                        likeItems.add(String.valueOf(item.getId()));
                        thingsLIke.setImageResource(R.drawable.like_clicked);
                    } else {
                        checkAddLike = false;
                        likeItems.remove(String.valueOf(item.getId()));
                        thingsLIke.setImageResource(R.drawable.like);
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


}