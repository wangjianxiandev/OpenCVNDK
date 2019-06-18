package com.wjx.android.opencvndk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private ImageView imageView;
    private int a = 0, r = 0, g = 0, b = 0;
    private void initLoadOpenCV(){
        boolean success= OpenCVLoader.initDebug();
        if(success){
            Log.i(TAG, "OpenCV is loading");
        }else{
            Toast.makeText(this, "Couldn't load opencv", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoadOpenCV();
        button = (Button)findViewById(R.id.process_btn);
        imageView = (ImageView)findViewById(R.id.sample_img);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.niuniu);
                Bitmap bitmap1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                for(int row = 0;row<bitmap1.getHeight();row++){
                    for(int col = 0;col<bitmap1.getWidth();col++){
                        int pixel = bitmap1.getPixel(col,row);
                        //读取像素
                        a = Color.alpha(pixel);
                        r = Color.red(pixel);
                        g = Color.green(pixel);
                        b = Color.blue(pixel);
                        //修改像素
                        r =  255;
                        g = 255;
                        b = 0;
                        bitmap1.setPixel(col, row, Color.argb(a, r,g,b));
                    }
                }
//                imageView.setImageBitmap(bitmap1);

                int[] pixels = new int[bitmap1.getWidth() * bitmap1.getHeight()];
                bitmap1.getPixels(pixels, 0, bitmap1.getWidth(), 0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                int a = 0, r = 0, g = 0, b = 0;
                int index = 0;
                for(int row = 0;row<bitmap1.getHeight();row++){
                    for(int col = 0;col < bitmap1.getWidth();col++){
                        index = bitmap1.getWidth() * bitmap1.getHeight() + col;
                        a = (pixels[index]>>24)&0xff;
                        r = (pixels[index]>>16)&0xff;
                        g = (pixels[index]>>8)&0xff;
                        b = (pixels[index])&0xff;

                        r = 255-r;
                        g = 255 -g;
                        b = 255-b;
                        pixels[index] = (a<<24)|(r<<16)|(g<<8)|b;
                    }
                }
                bitmap1.setPixels(pixels,0, bitmap1.getWidth(), 0, 0, bitmap1.getWidth(), bitmap1.getHeight());

                imageView.setImageBitmap(bitmap1);
//                Mat src = new Mat();
//                Mat dst = new Mat();
//
//                Utils.bitmapToMat(bitmap, src);
//                System.out.println(src);
//                Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
//                Utils.matToBitmap(dst, bitmap);
//                src.release();
//                dst.release();
            }
        });
    }
}