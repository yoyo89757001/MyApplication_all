package megvii.testfacepass.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.badoo.mobile.util.WeakHandler;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.plattysoft.leonids.ParticleSystem;
import com.sdsmdg.tastytoast.TastyToast;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.query.LazyList;
import io.objectbox.query.Query;
import me.grantland.widget.AutofitTextView;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;
import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BenDiJiLuBean;
import megvii.testfacepass.beans.GuanHuai;
import megvii.testfacepass.beans.GuanHuai_;
import megvii.testfacepass.beans.MSRBean;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.beans.Subject_;
import megvii.testfacepass.beans.TianQiBean;
import megvii.testfacepass.beans.TodayBean;
import megvii.testfacepass.beans.XinXiAll;
import megvii.testfacepass.beans.XinXiIdBean;
import megvii.testfacepass.beans.XinXiIdBean_;
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;
import megvii.testfacepass.dialogall.XiuGaiListener;
import megvii.testfacepass.tts.control.InitConfig;
import megvii.testfacepass.tts.control.MySyntherizer;
import megvii.testfacepass.tts.control.NonBlockSyntherizer;
import megvii.testfacepass.tts.listener.UiMessageListener;
import megvii.testfacepass.tts.util.OfflineResource;
import megvii.testfacepass.utils.DateUtils;
import megvii.testfacepass.utils.FacePassUtil;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GlideUtils;
import megvii.testfacepass.utils.GsonUtil;
import megvii.testfacepass.utils.RandomDataUtil;
import megvii.testfacepass.utils.SettingVar;
import megvii.testfacepass.utils.ValueAnimatorIntface;
import megvii.testfacepass.utils.ValueAnimatorUtils;
import megvii.testfacepass.view.FKTopView_101;
import megvii.testfacepass.view.GlideRoundTransform;
import megvii.testfacepass.view.YGTopView;
import megvii.testfacepass.view.YGTopView_101;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;




public class MainActivity101 extends AppCompatActivity implements CameraManager.CameraListener, XiuGaiListener {


    protected Handler mainHandler;
    @BindView(R.id.root_layout)
    CardView rootLayout;
    @BindView(R.id.da_bg)
    ImageView daBg;
    // @BindView(R.id.scrollview)
    //  HorizontalScrollView scrollView;
    @BindView(R.id.tianqi_im)
    ImageView tianqiIm;
    @BindView(R.id.zidongtext)
    AutofitTextView zidongtext;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.xiaoshi)
    AutofitTextView xiaoshi;
    @BindView(R.id.riqi)
    AutofitTextView riqi;
    @BindView(R.id.xingqi)
    AutofitTextView xingqi;
    @BindView(R.id.wendu)
    AutofitTextView wendu;
    @BindView(R.id.xingqi2)
    AutofitTextView xingqi2;
    @BindView(R.id.tianqi_im2)
    ImageView tianqiIm2;
    @BindView(R.id.wendu2)
    AutofitTextView wendu2;
    @BindView(R.id.xingqi3)
    AutofitTextView xingqi3;
    @BindView(R.id.tianqi_im3)
    ImageView tianqiIm3;
    @BindView(R.id.wendu3)
    AutofitTextView wendu3;
    @BindView(R.id.shidu)
    AutofitTextView shidu;
    @BindView(R.id.heng_liebiao)
    FlexboxLayout hengLiebiao;


    private final Timer timer = new Timer();
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.name)
    AutofitTextView name;
    @BindView(R.id.hyy)
    AutofitTextView hyy;
    private TimerTask task;
    private Box<Subject> subjectBox = null;
    private static boolean isSC = true;
    private String appId = "11644783";
    private String appKey = "knGksRFLoFZ2fsjZaMC8OoC7";
    private String secretKey = "IXn1yrFezEo55LMkzHBGuTs1zOkXr9P4";
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.MIX;
    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_speech_female.data为离线男声模型；bd_etts_speech_female.data为离线女声模型
    private String offlineVoice = OfflineResource.VOICE_FEMALE;
    // 主控制类，所有合成控制方法从这个类开始
    private MySyntherizer synthesizer;
    //  private static Vector<Subject> dibuList = new Vector<>();//下面的弹窗
    //  private static Vector<View> allList = new Vector<>();//中间的弹窗
//    private RequestOptions myOptions = new RequestOptions()
//            .fitCenter()
//            .error(R.drawable.erroy_bg)
//            .transform(new GlideCircleTransform(MainActivity101.this, 10, Color.parseColor("#ffffff")));
    // .transform(new GlideRoundTransform(MainActivity.this,10));
    private ImageView ceshi;
    private RequestOptions myOptions2 = new RequestOptions()
            .fitCenter()
            .error(R.drawable.erroy_bg)
            //   .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
            .transform(new GlideRoundTransform(MainActivity101.this, 20));
    private ValueAnimatorUtils utils = null;

    private LinkedBlockingQueue<Subject> linkedBlockingQueue;
    /* 人脸识别Group */
    private static final String group_name = "facepasstestx";
    /* 程序所需权限 ：相机 文件存储 网络访问 */
 //  private WindowManager wm;
    /* SDK 实例对象 */
    public FacePassHandler mFacePassHandler;
    /* 相机实例 */
    private CameraManager manager;
    /* 显示人脸位置角度信息 */
   // private XiuGaiGaoKuanDialog dialog = null;
    /* 相机预览界面 */
    private CameraPreview cameraView;
    private boolean isAnXia = true;
    private ConcurrentHashMap<Long, Integer> concurrentHashMap = new ConcurrentHashMap<Long, Integer>();
    private static boolean cameraFacingFront = true;
    private int cameraRotation;
    private static final int cameraWidth = 640;
    private static final int cameraHeight = 480;
    // private IjkVideoView shipingView;
    private int heightPixels;
    private int widthPixels;
    int screenState = 0;// 0 横 1 竖
    /* 网络请求队列*/
    /*Toast 队列*/
    LinkedBlockingQueue<Toast> mToastBlockQueue;
    private static boolean isDH = false;
    private static boolean isLink = true;
    private long tID = -1;
   // private boolean isNet = false;
    /*DetectResult queue*/
    ArrayBlockingQueue<FacePassDetectionResult> mDetectResultQueue;
    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;
    /*recognize thread*/
    RecognizeThread mRecognizeThread;
    FeedFrameThread mFeedFrameThread;
    TanChuangThread tanChuangThread;
    private int dw, dh;
    private Box<BaoCunBean> baoCunBeanDao = null;
    private Box<TodayBean> todayBeanBox = null;
    private Box<BenDiJiLuBean> benDiJiLuBeanBox = null;
    private BaoCunBean baoCunBean = null;
    private TodayBean todayBean = null;
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;
    private WeakHandler mHandler;

    private List<Integer> topZuoBiao = new ArrayList<>();
    private List<Integer> bootomZuoBiao = new ArrayList<>();
    private int[] topIm = new int[]{R.drawable.sp1, R.drawable.sp2, R.drawable.sp3, R.drawable.sp4, R.drawable.sp5,
            R.drawable.sp6, R.drawable.sp7, R.drawable.sp8, R.drawable.sp9,};

    private int[] qqIm = new int[]{R.drawable.qq3, R.drawable.
            qq5, R.drawable.qq6, R.drawable.qq7, R.drawable.qq8};
    private ShengRiThierd shengRiThierd = null;
    private VipThired vipThired = null;
    private FangkeThired fangkeThired = null;
    private Query<Subject> query = null;
    private NetWorkStateReceiver netWorkStateReceiver = null;
    private Box<GuanHuai> guanHuaiBox = null;
    private Box<XinXiAll> xinXiAllBox = null;
    private List<GuanHuai> guanHuaiList = new ArrayList<>();
    //   private List<String> bumenString = new ArrayList<>();
    //  private String leixing[] = new String[]{"员工", "普通访客", "白名单", "陌生人"};
    private Box<XinXiIdBean> xinXiIdBeanBox = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mImageCache = new FaceImageCache();
        mToastBlockQueue = new LinkedBlockingQueue<>();
        mDetectResultQueue = new ArrayBlockingQueue<FacePassDetectionResult>(5);
        mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);
        todayBeanBox = MyApplication.myApplication.getTodayBeanBox();
        todayBean = todayBeanBox.get(123456L);
        benDiJiLuBeanBox = MyApplication.myApplication.getBenDiJiLuBeanBox();
        guanHuaiBox = MyApplication.myApplication.getGuanHuaiBox();
        xinXiAllBox = MyApplication.myApplication.getXinXiAllBox();
        xinXiIdBeanBox = MyApplication.myApplication.getXinXiIdBeanBox();
        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };


        baoCunBean = baoCunBeanDao.get(123456L);
        subjectBox = MyApplication.myApplication.getSubjectBox();
        //网络状态关闭
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netWorkStateReceiver, filter);
        }

//        final GuanHuai guanHuai = new GuanHuai("0", "你有一个新的快递,请到前台领取", "未领取");
//        GuanHuai guanHuai2 = new GuanHuai("1", "祝你生日快乐", "");
//        GuanHuai guanHuai3 = new GuanHuai("2", "今天是你入职100天,请到前台领取小礼物", "");
//        GuanHuai guanHuai4 = new GuanHuai("3", "@所有员工,国庆节快乐", "");
//        guanHuaiList.add(guanHuai);
//        guanHuaiList.add(guanHuai2);
//        guanHuaiList.add(guanHuai3);
//        guanHuaiList.add(guanHuai4);
//
//        bumenString.add("技术部");
//        bumenString.add("总裁办");
//        bumenString.add("销售部");
//        bumenString.add("人力资源部");
//        bumenString.add("后勤部");

        //每分钟的广播

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);
        linkedBlockingQueue = new LinkedBlockingQueue<>();

        EventBus.getDefault().register(this);//订阅
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;

        //初始化动画坐标
        new Thread(new Runnable() {
            @Override
            public void run() {
                int temp = dw / 9;
                for (int i = 1; i < 10; i++) {
                    if (i == 9) {
                        topZuoBiao.add(temp * 9 - 80);
                    } else {
                        topZuoBiao.add(temp * i);
                    }
                }

                int temp2 = dw / 6;
                for (int i = 1; i < 7; i++) {
                    if (i == 6) {
                        bootomZuoBiao.add(temp2 * 6 - 200);
                    } else {
                        bootomZuoBiao.add(temp2 * i);
                    }
                }
            }
        }).start();

        // Log.d("MainActivity101", DateUtils.yrn((System.currentTimeMillis() + 86400000L + 86400000L) + ""));

        /* 初始化界面 */
        initView();

