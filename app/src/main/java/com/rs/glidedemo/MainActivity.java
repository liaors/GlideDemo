package com.rs.glidedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rs.glidedemo.glide.GlideUtil;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584883993436&di=9c1230d6cda39dc0b5e6b3a1e33e8f78&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201706%2F29%2F185019650h40s9s4v4nihe.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
    }

    public void onClick(View view) {

//        final ProgressBar bar = new ProgressBar(this);
//        RequestOptions options = new RequestOptions()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
//        GlideUtil glideUtil = new GlideUtil(bar,img);
//        glideUtil.load(url,options);

        /**
         *加载圆形图片
         */
//        Glide.with(this).load(url).override(100,100).centerCrop().into(img);//override加载正方形图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //        Glide.with(this).load("url").diskCacheStrategy(DiskCacheStrategy.SOURCE).preload();//预加载
                    FutureTarget<File> target = Glide.with(MainActivity.this).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);//如果没有预加载，就直接请求图片，否则使用预加载的图片
                    try {
                        final File file =target.get();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),file.getPath());
                               Drawable drawable = createRoundImageWithBorder(BitmapFactory.decodeFile(file.getPath())) ;
//                                roundedBitmapDrawable.setCircular(true);
                              img.setImageDrawable(drawable);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
    }

    /**
     *
     * @param bitmap
     * @return
     */
    private Drawable createRoundImageWithBorder(Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        //边框宽度 pixel
        int borderWidthHalf = 20;
        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);
        int newBitmapSquareWidth = bitmapSquareWidth+borderWidthHalf;

        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth,newBitmapSquareWidth,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

        //裁剪后图像,中心裁剪
        canvas.drawBitmap(bitmap, x/2, y/2, null);
        bitmap.recycle();
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf);
        borderPaint.setColor(Color.WHITE);

        //添加边框
        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newBitmapSquareWidth/2, borderPaint);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),roundedBitmap);
        roundedBitmapDrawable.setGravity(Gravity.CENTER);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
}

