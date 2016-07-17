package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class FavoriteStoreListActivity extends Activity {
    Intent i;

    /*
    //이미지 리스트
    int img[] = {R.drawable.pic1,R.drawable.pic2, R.drawable.pic3, R.drawable.pic8, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_store_list);

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);

        //가게정보로 넘어감
        MyListAdapter lIstAdapter = new MyListAdapter (getApplicationContext(), R.layout.stores_item, img, getWindowManager().getDefaultDisplay().getWidth());
        ListView lv = (ListView)findViewById(R.id.favoriteStoreList);
        lv.setAdapter(lIstAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = new Intent(FavoriteStoreListActivity.this, StoreInfoActivity.class);
                i.putExtra("position", position);
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
    */
}