//        dibuliebiao.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//                    @Override
//                    public void onGlobalLayout(){
//                        //只需要获取一次高度，获取后移除监听器
//                        dibuliebiao.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                    }
//
//                });



        if (baoCunBean != null)
            initialTts();

        if (baoCunBean != null) {
            FacePassUtil util = new FacePassUtil();
            util.init(MainActivity101.this, getApplicationContext(), cameraRotation, baoCunBean);
        } else {
            Toast tastyToast = TastyToast.makeText(MainActivity101.this, "获取本地设置失败,请进入设置界面设置基本信息", TastyToast.LENGTH_LONG, TastyToast.INFO);
            tastyToast.setGravity(Gravity.CENTER, 0, 0);
            tastyToast.show();
        }


        mFeedFrameThread = new FeedFrameThread();
        mFeedFrameThread.start();

        mRecognizeThread = new RecognizeThread();
        mRecognizeThread.start();

        tanChuangThread = new TanChuangThread();
        tanChuangThread.start();


        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 111: {


                        if (shengRiThierd != null) {
                            shengRiThierd.interrupt();
                            shengRiThierd = null;
                        }
                        if (fangkeThired != null) {
                            fangkeThired.interrupt();
                            fangkeThired = null;
                        }
                        if (vipThired != null) {
                            vipThired.interrupt();
                            vipThired = null;
                        }
                        vipThired = new VipThired();
                        vipThired.start();

                        final Subject bean2 = (Subject) msg.obj;
                      //  final View view_dk = View.inflate(MainActivity101.this, R.layout.vip_item_101, null);

                        name.setText(bean2.getName());
                        hyy.setText("欢迎贵宾VIP来访");
                        synthesizer.speak("欢迎贵宾VIP来访");
                        try {
                            if (bean2.getDisplayPhoto() != null) {

                                Glide.with(MainActivity101.this)
                                        .load(bean2.getDisplayPhoto())
                                        .apply(myOptions2)
                                        .into(touxiang);
                            } else {
                                Bitmap bitmap = mFacePassHandler.getFaceImage(bean2.getTeZhengMa().getBytes());
                                //   mianBanJiView.setBitmap(FileUtil.toRoundBitmap(bitmap), 0);
                                //     Bitmap bitmap=BitmapFactory.decodeByteArray(bean2.getBitmap(),0,bean2.getBitmap().length);
                                //    mianBanJiView.setBitmap(FileUtil.toRoundBitmap(bitmap), 0);
                                Glide.with(MainActivity101.this)
                                        .load(new BitmapDrawable(getResources(), bitmap))
                                        .apply(myOptions2)
                                        .into(touxiang);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        rootLayout.setX((int) ((float) dw * 0.06f));
                        rootLayout.setY(dh - (int) ((float) dh * 0.08f));


                        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) touxiang.getLayoutParams();
                        layoutParams2.width = (int) ((float) dw * 0.18f);
                        layoutParams2.leftMargin = 50;
                        layoutParams2.height = (int) ((float) dw * 0.23f);
                        touxiang.setLayoutParams(layoutParams2);
                        touxiang.invalidate();

                        //入场动画(从右往左)
                        ValueAnimator anim = ValueAnimator.ofInt(dh - (int) ((float) dh * 0.08f), rootLayout.getHeight() - (int) ((float) dw * 0.058f));
                        anim.setDuration(300);
                        anim.setRepeatMode(ValueAnimator.RESTART);
                        Interpolator interpolator = new DecelerateInterpolator(2f);
                        anim.setInterpolator(interpolator);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int currentValue = (Integer) animation.getAnimatedValue();
                                rootLayout.setY(currentValue);
                                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                                rootLayout.requestLayout();
                            }
                        });
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                rootLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {


                                //启动定时器或重置定时器
                                if (task != null) {
                                    task.cancel();
                                    //timer.cancel();
                                    task = new TimerTask() {
                                        @Override
                                        public void run() {

                                            Message message = new Message();
                                            message.what = 900;
                                            mHandler.sendMessage(message);
                                        }
                                    };
                                    timer.schedule(task, 8000);

                                } else {
                                    task = new TimerTask() {
                                        @Override
                                        public void run() {

                                            Message message = new Message();
                                            message.what = 900;
                                            mHandler.sendMessage(message);

                                        }
                                    };
                                    timer.schedule(task, 8000);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        anim.start();


                        break;
                    }

                    case 444: {

                        if (shengRiThierd != null) {
                            shengRiThierd.interrupt();
                            shengRiThierd = null;
                        }
                        if (fangkeThired != null) {
                            fangkeThired.interrupt();
                            fangkeThired = null;
                        }
                        if (vipThired != null) {
                            vipThired.interrupt();
                            vipThired = null;
                        }

                        //普通打卡
                        final Subject bean2 = (Subject) msg.obj;
                        boolean isRT = false;
                        synchronized (MainActivity101.this) {
                            int sizes = hengLiebiao.getFlexItemCount();
                            for (int i = 0; i < sizes; i++) {
                                if ((bean2.getId() + "").equals(hengLiebiao.getReorderedFlexItemAt(i).getTag().toString())) {
                                    isRT = true;
                                    break;
                                }
                            }
                        }
                        if (isRT)
                            break;

                        //0小邮局 1生日提醒 2入职关怀 3节日关怀
                        boolean isSR = false;
                        final List<GuanHuai> ygguanHuaiList = guanHuaiBox.query().equal(GuanHuai_.employeeId, bean2.getId()).build().find();
                        final List<GuanHuai> ygguanHuaiList2 = guanHuaiBox.query().equal(GuanHuai_.employeeId, 0L).build().find();
                        if (ygguanHuaiList2.size() > 0) {
                            ygguanHuaiList.addAll(ygguanHuaiList2);
                        }
                        //信息推送
                        List<XinXiAll> xinXiAlls = xinXiAllBox.getAll();
                        for (XinXiAll all : xinXiAlls) {
                            if (all.getEmployeeId().equals("0") && all.getStartTime() <= System.currentTimeMillis()) {
                                GuanHuai guanHuai1 = new GuanHuai();
                                guanHuai1.setMarkedWords(all.getEditNews());
                                guanHuai1.setNewsStatus("");
                                guanHuai1.setProjectileStatus("4");
                                ygguanHuaiList.add(guanHuai1);

                            } else {
                                //查出来
                                final List<XinXiIdBean> xinXiIdBeans = xinXiIdBeanBox.query().equal(XinXiIdBean_.ygid, bean2.getId()).build().find();
                                for (XinXiIdBean idBean : xinXiIdBeans) {
                                    if (idBean.getUuid().equals(all.getId()) && all.getStartTime() <= System.currentTimeMillis()) {
                                        GuanHuai guanHuai1 = new GuanHuai();
                                        guanHuai1.setMarkedWords(all.getEditNews());
                                        guanHuai1.setNewsStatus("");
                                        guanHuai1.setProjectileStatus("4");
                                        ygguanHuaiList.add(guanHuai1);
                                    }
                                }
                            }
                        }
                        //有没有生日
                        final int si = ygguanHuaiList.size();
                        for (GuanHuai huai : ygguanHuaiList) {
                            if (huai.getProjectileStatus().equals("1")) {
                                isSR = true;
                                break;
                            }
                        }

                        if (isSR) {
                            shengRiThierd = new ShengRiThierd();
                            shengRiThierd.start();
                        }

                        final View view_dk = View.inflate(MainActivity101.this, R.layout.yuangong_item_101, null);
                        final YGTopView_101 ygTopView = view_dk.findViewById(R.id.ygtopview);
                        final RelativeLayout root_rl = view_dk.findViewById(R.id.root_rl);
                        LottieAnimationView wangluo = view_dk.findViewById(R.id.wangluo);
                        wangluo.setPerformanceTrackingEnabled(true);
                        wangluo.setSpeed(0.6f);
                        final ScrollView scrollView_03 = view_dk.findViewById(R.id.scrollview_03);
                        ygTopView.setHight((int) ((float) dw * 0.28f), (int) ((float) dh * 0.70f));
                        ygTopView.setBitmapHG();
                        if (isSR) {
                            ygTopView.setName(bean2.getName(), bean2.getDepartmentName(), true);
                        } else {
                            ygTopView.setName(bean2.getName(), bean2.getDepartmentName(), false);
                        }

                        try {
                            if (bean2.getDisplayPhoto() != null) {
                                ygTopView.setBitmapTX(BitmapFactory.decodeFile(bean2.getDisplayPhoto()));
                            } else {
                                Bitmap bitmap = mFacePassHandler.getFaceImage(bean2.getTeZhengMa().getBytes());
                                ygTopView.setBitmapTX(bitmap);
                                //    Bitmap bitmap=BitmapFactory.decodeByteArray(bean2.getBitmap(),0,bean2.getBitmap().length);
                                //  ygTopView.setBitmapTX(FileUtil.toRoundBitmap(bitmap));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        final LinearLayout xiaoxi_ll = view_dk.findViewById(R.id.xiaoxi_ll);
                        // view_dk.setX(dw);

                        //动画
                        SpringSystem springSystem3 = SpringSystem.create();
                        final Spring spring3 = springSystem3.createSpring();
                        //两个参数分别是弹力系数和阻力系数
                        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
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
                                view_dk.setScaleX(value);
                                view_dk.setScaleY(value);

                            }
                        });
                        // 设置动画结束值
                        spring3.setEndValue(1f);
                        view_dk.setTag(bean2.getId() + "");
                        hengLiebiao.addView(view_dk);


                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ygTopView.getLayoutParams();
                        layoutParams2.width = (int) ((float) dw * 0.28f);
                        layoutParams2.height = (int) ((float) dh * 0.70f) - 20;
                        layoutParams2.topMargin = 10;
                        ygTopView.setLayoutParams(layoutParams2);
                        ygTopView.invalidate();

                        //中间打勾动画的高宽
                        RelativeLayout.LayoutParams wangluop = (RelativeLayout.LayoutParams) wangluo.getLayoutParams();
                        wangluop.topMargin = (int) (((float) dh * 0.70f) / 2 + ((float) dw * 0.01f) / 2);
                        wangluop.leftMargin = (int) (((float) dw * 0.28f) / 2 - ((float) dw * 0.04f) / 2);
                        wangluop.width = (int) ((float) dw * 0.04f);
                        wangluop.height = (int) ((float) dw * 0.04f);
                        wangluo.setLayoutParams(wangluop);
                        wangluo.invalidate();


                        if (si > 0) {
                            //有消息
                            view_dk.setEnabled(false);

                            FlexboxLayout.LayoutParams rlLayoutParams = (FlexboxLayout.LayoutParams) root_rl.getLayoutParams();
                            rlLayoutParams.width = (int) ((float) dw * 0.5f);
                            rlLayoutParams.leftMargin = (int) ((float) dw * 0.02f);
                            rlLayoutParams.rightMargin = (int) ((float) dw * 0.02f);
                            root_rl.setLayoutParams(rlLayoutParams);
                            root_rl.invalidate();

                            //右边消息列表的高
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scrollView_03.getLayoutParams();
                            layoutParams.height = (int) (((float) dh * 0.70f) - ((float) dw * 0.05f)) - 10;
                            layoutParams.topMargin = (int) ((float) dw * 0.05f);
                            scrollView_03.setLayoutParams(layoutParams);
                            scrollView_03.invalidate();

                            scrollView_03.setVisibility(View.VISIBLE);

                            while (true) {
                                if (hengLiebiao.getFlexItemCount() > 2) {
                                    hengLiebiao.removeViewAt(0);
                                } else {
                                    break;
                                }
                            }

                        } else {
                            //没消息
                            FlexboxLayout.LayoutParams rlLayoutParams = (FlexboxLayout.LayoutParams) root_rl.getLayoutParams();
                            rlLayoutParams.width = (int) ((float) dw * 0.28f);
                            rlLayoutParams.leftMargin = (int) ((float) dw * 0.02f);
                            rlLayoutParams.rightMargin = (int) ((float) dw * 0.02f);
                            root_rl.setLayoutParams(rlLayoutParams);
                            root_rl.invalidate();
                            scrollView_03.setVisibility(View.GONE);
                            boolean sskk = false;
                            for (int i = 0; i < hengLiebiao.getFlexItemCount(); i++) {
                                if (!hengLiebiao.getChildAt(i).isEnabled()) {
                                    sskk = true;
                                    break;
                                }
                            }
                            if (sskk) {
                                while (true) {
                                    if (hengLiebiao.getFlexItemCount() > 2) {
                                        hengLiebiao.removeViewAt(0);
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                while (true) {
                                    if (hengLiebiao.getFlexItemCount() > 3) {
                                        hengLiebiao.removeViewAt(0);
                                    } else {
                                        break;
                                    }
                                }
                            }


                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < si; i++) {

                                    final int finalI = i;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final View view_xiaoxi = View.inflate(MainActivity101.this, R.layout.xiaoxi_item, null);
                                            ScreenAdapterTools.getInstance().loadView(view_xiaoxi);
                                            LinearLayout rl_xiaoxi = view_xiaoxi.findViewById(R.id.rl_xiaoxi);
                                            TextView neirong = view_xiaoxi.findViewById(R.id.neirong);
                                            TextView lingqu = view_xiaoxi.findViewById(R.id.lingqu);
                                            TextView biaoti = view_xiaoxi.findViewById(R.id.biaoti);
                                            ImageView xiaoxi_im = view_xiaoxi.findViewById(R.id.xiaoxi_im);
                                            switch (ygguanHuaiList.get(finalI).getProjectileStatus()) {
                                                case "0":
                                                    //小邮局
                                                    xiaoxi_im.setBackgroundResource(R.drawable.youjian_bg);
                                                    try {
                                                        biaoti.setText("邮件");
                                                        lingqu.setText(ygguanHuaiList.get(finalI).getNewsStatus());
                                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case "1":
                                                    // 生日提醒
                                                    xiaoxi_im.setBackgroundResource(R.drawable.shengri_bg2);
                                                    try {
                                                        biaoti.setText("生日");
                                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
                                                        lingqu.setText("");
                                                        lingqu.setVisibility(View.GONE);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case "2":
                                                    //入职关怀
                                                    xiaoxi_im.setBackgroundResource(R.drawable.guanhuai_bg);
                                                    try {
                                                        biaoti.setText("入职关怀");
                                                        lingqu.setText("");
                                                        lingqu.setVisibility(View.GONE);
                                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case "3":
                                                    //节日关怀
                                                    xiaoxi_im.setBackgroundResource(R.drawable.jieri_xx);
                                                    try {
                                                        biaoti.setText("节日关怀");
                                                        lingqu.setText("");
                                                        lingqu.setVisibility(View.GONE);
                                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case "4":
                                                    //节日关怀
                                                    xiaoxi_im.setBackgroundResource(R.drawable.ruzhiguahuai_b);
                                                    try {
                                                        biaoti.setText("信息推送");
                                                        lingqu.setText("");
                                                        lingqu.setVisibility(View.GONE);
                                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                            }

                                            view_xiaoxi.setY(dh);
                                            xiaoxi_ll.addView(view_xiaoxi);

                                            RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) rl_xiaoxi.getLayoutParams();
                                            layoutParams6.bottomMargin = 20;
                                            layoutParams6.topMargin = 10;
                                            layoutParams6.height = ((int) ((float) dh * 0.11));
                                            rl_xiaoxi.setLayoutParams(layoutParams6);
                                            rl_xiaoxi.invalidate();

                                            RelativeLayout.LayoutParams layoutParams8 = (RelativeLayout.LayoutParams) xiaoxi_im.getLayoutParams();
                                            layoutParams8.leftMargin = 30;
                                            layoutParams8.width = (int) (dw * 0.02f);
                                            layoutParams8.height = (int) (dw * 0.02f);
                                            xiaoxi_im.setLayoutParams(layoutParams8);
                                            xiaoxi_im.invalidate();

                                            float sfff = 30 + ((float) dh * 0.11f);

                                            ValueAnimator animator = ValueAnimator.ofFloat(dh, sfff * finalI);
                                            //动画时长，让进度条在CountDown时间内正好从0-360走完，
                                            animator.setDuration(1000);
                                            animator.setInterpolator(new DecelerateInterpolator());//匀速
                                            animator.setRepeatCount(0);//0表示不循环，-1表示无限循环
                                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                @Override
                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                    /**
                                                     * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
                                                     * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
                                                     * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
                                                     */
                                                    float jiaodu = (float) animation.getAnimatedValue();
                                                    view_xiaoxi.setY(jiaodu);

                                                }
                                            });
                                            animator.start();

                                            scrollView_03.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });
                                    SystemClock.sleep(600);

                                }
                            }
                        }).start();

                        //消失
                        Message message = Message.obtain();
                        message.obj = bean2.getId() + "";
                        message.what = 999;
                        mHandler.sendMessageDelayed(message, 8000);

