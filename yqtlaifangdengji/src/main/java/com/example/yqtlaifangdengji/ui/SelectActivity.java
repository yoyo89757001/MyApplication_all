package com.example.yqtlaifangdengji.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.utils.DateUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;



public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
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
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;
    private int dw, dh;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;
        //每分钟的广播
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);


        final View view1 = View.inflate(SelectActivity.this, R.layout.ss11, null);
        final View view2 = View.inflate(SelectActivity.this, R.layout.ss22, null);
        final View view3 = View.inflate(SelectActivity.this, R.layout.ss33, null);
        final View view4 = View.inflate(SelectActivity.this, R.layout.ss44, null);


        linearLayout.addView(view1);
        linearLayout.addView(view2);
        linearLayout.addView(view3);
        linearLayout.addView(view4);


        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) view1.getLayoutParams();
        layoutParams2.width = (int) ((float) dh * 0.33f);
        layoutParams2.height = (int) ((float) dh * 0.33f);
        layoutParams2.leftMargin = 10;
        layoutParams2.rightMargin = 10;
        view1.setLayoutParams(layoutParams2);
        view1.invalidate();

        LinearLayout.LayoutParams layoutParams22 = (LinearLayout.LayoutParams) view2.getLayoutParams();
        layoutParams22.width = (int) ((float) dh * 0.33f);
        layoutParams22.height = (int) ((float) dh * 0.33f);
        layoutParams22.leftMargin = 10;
        layoutParams22.rightMargin = 10;
        view2.setLayoutParams(layoutParams22);
        view2.invalidate();

        LinearLayout.LayoutParams layoutParams23 = (LinearLayout.LayoutParams) view3.getLayoutParams();
        layoutParams23.width = (int) ((float) dh * 0.33f);
        layoutParams23.height = (int) ((float) dh * 0.33f);
        layoutParams23.leftMargin = 10;
        layoutParams23.rightMargin = 10;
        view3.setLayoutParams(layoutParams23);
        view3.invalidate();


        LinearLayout.LayoutParams layoutParams24 = (LinearLayout.LayoutParams) view4.getLayoutParams();
        layoutParams24.width = (int) ((float) dh * 0.33f);
        layoutParams24.height = (int) ((float) dh * 0.33f);
        layoutParams24.leftMargin = 10;
        layoutParams24.rightMargin = 10;
        view4.setLayoutParams(layoutParams24);
        view4.invalidate();


        linearLayout.getChildAt(0).setOnClickListener(this);
        linearLayout.getChildAt(1).setOnClickListener(this);
        linearLayout.getChildAt(2).setOnClickListener(this);
        linearLayout.getChildAt(3).setOnClickListener(this);

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




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll1: {

                donghua(v);
                Intent intent = new Intent(SelectActivity.this, PaiZhaoActivity.class);
                intent.putExtra("shiyou", "面试");
                startActivity(intent);
            }
                break;

            case R.id.ll2: {
                donghua(v);
                Intent intent = new Intent(SelectActivity.this, PaiZhaoActivity.class);
                intent.putExtra("shiyou", "公务");
                startActivity(intent);
                break;
            }

            case R.id.ll3: {
                donghua(v);
                Intent intent = new Intent(SelectActivity.this, PaiZhaoActivity.class);
                intent.putExtra("shiyou", "亲友");
                startActivity(intent);
                break;
            }
            case R.id.ll4: {
                donghua(v);
                Intent intent = new Intent(SelectActivity.this, PaiZhaoActivity.class);
                intent.putExtra("shiyou", "其他");
                startActivity(intent);
                break;
            }
        }
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
            if (event.equals("guanbi")){
                finish();
            }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeChangeReceiver);
        EventBus.getDefault().unregister(this);
    }



}
