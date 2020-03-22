package com.rs.glidedemo.glide;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideUtil {
    private ImageView imageView ;
    private ProgressBar progressBar;
    public  GlideUtil(ProgressBar progressBar , ImageView imageView){
        this.progressBar = progressBar;
        this.imageView = imageView;
    }


    public void  load(final String url, RequestOptions options){
        ProgressInterceptor.addListener(url, progressListener);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    ProgressListener progressListener = new ProgressListener() {
        @Override
        public void onProgress(int progress) {
            progressBar.setProgress(progress);
        }
    };
}