//                                    //启动定时器或重置定时器
//                                    if (task != null) {
//                                        task.cancel();
//                                        //timer.cancel();
//                                        task = new TimerTask() {
//                                            @Override
//                                            public void run() {
//                                                Message message = new Message();
//                                                message.what = 999;
//                                                mHandler.sendMessage(message);
//                                            }
//                                        };
//                                        timer.schedule(task, 8000);
//                                    } else {
//                                        task = new TimerTask() {
//                                            @Override
//                                            public void run() {
//                                                Message message = new Message();
//                                                message.what = 999;
//                                                mHandler.sendMessage(message);
//                                            }
//                                        };
//                                        timer.schedule(task, 8000);
//                                    }

                        break;

//                        //动画
//                        SpringSystem springSystem3 = SpringSystem.create();
//                        final Spring spring3 = springSystem3.createSpring();
//                        //两个参数分别是弹力系数和阻力系数
//                        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
//                        // 添加弹簧监听器
//                        spring3.addListener(new SimpleSpringListener() {
//                            @Override
//                            public void onSpringUpdate(Spring spring) {
//                                // value是一个符合弹力变化的一个数，我们根据value可以做出弹簧动画
//                                float value = (float) spring.getCurrentValue();
//                                //  Log.d("kkkk", "value:" + value);
//                                //基于Y轴的弹簧阻尼动画
//                                //	helper.itemView.setTranslationY(value);
//                                // 对图片的伸缩动画
//                                //float scale = 1f - (value * 0.5f);
//                                view_dk.setScaleX(value);
//                                view_dk.setScaleY(value);
//                                if (value == 1 && isNet) {
//
//
//                                }
//                            }
//                        });
//                        // 设置动画结束值
//                        spring3.setEndValue(1f);break;
                    }
                    case 555: {

                        // Log.d("MainActivity101", "收到555");
                        //零时陌生人
                        final Subject bean2 = (Subject) msg.obj;
                        //生日的时候开启
                        //0小邮局 1生日提醒 2入职关怀 3节日关怀
                        boolean isSR = true;

                        if (isSR) {
                            if (shengRiThierd != null) {
                                shengRiThierd.interrupt();
                                shengRiThierd = null;
                            }
                            shengRiThierd = new ShengRiThierd();
                            shengRiThierd.start();
                        }

                        final View view_dk = View.inflate(MainActivity101.this, R.layout.yuangong_item_03, null);
                        ScreenAdapterTools.getInstance().loadView(view_dk);
                        final YGTopView ygTopView = view_dk.findViewById(R.id.ygtopview);
                        TextView ygwenzitext = view_dk.findViewById(R.id.ygwenzitext);
                        final LinearLayout linearygwenzi = view_dk.findViewById(R.id.ygwenzi);
                        final ScrollView scrollView_03 = view_dk.findViewById(R.id.scrollview_03);
                        final TextView xiabandaka = view_dk.findViewById(R.id.xiabandaka);
                        final ImageView xiabandagou = view_dk.findViewById(R.id.xiabandagou);
                        final RelativeLayout xiaban_rl = view_dk.findViewById(R.id.xiaban_rl);
                        xiaban_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bean2.setEmail("1");
                                subjectBox.put(bean2);
                                xiabandagou.setBackgroundResource(R.drawable.heigou);
                                xiabandaka.setTextColor(Color.BLACK);
                                xiaban_rl.setBackgroundResource(R.drawable.daka_r_tuhuang);
                            }
                        });

                        ygTopView.setHight(dh, dw);
                        ygTopView.setBitmapHG();
                        if (isSR) {
                            ygTopView.setName(bean2.getName(), bean2.getDepartmentName(), true);
                        } else {
                            ygTopView.setName(bean2.getName(), bean2.getDepartmentName(), false);
                        }

                        try {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(bean2.getBitmap(), 0, bean2.getBitmap().length);
                            ygTopView.setBitmapTX(FileUtil.toRoundBitmap(bitmap));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final LinearLayout xiaoxi_ll = view_dk.findViewById(R.id.xiaoxi_ll);
                        view_dk.setX(dw);
                        hengLiebiao.addView(view_dk);

                        if (isDH) { //表示在执行动画
                            isDH = false;
                            utils.getValueAnimator().cancel();
                        }
                        //大于1个就
                        if (hengLiebiao.getChildCount() > 1) {

                            ValueAnimatorUtils utils = new ValueAnimatorUtils();
                            utils.setIntface(new ValueAnimatorIntface() {
                                @Override
                                public void end() {
                                    hengLiebiao.removeViewAt(0);

                                    final int si = guanHuaiList.size();
                                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ygTopView.getLayoutParams();
                                    layoutParams2.height = dh / 3;
                                    ygTopView.setLayoutParams(layoutParams2);
                                    ygTopView.invalidate();
                                    if (si > 0) {
                                        //有消息
                                        linearygwenzi.setVisibility(View.GONE);
                                        scrollView_03.setVisibility(View.VISIBLE);


                                    } else {
                                        //没消息
                                        //  Log.d("MainActivity101", "没消息");

                                        linearygwenzi.setVisibility(View.VISIBLE);
                                        scrollView_03.setVisibility(View.GONE);
                                    }

                                    //入场动画(从右往左)
                                    ValueAnimator anim = ValueAnimator.ofInt(dw, 0);
                                    anim.setDuration(900);
                                    anim.setRepeatMode(ValueAnimator.RESTART);
                                    Interpolator interpolator = new DecelerateInterpolator(2f);
                                    anim.setInterpolator(interpolator);
                                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            int currentValue = (Integer) animation.getAnimatedValue();
                                            view_dk.setX(currentValue);
                                            // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                                            view_dk.requestLayout();
                                        }
                                    });
                                    anim.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {


                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for (int i = 0; i < si; i++) {

                                                        final int finalI = i;
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                final View view_xiaoxi = View.inflate(MainActivity101.this, R.layout.xiaoxi_item, null);
                                                                ScreenAdapterTools.getInstance().loadView(view_xiaoxi);
                                                                RelativeLayout rl_xiaoxi = view_xiaoxi.findViewById(R.id.rl_xiaoxi);
                                                                TextView neirong = view_xiaoxi.findViewById(R.id.neirong);
                                                                TextView lingqu = view_xiaoxi.findViewById(R.id.lingqu);
                                                                TextView biaoti = view_xiaoxi.findViewById(R.id.biaoti);
                                                                ImageView xiaoxi_im = view_xiaoxi.findViewById(R.id.xiaoxi_im);
                                                                switch (guanHuaiList.get(finalI).getProjectileStatus()) {
                                                                    case "0":
                                                                        //小邮局
                                                                        xiaoxi_im.setBackgroundResource(R.drawable.youjian_bg);
                                                                        try {
                                                                            biaoti.setText("邮件");
                                                                            lingqu.setText(guanHuaiList.get(finalI).getNewsStatus());
                                                                            neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        break;
                                                                    case "1":
                                                                        // 生日提醒
                                                                        xiaoxi_im.setBackgroundResource(R.drawable.shengri_bg2);
                                                                        try {
                                                                            biaoti.setText("生日");
                                                                            neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                            lingqu.setText("");
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        break;
                                                                    case "2":
                                                                        //入职关怀
                                                                        xiaoxi_im.setBackgroundResource(R.drawable.guanhuai_bg);
                                                                        try {
                                                                            biaoti.setText("入职关怀");
                                                                            lingqu.setText("");
                                                                            neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        break;
                                                                    case "3":
                                                                        //节日关怀
                                                                        xiaoxi_im.setBackgroundResource(R.drawable.jieri_xx);
                                                                        try {
                                                                            biaoti.setText("节日关怀");
                                                                            lingqu.setText("");
                                                                            neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        break;
                                                                }

                                                                view_xiaoxi.setY(dh);
                                                                xiaoxi_ll.addView(view_xiaoxi);

                                                                RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) rl_xiaoxi.getLayoutParams();
                                                                layoutParams6.topMargin = 30;
                                                                layoutParams6.bottomMargin = 20;
                                                                layoutParams6.height = ((int) ((float) dh * 0.13)) - 10;
                                                                rl_xiaoxi.setLayoutParams(layoutParams6);
                                                                rl_xiaoxi.invalidate();

                                                                float sfff = 50 + ((float) dh * 0.13f);
                                                                ValueAnimator animator = ValueAnimator.ofFloat(dh, sfff * finalI);
                                                                //动画时长，让进度条在CountDown时间内正好从0-360走完，
                                                                animator.setDuration(1000);
                                                                animator.setInterpolator(new DecelerateInterpolator());//匀速
                                                                animator.setRepeatCount(0);//0表示不循环，-1表示无限循环
                                                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                    @Override
                                                                    public void onAnimationUpdate(ValueAnimator animation) {
                                                                        /**
                                                                         * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
                                                                         * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
                                                                         * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
                                                                         */
                                                                        float jiaodu = (float) animation.getAnimatedValue();
                                                                        view_xiaoxi.setY(jiaodu);

                                                                    }
                                                                });
                                                                animator.start();

                                                                scrollView_03.fullScroll(ScrollView.FOCUS_DOWN);
                                                            }
                                                        });
                                                        SystemClock.sleep(800);

                                                    }
                                                }
                                            }).start();


                                            //启动定时器或重置定时器
                                            if (task != null) {
                                                task.cancel();
                                                //timer.cancel();
                                                task = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Message message = new Message();
                                                        message.what = 999;
                                                        mHandler.sendMessage(message);
                                                    }
                                                };
                                                timer.schedule(task, 8000);
                                            } else {
                                                task = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Message message = new Message();
                                                        message.what = 999;
                                                        mHandler.sendMessage(message);
                                                    }
                                                };
                                                timer.schedule(task, 8000);
                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                    anim.start();
                                }

                                @Override
                                public void update(float value) {
                                    hengLiebiao.getChildAt(0).setX(value);
                                }

                                @Override
                                public void start() {


                                }
                            });
                            utils.animator(0, -dw, 900, 0, 0);
                        } else {


                            final int si = guanHuaiList.size();
                            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ygTopView.getLayoutParams();
                            layoutParams2.height = dh / 3;
                            ygTopView.setLayoutParams(layoutParams2);
                            ygTopView.invalidate();
                            if (si > 0) {
                                //有消息
                                linearygwenzi.setVisibility(View.GONE);
                                scrollView_03.setVisibility(View.VISIBLE);

                            } else {
                                //没消息
                                //  Log.d("MainActivity101", "没消息");
                                linearygwenzi.setVisibility(View.VISIBLE);
                                scrollView_03.setVisibility(View.GONE);
                            }

                            //入场动画(从右往左)
                            ValueAnimator anim = ValueAnimator.ofInt(dw, 0);
                            anim.setDuration(900);
                            anim.setRepeatMode(ValueAnimator.RESTART);
                            Interpolator interpolator = new DecelerateInterpolator(2f);
                            anim.setInterpolator(interpolator);
                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int currentValue = (Integer) animation.getAnimatedValue();
                                    view_dk.setX(currentValue);
                                    // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                                    view_dk.requestLayout();
                                }
                            });
                            anim.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < si; i++) {

                                                final int finalI = i;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final View view_xiaoxi = View.inflate(MainActivity101.this, R.layout.xiaoxi_item, null);
                                                        ScreenAdapterTools.getInstance().loadView(view_xiaoxi);
                                                        RelativeLayout rl_xiaoxi = view_xiaoxi.findViewById(R.id.rl_xiaoxi);
                                                        TextView neirong = view_xiaoxi.findViewById(R.id.neirong);
                                                        TextView lingqu = view_xiaoxi.findViewById(R.id.lingqu);
                                                        TextView biaoti = view_xiaoxi.findViewById(R.id.biaoti);
                                                        ImageView xiaoxi_im = view_xiaoxi.findViewById(R.id.xiaoxi_im);
                                                        switch (guanHuaiList.get(finalI).getProjectileStatus()) {
                                                            case "0":
                                                                //小邮局
                                                                xiaoxi_im.setBackgroundResource(R.drawable.youjian_bg);
                                                                try {
                                                                    biaoti.setText("邮件");
                                                                    lingqu.setText(guanHuaiList.get(finalI).getNewsStatus());
                                                                    neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                break;
                                                            case "1":
                                                                // 生日提醒
                                                                xiaoxi_im.setBackgroundResource(R.drawable.shengri_bg2);
                                                                try {
                                                                    biaoti.setText("生日");
                                                                    neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                    lingqu.setText("");
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                break;
                                                            case "2":
                                                                //入职关怀
                                                                xiaoxi_im.setBackgroundResource(R.drawable.guanhuai_bg);
                                                                try {
                                                                    biaoti.setText("入职关怀");
                                                                    lingqu.setText("");
                                                                    neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                break;
                                                            case "3":
                                                                //节日关怀
                                                                xiaoxi_im.setBackgroundResource(R.drawable.jieri_xx);
                                                                try {
                                                                    biaoti.setText("节日关怀");
                                                                    lingqu.setText("");
                                                                    neirong.setText(guanHuaiList.get(finalI).getMarkedWords());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                break;
                                                        }

                                                        view_xiaoxi.setY(dh);
                                                        xiaoxi_ll.addView(view_xiaoxi);

                                                        RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) rl_xiaoxi.getLayoutParams();
                                                        layoutParams6.topMargin = 30;
                                                        layoutParams6.bottomMargin = 20;
                                                        layoutParams6.height = ((int) ((float) dh * 0.13)) - 10;
                                                        rl_xiaoxi.setLayoutParams(layoutParams6);
                                                        rl_xiaoxi.invalidate();

                                                        float sfff = 50 + ((float) dh * 0.13f);

                                                        ValueAnimator animator = ValueAnimator.ofFloat(dh, sfff * finalI);
                                                        //动画时长，让进度条在CountDown时间内正好从0-360走完，
                                                        animator.setDuration(1000);
                                                        animator.setInterpolator(new DecelerateInterpolator());//匀速
                                                        animator.setRepeatCount(0);//0表示不循环，-1表示无限循环
                                                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                            @Override
                                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                                /**
                                                                 * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
                                                                 * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
                                                                 * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
                                                                 */
                                                                float jiaodu = (float) animation.getAnimatedValue();
                                                                view_xiaoxi.setY(jiaodu);

                                                            }
                                                        });
                                                        animator.start();

                                                        scrollView_03.fullScroll(ScrollView.FOCUS_DOWN);
                                                    }
                                                });
                                                SystemClock.sleep(800);

                                            }
                                        }
                                    }).start();


                                    //启动定时器或重置定时器
                                    if (task != null) {
                                        task.cancel();
                                        //timer.cancel();
                                        task = new TimerTask() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = 999;
                                                mHandler.sendMessage(message);
                                            }
                                        };
                                        timer.schedule(task, 12000);
                                    } else {
                                        task = new TimerTask() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = 999;
                                                mHandler.sendMessage(message);
                                            }
                                        };
                                        timer.schedule(task, 12000);
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            anim.start();

                        }

