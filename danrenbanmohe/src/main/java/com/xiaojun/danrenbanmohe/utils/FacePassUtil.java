package com.xiaojun.danrenbanmohe.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.xiaojun.danrenbanmohe.MyApplication;
import com.xiaojun.danrenbanmohe.R;
import com.xiaojun.danrenbanmohe.bean.BaoCunBean;

import org.greenrobot.eventbus.EventBus;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassAddFaceResult;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;


public class FacePassUtil {
    FacePassModel poseModel;
    FacePassModel blurModel;
    FacePassModel livenessModel;
    FacePassModel searchModel;
    FacePassModel detectModel;
    FacePassModel detectRectModel;
    FacePassModel landmarkModel;
    FacePassModel smileModel;
    FacePassModel ageGenderModel;
    /* SDK 实例对象 */

 private FacePassHandler mFacePassHandler;  /* 人脸识别Group */
    private  final String group_name = "facepasstestx";
    private static final String group_name_mxNan = "facepasstestxnan";
    private static final String group_name_mxNv = "facepasstestxnan2";
    private  boolean isLocalGroupExist = false;
    private BaoCunBean baoCunBean=null;
    private int cameraRotation;
    private Context context;

    public FacePassUtil() {

    }

    public  void init(final Activity activity , final Context context, final int cameraRotation, final BaoCunBean baoCunBean){
            this.baoCunBean=baoCunBean;
            this.context=context;
            this.cameraRotation=cameraRotation;

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
                                float searchThreshold2 = 75f;
                                float livenessThreshold2 = 48f;
                                boolean livenessEnabled2 = true;
                                int faceMinThreshold2 = baoCunBean.getRuKuFaceSize();
                                float blurThreshold2 = baoCunBean.getRuKuMoHuDu();
                                float lowBrightnessThreshold2 = 50f;
                                float highBrightnessThreshold2 = 250f;
                                float brightnessSTDThreshold2 = 60f;
                                FacePassConfig config1=new FacePassConfig(faceMinThreshold2,30f,30f,30f,blurThreshold2,
                                        lowBrightnessThreshold2,highBrightnessThreshold2,brightnessSTDThreshold2);

                                Log.d("FacePassUtil", "设置入库质量配置" + mFacePassHandler.setAddFaceConfig(config1));

                                MyApplication.myApplication.setFacePassHandler(mFacePassHandler);

                                try {

                                 //   boolean a=  mFacePassHandler.createLocalGroup(group_name);
                                    boolean a1=  mFacePassHandler.createLocalGroup(group_name_mxNan);
                                    boolean a2=  mFacePassHandler.createLocalGroup(group_name_mxNv);

                                    if (a1 && a2){
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //添加明星库
                                                try {
                                                    int wmen[]={R.drawable.m1,R.drawable.n2,R.drawable.n3,R.drawable.n4,R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8};
                                                    int man[]={R.drawable.n1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8,R.drawable.m9};

                                                    //男明星库
                                                    for (int i=0;i<1;i++){
                                                        FacePassAddFaceResult result= mFacePassHandler.addFace(BitmapFactory.decodeResource(context.getResources(),man[i]));
                                                        if (result.result==0){
                                                            Log.d("FacePassUtil", "添加男明星" + mFacePassHandler.bindGroup(group_name_mxNan,result.faceToken));
                                                        }
                                                    }
                                                    //女明星库
                                                    for (int i=0;i<1;i++){
                                                        FacePassAddFaceResult result= mFacePassHandler.addFace(BitmapFactory.decodeResource(context.getResources(),wmen[i]));
                                                        if (result.result==0){
                                                            Log.d("FacePassUtil", "添加女明星" + mFacePassHandler.bindGroup(group_name_mxNv,result.faceToken));
                                                        }

                                                    }

                                                } catch (FacePassException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }).start();

                                    }

                                    Log.d("FacePassUtil", "创建组" + a1+a2);
                                } catch (FacePassException e) {
                                  Log.d("FacePassUtil", e.getMessage()+"方法撒旦");
                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast tastyToast= TastyToast.makeText(context,"识别模块初始化成功", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                        tastyToast.setGravity(Gravity.CENTER,0,0);
                                        tastyToast.show();
                                       MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                        EventBus.getDefault().post("mFacePassHandler");

                                    }
                                });

                                //checkGroup(activity,context);


                            } catch (FacePassException e) {
                                e.printStackTrace();

                                return;
                            }
                            return;
                        }
                        try {
                            /* 如果SDK初始化未完成则需等待 */
                            sleep(500);
                        } catch (InterruptedException e) {
                           Log.d("FacePassUtil", e.getMessage()+"ddd");
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


                    Toast tastyToast= TastyToast.makeText(context,"还没创建识别组", TastyToast.LENGTH_LONG, TastyToast.INFO);
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
                    Toast tastyToast= TastyToast.makeText(context,"还没创建识别组", TastyToast.LENGTH_LONG, TastyToast.INFO);
                    tastyToast.setGravity(Gravity.CENTER,0,0);
                    tastyToast.show();
                }
            });
        }
    }



}
