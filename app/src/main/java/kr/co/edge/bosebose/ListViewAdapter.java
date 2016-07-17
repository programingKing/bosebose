package kr.co.edge.bosebose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
        Log.i("lsw",storeList.get(position).getImage());
        Picasso.with(context)
                .load(storeList.get(position).getImage())
                .fit()
                .centerCrop()
                .into(image);

        return convertView;
    }
}