//                        //动画
//                        SpringSystem springSystem3 = SpringSystem.create();
//                        final Spring spring3 = springSystem3.createSpring();
//                        //两个参数分别是弹力系数和阻力系数
//                        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
//                        // 添加弹簧监听器
//                        spring3.addListener(new SimpleSpringListener() {
//                            @Override
//                            public void onSpringUpdate(Spring spring) {
//                                // value是一个符合弹力变化的一个数，我们根据value可以做出弹簧动画
//                                float value = (float) spring.getCurrentValue();
//                                //  Log.d("kkkk", "value:" + value);
//                                //基于Y轴的弹簧阻尼动画
//                                //	helper.itemView.setTranslationY(value);
//                                // 对图片的伸缩动画
//                                //float scale = 1f - (value * 0.5f);
//                                view_dk.setScaleX(value);
//                                view_dk.setScaleY(value);
//                                if (value == 1 && isNet) {
//
//
//                                }
//                            }
//                        });
//                        // 设置动画结束值
//                        spring3.setEndValue(1f);

                        break;
                    }
                    case 666: {
                        //普通访客

                        if (shengRiThierd != null) {
                            shengRiThierd.interrupt();
                            shengRiThierd = null;
                        }
                        if (fangkeThired != null) {
                            fangkeThired.interrupt();
                            fangkeThired = null;
                        }
                        if (vipThired != null) {
                            vipThired.interrupt();
                            vipThired = null;
                        }

                        fangkeThired = new FangkeThired();
                        fangkeThired.start();

                        final Subject bean2 = (Subject) msg.obj;
                        boolean isRT = false;
                        synchronized (MainActivity101.this) {
                            int sizes = hengLiebiao.getFlexItemCount();
                            for (int i = 0; i < sizes; i++) {
                                if ((bean2.getId() + "").equals(hengLiebiao.getReorderedFlexItemAt(i).getTag().toString())) {
                                    isRT = true;
                                    break;
                                }
                            }
                        }
                        if (isRT)
                            break;


                        final View view_dk = View.inflate(MainActivity101.this, R.layout.fangke_item_101, null);
                        final FKTopView_101 ygTopView = view_dk.findViewById(R.id.ygtopview);
                        final RelativeLayout root_rl = view_dk.findViewById(R.id.root_rl);
                        LottieAnimationView wangluo = view_dk.findViewById(R.id.wangluo);
                        wangluo.setPerformanceTrackingEnabled(true);
                        wangluo.setSpeed(0.6f);
                        final ScrollView scrollView_03 = view_dk.findViewById(R.id.scrollview_03);
                        ygTopView.setHight((int) ((float) dw * 0.28f), (int) ((float) dh * 0.70f));
                        ygTopView.setBitmapHG();
                        ygTopView.setName(bean2.getName(), bean2.getDepartmentName(), false);


                        try {
                            if (bean2.getDisplayPhoto() != null) {
                                ygTopView.setBitmapTX(BitmapFactory.decodeFile(bean2.getDisplayPhoto()));
                            } else {
                                Bitmap bitmap = mFacePassHandler.getFaceImage(bean2.getTeZhengMa().getBytes());
                                ygTopView.setBitmapTX(bitmap);
                                //    Bitmap bitmap=BitmapFactory.decodeByteArray(bean2.getBitmap(),0,bean2.getBitmap().length);
                                //  ygTopView.setBitmapTX(FileUtil.toRoundBitmap(bitmap));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        //动画
                        SpringSystem springSystem3 = SpringSystem.create();
                        final Spring spring3 = springSystem3.createSpring();
                        //两个参数分别是弹力系数和阻力系数
                        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 6));
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
                                view_dk.setScaleX(value);
                                view_dk.setScaleY(value);

                            }
                        });
                        // 设置动画结束值
                        spring3.setEndValue(1f);
                        view_dk.setTag(bean2.getId() + "");
                        hengLiebiao.addView(view_dk);


                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ygTopView.getLayoutParams();
                        layoutParams2.width = (int) ((float) dw * 0.28f);
                        layoutParams2.height = (int) ((float) dh * 0.70f) - 20;
                        layoutParams2.topMargin = 10;
                        ygTopView.setLayoutParams(layoutParams2);
                        ygTopView.invalidate();

                        //中间打勾动画的高宽
                        RelativeLayout.LayoutParams wangluop = (RelativeLayout.LayoutParams) wangluo.getLayoutParams();
                        wangluop.topMargin = (int) (((float) dh * 0.70f) / 2 + ((float) dw * 0.01f) / 2);
                        wangluop.leftMargin = (int) (((float) dw * 0.28f) / 2 - ((float) dw * 0.04f) / 2);
                        wangluop.width = (int) ((float) dw * 0.04f);
                        wangluop.height = (int) ((float) dw * 0.04f);
                        wangluo.setLayoutParams(wangluop);
                        wangluo.invalidate();

                        //没消息
                        FlexboxLayout.LayoutParams rlLayoutParams = (FlexboxLayout.LayoutParams) root_rl.getLayoutParams();
                        rlLayoutParams.width = (int) ((float) dw * 0.28f);
                        rlLayoutParams.leftMargin = (int) ((float) dw * 0.02f);
                        rlLayoutParams.rightMargin = (int) ((float) dw * 0.02f);
                        root_rl.setLayoutParams(rlLayoutParams);
                        root_rl.invalidate();
                        scrollView_03.setVisibility(View.GONE);
                        boolean sskk = false;
                        for (int i = 0; i < hengLiebiao.getFlexItemCount(); i++) {
                            if (!hengLiebiao.getChildAt(i).isEnabled()) {
                                sskk = true;
                                break;
                            }
                        }
                        if (sskk) {
                            while (true) {
                                if (hengLiebiao.getFlexItemCount() > 2) {
                                    hengLiebiao.removeViewAt(0);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            while (true) {
                                if (hengLiebiao.getFlexItemCount() > 3) {
                                    hengLiebiao.removeViewAt(0);
                                } else {
                                    break;
                                }
                            }
                        }


                        //消失
                        Message message = Message.obtain();
                        message.obj = bean2.getId() + "";
                        message.what = 999;
                        mHandler.sendMessageDelayed(message, 8000);

                        break;
                    }
                    case 999: {
                        // Log.d("MainActivity101", "999");

                        final String tag = (String) msg.obj;
                        for (int i = 0; i < hengLiebiao.getFlexItemCount(); i++) {
                            if (hengLiebiao.getReorderedFlexItemAt(i).getTag().toString().equals(tag)) {
                                final View vieww = hengLiebiao.findViewWithTag(tag);
                                ValueAnimatorUtils utils = new ValueAnimatorUtils();
                                utils.setIntface(new ValueAnimatorIntface() {
                                    @Override
                                    public void end() {
                                        synchronized (MainActivity101.this) {
                                            hengLiebiao.removeView(vieww);
                                        }
                                    }

                                    @Override
                                    public void update(float value) {
                                        try {
                                            vieww.setX(value);
                                        } catch (Exception e) {
                                            Log.d("MainActivity101", e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void start() {
                                        // boxfargment.setVisibility(View.GONE);
                                    }
                                });
                                utils.animator(vieww.getLeft(), -vieww.getWidth(), 400, 0, 0);
                            }

                        }


                        break;
                    }
                    case -100: {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(MainActivity101.this).clearDiskCache();
                            }
                        }).start();
                        //陌生人
                        final MSRBean bean2 = (MSRBean) msg.obj;

                        final View view_dk = View.inflate(MainActivity101.this, R.layout.moshengren_item_jj, null);
                        ScreenAdapterTools.getInstance().loadView(view_dk);
                        TextView name2 = (TextView) view_dk.findViewById(R.id.name);
                        ImageView touxiang2 = (ImageView) view_dk.findViewById(R.id.touxiang);
                        TextView xingbie = (TextView) view_dk.findViewById(R.id.xingbie);
                        TextView nianling = (TextView) view_dk.findViewById(R.id.nianling);
                        name2.setText("您好陌生人");
                        xingbie.setText("性别:" + bean2.getSex());
                        nianling.setText("年龄:" + bean2.getAge());

                        Glide.get(MainActivity101.this).clearMemory();
                        Glide.with(MainActivity101.this)
                                .load(bean2.getBitmap())
                                .apply(myOptions2)
                                .into(touxiang2);

                        hengLiebiao.addView(view_dk);

                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) touxiang2.getLayoutParams();
                        layoutParams2.width = dw / 5;
                        layoutParams2.height = dw / 4;
                        touxiang2.setLayoutParams(layoutParams2);
                        touxiang2.invalidate();

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
                                view_dk.setScaleX(value);
                                view_dk.setScaleY(value);
                                if (value == 1) {


                                    final View view_dk = View.inflate(MainActivity101.this, R.layout.shulianbiao_jj, null);
                                    ScreenAdapterTools.getInstance().loadView(view_dk);
                                    TextView name = view_dk.findViewById(R.id.name);
                                    ImageView touxiang = view_dk.findViewById(R.id.touxiang);
                                    name.setText("陌生人");

                                    Glide.with(MainActivity101.this)
                                            .load(bean2.getBitmap())
                                            .apply(GlideUtils.getRequestOptions())
                                            .into(touxiang);

                                    //         shuLiebiao.addView(view_dk, 0);

                                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) touxiang.getLayoutParams();
                                    layoutParams2.width = dw / 9;
                                    layoutParams2.topMargin = 30;
                                    layoutParams2.height = dw / 9;
                                    touxiang.setLayoutParams(layoutParams2);
                                    touxiang.invalidate();
                                    //消失
                                    Message message = Message.obtain();
                                    message.what = 999;
                                    mHandler.sendMessageDelayed(message, 9000);

                                }
                            }
                        });
                        // 设置动画结束值
                        spring3.setEndValue(1f);
                        break;
                    }
                    case 123: {
                        //生日粒子动画
                        Collections.shuffle(topZuoBiao);
                        for (int i = 0; i < 9; i++) {
                            int min = 0;
                            int max = 8;
                            Random random = new Random();
                            int num = random.nextInt(max) % (max - min + 1) + min;
                            new ParticleSystem(MainActivity101.this, 100, topIm[i], 3000)
                                    .setSpeedModuleAndAngleRange(0.05f, 0.2f, 45, 135)
                                    .setRotationSpeed(30)
                                    .setFadeOut(1000, new LinearInterpolator())
                                    .setAcceleration(0.0001f, 90)
                                    .emit(topZuoBiao.get(num), 0, 1, 100);
                        }

                        break;
                    }
                    case 122: {
                        //vip粒子动画
                        List<Bitmap> tempLists = ((List<Bitmap>) msg.obj);
                        if (tempLists == null)
                            break;
                        int size = tempLists.size();
                        for (int i = 0; i < size; i++) {
                            int min = 0;
                            int max = 5;
                            Random random = new Random();
                            int num = random.nextInt(max) % (max - min + 1) + min;
                            new ParticleSystem(MainActivity101.this, 100, tempLists.get(i), 6000)
                                    .setSpeedModuleAndAngleRange(0.08f, 0.16f, 250, 290)
                                    .setRotationSpeed(0)
                                    .setFadeOut(1000, new LinearInterpolator())
                                    .setAcceleration(0.000004f, 270)
                                    .emit(bootomZuoBiao.get(num), dh + 50, 1, 100);
                        }
                        break;
                    }
                    case 121: {
                        //普通访客粒子动画
                        for (int i = 0; i < 5; i++) {
                            int min = 0;
                            int max = 5;
                            Random random = new Random();
                            int num = random.nextInt(max) % (max - min + 1) + min;

                            int min2 = 0;
                            int max2 = 4;
                            Random random2 = new Random();
                            int num2 = random2.nextInt(max2) % (max2 - min2 + 1) + min2;

                            new ParticleSystem(MainActivity101.this, 100, qqIm[num2], 7000)
                                    .setSpeedModuleAndAngleRange(0.08f, 0.18f, 250, 290)
                                    .setRotationSpeed(0)
                                    .setFadeOut(1000, new LinearInterpolator())
                                    .setAcceleration(0.000004f, 270)
                                    .emit(bootomZuoBiao.get(num), dh + 260, 1, 100);
                        }

                        break;
                    }

                    case 900:{

                        rootLayout.setVisibility(View.GONE);

                        break;
                    }
                }
                return false;
            }
        });

        isSC = true;

    }


    @Override
    protected void onResume() {
        initToast();
        /* 打开相机 */
        manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);

        query = subjectBox.query().equal(Subject_.peopleType, "员工").build();
        adaptFrameLayout();
        super.onResume();


    }


    /* 相机回调函数 */
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
                    FacePassDetectionResult detectionResult = null;
                    detectionResult = mFacePassHandler.feedFrame(image);

                    if (detectionResult != null && detectionResult.faceList.length > 0) {
                        showFacePassFace(detectionResult.faceList, image);
                    }
                    /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                    if (detectionResult != null && detectionResult.message.length != 0) {
                        mDetectResultQueue.offer(detectionResult);
                        // Log.d(DEBUG_TAG, "1 mDetectResultQueue.size = " + mDetectResultQueue.size());
                    }
                    //     }

                } catch (InterruptedException | FacePassException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isIterrupt = true;
            super.interrupt();
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
                                //  getFaceImageByFaceToken(result.trackId, faceToken);
                                Log.d("RecognizeThread", "识别了");
                                //  Log.d("RecognizeThread", subjectBox.getAll().get(0).toString());
                                Subject subject = subjectBox.query().equal(Subject_.teZhengMa, new String(result.faceToken)).build().findUnique();
                                Log.d("RecognizeThread", "subject:" + subject);

                                if (subject != null) {
                                    linkedBlockingQueue.offer(subject);
                                    link_shangchuanjilu(subject);
                                }


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
                                    isLink = true;
                                    //   Log.d("RecognizeThread", "入库"+tID);
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


    private class TanChuangThread extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {
                    //有动画 ，延迟到一秒一次

                    Subject subject = linkedBlockingQueue.take();

                    if (subject.getPeopleType() != null) {
                        switch (subject.getPeopleType()) {
                            case "员工":

                                //普通打卡
                                Message messagey = Message.obtain();
                                messagey.what = 444;
                                messagey.obj = subject;
                                mHandler.sendMessage(messagey);

                                break;

                            case "普通访客": {
                                //普通访客
                                Message message2 = Message.obtain();
                                message2.what = 666;
                                message2.obj = subject;
                                mHandler.sendMessage(message2);

                                break;
                            }
                            case "白名单":
                                //vip

                                Message message2 = Message.obtain();
                                message2.what = 111;
                                message2.obj = subject;
                                mHandler.sendMessage(message2);

                                break;
                            case "陌生人":
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.get(MainActivity101.this).clearDiskCache();
                                    }
                                }).start();
                                Message message = Message.obtain();
                                message.obj = subject;
                                message.what = -100;
                                mHandler.sendMessage(message);

                                break;
                            case "黑名单":

                                break;
                            default:
                                EventBus.getDefault().post("没有对应身份类型,无法弹窗");

                        }
                    }

                    SystemClock.sleep(2100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isRing = true;
            super.interrupt();
        }
    }

    @Override
    protected void onPause() {
        synchronized (MainActivity101.this) {
            hengLiebiao.removeAllViews();
        }
        super.onPause();
    }



    private void adaptFrameLayout() {
        SettingVar.isButtonInvisible = false;
        SettingVar.iscameraNeedConfig = false;
    }

    private void initToast() {
        SettingVar.isButtonInvisible = false;
    }

    private int shibai;

    private void initView() {
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
//        Log.i(DEBUG_TAG, "cameraRation: " + cameraRotation);
        cameraFacingFront = true;
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

        //  Log.i("orientation", String.valueOf(windowRotation));
        final int mCurrentOrientation = getResources().getConfiguration().orientation;
        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }

        setContentView(R.layout.activity_main101);
        //  ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);
        AssetManager mgr = getAssets();
        //Univers LT 57 Condensed
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
        Typeface tf2 = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
        Typeface tf3 = Typeface.createFromAsset(mgr, "fonts/kai.ttf");
        // String riqi2 = DateUtils.timesTwo(System.currentTimeMillis() + "") + "   " + DateUtils.getWeek(System.currentTimeMillis());
        //  riqi.setTypeface(tf);
        //    riqi.setText(riqi2);
        xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
        riqi.setText(DateUtils.timesTwodian(System.currentTimeMillis() + ""));
        xiaoshi.setTypeface(tf);
        xiaoshi.setText(DateUtils.timeMinute(System.currentTimeMillis() + ""));
        // TableLayout mHudView = findViewById(R.id.hud_view);
        // shipingView = findViewById(R.id.ijkplayview);
        // shipingView.setVisibility(View.GONE);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                //只需要获取一次高度，获取后移除监听器
                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                rootLayout.setVisibility(View.GONE);
            }

        });


        //背景
        daBg.setBackgroundResource(R.color.dabg);
        //  scrollView.setSmoothScrollingEnabled(true);
        ceshi = findViewById(R.id.ceshi);
        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity101.this, SheZhiActivity.class));
                finish();

