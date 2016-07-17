package kr.co.edge.bosebose;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MyPagerAdapter extends PagerAdapter {

    ArrayList<Item> itemList;
    ArrayList<Store> storeList;


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
        if(position==0){
            v = mInflater.inflate(R.layout.main_grid_items, null);
            v.findViewById(R.id.mainGridViewThings);

            MyAdapter thingsAdapter = new MyAdapter (context, R.layout.things_item, itemList, context.getResources().getDisplayMetrics().widthPixels);
            GridViewWithHeaderAndFooter gvThings = (GridViewWithHeaderAndFooter)v.findViewById(R.id.mainGridViewThings);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View headerView = layoutInflater.inflate(R.layout.main_grid_items_header, null);
            Spinner headerCategorie = (Spinner)headerView.findViewById(R.id.selectCategorie);
            Spinner headerFilter = (Spinner)headerView.findViewById(R.id.selectFilter);
            headerCategorie.setOnItemSelectedListener(mGetItemClickListener);
            headerFilter.setOnItemSelectedListener(mGetItemClickListener);
            gvThings.addHeaderView(headerView);
            gvThings.setAdapter(thingsAdapter);
            gvThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(context, ItemInfoActivity.class);
                    i.putExtra("item",itemList.get(position));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
        else if(position==1){
            v = mInflater.inflate(R.layout.main_grid_stores, null);
            v.findViewById(R.id.mainGridViewStores);

            MyListAdapter storesAdapter = new MyListAdapter(context, R.layout.stores_item, storeList, context.getResources().getDisplayMetrics().widthPixels);
            ListView lvStores = (ListView)v.findViewById(R.id.mainGridViewStores);
            lvStores.setAdapter(storesAdapter);
            lvStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(context, StoreInfoActivity.class);
                    i.putExtra("store",storeList.get(position));
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
                case R.id.selectFilter:
                    System.out.println(item);
                    //TODO header의 값 가져오기
                    //여기서 그 카테고리나 필터의 값을 가져오는데 이값을 기준으로 리스트를 최신화 하면 됩니다
                    break;
            }
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
}