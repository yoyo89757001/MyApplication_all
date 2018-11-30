package com.example.yqtlaifangdengji.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.camera.CameraManager;
import com.example.yqtlaifangdengji.camera.CameraPreview;
import com.example.yqtlaifangdengji.camera.CameraPreviewData;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity implements CameraManager.CameraListener {
    @BindView(R.id.paizhao)
    Button paizhao;
    @BindView(R.id.mycamera)
    CameraPreview mycamera;
    private CameraManager manager;
    /* 显示人脸位置角度信息 */
    /* 相机预览界面 */
    private CameraPreview cameraView;
    private int dw, dh;
    private static final int cameraWidth = 1280;
    private static final int cameraHeight = 720;
    private static boolean isT=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;


        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.mycamera);
        manager.setPreviewDisplay(cameraView);
        /* 注册相机回调函数 */
        manager.setListener(this);

        RelativeLayout.LayoutParams ppp = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        ppp.width = (int) ((float) dh * 1.2f);
        ppp.height = (int) ((float) dh * 0.7f);
        cameraView.setLayoutParams(ppp);
        cameraView.invalidate();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(CameraActivity.this).clearDiskCache();
//            }
//        }).start();

    }

    @Override
    public void onPictureTaken(final CameraPreviewData cameraPreviewData) {

        if (isT){
            isT=false;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        YuvImage image2 = new YuvImage(cameraPreviewData.nv21Data, ImageFormat.NV21, cameraPreviewData.width, cameraPreviewData.height, null);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image2.compressToJpeg(new Rect(0, 0, cameraPreviewData.width, cameraPreviewData.height), 100, stream);
                        final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                        stream.close();

                        compressImage(bmp);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }

    }


    @Override
    protected void onStop() {
        manager.release();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.open(getWindowManager(), false, cameraWidth, cameraHeight);
        paizhao.setEnabled(true);


    }

    @OnClick(R.id.paizhao)
    public void onViewClicked(View v) {
        donghua(v);
        paizhao.setEnabled(false);
        isT=true;

    }

    public void donghua(final View view) {
        //动画
        SpringSystem springSystem3 = SpringSystem.create();
        final Spring spring3 = springSystem3.createSpring();
        //两个参数分别是弹力系数和阻力系数
        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(80, 6));
        // 添加弹簧监听器
        spring3.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // value是一个符合弹力变化的一个数，我们根据value可以做出弹簧动画
                float value = (float) spring.getCurrentValue();
                //  Log.d("kkkk", "value:" + value);
                //基于Y轴的弹簧阻尼动画
                //	helper.itemView.setTranslationY(value);
                // 对图片的伸缩动画
                //float scale = 1f - (value * 0.5f);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        // 设置动画结束值
        spring3.setEndValue(1f);
    }


    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    private void compressImage(Bitmap bitmap) {

        File file = new File(Environment.getExternalStorageDirectory(), "mmruitong.png");//将要保存图片的路径
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            Log.d("ggg", e.getMessage() + "ffffgg");
        } finally {

            if (bitmap != null)
                bitmap.recycle();

            EventBus.getDefault().post("tupiangengxin");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });

        }

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            options -= 10;//每次都减少10
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            //long length = baos.toByteArray().length;
//        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date(System.currentTimeMillis());
//        String filename = format.format(date);
//        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            try {
//                fos.write(baos.toByteArray());
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//
//            e.printStackTrace();
//        }
//        //	recycleBitmap(bitmap);
//        return file;
    }


}