//                if (usbPath != null) {
//
//                    ToastUtils.getInstances().showDialog("获取图片", "获取图片", 0);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<String> strings = new ArrayList<>();
//                            FileUtil.getAllFiles(usbPath + File.separator + "入库照片2", strings);
//                            FacePassHandler facePassHandler = MyApplication.myApplication.getFacePassHandler();
//
//                            int size = strings.size();
//                            for (int i = 0; i < size; i++) {
//                                try {
//                                    String sp = strings.get(i);
//                                    Log.d("SheZhiActivity", sp);
//                                    Log.d("SheZhiActivity", "i" + i);
//                                    FacePassAddFaceResult result = facePassHandler.addFace(BitmapFactory.decodeFile(sp));
//                                    if (result.result == 0) {
//                                        facePassHandler.bindGroup(group_name, result.faceToken);
//                                        Subject subject = new Subject();
//                                        int oo = sp.length();
//                                        subject.setName(sp.substring(oo - 6, oo - 1));
//                                        subject.setTeZhengMa(new String(result.faceToken));
//                                        subject.setPeopleType("员工");
//                                        subject.setId(System.currentTimeMillis());
//                                        subjectBox.put(subject);
//
//                                    } else {
//                                        shibai++;
//                                    }
//
//                                    ToastUtils.getInstances().showDialog("入库中", "失败了:" + shibai, (i / size) * 100);
//                                } catch (FacePassException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }
//
//                    }).start();
//                }
            }
        });

        // marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        //   List<String> info = new ArrayList<>();
        //  info.add("大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦" +
        //       "大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦大家好我是孙福生哦哦");
        //70个字
        // info.add("明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会" +
        //        "明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会明天上午12点开会");
        //marqueeView.startWithList(info);
        // 在代码里设置自己的动画
        //  marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
