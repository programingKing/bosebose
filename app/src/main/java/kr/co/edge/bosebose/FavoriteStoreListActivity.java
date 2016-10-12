package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteStoreListActivity extends Activity {
    Intent i;

    ArrayList<Store> storeList;
    ArrayList<String> likeStores;
    ArrayList<Store> likeStoreList;
    MyListAdapter lIstAdapter;

    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_store_list);
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        TextView textView=(TextView)findViewById(R.id.storeName);
        textView.setTypeface(typeface);

        likeStoreList = new ArrayList<>();

        lIstAdapter = new MyListAdapter (getApplicationContext(), R.layout.stores_item, likeStoreList, getWindowManager().getDefaultDisplay().getWidth());
        ListView lv = (ListView)findViewById(R.id.favoriteStoreList);
        lv.setAdapter(lIstAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(FavoriteStoreListActivity.this, StoreInfoActivity.class);
                i.putExtra("storeID",likeStoreList.get(position).getId());
                startActivity(i);
            }
        });

        setFavoriteStores();
    }


    //뒤로가기 버튼 제어
    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
            }
        }
    };


    public void setFavoriteStores(){

        Call<List<Store>> callback;
        final NetworkService service = ServiceGenerator.createService(NetworkService.class);
        callback = service.getFavoriteStores(LoadingActivity.uuid);

        callback.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                ArrayList<Store> storeList = new ArrayList<>();
                storeList.addAll(response.body());
                lIstAdapter.renewItem(storeList);
                lIstAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {

            }
        });

    }

}