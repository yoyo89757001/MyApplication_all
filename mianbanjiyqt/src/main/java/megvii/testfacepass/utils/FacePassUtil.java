package megvii.testfacepass.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.beans.BaoCunBean;



public class FacePassUtil {

    FacePassModel poseModel;
    FacePassModel blurModel;
    FacePassModel livenessModel;
    FacePassModel searchModel;
    FacePassModel detectModel;
    FacePassModel detectRectModel;
    FacePassModel landmarkModel;
    FacePassModel smileModel;
    /* SDK 实例对象 */

 private    FacePassHandler mFacePassHandler;  /* 人脸识别Group */
    private  final String group_name = "face-pass-test-x";
    private  boolean isLocalGroupExist = false;

    public  void init(final Activity activity , final Context context, final int cameraRotation, final BaoCunBean baoCunBean){

            new Thread() {
                @Override
                public void run() {
                    while (!activity.isFinishing()) {
                        if (FacePassHandler.isAvailable()) {
                            /* FacePass SDK 所需模型， 模型在assets目录下 */

                           // trackModel = FacePassModel.initModel(context.getAssets(), "tracker.retina.x20.20180607.bin");
                            poseModel = FacePassModel.initModel(context.getAssets(), "pose.alfa.tiny.170515.bin");
                            blurModel = FacePassModel.initModel(context.getAssets(), "blurness.v5.l2rsmall.bin");
                            livenessModel = FacePassModel.initModel(context.getAssets(), "panorama.facepass.181129_3288_3models_1core.combine.bin");
                            searchModel = FacePassModel.initModel(context.getAssets(), "feat.inu.3comps.inp96.200ms.e6000.pca512.bin");
                            detectModel = FacePassModel.initModel(context.getAssets(), "detector.retinanet.facei2head.x14.180910.bin");
//                        detectRectModel = FacePassModel.initModel(context.getAssets(), "det.retinanet.head2face.x20.180613.bin");
                            detectRectModel = FacePassModel.initModel(context.getAssets(), "det.retinanet.face2head.x14.180906.bin");

                            landmarkModel = FacePassModel.initModel(context.getAssets(), "lmk.postfilter.tiny.dt1.4.1.20180602.3dpose.bin");
                            smileModel = FacePassModel.initModel(context.getAssets(), "attr.blur.align.gray.general.mgf29.0.1.0.181127.smile.bin");

                            
                            /* SDK 配置 */
                            boolean smileEnabled = false;
                            float searchThreshold = baoCunBean.getShibieFaZhi();
                            float livenessThreshold = baoCunBean.getHuoTiFZ();
                            boolean livenessEnabled = baoCunBean.isHuoTi();
                            int faceMinThreshold =baoCunBean.getShibieFaceSize();
                            FacePassPose poseThreshold = new FacePassPose(20f, 20f, 20f);
                            float blurThreshold = 0.3f;
                            float lowBrightnessThreshold = 70f;
                            float highBrightnessThreshold = 210f;
                            float brightnessSTDThreshold = 60f;
                            int retryCount = 2;
                            int rotation = cameraRotation;
                            String fileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            FacePassConfig config;
                            try {

                                /* 填入所需要的配置 */
                                config = new FacePassConfig(searchThreshold, livenessThreshold, livenessEnabled, smileEnabled,
                                        faceMinThreshold, poseThreshold, blurThreshold,
                                        lowBrightnessThreshold, highBrightnessThreshold, brightnessSTDThreshold,
                                        retryCount, rotation, fileRootPath,
                                        poseModel, blurModel, livenessModel, searchModel, detectModel,
                                        detectRectModel, landmarkModel, smileModel);
                                /* 创建SDK实例 */
                                mFacePassHandler = new FacePassHandler(config);
                                MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                try {

                                    boolean a=  mFacePassHandler.createLocalGroup(group_name);

                                } catch (FacePassException e) {
                                    e.printStackTrace();
                                }

                                float searchThreshold2 = 75f;
                                float livenessThreshold2 = 48f;
                                boolean livenessEnabled2 = true;
                                int faceMinThreshold2 = baoCunBean.getRuKuFaceSize();
                                float blurThreshold2 = baoCunBean.getRuKuMoHuDu();
                                float lowBrightnessThreshold2 = 70f;
                                float highBrightnessThreshold2 = 210f;
                                float brightnessSTDThreshold2 = 60f;
                                FacePassConfig config1=new FacePassConfig(faceMinThreshold2,26f,26f,26f,blurThreshold2,
                                        lowBrightnessThreshold2,highBrightnessThreshold2,brightnessSTDThreshold2);

                                Log.d("YanShiActivity", "设置入库质量配置" + mFacePassHandler.setAddFaceConfig(config1));

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast tastyToast= TastyToast.makeText(context,"识别模块初始化成功",TastyToast.LENGTH_LONG,TastyToast.INFO);
                                        tastyToast.setGravity(Gravity.CENTER,0,0);
                                        tastyToast.show();
                                       MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                        EventBus.getDefault().post("mFacePassHandler");

                                    }
                                });

                                checkGroup(activity,context);


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


}