//        shipingView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                long curTime = System.currentTimeMillis();
//                long durTime = curTime - mLastClickTime;
//                mLastClickTime = curTime;
//                if (durTime < CLICK_INTERVAL) {
//                    ++mSecretNumber;
//                    if (mSecretNumber == 3) {
//                        dialog=new XiuGaiGaoKuanDialog(MainActivity.this,MainActivity.this);
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setContents("修改视频的宽高",(shipingView.getRight()-shipingView.getLeft())+"",(shipingView.getBottom()-shipingView.getTop())+"",2);
//                        dialog.setOnQueRenListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show();
//                    }
//                } else {
//                    mSecretNumber = 0;
//                }
//            }
//        });
//
//

//        shipingView.setHudView(mHudView); //http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
//        shipingView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "laowang.mp4");
//        // shipingView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//        shipingView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                //  shipingView.start();
//            }
//        });

//        shipingView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                TastyToast.makeText(MainActivity201.this, "播放视频失败", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
//                return false;
//            }
//        });

        //  shipingView.start();


//        dbg_view=findViewById(R.id.dabg);
//        dbg_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long curTime = System.currentTimeMillis();
//                long durTime = curTime - mLastClickTime;
//                mLastClickTime = curTime;
//                if (durTime < CLICK_INTERVAL) {
//                    ++mSecretNumber;
//                    if (mSecretNumber == 3) {
//                        dialog=new XiuGaiGaoKuanDialog(MainActivity.this,MainActivity.this);
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setContents("修改背景宽高",(dbg_view.getRight()-dbg_view.getLeft())+"",(dbg_view.getBottom()-dbg_view.getTop())+"",1);
//                        dialog.setOnQueRenListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show();
//                    }
//                } else {
//                    mSecretNumber = 0;
//                }
//            }
//        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;

        /* 初始化界面 */
        //  faceView = (FaceView) this.findViewById(R.id.fcview);
        SettingVar.cameraSettingOk = false;
        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
        /* 注册相机回调函数 */
        manager.setListener(this);
        if (todayBean != null) {
            //更新天气界面
            //    wendu.setTypeface(tf2);
            //  tianqi.setTypeface(tf2);
            //  fengli.setTypeface(tf2);
            //  ziwaixian.setTypeface(tf2);
            //  shidu.setTypeface(tf2);
            //  jianyi.setTypeface(tf3);
            shidu.setText(todayBean.getHumidity());
            xingqi2.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L));
            xingqi3.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L + 86400000L));
            wendu.setText(todayBean.getTemperature().replace("℃~", "/"));
            wendu2.setText(todayBean.getTianqi2_wendu().replace("℃~", "/"));
            wendu3.setText(todayBean.getTianqi3_wendu().replace("℃~", "/"));

            if (todayBean.getWeather().contains("晴")) {
                tianqiIm.setBackgroundResource(R.drawable.qing);
            } else if (todayBean.getWeather().contains("雨")) {
                tianqiIm.setBackgroundResource(R.drawable.xiayu);
            } else if (todayBean.getWeather().contains("多云")) {
                tianqiIm.setBackgroundResource(R.drawable.duoyun);
            } else if (todayBean.getWeather().contains("阴")) {
                tianqiIm.setBackgroundResource(R.drawable.yintian);
            }

            if (todayBean.getTianqi2().contains("晴")) {
                tianqiIm2.setBackgroundResource(R.drawable.qing);
            } else if (todayBean.getTianqi2().contains("雨")) {
                tianqiIm2.setBackgroundResource(R.drawable.xiayu);
            } else if (todayBean.getTianqi2().contains("多云")) {
                tianqiIm2.setBackgroundResource(R.drawable.duoyun);
            } else if (todayBean.getTianqi2().contains("阴")) {
                tianqiIm2.setBackgroundResource(R.drawable.yintian);
            }


            if (todayBean.getTianqi3().contains("晴")) {
                tianqiIm3.setBackgroundResource(R.drawable.qing);
            } else if (todayBean.getTianqi3().contains("雨")) {
                tianqiIm3.setBackgroundResource(R.drawable.xiayu);
            } else if (todayBean.getTianqi3().contains("多云")) {
                tianqiIm3.setBackgroundResource(R.drawable.duoyun);
            } else if (todayBean.getTianqi3().contains("阴")) {
                tianqiIm3.setBackgroundResource(R.drawable.yintian);
            }


        }

        RelativeLayout.LayoutParams ppp0 = (RelativeLayout.LayoutParams) zidongtext.getLayoutParams();
        ppp0.width = (int) ((float) dw * 0.7f);
        ppp0.height = (int) ((float) dw * 0.21f);
        ppp0.topMargin = -10;
        zidongtext.setLayoutParams(ppp0);
        zidongtext.invalidate();
     //   Log.d("MainActivity101", baoCunBean.getWenzi1() + "jjjjj");
        if (baoCunBean.getWenzi1() != null)
            zidongtext.setText(baoCunBean.getWenzi1());
        if (baoCunBean.getTouxiangzhuji() != null)
            daBg.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getTouxiangzhuji()));
        if (baoCunBean.getXiaBanTime() != null) {
            logo.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getXiaBanTime()));
        }


        RelativeLayout.LayoutParams ppp2 = (RelativeLayout.LayoutParams) rootLayout.getLayoutParams();
        ppp2.width = (int) ((float) dw * 0.88f);
        ppp2.height = (int) ((float) dh * 0.5f);
        ppp2.leftMargin = (int) ((float) dw * 0.06f);
        ppp2.bottomMargin = (int) ((float) dh * 0.08f);
        rootLayout.setLayoutParams(ppp2);
        rootLayout.invalidate();

        RelativeLayout.LayoutParams hengLiebiaoLayoutParams = (RelativeLayout.LayoutParams) hengLiebiao.getLayoutParams();
        hengLiebiaoLayoutParams.height = (int) ((float) dh * 0.70f);
        hengLiebiao.setLayoutParams(hengLiebiaoLayoutParams);
        hengLiebiao.invalidate();


        RelativeLayout.LayoutParams tianqiP = (RelativeLayout.LayoutParams) tianqiIm.getLayoutParams();
        tianqiP.width = (int) ((float) dh * 0.08f);
        tianqiP.height = (int) ((float) dh * 0.08f);
        tianqiIm.setLayoutParams(tianqiP);
        tianqiIm.invalidate();

        RelativeLayout.LayoutParams tianqiP2 = (RelativeLayout.LayoutParams) tianqiIm2.getLayoutParams();
        tianqiP2.width = (int) ((float) dh * 0.05f);
        tianqiP2.height = (int) ((float) dh * 0.05f);
        tianqiIm2.setLayoutParams(tianqiP2);
        tianqiIm2.invalidate();

        RelativeLayout.LayoutParams tianqiP3 = (RelativeLayout.LayoutParams) tianqiIm3.getLayoutParams();
        tianqiP3.width = (int) ((float) dh * 0.05f);
        tianqiP3.height = (int) ((float) dh * 0.05f);
        tianqiIm3.setLayoutParams(tianqiP3);
        tianqiIm3.invalidate();

        RelativeLayout.LayoutParams logo2 = (RelativeLayout.LayoutParams) logo.getLayoutParams();
        logo2.leftMargin = (int) ((float) dw * 0.15f);
        logo2.topMargin = (int) ((float) dh * 0.05f);
        logo2.width = (int) ((float) dh * 0.1f);
        logo2.height = (int) ((float) dh * 0.1f) - 10;
        logo.setLayoutParams(logo2);
        logo.invalidate();

        RelativeLayout.LayoutParams zdp = (RelativeLayout.LayoutParams) zidongtext.getLayoutParams();
        zdp.topMargin = (int) ((float) dh * 0.05f);
        zdp.height = (int) ((float) dh * 0.08f);
        zdp.width = (int) ((float) dh * 0.2f);
        zidongtext.setLayoutParams(zdp);
        zidongtext.invalidate();


    }


    //修改监听
    @Override
    public void setKG(final int k, final int g, int type) {
        switch (type) {
            case 1:
                //大背景的
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //     RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dbg_view.getLayoutParams();
//                                int[] location = new  int[2] ;
//                                dbg_view.getLocationOnScreen(location);
                        //   int L = dbg_view.getLeft();
                        //  int R = dbg_view.getRight();
                        //  int T = dbg_view.getTop();
                        //  int B = dbg_view.getBottom();
                        //   params.topMargin = T;
                        //   params.leftMargin = L;
                        //   params.height = ((B - T) + g) < 0 ? 0 : (B - T) + g;
                        //  params.width = ((R - L) + k) < 0 ? 0 : (R - L) + k;
                        //  dbg_view.setLayoutParams(params);//将设置好的布局参数应用到控件中
                        //  dialog.setContents("修改背景宽高", (dbg_view.getRight() - dbg_view.getLeft()) + "", (dbg_view.getBottom() - dbg_view.getTop()) + "", 1);

                    }
                });


                break;
            case 2:
                //视频的
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shipingView.getLayoutParams();
////                                int[] location = new  int[2] ;
////                                dbg_view.getLocationOnScreen(location);
//                        int L = shipingView.getLeft();
//                        int R = shipingView.getRight();
//                        int T = shipingView.getTop();
//                        int B = shipingView.getBottom();
//                        params.topMargin = T;
//                        params.leftMargin = L;
//                        params.height = ((B - T) + g) < 0 ? 0 : (B - T) + g;
//                        params.width = ((R - L) + k) < 0 ? 0 : (R - L) + k;
//                        shipingView.setLayoutParams(params);//将设置好的布局参数应用到控件中
//                        dialog.setContents("修改视频宽高", (shipingView.getRight() - shipingView.getLeft()) + "", (shipingView.getBottom() - shipingView.getTop()) + "", 2);
//
//                    }
//                });

                break;
            case 3:

                break;
            case 4:

                break;
        }

    }


    @Override
    protected void onStop() {

        SettingVar.isButtonInvisible = false;
        //shuLiebiao.removeAllViews();
        hengLiebiao.removeAllViews();
        mToastBlockQueue.clear();
        mDetectResultQueue.clear();
        mFeedFrameQueue.clear();
        linkedBlockingQueue.clear();
        if (manager != null) {
            manager.release();
        }
        // marqueeView.stopFlipping();

        super.onStop();
    }

    @Override
    protected void onRestart() {
        //faceView.clear();
        // faceView.invalidate();
        //  if (shipingView!=null)
        // shipingView.start();


        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();

        //marqueeView.startFlipping();
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();

        if (mToastBlockQueue != null) {
            mToastBlockQueue.clear();
        }
        if (linkedBlockingQueue != null) {
            linkedBlockingQueue.clear();
        }
        if (mFeedFrameQueue != null) {
            mFeedFrameQueue.clear();
        }
        if (mFeedFrameThread != null) {
            mFeedFrameThread.isIterrupt = true;
            mFeedFrameThread.interrupt();
        }

        if (tanChuangThread != null) {
            tanChuangThread.isRing = true;
            tanChuangThread.interrupt();
        }

        if (mRecognizeThread != null) {
            mRecognizeThread.isInterrupt = true;
            mRecognizeThread.interrupt();
        }

        //  shipingView.release(true);
        unregisterReceiver(timeChangeReceiver);
        unregisterReceiver(netWorkStateReceiver);
        EventBus.getDefault().unregister(this);//解除订阅
        if (manager != null) {
            manager.release();
        }
        if (mFacePassHandler != null) {
            mFacePassHandler.release();
        }
        if (synthesizer != null)
            synthesizer.release();


        super.onDestroy();
    }


    private void showFacePassFace(FacePassFace[] detectResult, final FacePassImage image) {

        for (FacePassFace face : detectResult) {
            boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
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
            RectF srect2 = new RectF(face.rect.left - 40 < 0 ? 0 : face.rect.left - 40, face.rect.top - 100 < 0 ? 0 : face.rect.top - 100,
                    face.rect.right + 40 > image.width ? image.width : face.rect.right + 40, face.rect.bottom + 100 > image.height ? image.height : face.rect.bottom + 100);


            float pitch = face.pose.pitch;
            float roll = face.pose.roll;
            float yaw = face.pose.yaw;
            //  Log.d("MainActivity101", "pitch:" + pitch);
            //  Log.d("MainActivity101", "roll:" + roll);
            //  Log.d("MainActivity101", "yaw:" + yaw);
            if (pitch < 25 && pitch > -25 && roll < 25 && roll > -25 && yaw < 25 && yaw > -25 && face.blur < 0.4) {
                try {
                    //  Log.d("MainActivity101", "tID:" + tID);
                    //  Log.d("MainActivity101", "face.trackId:" + face.trackId);
                    //   Log.d("MainActivity101", "isLink:" + isLink);
                    if (tID == face.trackId && isLink) {  //入库成功后将 tID=-1;
                        Log.d("MainActivity101", "进来");
                        isLink = false;
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

                        Subject b = new Subject();

                        b.setPeopleType("员工");
                        //  b.setAge(face.age);

                        String sex = "";
//                        switch (face.gender) {
//                            case 0:
//                                sex = "男";
//                                b.setName("先生");
//                                break;
//                            case 1:
//                                sex = "女";
//                                b.setName("女士");
//                                break;
//                            default:
//                                sex = "未知";
//                        }

                        //  b.setDepartmentName(bumenString.get((int) (Math.random()*5)));
                        b.setSex(sex);
                        b.setId(System.currentTimeMillis());
                        b.setBitmap(bitmabToBytes2(bitmap));

                        b.setPeopleType("陌生人");
                        // linkedBlockingQueue.offer(b);
                        // Log.d("MainActivity101", "陌生人入队列");


                    }


                } catch (Exception ex) {
                    isLink = true;
                    Log.e("Sys", "Error:" + ex.getMessage());
                }

            }
        }


    }

    private static final int REQUEST_CODE_CHOOSE_PICK = 1;


    //图片转为二进制数据
    public byte[] bitmabToBytes2(Bitmap bitmap) {
        //将图片转化为位图
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            return baos.toByteArray();
        } catch (Exception ignored) {
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //从相册选取照片后读取地址
            case REQUEST_CODE_CHOOSE_PICK:
                if (resultCode == RESULT_OK) {
                    String path = "";
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    if (cursor != null) {
                        cursor.moveToFirst();
                        path = cursor.getString(cursor.getColumnIndex(pojo[0]));
                    }
                    if (!TextUtils.isEmpty(path) && "file".equalsIgnoreCase(uri.getScheme())) {
                        path = uri.getPath();
                    }
                    if (TextUtils.isEmpty(path)) {
                        try {
                            path = FileUtil.getPath(getApplicationContext(), uri);
                        } catch (Exception e) {
                        }
                    }
                    if (TextUtils.isEmpty(path)) {
                        toast("图片选取失败！");
                        return;
                    }

                }
                break;
        }
    }


    private void toast(String msg) {
        Toast.makeText(MainActivity101.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                startActivity(new Intent(MainActivity101.this, SheZhiActivity.class));
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }





    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //  Log.d("MainActivity", "ev.getPointerCount()1:" + ev.getPointerCount());
        //   Log.d("MainActivity", "ev.getAction()1:" + ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isAnXia = true;
        }
        if (isAnXia) {
            if (ev.getPointerCount() == 4) {
                isAnXia = false;
                startActivity(new Intent(MainActivity101.this, SheZhiActivity.class));
                finish();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        // 设置初始化参数
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler); // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程

    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        params.put(SpeechSynthesizer.PARAM_SPEAKER, baoCunBean.getBoyingren() + ""); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_VOLUME, "8"); // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, baoCunBean.getYusu() + "");// 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, baoCunBean.getYudiao() + "");// 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);         // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());

        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            // toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String event) {
        if (event.equals("mFacePassHandler")) {
            mFacePassHandler = MyApplication.myApplication.getFacePassHandler();
            // diBuAdapter = new DiBuAdapter(dibuList, MainActivity202.this, dibuliebiao.getWidth(), dibuliebiao.getHeight(), mFacePassHandler);
            //  dibuliebiao.setLayoutManager(gridLayoutManager);
            //  dibuliebiao.setAdapter(diBuAdapter);
            return;
        }
        if (event.equals("ditu123")) {
            if (baoCunBean.getTouxiangzhuji() != null)
                daBg.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getTouxiangzhuji()));
            if (baoCunBean.getWenzi1() != null)
                zidongtext.setText(baoCunBean.getWenzi1());

            if (baoCunBean.getXiaBanTime() != null) {
                logo.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getXiaBanTime()));
            }
            daBg.invalidate();
            logo.invalidate();
            zidongtext.invalidate();
            Log.d("MainActivity101", "dfgdsgfdgfdgfdg");
            return;
        }

        Toast tastyToast = TastyToast.makeText(MainActivity101.this, event, TastyToast.LENGTH_LONG, TastyToast.INFO);
        tastyToast.setGravity(Gravity.CENTER, 0, 0);
        tastyToast.show();

    }


    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_TIME_TICK:

                    AssetManager mgr = getAssets();
                    //Univers LT 57 Condensed
                    Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
                    //   Typeface tf2 = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
                    //   Typeface tf3 = Typeface.createFromAsset(mgr, "fonts/kai.ttf");
                    //   String riqi2 = DateUtils.timesTwo(System.currentTimeMillis() + "") + "   " + DateUtils.getWeek(System.currentTimeMillis());
                    //  riqi.setTypeface(tf);
                    //  riqi.setText(riqi2);
                    xiaoshi.setTypeface(tf);

                    String xiaoshiss = DateUtils.timeMinute(System.currentTimeMillis() + "");
                    if (xiaoshiss.split(":")[0].equals("06") && xiaoshiss.split(":")[1].equals("30")) {

                        final List<BenDiJiLuBean> benDiJiLuBeans = benDiJiLuBeanBox.getAll();
                        final int size = benDiJiLuBeans.size();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < size; i++) {
                                    while (isSC) {
                                        isSC = false;
                                        link_shangchuanjilu2(benDiJiLuBeans.get(i));
                                    }

                                }

                            }
                        }).start();

                    }
                    xiaoshi.setText(xiaoshiss);
                    xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
                    riqi.setText(DateUtils.timesTwodian(System.currentTimeMillis() + ""));
                    xingqi2.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L));
                    xingqi3.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L + 86400000L));


                    Date date = new Date();
                    date.setTime(System.currentTimeMillis());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int t = calendar.get(Calendar.HOUR_OF_DAY);

                    //每过一分钟 触发
                    if (baoCunBean != null && baoCunBean.getDangqianShiJian() != null && !baoCunBean.getDangqianShiJian().equals(DateUtils.timesTwo(System.currentTimeMillis() + "")) && t >= 6) {

                        //一天请求一次
                        try {
                            if (baoCunBean.getDangqianChengShi2() == null) {
                                Toast tastyToast = TastyToast.makeText(MainActivity101.this, "获取天气失败,没有设置当前城市", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                tastyToast.show();
                                return;
                            }
                            //  Log.d("TimeChangeReceiver", baoCunBean.getDangqianChengShi());
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Request.Builder requestBuilder = new Request.Builder()
                                    .get()
                                    .url("http://v.juhe.cn/weather/index?format=1&cityname=" + baoCunBean.getDangqianChengShi() + "&key=356bf690a50036a5cfc37d54dc6e8319");
                            // step 3：创建 Call 对象
                            Call call = okHttpClient.newCall(requestBuilder.build());
                            //step 4: 开始异步请求
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("AllConnects", "请求失败" + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.d("AllConnects", "请求成功" + call.request().toString());
                                    //获得返回体
                                    try {

                                        ResponseBody body = response.body();
                                        String ss = body.string().trim();
                                        Log.d("AllConnects", "天气" + ss);
                                        JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                                        Gson gson = new Gson();
                                        final TianQiBean renShu = gson.fromJson(jsonObject, TianQiBean.class);
                                        final TodayBean todayBean = new TodayBean();
                                        todayBean.setId(123456L);
                                        todayBean.setTemperature(renShu.getResult().getToday().getTemperature());//温度
                                        todayBean.setWeather(renShu.getResult().getToday().getWeather()); //天气
                                        todayBean.setWind(renShu.getResult().getToday().getWind()); //风力
                                        todayBean.setUv_index(renShu.getResult().getToday().getUv_index()); //紫外线
                                        todayBean.setHumidity(renShu.getResult().getSk().getHumidity());//湿度
                                        todayBean.setDressing_advice(renShu.getResult().getToday().getDressing_advice());
                                        String tianqi1 = jsonObject.get("result").getAsJsonObject().get("future").getAsJsonObject().get("day_" + DateUtils.yrn((System.currentTimeMillis() + 86400000L) + ""))
                                                .getAsJsonObject().get("weather").getAsString();
                                        String tianqi1_wendu = jsonObject.get("result").getAsJsonObject().get("future").getAsJsonObject().get("day_" + DateUtils.yrn((System.currentTimeMillis() + 86400000L) + ""))
                                                .getAsJsonObject().get("temperature").getAsString();
                                        String tianqi2 = jsonObject.get("result").getAsJsonObject().get("future").getAsJsonObject().get("day_" + DateUtils.yrn((System.currentTimeMillis() + 86400000L + 86400000L) + ""))
                                                .getAsJsonObject().get("weather").getAsString();
                                        String tianqi2_wendu = jsonObject.get("result").getAsJsonObject().get("future").getAsJsonObject().get("day_" + DateUtils.yrn((System.currentTimeMillis() + 86400000L + 86400000L) + ""))
                                                .getAsJsonObject().get("temperature").getAsString();
                                        Log.d("TimeChangeReceiver", tianqi1 + "retretretretre" + tianqi1_wendu);
                                        todayBean.setTianqi2(tianqi1);
                                        todayBean.setTianqi2_wendu(tianqi1_wendu);
                                        todayBean.setTianqi3(tianqi2);
                                        todayBean.setTianqi3_wendu(tianqi2_wendu);
                                        // todayBean.setTianqi2();

                                        todayBeanBox.put(todayBean);
                                        baoCunBean.setDangqianShiJian(DateUtils.timesTwo(System.currentTimeMillis() + ""));
                                        baoCunBeanDao.put(baoCunBean);
                                        //更新界面
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  AssetManager mgr = getAssets();
                                                //Univers LT 57 Condensed
                                                //   Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
                                                //  Typeface tf2 = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
                                                //    String riqi2 = DateUtils.timesTwo(System.currentTimeMillis() + "") + "   " + DateUtils.getWeek(System.currentTimeMillis());

                                                //   wendu.setTypeface(tf2);
                                                //  tianqi.setTypeface(tf2);
                                                //  fengli.setTypeface(tf2);
                                                //  ziwaixian.setTypeface(tf2);
                                                // shidu.setTypeface(tf2);
//                                                jianyi.setTypeface(tf2);

                                                xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
                                                riqi.setText(DateUtils.timesTwodian(System.currentTimeMillis() + ""));
                                                shidu.setText(todayBean.getHumidity());
                                                xingqi2.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L));
                                                xingqi3.setText(DateUtils.getWeek(System.currentTimeMillis() + 86400000L + 86400000L));
                                                wendu.setText(todayBean.getTemperature().replace("℃~", "/"));
                                                wendu2.setText(todayBean.getTianqi2_wendu().replace("℃~", "/"));
                                                wendu3.setText(todayBean.getTianqi3_wendu().replace("℃~", "/"));

                                                if (todayBean.getWeather().contains("晴")) {
                                                    tianqiIm.setBackgroundResource(R.drawable.qing);
                                                } else if (todayBean.getWeather().contains("雨")) {
                                                    tianqiIm.setBackgroundResource(R.drawable.xiayu);
                                                } else if (todayBean.getWeather().contains("多云")) {
                                                    tianqiIm.setBackgroundResource(R.drawable.duoyun);
                                                } else if (todayBean.getWeather().contains("阴")) {
                                                    tianqiIm.setBackgroundResource(R.drawable.yintian);
                                                }

                                                if (todayBean.getTianqi2().contains("晴")) {
                                                    tianqiIm2.setBackgroundResource(R.drawable.qing);
                                                } else if (todayBean.getTianqi2().contains("雨")) {
                                                    tianqiIm2.setBackgroundResource(R.drawable.xiayu);
                                                } else if (todayBean.getTianqi2().contains("多云")) {
                                                    tianqiIm2.setBackgroundResource(R.drawable.duoyun);
                                                } else if (todayBean.getTianqi2().contains("阴")) {
                                                    tianqiIm2.setBackgroundResource(R.drawable.yintian);
                                                }


                                                if (todayBean.getTianqi3().contains("晴")) {
                                                    tianqiIm3.setBackgroundResource(R.drawable.qing);
                                                } else if (todayBean.getTianqi3().contains("雨")) {
                                                    tianqiIm3.setBackgroundResource(R.drawable.xiayu);
                                                } else if (todayBean.getTianqi3().contains("多云")) {
                                                    tianqiIm3.setBackgroundResource(R.drawable.duoyun);
                                                } else if (todayBean.getTianqi3().contains("阴")) {
                                                    tianqiIm3.setBackgroundResource(R.drawable.yintian);
                                                }

                                            }
                                        });
                                        //把所有人员的打卡信息重置
                                        List<Subject> subjectList = subjectBox.getAll();
                                        for (Subject s : subjectList) {
                                            s.setDaka(0);
                                            subjectBox.put(s);
                                        }

                                    } catch (Exception e) {
                                        Log.d("WebsocketPushMsg", e.getMessage() + "ttttt");
                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast tastyToast = TastyToast.makeText(MainActivity101.this, "获取天气失败,没有设置当前城市", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            tastyToast.setGravity(Gravity.CENTER, 0, 0);
                            tastyToast.show();
                            return;
                        }

                    }
                    //  Toast.makeText(context, "1 min passed", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_TIME_CHANGED:
                    //设置了系统时间
                    // Toast.makeText(context, "system time changed", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_TIMEZONE_CHANGED:
                    //设置了系统时区的action
                    //  Toast.makeText(context, "system time zone changed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


