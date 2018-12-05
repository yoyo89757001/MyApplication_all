package com.xiaojun.danrenbanmohe.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaojun.danrenbanmohe.MyApplication;
import com.xiaojun.danrenbanmohe.R;
import com.xiaojun.danrenbanmohe.bean.BaoCunBean;
import com.xiaojun.danrenbanmohe.bean.PaiHangBean;
import com.xiaojun.danrenbanmohe.bean.Subjects;
import com.xiaojun.danrenbanmohe.bean.YanZhiBean;
import com.xiaojun.danrenbanmohe.camera.CameraManager;
import com.xiaojun.danrenbanmohe.camera.CameraPreview;
import com.xiaojun.danrenbanmohe.camera.CameraPreviewData;
import com.xiaojun.danrenbanmohe.camera.SettingVar;
import com.xiaojun.danrenbanmohe.utils.DateUtils;
import com.xiaojun.danrenbanmohe.utils.FacePassUtil;
import com.xiaojun.danrenbanmohe.utils.FileUtil;
import com.xiaojun.danrenbanmohe.utils.GsonUtil;
import com.xiaojun.danrenbanmohe.view.FaceView;
import com.xiaojun.danrenbanmohe.view.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassCompareResult;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;
import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


public class DingZhiActivity extends Activity implements CameraManager.CameraListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.biaoti)
    TextView biaoti;
    @BindView(R.id.dibutv)
    TextView dibutv;
    @BindView(R.id.dibutv2)
    TextView dibutv2;
    @BindView(R.id.erweima)
    ImageView erweima;
    @BindView(R.id.cameraView)
    CameraPreview cameraView;
    @BindView(R.id.fcview)
    FaceView faceView;
    @BindView(R.id.ceshi)
    ImageView ceshi;

    private static final String authIP = "https://api-cn.faceplusplus.com";
    private static final String apiKey = "JHt8TdGoELfkEKYkjQMogR8GPLIPAfRM";
    private static final String apiSecret = "qgPwtgw9Yiqn2aL9KQyv1ukigAV7xWup";
    private long tID = -1;
    private ConcurrentHashMap<Long, Integer> concurrentHashMap = new ConcurrentHashMap<Long, Integer>();
    private static final String group_name = "facepasstestxnan";
    private int dw, dh;
    private int cameraRotation;
    private static final int cameraWidth = 1920;
    private static final int cameraHeight = 1080;
    public FacePassHandler mFacePassHandler;
    /* 相机实例 */
    private CameraManager manager;
    /* 显示人脸位置角度信息 */
    private RequestOptions myOptions = new RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.erroy_bg)
            .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#FFFFFB00")));
    // .transform(new GlideRoundTransform(MainActivity.this,10));
    private boolean isAnXia = true;
    /* 在预览界面圈出人脸 */
    /* 相机是否使用前置摄像头 */
    private static boolean cameraFacingFront = true;
    int screenState = 0;// 0 横 1 竖
    private Box<BaoCunBean> baoCunBeanDao = null;
    private BaoCunBean baoCunBean = null;
    LinkedBlockingQueue<Toast> mToastBlockQueue;
    /*DetectResult queue*/
    ArrayBlockingQueue<FacePassDetectionResult> mDetectResultQueue;
    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;
    /*recognize thread*/
    RecognizeThread mRecognizeThread;
    FeedFrameThread mFeedFrameThread;
    TanChuangThread tanChuangThread;
    private static boolean isLink = true;
    private static boolean isLink2 = true;
    private final int TIMEOUT = 1000 * 6;
    private   OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            //   .cookieJar(new CookiesManager())
            //  .retryOnConnectionFailure(true)
            .build();
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ruitongfile" + File.separator;
    private static Vector<PaiHangBean> paiHangBeanVector = new Vector<>();
    private float yanzhiP;
    private int nianlingP;
    private byte[] filePathP;
    private int paihangP;
    private final Timer timer = new Timer();
    private TimerTask task;
    private Box<Subjects> subjectsBox = null;
    private WeakHandler mHandler=new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 110:
                    PaiHangBean bean= (PaiHangBean) msg.obj;

                    Glide.get(DingZhiActivity.this).clearMemory();
                    Glide.with(DingZhiActivity.this)
                            .load(bean.getBytes())
                            .apply(myOptions)
                            .into(ceshi);


                    break;

            }

            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ding_zhi);
        ButterKnife.bind(this);

        subjectsBox = MyApplication.myApplication.getBoxStore().boxFor(Subjects.class);

        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 1001,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                        .build());


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {

        if (list != null && list.size() >= 3 && requestCode == 1001) {

            File file = new File(filePath);
            if (!file.exists()) {
                Log.d("DingZhiActivity", "创建文件夹:" + file.mkdirs());
            }

            EventBus.getDefault().register(this);//订阅
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            dw = dm.widthPixels;
            dh = dm.heightPixels;
            baoCunBeanDao = MyApplication.myApplication.getBoxStore().boxFor(BaoCunBean.class);
            baoCunBean = baoCunBeanDao.get(123456L);
            mToastBlockQueue = new LinkedBlockingQueue<>();
            mDetectResultQueue = new ArrayBlockingQueue<FacePassDetectionResult>(5);
            mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);


            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
            if (windowRotation == 0) {
                cameraRotation = FacePassImageRotation.DEG90;
            } else if (windowRotation == 90) {
                cameraRotation = FacePassImageRotation.DEG0;
            } else if (windowRotation == 270) {
                cameraRotation = FacePassImageRotation.DEG180;
            } else {
                cameraRotation = FacePassImageRotation.DEG270;
            }
            SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
            SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
            SettingVar.isCross = preferences.getBoolean("isCross", SettingVar.isCross);
            SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
            SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
            SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);
            if (SettingVar.isSettingAvailable) {
                cameraRotation = SettingVar.faceRotation;
                cameraFacingFront = SettingVar.cameraFacingFront;
            }

            Log.i("orientation", String.valueOf(windowRotation));

            final int mCurrentOrientation = getResources().getConfiguration().orientation;

            if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                screenState = 1;
            } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                screenState = 0;
            }

            FacePassHandler.getAuth(authIP, apiKey, apiSecret);
            FacePassHandler.initSDK(getApplicationContext());

            cameraFacingFront = true;
            FacePassUtil facePassUtil = new FacePassUtil();
            facePassUtil.init(DingZhiActivity.this, getApplicationContext(), cameraRotation, baoCunBean);

            manager = new CameraManager();
            cameraView.setH(dh, faceView);
            manager.setPreviewDisplay(cameraView);
            /* 注册相机回调函数 */
            manager.setListener(this);


            mFeedFrameThread = new FeedFrameThread();
            mFeedFrameThread.start();

            mRecognizeThread = new RecognizeThread();
            mRecognizeThread.start();

            tanChuangThread = new TanChuangThread();
            tanChuangThread.start();

            AssetManager mgr = getAssets();
            //Univers LT 57 Condensed
            Typeface tf = Typeface.createFromAsset(mgr, "baiduzongyijianti.ttf");
            biaoti.setTypeface(tf);
            biaoti.setText("颜 值 测 试");
            dibutv.setText("颜值等级测试贷款机");
            dibutv2.setText("Face Level Test Loan Machine");


            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) erweima.getLayoutParams();
            params.width = (int) (dw * 0.2f);
            params.height = (int) (dw * 0.2f);
            erweima.setLayoutParams(params);
            erweima.invalidate();

            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);

        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
        Log.d("DingZhiActivity", "requestCode222:" + requestCode);
        for (String s : list) {
            Log.d("DingZhiActivity", s + "www");
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String event) {
        if (event.equals("mFacePassHandler")) {
            mFacePassHandler = MyApplication.myApplication.getFacePassHandler();
        }


    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);


        super.onDestroy();

    }

    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {
        /* 如果SDK实例还未创建，则跳过 */
        if (mFacePassHandler == null) {
            return;
        }
        /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
        FacePassImage image;
        try {
            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
        } catch (FacePassException e) {
            e.printStackTrace();
            return;
        }
        mFeedFrameQueue.offer(image);
    }


    private class FeedFrameThread extends Thread {
        boolean isIterrupt;

        @Override
        public void run() {
            while (!isIterrupt) {
                try {

                    FacePassImage image = mFeedFrameQueue.take();
                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
                    final FacePassDetectionResult detectionResult = mFacePassHandler.feedFrame(image);
                    if (detectionResult == null || detectionResult.faceList.length == 0) {
                        faceView.clear();
                        faceView.postInvalidate();

                    } else {
                        //拿陌生人图片
                        showFacePassFace(detectionResult, image);
                        //   Log.d("FeedFrameThread", "detectionResult.images.length:" + image.width+"  "+image.height);
                    }

                    /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                    if (detectionResult != null && detectionResult.message.length != 0) {
                        mDetectResultQueue.offer(detectionResult);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RecognizeThread extends Thread {

        boolean isInterrupt;

        @Override
        public void run() {
            while (!isInterrupt) {
                try {

                    FacePassDetectionResult detectionResult = mDetectResultQueue.take();

                    FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult.message);
                    //   Log.d("RecognizeThread", "识别线程");
                    if (recognizeResult != null && recognizeResult.length > 0) {
                        for (FacePassRecognitionResult result : recognizeResult) {
                            //String faceToken = new String(result.faceToken);
                            if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
                                //识别的

                            } else {
                                Log.d("RecognizeThread", "未识别");
                                //未识别的
                                // 防止concurrentHashMap 数据过多 ,超过一定数据 删除没用的
                                if (concurrentHashMap.size() > 10) {
                                    concurrentHashMap.clear();
                                }
                                if (concurrentHashMap.get(result.trackId) == null) {
                                    //找不到新增
                                    concurrentHashMap.put(result.trackId, 1);
                                } else {
                                    //找到了 把value 加1
                                    concurrentHashMap.put(result.trackId, (concurrentHashMap.get(result.trackId)) + 1);
                                }
                                //判断次数超过3次
                                if (concurrentHashMap.get(result.trackId) == 2) {
                                    tID = result.trackId;
                                    isLink2 = true;
                                    //Log.d("RecognizeThread", "入库"+tID);
                                }

                            }

                        }
                    }

                } catch (InterruptedException | FacePassException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isInterrupt = true;
            super.interrupt();
        }

    }


    private void showFacePassFace(FacePassDetectionResult detectResults, final FacePassImage image) {
        FacePassFace[] detectResult = detectResults.faceList;
        faceView.clear();
        for (final FacePassFace face : detectResult) {
            boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
            StringBuilder faceIdString = new StringBuilder();
            faceIdString.append("ID = ").append(face.trackId);
            // SpannableString faceViewString = new SpannableString(faceIdString);
            //faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StringBuilder faceRollString = new StringBuilder();
            faceRollString.append("旋转: ").append((int) face.pose.roll).append("°");
            StringBuilder facePitchString = new StringBuilder();
            facePitchString.append("上下: ").append((int) face.pose.pitch).append("°");
            StringBuilder faceYawString = new StringBuilder();
            faceYawString.append("左右: ").append((int) face.pose.yaw).append("°");
            StringBuilder faceBlurString = new StringBuilder();
            faceBlurString.append("模糊: ").append(String.format("%.2f", face.blur));
            StringBuilder faceAgeString = new StringBuilder();
            faceAgeString.append("年龄: ").append("0");
            StringBuilder faceGenderString = new StringBuilder();

//            switch (face.gender) {
//                case 0:
//                    faceGenderString.append("性别: 男");
//                    break;
//                case 1:
//                    faceGenderString.append("性别: 女");
//                    break;
//                default:
//                    faceGenderString.append("性别: ?");
//            }

            Matrix mat = new Matrix();
            int w = cameraView.getMeasuredWidth();
            int h = cameraView.getMeasuredHeight();

            int cameraHeight = manager.getCameraheight();
            int cameraWidth = manager.getCameraWidth();

            float left = 0;
            float top = 0;
            float right = 0;
            float bottom = 0;
            switch (cameraRotation) {
                case 0:
                    left = face.rect.left;
                    top = face.rect.top;
                    right = face.rect.right;
                    bottom = face.rect.bottom;
                    mat.setScale(mirror ? -1 : 1, 1);
                    mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                    mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                    break;
                case 90:
                    mat.setScale(mirror ? -1 : 1, 1);
                    mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                    mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                    left = face.rect.top;
                    top = cameraWidth - face.rect.right;
                    right = face.rect.bottom;
                    bottom = cameraWidth - face.rect.left;

                    //北京面板机特有方向
//                            left =cameraHeight-face.rect.bottom;
//                            top = face.rect.left;
//                            right =cameraHeight-face.rect.top;
//                            bottom =face.rect.right;

                    break;
                case 180:
                    mat.setScale(1, mirror ? -1 : 1);
                    mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                    mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                    left = face.rect.right;
                    top = face.rect.bottom;
                    right = face.rect.left;
                    bottom = face.rect.top;
                    break;
                case 270:
                    mat.setScale(mirror ? -1 : 1, 1);
                    mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                    mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                    left = cameraHeight - face.rect.bottom;
                    top = face.rect.left;
                    right = cameraHeight - face.rect.top;
                    bottom = face.rect.right;
            }

            RectF drect = new RectF();
            RectF srect = new RectF(left, top, right, bottom);
            mat.mapRect(drect, srect);

            //头像加宽加高点
//            RectF srect2 = new RectF(face.rect.left - 40 < 0 ? 0 : face.rect.left - 40, face.rect.top - 100 < 0 ? 0 : face.rect.top - 100,
//                    face.rect.right + 40 > image.width ? image.width : face.rect.right + 40, face.rect.bottom + 100 > image.height ? image.height : face.rect.bottom + 100);
            RectF srect2 = new RectF(face.rect.left - 110 < 0 ? 0 : face.rect.left - 110, face.rect.top - 100 < 0 ? 0 : face.rect.top - 100,
                    face.rect.right + 200 > image.width ? image.width : face.rect.right + 200, face.rect.bottom + 100 > image.height ? image.height : face.rect.bottom + 100);


            float pitch = face.pose.pitch;
            float roll = face.pose.roll;
            float yaw = face.pose.yaw;
            //  Log.d("MainActivity203", "pitch:" + pitch);
            //  Log.d("MainActivity203", "roll:" + roll);
            //  Log.d("MainActivity203", "yaw:" + yaw);
            if (pitch < 25 && pitch > -25 && roll < 25 && roll > -25 && yaw < 25 && yaw > -25 && face.blur < 0.3) {
                try {
                    //  Log.d("MainActivity203", "tID:" + tID);
                    //  Log.d("MainActivity203", "face.trackId:" + face.trackId);
                    //   Log.d("MainActivity203", "isLink:" + isLink);
                    if (tID == face.trackId && isLink && isLink2) {  //入库成功后将 tID=-1;
                        //  Log.d("MainActivity203", "进来");
                        isLink = false;
                        isLink2=false;
                        tID = -1;
                        //获取图片
                        YuvImage image2 = new YuvImage(image.image, ImageFormat.NV21, image.width, image.height, null);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image2.compressToJpeg(new Rect(0, 0, image.width, image.height), 100, stream);
                        final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                        stream.close();

                        int x1, y1, x2, y2 = 0;
                        x1 = (int) srect2.left;
                        y1 = (int) srect2.top;
                        //是宽高，不是坐标
                        x2 = (srect2.left + (srect2.right - srect2.left)) > image.width ? (int) (image.width - srect2.left) : (int) (srect2.right - srect2.left);
                        y2 = (srect2.top + (srect2.bottom - srect2.top)) > image.height ? (int) (image.height - srect2.top) : (int) (srect2.bottom - srect2.top);
                        //截取单个人头像
                        final Bitmap bitmap = Bitmap.createBitmap(bmp, x1, y1, x2, y2);
                        Bitmap fileBitmap = FileUtil.adjustPhotoRotation(bitmap, 270);

                        long t = System.currentTimeMillis();
                        String fname = DateUtils.timesTwo(t + "");
                        File file = new File(filePath + fname);
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        saveBitmap2File(fileBitmap, filePath + fname + File.separator + t + ".jpg", 100);

                        getOkHttpClient2(filePath + fname + File.separator + t + ".jpg");


                        //linkedBlockingQueue.offer(b);
                        Log.d("MainActivity203", "陌生人入队列");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ceshi.setImageBitmap(bitmap);
                            }
                        });

                    }


                } catch (Exception ex) {
                    isLink = true;
                    Log.e("Sys", "Error:" + ex.getMessage());
                }

            }


            faceView.addRect(drect);
            faceView.addId(faceIdString.toString());
            faceView.addRoll(faceRollString.toString());
            faceView.addPitch(facePitchString.toString());
            faceView.addYaw(faceYawString.toString());
            faceView.addBlur("");
            faceView.addAge(faceAgeString.toString());
            faceView.addGenders(faceGenderString.toString());

            //   }
            break;
        }

        faceView.postInvalidate();
//            }
//
//        });

    }


    private class TanChuangThread extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {
                    //有动画 ，延迟到一秒一次
                    SystemClock.sleep(1100);
                    //    Subject subject = linkedBlockingQueue.take();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isRing = true;
            // Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        shipingView.pause();
    }


    /***
     *保存bitmap对象到文件中
     * @param bm
     * @param path
     * @param quality
     * @return
     */
    public void saveBitmap2File(Bitmap bm, final String path, int quality) {
        if (null == bm || bm.isRecycled()) {
            Log.d("InFoActivity", "回收|空");
            return;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (!bm.isRecycled()) {
                bm.recycle();
            }
            bm = null;
        }
    }

    //首先登录-->获取所有主机-->创建或者删除或者更新门禁
    private void getOkHttpClient2( final String fileName) {

        MultipartBody mBody = null;
        try {
            File batt = new File(fileName);
            if (batt.length() <= 0) {
                isLink = true;
                mFacePassHandler.reset();
                return;
            }

            RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), batt);
            final String file1Name = System.currentTimeMillis() + "testFile1.jpg";
            mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    /* 底下是上传了两个文件 */
                    .addFormDataPart("image_file", file1Name, fileBody1)
                    /* 上传一个普通的String参数 */
                    .addFormDataPart("api_key", "BBDRR-nwJM38qGHUiBV0k4eMUZ2jDsa1")
                    .addFormDataPart("api_secret", "YDhcIhcjc5OVnDVQvspwSoSQnjM-fWYn")
                    .addFormDataPart("return_attributes", "gender,age,emotion,eyestatus,beauty,skinstatus")
                    .build();

        } catch (Exception e) {
            isLink = true;
            mFacePassHandler.reset();
            Log.d("YanShiActivityttttttt", e.getMessage() + "bitmap回收");
            return;
        }

        //  Base64.decode(printData.getBytes(), Base64.DEFAULT);

//        RequestBody body = new FormBody.Builder()
//                .add("api_key", "BBDRR-nwJM38qGHUiBV0k4eMUZ2jDsa1")
//                .add("api_secret", "YDhcIhcjc5OVnDVQvspwSoSQnjM-fWYn")
//                .add("image_base64", batt)
//                .add("return_attributes", "gender,age,emotion,eyestatus,beauty,skinstatus")
//                .build();

        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        //requestBuilder.header("User-Agent", "Koala Admin");
        //requestBuilder.header("Content-Type","application/json");
        requestBuilder.post(mBody);
        requestBuilder.url("https://api-cn.faceplusplus.com/facepp/v3/detect");
        final okhttp3.Request request = requestBuilder.build();

        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("YanShiActivity", "请求失败" + e.getMessage());
                isLink = true;
                mFacePassHandler.reset();
                // batt.delete();

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    PaiHangBean pp=null;
                    String s = response.body().string();
                    Log.d("YanShiActivitytttttt", "检测" + s);
                    JsonObject jsonObject = GsonUtil.parse(s).getAsJsonObject();
                    Gson gson = new Gson();
                    YanZhiBean menBean = gson.fromJson(jsonObject, YanZhiBean.class);
                    if (menBean.getFaces() != null && menBean.getFaces().get(0) != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                        PaiHangBean diKu=null;
                        if (bitmap!=null) {
                             diKu = new PaiHangBean();
                            //更新
                            YanZhiBean.FacesBean.AttributesBean.SkinstatusBean nn = menBean.getFaces().get(0).getAttributes().getSkinstatus();
                            HashMap<Double, String> kk = new HashMap<>();
                            double[] a = new double[4];
                            a[0] = nn.getAcne();
                            a[1] = nn.getDark_circle();
                            a[2] = nn.getHealth();
                            a[3] = nn.getStain();
                            kk.put(a[0], "青春痘");
                            kk.put(a[1], "黑眼圈");
                            kk.put(a[2], "健康");
                            kk.put(a[3], "暗淡");
                            Arrays.sort(a);  //进行排序

                            YanZhiBean.FacesBean.AttributesBean.EmotionBean nn2 = menBean.getFaces().get(0).getAttributes().getEmotion();
                            HashMap<Double, String> kk2 = new HashMap<>();
                            double[] a2 = new double[7];
                            a2[0] = nn2.getAnger();
                            a2[1] = nn2.getDisgust();
                            a2[2] = nn2.getFear();
                            a2[3] = nn2.getHappiness();
                            a2[4] = nn2.getNeutral();
                            a2[5] = nn2.getSadness();
                            a2[6] = nn2.getSurprise();
                            kk2.put(a2[0], "愤怒");
                            kk2.put(a2[1], "厌恶");
                            kk2.put(a2[2], "害怕");
                            kk2.put(a2[3], "高兴");
                            kk2.put(a2[4], "平静");
                            kk2.put(a2[5], "悲伤");
                            kk2.put(a2[6], "惊讶");
                            Arrays.sort(a2);  //进行排序

                            String xb = "";
                            if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")) {
                                xb = "男";
                            } else {
                                xb = "女";
                            }

                            diKu.setCishu(diKu.getCishu() + 1);
                            diKu.setFuzhi(kk.get(a[3]));
                            diKu.setNianl(menBean.getFaces().get(0).getAttributes().getAge().getValue() - 3);
                            diKu.setXingbie(xb);
                            diKu.setGuanzhu(System.currentTimeMillis());
                            float yan = (float) (xb.equals("女") ? menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score() : menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score());
                            float fl = yan + 18 >= 100 ? 99.9f : yan + 18;
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            diKu.setYanzhi(Float.valueOf(decimalFormat.format(fl)));
                            diKu.setBiaoqing(kk2.get(a2[6]));
                            diKu.setBytes(bitmabToBytes(bitmap));

                            Message message = new Message();
                            message.obj = diKu;
                            message.what = 110;
                            mHandler.sendMessage(message);
                        }



                        //  gengxingjiemian(menBean,facetoken,BitmapFactory.decodeFile(filePath));

                        //判断是否是同一个人
                        if (paiHangBeanVector.size() > 0) {
                            //比对
                            int dui = 0;
                            int size = paiHangBeanVector.size();
                            if (size > 4) {
                                //只比对前4个
                                size = 4;
                            }
                            for (int i = 0; i < size; i++) {
                                float sou = 0;
                                try {

                                    final Bitmap map=BitmapFactory.decodeByteArray(paiHangBeanVector.get(i).getBytes(), 0, paiHangBeanVector.get(i).getBytes().length);
                                    FacePassCompareResult result = mFacePassHandler.compare(map, bitmap, false);
                                    sou= result.score;

                                    Log.d("MainActivity", "sou:" + sou);


                                } catch (FacePassException e) {
                                    isLink = true;
                                    //  Log.d("YanShiActivity", e.getMessage()+"ggggg");
                                }
                                //  Log.d("YanShiActivity", "sou:" + sou);
                                if (sou >= 68) {
                                    dui = 1;
                                    //通过

                                    String xb = "";
                                    if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")) {
                                        xb = "男";
                                    } else {
                                        xb = "女";
                                    }
                                    //  Log.d("YanShiActivityttttt", "bitmabToBytes(bitmap):" + bitmabToBytes(bitmap).length);
                                    // diKu.setTrackId(trackId);
                                    diKu.setCishu(paiHangBeanVector.get(i).getCishu() + 1);

                                    if (paiHangBeanVector.get(i).getNianl() > (menBean.getFaces().get(0).getAttributes().getAge().getValue() - 3)) {
                                        diKu.setNianl(menBean.getFaces().get(0).getAttributes().getAge().getValue() - 3);
                                    } else {
                                        diKu.setNianl(paiHangBeanVector.get(i).getNianl());
                                    }

                                    diKu.setXingbie(xb);
                                    //  diKu.setGuanzhu(System.currentTimeMillis());
                                    float yan = (float) (xb.equals("女") ? menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score() : menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score());
                                    float fl = yan + 18 >= 100 ? 99.9f : yan + 18;
                                    if (paiHangBeanVector.get(i).getYanzhi() < yan) {
                                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                        diKu.setYanzhi(Float.valueOf(decimalFormat.format(fl)));
                                    } else {
                                        diKu.setYanzhi(paiHangBeanVector.get(i).getYanzhi());
                                    }

                                    //替换掉
                                    paiHangBeanVector.set(i, diKu);
                                    pp=diKu;

                                    //更新界面
                                   // setViewFullScreen(zhongjianview, diKu, facetoken);

                                    //计时
                                    task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            Message message = new Message();
                                            message.what = 111;
                                            mHandler.sendMessage(message);
                                        }
                                    };
                                    timer.schedule(task, 10000);
                                    //保存到本地，以后上传
                                    Subjects subjects = new Subjects();
                                    subjects.setAge(diKu.getNianl());
                                    subjects.setFilePath(fileName);
                                    subjects.setSex(diKu.getXingbie());
                                    subjects.setFaceScore(diKu.getYanzhi());
                                    subjectsBox.put(subjects);
                                    yanzhiP=diKu.getYanzhi();
                                    nianlingP=diKu.getNianl();
                                    filePathP=diKu.getBytes();

                                    break;

                                } else {
                                    //不是同一个人
                                    dui = 0;
                                }
                            }
                            //跟所有人不同 就添加
                            if (dui == 0) {


                                String xb = "";
                                if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")) {
                                    xb = "男";
                                } else {
                                    xb = "女";
                                }
                                //  Log.d("YanShiActivityttttt", "bitmabToBytes(bitmap):" + bitmabToBytes(bitmap).length);
                                //  diKu.setTrackId(trackId);
                                diKu.setCishu(diKu.getCishu() + 1);
                                diKu.setNianl(menBean.getFaces().get(0).getAttributes().getAge().getValue() - 3);
                                diKu.setXingbie(xb);
                                diKu.setGuanzhu(System.currentTimeMillis());
                                float yan = (float) (xb.equals("女") ? menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score() : menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score());
                                float fl = yan + 18 >= 100 ? 99.9f : yan + 18;
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                diKu.setYanzhi(Float.valueOf(decimalFormat.format(fl)));

                                paiHangBeanVector.add(diKu);
                                pp=diKu;

                                //更新界面
                             //   setViewFullScreen(zhongjianview, diKu, facetoken);

                                //计时
                                task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = 111;
                                        mHandler.sendMessage(message);
                                    }
                                };
                                timer.schedule(task, 10000);
                                //保存到本地，以后上传
                                Subjects subjects = new Subjects();
                                subjects.setAge(diKu.getNianl());
                                subjects.setFilePath(fileName);
                                subjects.setSex(diKu.getXingbie());
                                subjects.setFaceScore(diKu.getYanzhi());
                                subjectsBox.put(subjects);
                                yanzhiP=diKu.getYanzhi();
                                nianlingP=diKu.getNianl();
                                filePathP=diKu.getBytes();
                            }


                        } else {

                            //更新
                            String xb = "";
                            if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")) {
                                xb = "男";
                            } else {
                                xb = "女";
                            }
                            //  Log.d("YanShiActivityttttt", "bitmabToBytes(bitmap):" + bitmabToBytes(bitmap).length);
                            // diKu.setTrackId(trackId);
                            diKu.setCishu(diKu.getCishu() + 1);
                            diKu.setNianl(menBean.getFaces().get(0).getAttributes().getAge().getValue() - 3);
                            diKu.setXingbie(xb);
                            diKu.setGuanzhu(System.currentTimeMillis());
                            float yan = (float) (xb.equals("女") ? menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score() : menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score());
                            float fl = yan + 18 >= 100 ? 99.9f : yan + 18;
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            diKu.setYanzhi(Float.valueOf(decimalFormat.format(fl)));
                            paiHangBeanVector.add(diKu);
                            pp=diKu;
                            //更新界面
                          //  setViewFullScreen(zhongjianview, diKu, facetoken);

                            //计时
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = 111;
                                    mHandler.sendMessage(message);
                                }
                            };
                            timer.schedule(task, 10000);
                            //保存到本地，以后上传
                            Subjects subjects = new Subjects();
                            subjects.setAge(diKu.getNianl());
                            subjects.setFilePath(fileName);
                            subjects.setSex(diKu.getXingbie());
                            subjects.setFaceScore(diKu.getYanzhi());
                            subjectsBox.put(subjects);
                            yanzhiP=diKu.getYanzhi();
                            nianlingP=diKu.getNianl();
                            filePathP=diKu.getBytes();
                        }

                        //排序
                        //  Log.d("YanShiActivity", "paiHangBeanVector.size():" + paiHangBeanVector.size());

                        Comparator<PaiHangBean> studentComparator2 = new Comparator<PaiHangBean>() {
                            @Override
                            public int compare(PaiHangBean o1, PaiHangBean o2) {

                                if (o1.getYanzhi() != o2.getYanzhi()) {
                                    return (int) (o2.getYanzhi() - o1.getYanzhi());
                                }
                                return 0;
                            }
                        };
                        Collections.sort(paiHangBeanVector, studentComparator2);

                        //  adapter_zuo.notifyDataSetChanged();
                        //  paihangP= paiHangBeanVector.lastIndexOf(pp)+1;

                        int size = paiHangBeanVector.size();
                        for (int ii=0;ii<size;ii++){
                            if (pp.getYanzhi()==paiHangBeanVector.get(ii).getYanzhi()){
                                paihangP=ii+1;
                                break;
                            }
                            //  Log.d("MainActivity", "pp.getYanzhi():" + pp.getYanzhi());
                            //  Log.d("MainActivity", "paiHangBeanVector.get(ii).getYanzhi():" + paiHangBeanVector.get(ii).getYanzhi());
                        }
                        //  Log.d("MainActivity", "paihangP:" + paihangP);
                        if (size > 6) {
                            paiHangBeanVector.remove(paiHangBeanVector.lastElement());
                        }
                        Log.d("YanShiActivitytttttt", "更新成功");


                    }else {
                        isLink = true;
                        mFacePassHandler.reset();
                    }

                } catch (Exception e) {
                    Log.d("YanShiActivity", e.getMessage() + "");
                    isLink = true;
                    mFacePassHandler.reset();
                }

            }
        });

    }


    //图片转为二进制数据
    public byte[] bitmabToBytes(Bitmap bitmap) {
        //将图片转化为位图
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            return baos.toByteArray();
        } catch (Exception ignored) {
        } finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                Log.d("MainActivity", e.getMessage()+"bitmap转byte异常");
                e.printStackTrace();
            }
        }
        Log.d("MainActivity", "返回空byte[]");
        return new byte[0];
    }



}
