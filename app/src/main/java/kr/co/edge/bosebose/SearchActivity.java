package kr.co.edge.bosebose;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends Activity {
    EditText searchKeyword;
    ArrayList<Item> searchItemList;
    ArrayList<Store> storeList;
    ArrayList<Item> itemList;
    MyAdapter myGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        setContentView(R.layout.activity_search);

        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");
        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        String searchItem = (String)getIntent().getSerializableExtra("searchItem");


        searchItemList = new ArrayList<>();

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        searchKeyword = (EditText)findViewById(R.id.searchKeyword);
        ImageButton headerSearchBtn = (ImageButton)findViewById(R.id.HeaderSearchBtn);
        headerSearchBtn.setOnClickListener(mClickListener);
        if (searchItem != null) {
            searchKeyword.setText(searchItem);
            getSearchKeyword();
        }
        searchKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getSearchKeyword();
                }
                return false;
            }
        });

        myGridViewAdapter = new MyAdapter (getApplicationContext(), R.layout.things_item, searchItemList, getWindowManager().getDefaultDisplay().getWidth());
        //헤더가 될 뷰를 추가함 GridViewWithHeaderAndFooter는 라이브러리입니다
        GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.searchResultList);
        gvThings.setAdapter(myGridViewAdapter);
        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Store sStore =null;
                Item sItem = searchItemList.get(position);
                for(Store store : storeList ) {
                    if (store.id == sItem.storeID) {
                        sStore = store;
                        break;
                    }
                }
                Intent i = new Intent(getApplicationContext(), ItemInfoActivity.class);
                i.putExtra("item",sItem);
                i.putExtra("store",sStore);
                i.putExtra("storeList",storeList);
                i.putExtra("itemList",itemList);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }
    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.HeaderSearchBtn:
                    getSearchKeyword();
                    break;
            }
        }
    };

    void getSearchKeyword () {
        String searchWord =  searchKeyword.getText().toString();

        if(searchWord.toString().length()<2){
            Toast.makeText(getApplicationContext(),"검색어는 2자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
        }else{
            searchItem(searchWord);
        }
    }

    public void searchItem(String searchWord){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkService.SERVICE_URL)
                .build();

        Call<List<Item>> callback;
        final NetworkService service = retrofit.create(NetworkService.class);
        callback = service.searchItem(searchWord);
        callback.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                if(response.body().size()==0) {  Toast.makeText(getApplicationContext(),"검색 결과 없습니다.", Toast.LENGTH_SHORT).show(); }
                else {
                    ArrayList<Item> itemList = new ArrayList<>();
                    itemList.addAll(response.body());
                    myGridViewAdapter.renewItem(itemList);
                    myGridViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.i("lsw","call error:"+t.getMessage());
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
}