//    //轮播适配器
//    private class TestNomalAdapter extends StaticPagerAdapter {
//        private int[] imgs = {
//                R.drawable.dbg_1,
//                R.drawable.ceshi,
//                R.drawable.ceshi3,
//        };
//
//        @Override
//        public View getView(ViewGroup container, int position) {
//            ImageView view = new ImageView(container.getContext());
//            view.setImageResource(imgs[position]);
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return imgs.length;
//        }
//    }


    //上传识别记录
    private void link_shangchuanjilu(final Subject subject) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        RequestBody body = null;

        body = new FormBody.Builder()
                //.add("name", subject.getName()) //
                //.add("companyId", subject.getCompanyId()+"") //公司di
                //.add("companyName",subject.getCompanyName()+"") //公司名称
                //.add("storeId", subject.getStoreId()+"") //门店id
                //.add("storeName", subject.getStoreName()+"") //门店名称
                .add("subjectId", subject.getId() + "") //员工ID
                .add("subjectType", subject.getPeopleType()) //人员类型
                // .add("department", subject.getPosition()+"") //部门
                .add("discernPlace",baoCunBean.getTuisongDiZhi()+"")//识别地点
                // .add("discernAvatar",  "") //头像
                .add("identificationTime", DateUtils.time(System.currentTimeMillis() + ""))//时间
                .build();


        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi() + "/app/historySave");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                BenDiJiLuBean bean = new BenDiJiLuBean();
                bean.setSubjectId(subject.getId());
                bean.setDiscernPlace(baoCunBean.getTuisongDiZhi()+"");
                bean.setSubjectType(subject.getPeopleType());
                bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
                benDiJiLuBeanBox.put(bean);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);


                } catch (Exception e) {
                    BenDiJiLuBean bean = new BenDiJiLuBean();
                    bean.setSubjectId(subject.getId());
                    bean.setDiscernPlace(baoCunBean.getTuisongDiZhi()+"");
                    bean.setSubjectType(subject.getPeopleType());
                    bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
                    benDiJiLuBeanBox.put(bean);

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
                }
            }
        });
    }

    //上传识别记录2
    private void link_shangchuanjilu2(final BenDiJiLuBean subject) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        RequestBody body = null;

        body = new FormBody.Builder()
                //.add("name", subject.getName()) //
                //.add("companyId", subject.getCompanyId()+"") //公司di
                //.add("companyName",subject.getCompanyName()+"") //公司名称
                //.add("storeId", subject.getStoreId()+"") //门店id
                //.add("storeName", subject.getStoreName()+"") //门店名称
                .add("subjectId", subject.getSubjectId() + "") //员工ID
                .add("subjectType", subject.getSubjectType() + "") //人员类型
                // .add("department", subject.getPosition()+"") //部门
                .add("discernPlace", baoCunBean.getTuisongDiZhi()+ "")//识别地点
                // .add("discernAvatar",  "") //头像
                .add("identificationTime", subject.getIdentificationTime() + "")//时间
                .build();


        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi() + "/app/historySave");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                isSC = true;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);
                    //成功的话 删掉本地保存的记录
                    benDiJiLuBeanBox.remove(subject.getId());

                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");

                } finally {
                    isSC = true;
                }
            }
        });
    }


    private class ShengRiThierd extends Thread {
        @Override
        public void run() {
            int i = 0;
            while (!this.isInterrupted()) {
                Message message = Message.obtain();
                message.what = 123;
                mHandler.sendMessage(message);
                SystemClock.sleep(1000);
                i++;
                if (i >= 6) {
                    break;
                }
            }

        }
    }

    private class FangkeThired extends Thread {
        @Override
        public void run() {
            int i = 0;
            while (!this.isInterrupted()) {
                Message message = Message.obtain();
                message.what = 121;
                mHandler.sendMessage(message);
                SystemClock.sleep(1000);
                i++;
                if (i >= 4) {
                    break;
                }
            }
        }
    }

    private class VipThired extends Thread {
        @Override
        public void run() {
            LazyList<Subject> subjects = query.findLazy();
            RandomDataUtil util = new RandomDataUtil();
            List<Bitmap> bitmapListAmin = new ArrayList<>();
            //  Log.d("VipThired", "subjects:" + subjects.size());
            if (subjects.size() > 30) {
                List<Subject> subjectList = util.generateRandomDataNoRepeat(subjects, 30);
                //  Log.d("VipThired", "subjectList.size():" + subjectList.size());
                for (Subject subject : subjectList) {
                    try {
                        Bitmap bitmap = mFacePassHandler.getFaceImage(subject.getTeZhengMa().getBytes());
                        bitmapListAmin.add(FileUtil.toRoundBitmap2(bitmap, BitmapFactory.decodeResource(getResources(), R.drawable.xinxinxin), 100, 100));
                    } catch (FacePassException e) {
                        Log.d("VipThired", e.getMessage() + "获取vip动画bitmap异常");
                    }
                }
            } else {

                for (Subject subject : subjects) {
                    try {
                        Bitmap bitmap = mFacePassHandler.getFaceImage(subject.getTeZhengMa().getBytes());
                        bitmapListAmin.add(FileUtil.toRoundBitmap2(bitmap, BitmapFactory.decodeResource(getResources(), R.drawable.xinxinxin), 100, 100));
                    } catch (FacePassException e) {
                        Log.d("VipThired", e.getMessage() + "获取vip动画bitmap异常");
                    }
                }
                //Collections.shuffle(bitmapListAmin);
            }
            //   Log.d("VipThired", "subjects.size():" + subjects.size());
            int j = 0;
            while (!this.isInterrupted()) {
                //   Collections.shuffle(bitmapListAmin);
                List<Bitmap> tempLS = new ArrayList<>();
                int tem = j * 6;
                int tesi = bitmapListAmin.size();
                for (int i = 0; i < 6; i++) {
                    if (tesi > tem + i) {
                        tempLS.add(bitmapListAmin.get(tem + i));
                    }
                }
                Message message = Message.obtain();
                message.what = 122;
                message.obj = tempLS;
                mHandler.sendMessage(message);
                SystemClock.sleep(1000);

                j++;
                if (j >= 5) {
                    break;
                }

            }


        }
    }


    public static class UsbBroadCastReceiver extends BroadcastReceiver {

        public UsbBroadCastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null && intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
                usbPath = intent.getData().getPath();
                List<String> sss = FileUtil.getMountPathList();
                int size = sss.size();
                for (int i = 0; i < size; i++) {

                    if (sss.get(i).contains(usbPath)) {
                        usbPath = sss.get(i);
                    }

                }

                Log.d("UsbBroadCastReceiver", usbPath);
            }


        }
    }

    private static String usbPath = null;


    public class NetWorkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //以太网
                NetworkInfo wifiNetworkInfo1 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo1.isConnected() || wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                   // isNet = true;

                } else {
                   // isNet = false;
                }


                //				if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //					Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                //				} else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                //					Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                //				} else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //					Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                //				} else {
                //					Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                //				}
                //API大于23时使用下面的方式进行网络监听
            } else {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                //  Log.d(TAG, "networks.length:" + networks.length);
                if (networks.length == 0) {
                    //isNet = false;
                }
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

                    if (networkInfo != null && networkInfo.isConnected()) {
                        //isNet = true;

                    }
                }

            }
        }
    }

}
