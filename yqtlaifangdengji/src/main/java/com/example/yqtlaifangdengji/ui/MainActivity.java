package com.example.yqtlaifangdengji.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.utils.DateUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.riqi)
    TextView riqi;
    @BindView(R.id.xingqi)
    TextView xingqi;
    @BindView(R.id.shijian)
    TextView shijian;
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //每分钟的广播
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);


        riqi.setText(DateUtils.timeyry(System.currentTimeMillis()+""));
        xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
        shijian.setText(DateUtils.timeshijian(System.currentTimeMillis()+""));
    }


    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_TIME_TICK:
                    riqi.setText(DateUtils.timeyry(System.currentTimeMillis()+""));
                    xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));
                    shijian.setText(DateUtils.timeshijian(System.currentTimeMillis()+""));

            }
        }
    }


    @OnClick(R.id.next)
    public void onViewClicked() {
        //动画
        SpringSystem springSystem3 = SpringSystem.create();
        final Spring spring3 = springSystem3.createSpring();
        //两个参数分别是弹力系数和阻力系数
        spring3.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(80, 8));
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
                next.setScaleX(value);
                next.setScaleY(value);

            }
        });
        // 设置动画结束值
        spring3.setEndValue(1f);


        startActivity(new Intent(MainActivity.this, SelectActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeChangeReceiver);
    }
}
