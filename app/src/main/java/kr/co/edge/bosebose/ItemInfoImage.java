package kr.co.edge.bosebose;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import retrofit2.http.Url;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jung In Huyk on 2016-10-27.
 */
public class ItemInfoImage extends Activity {
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info_image);

        ImageView item_info_image = (ImageView)findViewById(R.id.item_info_image);
        String imageUri = (String) getIntent().getSerializableExtra("imageUri");

        Picasso.with(ItemInfoImage.this)
                .load(imageUri)
                .resize(500,500)
                .centerCrop()
                .into(item_info_image);

        mAttacher = new PhotoViewAttacher(item_info_image);

    }
}