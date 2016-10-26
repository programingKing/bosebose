package kr.co.edge.bosebose;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class MyPagerAdapter extends PagerAdapter {

    ArrayList<Item> itemList;
    ArrayList<Store> storeList;
    String category = "전체";
    String order ="최신순";
    ListViewAdapter storesAdapter;
    GridViewAdapter thingsAdapter;

    LayoutInflater mInflater;
    Context context;

    public MyPagerAdapter(Context c, ArrayList<Item> items,ArrayList<Store> stores) {
        super();
        context = c;
        mInflater = LayoutInflater.from(c);
        itemList = items;
        storeList = stores;
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(View pager, int position) {
        View v = null;
        if (position == 0) {
            v = mInflater.inflate(R.layout.main_grid_items, null);
            v.findViewById(R.id.mainGridViewThings);

            thingsAdapter = new GridViewAdapter (context, R.layout.things_item, itemList, context.getResources().getDisplayMetrics().widthPixels);
            GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)v.findViewById(R.id.mainGridViewThings);
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


                    Item sItem = itemList.get(position);
                    Intent i = new Intent(context, ItemInfoActivity.class);
                    i.putExtra("item", sItem);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
        else if (position == 1) {
            v = mInflater.inflate(R.layout.main_grid_stores, null);
            v.findViewById(R.id.mainGridViewStores);

            storesAdapter = new ListViewAdapter(context, R.layout.stores_item, storeList, context.getResources().getDisplayMetrics().widthPixels);
            ListView lvStores = (ListView)v.findViewById(R.id.mainGridViewStores);
            lvStores.setAdapter(storesAdapter);
            lvStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(context, StoreInfoActivity.class);
                    i.putExtra("storeID", storeList.get(position).getId());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }

        ((ViewPager)pager).addView(v, 0);

        return v;
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
    };

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager)pager).removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }

    @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    @Override public Parcelable saveState() { return null; }
    @Override public void startUpdate(View arg0) {}
    @Override public void finishUpdate(View arg0) {}

    public void getItem(String category, String order){

        Call<List<Item>> callback;
        final NetworkService service = ServiceGenerator.createService(NetworkService.class);
        callback = service.getClothes(category,order,LoadingActivity.uuid);
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

