package megvii.testfacepass.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.beans.BaoCunBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class FacePassUtil {
    private   FacePassModel poseModel;
    private   FacePassModel blurModel;
    private   FacePassModel livenessModel;
    private   FacePassModel searchModel;
    private   FacePassModel detectModel;
    private   FacePassModel detectRectModel;
    private   FacePassModel landmarkModel;
    private   FacePassModel smileModel;
    private   FacePassModel ageGenderModel;


    /* SDK 实例对象 */
    private Context context;
    private int TIMEOUT=30*1000;

    private    FacePassHandler mFacePassHandler;  /* 人脸识别Group */
    private  final String group_name = "facepasstestx";
    private  boolean isLocalGroupExist = false;
    private   BaoCunBean baoCunBean;

    public  void init(final Activity activity , final Context context, final int cameraRotation, final BaoCunBean baoCunBean){
        this.context=context;
        this.baoCunBean=baoCunBean;

            new Thread() {
                @Override
                public void run() {
                    while (!activity.isFinishing()) {
                        if (FacePassHandler.isAvailable()) {
                            /* FacePass SDK 所需模型， 模型在assets目录下 */
                            poseModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "pose.alfa.tiny.170515.bin");
                            blurModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "blurness.v5.l2rsmall.bin");
                            livenessModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "panorama.facepass.181129_3288_3models_1core.combine.bin");
                            searchModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "feat.inu.3comps.inp96.200ms.e6000.pca512.bin");
                            detectModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "detector.retinanet.facei2head.x14.180910.bin");
                            detectRectModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "det.retinanet.face2head.x14.180906.bin");

                            landmarkModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "lmk.postfilter.tiny.dt1.4.1.20180602.3dpose.bin");
                            smileModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "attr.blur.align.gray.general.mgf29.0.1.0.181127.smile.bin");
                            ageGenderModel = FacePassModel.initModel(context.getApplicationContext().getAssets(), "age_gender.v2.bin");
                            /* SDK 配置 */
                            float searchThreshold = baoCunBean.getShibieFaZhi();
                            float livenessThreshold = baoCunBean.getHuoTiFZ();
                            boolean livenessEnabled = baoCunBean.isHuoTi();
                            boolean smileEnabled = false;
                            boolean ageGenderEnabled = false;
                            int faceMinThreshold = baoCunBean.getShibieFaceSize();
                            FacePassPose poseThreshold = new FacePassPose(30f, 30f, 30f);
                            float blurThreshold = 0.2f;
                            float lowBrightnessThreshold = 70f;
                            float highBrightnessThreshold = 210f;
                            float brightnessSTDThreshold = 60f;
                            int retryCount = 2;

                            int rotation = cameraRotation;
                            String fileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            FacePassConfig config;
                            try {
                                /* 填入所需要的配置 */
                                config = new FacePassConfig(searchThreshold, livenessThreshold, livenessEnabled, smileEnabled, ageGenderEnabled,
                                        faceMinThreshold, poseThreshold, blurThreshold,
                                        lowBrightnessThreshold, highBrightnessThreshold, brightnessSTDThreshold,
                                        retryCount, rotation, fileRootPath,
                                        poseModel, blurModel, livenessModel, searchModel, detectModel,
                                        detectRectModel, landmarkModel, smileModel, ageGenderModel);


                                /* 创建SDK实例 */
                                mFacePassHandler = new FacePassHandler(config);
                                MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                boolean a = false;
                                try {

                                     a=  mFacePassHandler.createLocalGroup(group_name);

                                } catch (FacePassException e) {
                                    e.printStackTrace();
                                }

                              //  float searchThreshold2 = 75f;
                              //  float livenessThreshold2 = 48f;
                             //   boolean livenessEnabled2 = true;
                                int faceMinThreshold2 = baoCunBean.getRuKuFaceSize();
                                float blurThreshold2 = baoCunBean.getRuKuMoHuDu();
                                float lowBrightnessThreshold2 = 70f;
                                float highBrightnessThreshold2 = 210f;
                                float brightnessSTDThreshold2 = 60f;
                                FacePassConfig config1=new FacePassConfig(faceMinThreshold2,25f,25f,25f,blurThreshold2,
                                        lowBrightnessThreshold2,highBrightnessThreshold2,brightnessSTDThreshold2);
                                boolean is=   mFacePassHandler.setAddFaceConfig(config1);

                                Log.d("YanShiActivity", a+" 设置入库质量配置"+is );

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast tastyToast= TastyToast.makeText(context,"识别模块初始化成功",TastyToast.LENGTH_LONG,TastyToast.INFO);
                                        tastyToast.setGravity(Gravity.CENTER,0,0);
                                        tastyToast.show();
                                       MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                        EventBus.getDefault().post("mFacePassHandler");
                                        chaxuncuowu();


                                    }
                                });

                                checkGroup(activity,context);

                                SystemClock.sleep(15000);

                                Intent intent = new Intent();
                                intent.setAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
                                intent.putExtra("msg", "接收静态注册广播成功！");
                                context.sendBroadcast(intent);

                                Intent intent2 = new Intent();
                                intent2.setAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
                                intent2.putExtra("msg", "接收静态注册广播成功！");
                                context.sendBroadcast(intent2);

                            } catch (FacePassException e) {
                                e.printStackTrace();

                                return;
                            }
                            return;
                        }
                        try {
                            /* 如果SDK初始化未完成则需等待 */
                            sleep(500);
                            Log.d("FacePassUtil", "激活中。。。");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }


    private  void checkGroup(Activity activity, final Context context) {
        if (mFacePassHandler == null) {
            return;
        }
        String[] localGroups = mFacePassHandler.getLocalGroups();
        isLocalGroupExist = false;
        if (localGroups == null || localGroups.length == 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast tastyToast= TastyToast.makeText(context,"还没创建识别组",TastyToast.LENGTH_LONG,TastyToast.INFO);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }
            });
            return;
        }
        for (String group : localGroups) {
            if (group_name.equals(group)) {

                isLocalGroupExist = true;
            }
        }
        if (!isLocalGroupExist) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast tastyToast= TastyToast.makeText(context,"还没创建识别组",TastyToast.LENGTH_LONG,TastyToast.INFO);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }
            });
        }
    }

    private void chaxuncuowu(){
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.cookieJar(new CookiesManager())
                //.retryOnConnectionFailure(true)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("machineCode)", FileUtil.getSerialNumber(context) == null ? FileUtil.getIMSI() : FileUtil.getSerialNumber(context))
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                //.header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi() + "/app/findFailurePush");

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
                    //没了删除，所有在添加前要删掉所有

                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "查询错误推送" + ss);

                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
                }
            }
        });
    }

}
