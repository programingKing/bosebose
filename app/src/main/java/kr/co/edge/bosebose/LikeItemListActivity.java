package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class LikeItemListActivity extends Activity {

    ArrayList<Item> itemList;
    ArrayList<String> likeItems;
    ArrayList<Item> likeItemList;

    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_item_list);
        sharedPreferencesHelper = (SharedPreferencesHelper)getApplicationContext();

        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("itemList");
        likeItems = sharedPreferencesHelper.getStringArrayPref(this, "likeItems");
        likeItemList = new ArrayList<Item>();

        for (int i = 0, ii = likeItems.size(); i < ii ; i++) {
            for (int j = 0, jj = itemList.size(); j < jj ; j++) {
                if (likeItems.get(i).equals(String.valueOf(itemList.get(j).getId()))) {
                    likeItemList.add(itemList.get(j));
                }
            }
        }

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);

        //MyAdapter이 girdView Adapter입니다 ㅠㅠ
        MyAdapter thingsAdapter = new MyAdapter (getApplicationContext(), R.layout.things_item, likeItemList, getWindowManager().getDefaultDisplay().getWidth());
        //헤더가 될 뷰를 추가함 GridViewWithHeaderAndFooter는 라이브러리입니다
        GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.favoriteTingsList);
        gvThings.setAdapter(thingsAdapter);
        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ItemInfoActivity.class);
                //값으로 position을 넘겨주는데 이를 이용해서 그 물품의 정보를 가져오면 될거라고 생각해봤어요..
                i.putExtra("position", position);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

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
}