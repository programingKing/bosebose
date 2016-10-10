package kr.co.edge.bosebose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class MyListAdapter extends BaseAdapter {
    Context context;
    int layout;
    int width;
    LayoutInflater inf;
    ArrayList<Store> storeList;

    public MyListAdapter(Context context, int layout, ArrayList<Store> storeList, int width) {
        this.context = context;
        this.layout = layout;
        this.storeList = storeList;
        this.width = width;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void renewItem(ArrayList<Store> itemList){
        this.storeList.clear();
        this.storeList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        ImageView image = (ImageView)convertView.findViewById(R.id.storesImagePreview);
        TextView listStoreContent = (TextView)convertView.findViewById(R.id.listStoreContent);
        TextView listStoreFavoriteNum = (TextView)convertView.findViewById(R.id.listStoreFavoriteNum);
        TextView storeName = (TextView) convertView.findViewById(R.id.listStoreName);

        storeName.setText(storeList.get(position).getName());
        ImageButton storesLIke = (ImageButton)convertView.findViewById(R.id.storesLIke);
        listStoreContent.setText(storeList.get(position).getIntroduction());
        listStoreFavoriteNum.setText(String.valueOf(storeList.get(position).getFavoriteCount()));


        Picasso.with(context)
                .load(storeList.get(position).getImage())
                .fit()
                .centerCrop()
                .into(image);

        return convertView;
    }
}