package megvii.testfacepass.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import io.objectbox.Box;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.utils.AppUtils;




public class BaseActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE, PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE};
    private BaoCunBean baoCunBean=null;
    private Box<BaoCunBean> baoCunBeanDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();

        baoCunBean = baoCunBeanDao.get(123456L);
        if (baoCunBean == null) {
            baoCunBean = new BaoCunBean();
            baoCunBean.setId(123456L);
            baoCunBean.setHoutaiDiZhi("http://hy.inteyeligence.com/front");
            baoCunBean.setShibieFaceSize(20);
            baoCunBean.setShibieFaZhi(70);
            baoCunBean.setYudiao(5);
            baoCunBean.setYusu(5);
            baoCunBean.setBoyingren(4);
            baoCunBean.setRuKuFaceSize(60);
            baoCunBean.setRuKuMoHuDu(0.3f);
            baoCunBean.setHuoTiFZ(70);
            baoCunBean.setHuoTi(false);
            baoCunBean.setDangqianShiJian("d");
            baoCunBean.setTianQi(false);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏
                Log.d("SheZhiActivity", "竖屏");
                baoCunBean.setHengOrShu(false);
            } else {
                //横屏
                Log.d("SheZhiActivity", "横屏");
                baoCunBean.setHengOrShu(true);
            }
            if (baoCunBean.isHengOrShu()){
                //true就是横
                baoCunBean.setMoban(101);
            }else {
                //竖屏
                baoCunBean.setMoban(201);
            }
            baoCunBeanDao.put(baoCunBean);


        }





        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {
            //初始化
            // FacePassHandler.getAuth(contents.authIP, contents.apiKey, contents.apiSecret);


            FacePassHandler.initSDK(getApplicationContext());
            Log.d("MainActivity201", FacePassHandler.getVersion());

            //1代表横  2代表竖
            switch (baoCunBean.getMoban()){
                case 101://横屏 第一版
                    startActivity(new Intent(BaseActivity.this,MainActivity102.class));
                    finish();

                    break;
                case 102:

                    break;
                case 103:

                    break;

                case 201:
                    //默认模版 //竖屏 第一版
                    startActivity(new Intent(BaseActivity.this,MainActivity204.class));
                    finish();
                    //  startActivity(new Intent(BaseActivity.this,MainActivity201.class));
                    //   finish();

                    break;
                case 202:
                    //   startActivity(new Intent(BaseActivity.this,MainActivity202.class));
                    //  finish();

                    break;
                case 203:


                    break;

            }


            //开启信鸽的日志输出，线上版本不建议调用
            XGPushConfig.enableDebug(this, true);
            //ed02bf3dc1780d644f0797a9153963b37ed570a5
 /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
            XGPushManager.registerPush(getApplicationContext(),
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.w("MainActivity", "+++ register push sucess. token:" + data + "flag" + flag);
                            //  m.obj = "+++ register push sucess. token:" + data;
                            //m.sendToTarget();0e88ac1601c0cf20c0682f8dcd917b39c98c395f
                            Log.d("MainActivity", AppUtils.getPackageName(BaseActivity.this) + "lllooo11");
                            Log.d("MainActivity", XGPushConfig.getToken(BaseActivity.this)+"lllll11");
                            baoCunBean.setTuisongDiZhi(XGPushConfig.getToken(BaseActivity.this));
                            baoCunBeanDao.put(baoCunBean);
                        }
                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.w("MainActivity",
                                    "+++ register push fail. token:" + data
                                            + ", errCode:" + errCode + ",msg:"
                                            + msg);
                            //  m.obj = "+++ register push fail. token:" + data
                            //     + ", errCode:" + errCode + ",msg:" + msg;
                            //  m.sendToTarget();

                            Log.d("MainActivity", AppUtils.getPackageName(BaseActivity.this) + "lllooo22");
                            Log.d("MainActivity", XGPushConfig.getToken(BaseActivity.this)+"lllll22");

                        }
                    });


        }


    }


    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /* 请求程序所需权限 */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Permission, PERMISSIONS_REQUEST);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED)
                    granted = false;
            }
            if (!granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA)
                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_WRITE_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_INTERNET)
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)) {
                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
                    }
            } else {

                FacePassHandler.initSDK(getApplicationContext());
                Log.d("MainActivity2013", FacePassHandler.getVersion());

                //1代表横  2代表竖
                switch (baoCunBean.getMoban()){
                    case 101://横屏  第一版
                        startActivity(new Intent(BaseActivity.this,MainActivity102.class));
                        finish();

                        break;
                    case 102:

                        break;
                    case 103:

                        break;

                    case 201:
                        //默认模版 //竖屏  第一版
                        startActivity(new Intent(BaseActivity.this,MainActivity204.class));
                        finish();
                        //  startActivity(new Intent(BaseActivity.this,MainActivity201.class));
                        //   finish();

                        break;
                    case 202:
                        //   startActivity(new Intent(BaseActivity.this,MainActivity202.class));
                        //  finish();

                        break;
                    case 203:

                        break;

                }

                //开启信鸽的日志输出，线上版本不建议调用
                XGPushConfig.enableDebug(this, true);
                //ed02bf3dc1780d644f0797a9153963b37ed570a5
 /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
                XGPushManager.registerPush(getApplicationContext(),
                        new XGIOperateCallback() {
                            @Override
                            public void onSuccess(Object data, int flag) {
                                Log.w("MainActivity", "+++ register push sucess. token:" + data + "flag" + flag);
                                //  m.obj = "+++ register push sucess. token:" + data;
                                //m.sendToTarget();0e88ac1601c0cf20c0682f8dcd917b39c98c395f
                                Log.d("MainActivity", AppUtils.getPackageName(BaseActivity.this) + "lllooo11");
                                Log.d("MainActivity", XGPushConfig.getToken(BaseActivity.this)+"lllll11");
                            }
                            @Override
                            public void onFail(Object data, int errCode, String msg) {
                                Log.w("MainActivity",
                                        "+++ register push fail. token:" + data
                                                + ", errCode:" + errCode + ",msg:"
                                                + msg);
                                //  m.obj = "+++ register push fail. token:" + data
                                //     + ", errCode:" + errCode + ",msg:" + msg;
                                //  m.sendToTarget();

                                Log.d("MainActivity", AppUtils.getPackageName(BaseActivity.this) + "lllooo22");
                                Log.d("MainActivity", XGPushConfig.getToken(BaseActivity.this)+"lllll22");

                            }
                        });

            }
        }
    }

    public static class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Intent i = new Intent(context, BaseActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }

}
