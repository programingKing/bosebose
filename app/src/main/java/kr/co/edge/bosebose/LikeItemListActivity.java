package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class LikeItemListActivity extends Activity {
    //이미지
    int img[] = {R.drawable.pic1,R.drawable.pic2, R.drawable.pic3, R.drawable.pic8, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_item_list);
/*
        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        //MyAdapter이 girdView Adapter입니다 ㅠㅠ
        MyAdapter thingsAdapter = new MyAdapter (getApplicationContext(), R.layout.things_item, img, getWindowManager().getDefaultDisplay().getWidth());
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
        */
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