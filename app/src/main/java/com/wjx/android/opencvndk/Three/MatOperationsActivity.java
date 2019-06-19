package com.wjx.android.opencvndk.Three;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.wjx.android.opencvndk.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MatOperationsActivity extends AppCompatActivity {

    private int REQUEST_CAPTURE_IMAGE = 1;
    private String TAG = "DEMO-OpenCV";
    private Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_operations);
        meanAndDev();
    }


    public void readAndWritePixels(){
//      首先将图片加载为Mat对象，然后获取图像的宽与高
        Mat src  = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return;
        }
        int channels = src.channels();
        int width = src.cols();
        int height = src.rows();
        //做好上书的准备之后，通过以下的三种方式进行读取，修改和写入像素的操作

        //从Mat对象中每次读取一个像素点数据
        byte[] data = new byte[channels];
        int b = 0, g = 0, r = 0;
        for(int row = 0; row<height;row++){
            for(int col = 0; col<width;col++){
                //读取
                src.get(row, col, data);
                b = data[0]&0xff;
                g = data[1]&0xff;
                r = data[2]&0xff;
                //修改
                b = 255 - b;
                g = 255 - g;
                r = 255 - r;
                //写入
                data[0] = (byte)b;
                data[1] = (byte)g;
                data[2] = (byte)r;
                src.put(row, col,  data);
            }
        }

        //从Mat中每次获取一行数据
        byte[] data1 = new byte[channels * width];
        int b1 = 0, g1 = 0, r1 = 0;
        int pv = 0;
        for(int row = 0;row<height;row++){
            src.get(row, 0, data);
            for(int col = 0;col<data1.length;col++){
                //读取
                pv = data1[col]&0xff;
                //修改
                pv = 255 - pv;
                data1[col] = (byte)pv;
            }
            //写入
            src.put(row, 0, data1);
        }

        //从Mat中一次性获取全部像素数据
        int pv1 = 0;
        byte[] data2 = new byte[channels*width*height];
        src.get(0, 0, data2);
        for(int i = 0;i<data2.length;i++){
            pv1 = data2[i]&0xff;
            pv1 = 255 - pv1;
            data2[i] = (byte)pv1;
        }
        src.put(0, 0,data2);
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
        ImageView iv = (ImageView)this.findViewById(R.id.chapter3_imageView);
        iv.setImageBitmap(bm);
    }


    public void channelsAndPixels(){
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return;
        }

        //分离
        List<Mat> mv=  new ArrayList<>();
        Core.split(src, mv);
        for(Mat m : mv){
            int pv = 0;
            int channels = m.channels();
            int width = m.cols();
            int height = m.rows();
            byte[] data = new byte[channels * width*height];
            m.get(0, 0, data);
            for(int i = 0;i<data.length;i++){
                pv = data[i]&0xff;
                pv = 255 - pv;
                data[i] = (byte)pv;
            }
            src.put(0, 0 ,data);
        }

        //合并
        Core.merge(mv, src);
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
        ImageView iv = (ImageView)this.findViewById(R.id.chapter3_imageView);
        iv.setImageBitmap(bm);
        dst.release();
        src.release();
    }


    public void meanAndDev(){
        //加载图像
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return;
        }
        //转换为灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGRA2GRAY);
        //计算均值与标准方差
        MatOfDouble means = new MatOfDouble();
        MatOfDouble stddevs = new MatOfDouble();
        Core.meanStdDev(gray, means, stddevs);


        //显示均值和标准方差
        double[] mean = means.toArray();
        double[] stddev = stddevs.toArray();


        //读取像素数组
        int width = gray.cols();
        int height = gray.rows();
        byte[] data = new byte[width*height];
        gray.get(0, 0 ,data);
        int pv = 0;

        //根据二进制图像进行分割
        int t = (int)mean[0];
        for(int i = 0;i<data.length;i++){
            pv = data[i]&0xff;
            if(pv>t){
                data[i] = (byte)255;
            }else{
                data[i] = (byte)0;
            }
        }
        gray.put(0, 0 ,data);

        Bitmap bm = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(gray, dst, Imgproc.COLOR_GRAY2RGBA);
        Utils.matToBitmap(dst, bm);
        ImageView iv = (ImageView)this.findViewById(R.id.chapter3_imageView);
        iv.setImageBitmap(bm);
        dst.release();
        gray.release();
        src.release();
    }


    public void matArithmeticDemo(){
        //输入图像
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return;
        }
        //输入图像src2
        Mat moon = Mat.zeros(src.rows(), src.cols(), src.type());
        int cx = src.cols() - 60;
        int cy = 60;
        Imgproc.circle(moon, new Point(cx, cy), 50, new Scalar(90, 95, 234), -1, 8, 0);

        Mat dst = new Mat();
        Core.add(src, moon, dst);


        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);
        ImageView iv = (ImageView)this.findViewById(R.id.chapter3_imageView);
        iv.setImageBitmap(bm);
    }
    public void adjustBrightAndContrast(int b, float c) {
        // 输入图像src1
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return;
        }

        // 调整亮度
        Mat dst1 = new Mat();
        Core.add(src, new Scalar(b, b, b), dst1);

        // 调整对比度
        Mat dst2 = new Mat();
        Core.multiply(dst1, new Scalar(c, c, c), dst2);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst2, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        // show
        ImageView iv = (ImageView)this.findViewById(R.id.chapter3_imageView);
        iv.setImageBitmap(bm);
    }

}
