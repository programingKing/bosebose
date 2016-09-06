package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jung In Huyk on 2016-09-07.
 */
public class StoreItemInfo extends Activity{
    MyAdapter thingsAdapter;
    Context context;
    ArrayList<Item> itemList;
    ArrayList<Store> storeList;
    ArrayList<Item> storeItemList;
    String storeName;
    String category = "전체";
    String order ="최신순";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_info);
        context = this;

        TextView storeItemTitle = (TextView)findViewById(R.id.storeItemTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"yanolja.ttf");
        storeItemTitle.setTypeface(typeface);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        storeItemList = new ArrayList<Item>();
        itemList = (ArrayList<Item>)getIntent().getSerializableExtra("itemList");
        storeList = (ArrayList<Store>)getIntent().getSerializableExtra("storeList");
        storeName  = (String) getIntent().getExtras().getSerializable("storeName");

        for (int i = 0, ii = itemList.size() ; i < ii ; i++) {
            if (itemList.get(i).getStoreName().equals(storeName)) {
                storeItemList.add(itemList.get(i));
            }
        }

        thingsAdapter = new MyAdapter (context, R.layout.things_item, storeItemList, context.getResources().getDisplayMetrics().widthPixels);
        GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)findViewById(R.id.storeItemList);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View headerView = layoutInflater.inflate(R.layout.main_grid_items_header, null);
        Spinner headerCategorie = (Spinner)headerView.findViewById(R.id.selectCategorie);
        Spinner headerFilter = (Spinner)headerView.findViewById(R.id.selectFilter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                (String[])context.getResources().getStringArray(R.array.category));
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                (String[])context.getResources().getStringArray(R.array.filter));
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerAdapter2.setDropDownViewResource(R.layout.spinner_dropdown);
        headerCategorie.setAdapter(spinnerAdapter);
        headerFilter.setAdapter(spinnerAdapter2);

        headerCategorie.setOnItemSelectedListener(mGetItemClickListener);
        headerFilter.setOnItemSelectedListener(mGetItemClickListener);
        gvThings.addHeaderView(headerView);
        gvThings.setAdapter(thingsAdapter);
        gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Store sStore =null;
                Item sItem = storeItemList.get(position);
                for(Store store : storeList ) {
                    if (store.id == sItem.storeID) {
                        sStore = store;
                        break;
                    }
                }
                Intent i = new Intent(context, ItemInfoActivity.class);
                i.putExtra("item", sItem);
                i.putExtra("store",sStore);
                i.putExtra("itemList",itemList);
                i.putExtra("storeList",storeList);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    AdapterView.OnItemSelectedListener mGetItemClickListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Object item = parent.getItemAtPosition(pos);

            switch (parent.getId()) {
                case R.id.selectCategorie:
                    category = item.toString();
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView)parent.getChildAt(0)).setTextSize(13);
                    ((TextView)parent.getChildAt(0)).setGravity(Gravity.CENTER);;
                    break;
                case R.id.selectFilter:
                    order = item.toString();
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView)parent.getChildAt(0)).setTextSize(13);
                    ((TextView)parent.getChildAt(0)).setGravity(Gravity.CENTER);;
                    break;
            }

            getItem(category,order);
            Log.i("lsw",category +" "+ order);
        }
        public void onNothingSelected(AdapterView<?> parent) {
            //TODO
        }

        public void getItem(String category, String order){

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkService.SERVICE_URL)
                    .build();

            Call<List<Item>> callback;
            final NetworkService service = retrofit.create(NetworkService.class);
            callback = service.getClothes(category,order);
            callback.enqueue(new Callback<List<Item>>() {
                @Override
                public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                    ArrayList<Item> itemList = new ArrayList<>();
                    ArrayList<Item> itemListResult = new ArrayList<>();
                    itemList.addAll(response.body());
                    for (int i = 0, ii = itemList.size() ; i < ii ; i++) {
                        if (itemList.get(i).getStoreName().equals(storeName)) {
                            itemListResult.add(itemList.get(i));
                        }
                    }
                    thingsAdapter.renewItem(itemListResult);
                    thingsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Item>> call, Throwable t) {
                    Log.i("lsw","call error:"+t.getMessage());
                }
            });
        }
    };
}

