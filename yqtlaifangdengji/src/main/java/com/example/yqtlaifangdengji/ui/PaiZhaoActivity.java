package com.example.yqtlaifangdengji.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.utils.DateUtils;
import com.example.yqtlaifangdengji.utils.FileUtil;
import com.example.yqtlaifangdengji.utils.GlideCircleTransform;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private String shiyou = null;
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;
    private int dw, dh;
    private RequestOptions myOptions = new RequestOptions()
        .fitCenter()
        .override(320,320)
        .transform(new GlideCircleTransform( PaiZhaoActivity.this));


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
               startActivity(new Intent(PaiZhaoActivity.this,CameraActivity.class));

                break;
            case R.id.queren:


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
        if (event.equals("tupiangengxin")){
           // Log.d("PaiZhaoActivity", "更新图片");

           // File file = new File(Environment.getExternalStorageDirectory(), "mmruitong.png");//将要保存图片的路径
//            Glide.get(PaiZhaoActivity.this).clearMemory();
//            Glide.with(PaiZhaoActivity.this)
//                    .load(Environment.getExternalStorageDirectory()+File.separator+"mmruitong.png")
//                    .apply(myOptions)
//                    .into(paizhao);

            paizhao.setImageBitmap(FileUtil.toRoundBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+File.separator+"mmruitong.png")));


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

}
