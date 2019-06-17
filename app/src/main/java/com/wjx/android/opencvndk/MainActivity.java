package com.wjx.android.opencvndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private ImageView imageView;
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
                Mat src = new Mat();
                Mat dst = new Mat();
                Utils.bitmapToMat(bitmap, src);
                Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
                Utils.matToBitmap(dst, bitmap);
                imageView.setImageBitmap(bitmap);
                src.release();
                dst.release();
            }
        });
    }
}
