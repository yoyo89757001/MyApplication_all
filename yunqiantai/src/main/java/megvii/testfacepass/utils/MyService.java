package megvii.testfacepass.utils;




import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import megvii.testfacepass.MyApplication;


public class MyService extends Service {

    private MyBinder binder = new MyBinder();
    private static final String UPLOAD_FILE = "com.nuoyuan.statistic.action.UPLOAD_FILE";
    private static String loadUrlPath = "";

    public static final String CHANNEL_ID_STRING = "nyd001";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
      //  createErrorNotification();

        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) MyApplication.myApplication.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "诺秒贷", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }

    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    private void createErrorNotification() {

        //启用前台服务，主要是startForeground()
        Notification.Builder notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this,"本地通讯服务");
        }else {
            //公共的属性都写到了这里避免代码重复
            notification = new Notification.Builder(this);//创建builder对象
            //指定点击通知后的动作，此处跳到我的博客

        }
        //  设置通知默认效果
         startForeground(1, notification.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);

    }

    public class MyBinder extends Binder {
        /** * 获取Service的方法 * @return 返回PlayerService */
        public MyService getService(){
            return MyService.this;
        }
    }



}
