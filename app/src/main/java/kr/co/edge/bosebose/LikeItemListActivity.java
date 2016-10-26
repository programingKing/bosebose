package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LikeItemListActivity extends Activity {


    ArrayList<Item> likeItemList;
    GridViewAdapter thingsAdapter;

    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_item_list);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        TextView textView=(TextView)findViewById(R.id.storeName);
        textView.setTypeface(typeface);

        likeItemList = new ArrayList<>();

        thingsAdapter = new GridViewAdapter (getApplicationContext(), R.layout.things_item, likeItemList, getWindowManager().getDefaultDisplay().getWidth());

        //헤더가 될 뷰를 추가함 GridViewWithHeaderAndFooter는 라이브러리입니다
        GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.favoriteTingsList);
        gvThings.setAdapter(thingsAdapter);
        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item sItem = likeItemList.get(position);
                Intent i = new Intent(getApplicationContext(), ItemInfoActivity.class);
                i.putExtra("item", sItem);
                startActivity(i);
            }
        });

        getLikeItem();
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
    }

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

    public void getLikeItem(){

        Call<List<Item>> callback;
        final NetworkService service = ServiceGenerator.createService(NetworkService.class);
        callback = service.getLikeItems(LoadingActivity.uuid);
        callback.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                ArrayList<Item> itemList = new ArrayList<>();
                itemList.addAll(response.body());
                thingsAdapter.renewItem(itemList);
                thingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.i("lsw","call error:"+t.getMessage());
            }
        });
    }




}