package com.hicc.cloud.teacher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hicc.cloud.R;

/**
 * Created by Administrator on 2017/8/24/024.
 */

public class ImageDetailsActivity extends AppCompatActivity {

    private ImageView zoomImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_details);
        zoomImageView = (ImageView) findViewById(R.id.zoom_image_view);
        String imagePath = getIntent().getStringExtra("image_path");
        Glide.with(ImageDetailsActivity.this).load(imagePath).placeholder(R.drawable.icon_pic)
                .centerCrop()
                .error(R.drawable.icon_pic)
                .into(zoomImageView);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
