package kr.co.edge.bosebose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kr.co.edge.bosebose.R;

class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    int width;
    LayoutInflater inf;
    ArrayList<Item> itemList;


    public MyAdapter(Context context, int layout, ArrayList<Item> itemList, int width) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        this.width = width;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
            convertView.setLayoutParams(new GridView.LayoutParams(width/2, width/2));
        }
        ImageView image = (ImageView)convertView.findViewById(R.id.thingsImagePreview);

        Picasso.with(context)
                .load(itemList.get(position).getImage1())
                .fit()
                .centerCrop()
                .into(image);

        return convertView;
    }
}