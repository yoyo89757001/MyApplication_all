package com.example.yqtlaifangdengji.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.utils.DateUtils;
import com.example.yqtlaifangdengji.utils.FileUtil;
import com.example.yqtlaifangdengji.utils.GlideCircleTransform;
import com.example.yqtlaifangdengji.utils.GsonUtil;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PaiZhaoActivity extends AppCompatActivity {
    @BindView(R.id.riqi)
    TextView riqi;
    @BindView(R.id.xingqi)
    TextView xingqi;
    @BindView(R.id.shijian)
    TextView shijian;
    @BindView(R.id.fanhui)
    ImageView fanhui;
    @BindView(R.id.shouye)
    ImageView shouye;
    @BindView(R.id.paizhao)
    ImageView paizhao;
    @BindView(R.id.xingming)
    EditText xingming;
    @BindView(R.id.shouji)
    EditText shouji;
    @BindView(R.id.danwei)
    EditText danwei;
    @BindView(R.id.queren)
    Button queren;
    @BindView(R.id.tishi)
    TextView tishi;
    private String shiyou = null;
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;
    private int dw, dh;
    private RequestOptions myOptions = new RequestOptions()
            .fitCenter()
            .override(320, 320)
            .transform(new GlideCircleTransform(PaiZhaoActivity.this));

    private int TIMEOUT2 = 30000;
    private boolean isL = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai_zhao);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        //每分钟的广播
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);
        shiyou = getIntent().getStringExtra("shiyou");

        riqi.setText(DateUtils.timeyry(System.currentTimeMillis() + ""));
        xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
        shijian.setText(DateUtils.timeshijian(System.currentTimeMillis() + ""));


        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donghua(v);
                finish();
            }
        });

        shouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donghua(v);
                EventBus.getDefault().post("guanbi");

            }
        });


    }

    @OnClick({R.id.paizhao, R.id.queren})
    public void onViewClicked(View view) {
        donghua(view);
        switch (view.getId()) {
            case R.id.paizhao:
                startActivity(new Intent(PaiZhaoActivity.this, CameraActivity.class));

                break;
            case R.id.queren:
                Intent intent=new Intent(PaiZhaoActivity.this,ChaXunActivity.class);
                intent.putExtra("shiyou",shiyou);
                intent.putExtra("name",xingming.getText().toString().trim());
                intent.putExtra("dianhua",xingming.getText().toString().trim());
                intent.putExtra("gongsi",xingming.getText().toString().trim());
                startActivity(intent);

                if (isL && !xingming.getText().toString().trim().equals("")){



                }else {
                    Toast tastyToast = TastyToast.makeText(PaiZhaoActivity.this, "数据不完整，不能进行下一步", TastyToast.LENGTH_LONG, TastyToast.INFO);
                    tastyToast.setGravity(Gravity.CENTER, 0, 0);
                    tastyToast.show();
                }


                break;
        }
    }

    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_TIME_TICK:
                    riqi.setText(DateUtils.timeyry(System.currentTimeMillis() + ""));
                    xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
                    shijian.setText(DateUtils.timeshijian(System.currentTimeMillis() + ""));

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String event) {
        if (event.equals("guanbi")) {
            finish();
        }
        if (event.equals("tupiangengxin")) {
            // Log.d("PaiZhaoActivity", "更新图片");

            // File file = new File(Environment.getExternalStorageDirectory(), "mmruitong.png");//将要保存图片的路径
//            Glide.get(PaiZhaoActivity.this).clearMemory();
//            Glide.with(PaiZhaoActivity.this)
//                    .load(Environment.getExternalStorageDirectory()+File.separator+"mmruitong.png")
//                    .apply(myOptions)
//                    .into(paizhao);


            paizhao.setImageBitmap(FileUtil.toRoundBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "mmruitong.png")));
            link_P2(new File(Environment.getExternalStorageDirectory() + File.separator + "mmruitong.png"));


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeChangeReceiver);
        EventBus.getDefault().unregister(this);
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


    // 照片质量
    private void link_P2(final File file) {

//        if (screen_token.equals("") || url.equals("")) {
//
//            return;
//        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT2, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT2, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT2, TimeUnit.MILLISECONDS)
                //  .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        ;
        MultipartBody mBody;
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        builder.addFormDataPart("myfiles", file.getName(), fileBody1);

        mBody = builder.build();

        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(mBody)
                .url("http://192.168.2.189:8980" + "/front/app/verifyPicture");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //  Log.d("CustomerDisplay", "file.delete():" + );
                Log.d("AllConnects", "请求识别失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求识别成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string();
                    Log.d("AllConnects", "传照片2" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    //  Gson gson = new Gson();
                    //  MenBean menBean = gson.fromJson(jsonObject, MenBean.class);
                    if (jsonObject.get("result").getAsString().equals("true")) {
                        isL = true;
                    }else {
                        isL = false;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tishi.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "klklklkl");
                }
            }
        });
    }


}
