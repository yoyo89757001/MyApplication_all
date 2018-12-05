package megvii.testfacepass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.bugly.Bugly;
import com.yatoooon.screenadaptation.ScreenAdapterTools;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Objects;

import android_serialport_api.Sending01010101Activity;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortActivity;
import android_serialport_api.SerialPortFinder;
import cn.jpush.android.api.JPushInterface;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.beans.ChengShiIDBean;
import megvii.testfacepass.beans.MyObjectBox;
import megvii.testfacepass.beans.ZhiChiChengShi;


import megvii.testfacepass.dialogall.CommonData;
import megvii.testfacepass.dialogall.CommonDialogService;
import megvii.testfacepass.dialogall.ToastUtils;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GsonUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/8/3.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    public  FacePassHandler facePassHandler=null;
    private static BoxStore mBoxStore;
    public static MyApplication myApplication;
    private Box<ChengShiIDBean> chengShiIDBeanBox;
    protected OutputStream mOutputStream;
   // private InputStream mInputStream;
   // private ReadThread mReadThread;
    //private SendingThread mSendingThread;
   // private   byte[] mBuffer;

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "/dev/ttyS4");
            int baudrate = Integer.decode(Objects.requireNonNull(sp.getString("BAUDRATE", "9600")));

            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            try {
                mSerialPort = new SerialPort(new File(path), baudrate, 0);
            }catch (Exception e){
                e.printStackTrace();
            }
            /* Open the serial port */

        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        mBoxStore = MyObjectBox.builder().androidContext(this).build();

        Bugly.init(getApplicationContext(), "f833ac8f0d", false);
        //适配
        ScreenAdapterTools.init(this);
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext());
        JPushInterface.setAlias(getApplicationContext(),1, FileUtil.getSerialNumber(this)==null?FileUtil.getIMSI():FileUtil.getSerialNumber(this));
        Log.d("MyApplication","机器码"+ FileUtil.getSerialNumber(this) == null ? FileUtil.getIMSI() : FileUtil.getSerialNumber(this));
        //全局dialog
        this.registerActivityLifecycleCallbacks(this);//注册
        CommonData.applicationContext = this;
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        CommonData.ScreenWidth = metric.widthPixels; // 屏幕宽度（像素）
        Intent dialogservice = new Intent(this, CommonDialogService.class);
        startService(dialogservice);

        chengShiIDBeanBox=mBoxStore.boxFor(ChengShiIDBean.class);
        if(chengShiIDBeanBox.getAll().size()==0){
            OkHttpClient okHttpClient= new OkHttpClient();
            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                    .get()
                    .url("http://v.juhe.cn/weather/citys?key=356bf690a50036a5cfc37d54dc6e8319");
            // .url("http://v.juhe.cn/weather/index?format=2&cityname="+text1+"&key=356bf690a50036a5cfc37d54dc6e8319");
            // step 3：创建 Call 对象
            Call call = okHttpClient.newCall(requestBuilder.build());
            //step 4: 开始异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("AllConnects", "请求失败"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    Log.d("AllConnects", "请求成功"+call.request().toString());
                    //获得返回体
                    try{

                        ResponseBody body = response.body();
                        String ss=body.string().trim();
                        Log.d("AllConnects", "天气"+ss);

                        JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                        Gson gson=new Gson();
                        final ZhiChiChengShi renShu=gson.fromJson(jsonObject,ZhiChiChengShi.class);
                        int size=renShu.getResult().size();
                        //  chengShiIDBeanBox.removeAll();

                        for (int i=0;i<size;i++){
                            ChengShiIDBean bean=new ChengShiIDBean();
                            bean.setId(renShu.getResult().get(i).getId());
                            bean.setCity(renShu.getResult().get(i).getCity());
                            bean.setDistrict(renShu.getResult().get(i).getDistrict());
                            bean.setProvince(renShu.getResult().get(i).getProvince());
                            chengShiIDBeanBox.put(bean);
                        }


                    }catch (Exception e){
                        Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                    }

                }
            });
        }


      //  tikong();

    }

    private void tikong() {

        try {
            mSerialPort = getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            //mInputStream = mSerialPort.getInputStream();

            /* Create a receiving thread */
         //   mReadThread = new SerialPortActivity.ReadThread();
          //  mReadThread.start();



        } catch (SecurityException e) {
            DisplayError(R.string.error_security);
        } catch (IOException e) {
            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            DisplayError(R.string.error_configuration);
        }


    }

    public BoxStore getBoxStore(){
        return mBoxStore;
    }

    public FacePassHandler getFacePassHandler() {

        return facePassHandler;
    }

    public void setFacePassHandler(FacePassHandler facePassHandler1){
        facePassHandler=facePassHandler1;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if(activity.getParent()!=null){
            CommonData.mNowContext = activity.getParent();
        }else
            CommonData.mNowContext = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(activity.getParent()!=null){
            CommonData.mNowContext = activity.getParent();
        }else
            CommonData.mNowContext = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ToastUtils.getInstances().cancel();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }



//    private class ReadThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//            while(!isInterrupted()) {
//                int size;
//                try {
//                    byte[] buffer = new byte[64];
//                    if (mInputStream == null) return;
//                    size = mInputStream.read(buffer);
//                    if (size > 0) {
//                        onDataReceived(buffer, size);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return;
//                }
//            }
//        }
//    }

    private void DisplayError(int resourceId) {

        ToastUtils.getInstances().showDialog("485初始化错误","错误码 "+resourceId, 0);
    }

//    protected abstract void onDataReceived(final byte[] buffer, final int size);
}
