package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class FavoriteStoreListActivity extends Activity {
    Intent i;

    ArrayList<Store> storeList;
    ArrayList<String> likeStores;
    ArrayList<Store> likeStoreList;

    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_store_list);
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();
        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        TextView textView=(TextView)findViewById(R.id.storeName);
        textView.setTypeface(typeface);
        storeList = (ArrayList<Store>) getIntent().getSerializableExtra("storeList");
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        likeStores = sharedPreferencesHelper.getStringArrayPref(this, "likeStores");
        likeStoreList = new ArrayList<Store>();
        for (int i = 0, ii = likeStores.size(); i < ii ; i++) {
            for (int j = 0, jj = storeList.size(); j < jj ; j++) {
                if (likeStores.get(i).equals(String.valueOf(storeList.get(j).getId()))) {
                    likeStoreList.add(storeList.get(j));
                }
            }
        }
        Collections.reverse(likeStoreList);
        //가게정보로 넘어감
        MyListAdapter lIstAdapter = new MyListAdapter (getApplicationContext(), R.layout.stores_item, likeStoreList, getWindowManager().getDefaultDisplay().getWidth());
        ListView lv = (ListView)findViewById(R.id.favoriteStoreList);
        lv.setAdapter(lIstAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(FavoriteStoreListActivity.this, StoreInfoActivity.class);
                i.putExtra("store",likeStoreList.get(position));
                startActivity(i);
            }
        });
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

}