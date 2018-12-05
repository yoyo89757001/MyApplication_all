package com.example.yqtlaifangdengji.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yqtlaifangdengji.R;
import com.example.yqtlaifangdengji.bean.ChaXun;
import com.example.yqtlaifangdengji.utils.DateUtils;
import com.example.yqtlaifangdengji.utils.GsonUtil;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChaXunActivity extends AppCompatActivity implements MoBanAdapter.OnRvItemClick {

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
    @BindView(R.id.shuru)
    TextView shuru;
    @BindView(R.id.al)
    LinearLayout al;
    @BindView(R.id.bl)
    LinearLayout bl;
    @BindView(R.id.cl)
    LinearLayout cl;
    @BindView(R.id.dl)
    LinearLayout dl;
    @BindView(R.id.el)
    LinearLayout el;
    @BindView(R.id.fl)
    LinearLayout fl;
    @BindView(R.id.gl)
    LinearLayout gl;
    @BindView(R.id.hl)
    LinearLayout hl;
    @BindView(R.id.il)
    LinearLayout il;
    @BindView(R.id.jl)
    LinearLayout jl;
    @BindView(R.id.kl)
    LinearLayout kl;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.ml)
    LinearLayout ml;
    @BindView(R.id.nl)
    LinearLayout nl;
    @BindView(R.id.ol)
    LinearLayout ol;
    @BindView(R.id.pl)
    LinearLayout pl;
    @BindView(R.id.ql)
    LinearLayout ql;
    @BindView(R.id.rl)
    LinearLayout rl;
    @BindView(R.id.sl)
    LinearLayout sl;
    @BindView(R.id.tl)
    LinearLayout tl;
    @BindView(R.id.ul)
    LinearLayout ul;
    @BindView(R.id.vl)
    LinearLayout vl;
    @BindView(R.id.wl)
    LinearLayout wl;
    @BindView(R.id.xl)
    LinearLayout xl;
    @BindView(R.id.yl)
    LinearLayout yl;
    @BindView(R.id.zl)
    LinearLayout zl;
    @BindView(R.id.shanchu)
    LinearLayout shanchu;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tishitishi)
    TextView tishitishi;
    private IntentFilter intentFilter;
    private TimeChangeReceiver timeChangeReceiver;
    private int dw, dh;
    private StringBuilder builder = new StringBuilder();
    private List<ChaXun.ListBean> listBeanList = new ArrayList<>();

    private GridLayoutManager gridLayoutManager2 = new GridLayoutManager(ChaXunActivity.this, 2, LinearLayoutManager.VERTICAL, false);
    private MoBanAdapter adapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_xun);
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

        recyclerview.setLayoutManager(gridLayoutManager2);
        adapter = new MoBanAdapter(listBeanList, ChaXunActivity.this, dw, dh, this);
        recyclerview.setAdapter(adapter);
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



    @OnClick({R.id.al, R.id.bl, R.id.cl, R.id.dl, R.id.el, R.id.fl, R.id.gl, R.id.hl, R.id.il, R.id.jl, R.id.kl, R.id.ll, R.id.ml, R.id.nl, R.id.ol, R.id.pl, R.id.ql, R.id.rl, R.id.sl, R.id.tl, R.id.ul, R.id.vl, R.id.wl, R.id.xl, R.id.yl, R.id.zl, R.id.shanchu})
    public void onViewClicked(View view) {
        donghua(view);
        switch (view.getId()) {
            case R.id.al:
                builder.append("A");
                shuru.setText(builder.toString());

                break;
            case R.id.bl:
                builder.append("B");
                shuru.setText(builder.toString());

                break;
            case R.id.cl:
                builder.append("C");
                shuru.setText(builder.toString());

                break;
            case R.id.dl:
                builder.append("D");
                shuru.setText(builder.toString());

                break;
            case R.id.el:
                builder.append("E");
                shuru.setText(builder.toString());

                break;
            case R.id.fl:
                builder.append("F");
                shuru.setText(builder.toString());

                break;
            case R.id.gl:
                builder.append("G");
                shuru.setText(builder.toString());

                break;
            case R.id.hl:
                builder.append("H");
                shuru.setText(builder.toString());

                break;
            case R.id.il:
                builder.append("I");
                shuru.setText(builder.toString());

                break;
            case R.id.jl:
                builder.append("J");
                shuru.setText(builder.toString());

                break;
            case R.id.kl:
                builder.append("K");
                shuru.setText(builder.toString());

                break;
            case R.id.ll:
                builder.append("L");
                shuru.setText(builder.toString());

                break;
            case R.id.ml:
                builder.append("M");
                shuru.setText(builder.toString());

                break;
            case R.id.nl:
                builder.append("N");
                shuru.setText(builder.toString());

                break;
            case R.id.ol:
                builder.append("O");
                shuru.setText(builder.toString());

                break;
            case R.id.pl:
                builder.append("P");
                shuru.setText(builder.toString());

                break;
            case R.id.ql:
                builder.append("Q");
                shuru.setText(builder.toString());

                break;
            case R.id.rl:
                builder.append("R");
                shuru.setText(builder.toString());

                break;
            case R.id.sl:
                builder.append("S");
                shuru.setText(builder.toString());

                break;
            case R.id.tl:
                builder.append("T");
                shuru.setText(builder.toString());

                break;
            case R.id.ul:
                builder.append("U");
                shuru.setText(builder.toString());

                break;
            case R.id.vl:
                builder.append("V");
                shuru.setText(builder.toString());

                break;
            case R.id.wl:
                builder.append("W");
                shuru.setText(builder.toString());

                break;
            case R.id.xl:
                builder.append("X");
                shuru.setText(builder.toString());

                break;
            case R.id.yl:
                builder.append("Y");
                shuru.setText(builder.toString());

                break;
            case R.id.zl:
                builder.append("Z");
                shuru.setText(builder.toString());

                break;
            case R.id.shanchu:
                if (!builder.toString().equals("")) {
                    builder.deleteCharAt(builder.length() - 1);
                    shuru.setText(builder.toString().trim());
                }
                break;

        }
        if (builder.length() != 0)
            link_chaxun();
    }

    @Override
    public void onItemClick(View v, int position) {
        donghua(v);
Log.d("ChaXunActivity", "房贷首付都是风");
        link(new File(Environment.getExternalStorageDirectory() + File.separator + "mmruitong.png"),listBeanList.get(position));

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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeChangeReceiver);
        EventBus.getDefault().unregister(this);
    }


    //上传识别记录2
    private void link_chaxun() {
        listBeanList.clear();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        RequestBody body = null;

        body = new FormBody.Builder()
                //.add("name", subject.getName()) //
                .add("companyId", "G6002") //公司di
                .add("name", builder.toString()) //公司名称
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url("http://192.168.2.189:8980/front" + "/app/findEmployes");

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
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "查询id" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    Gson gson = new Gson();
                    final ChaXun dingBean = gson.fromJson(jsonObject, ChaXun.class);
                    if (dingBean.getList()!=null && dingBean.getList().size()>0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listBeanList.addAll(dingBean.getList());
                                recyclerview.setVisibility(View.VISIBLE);
                                tishitishi.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerview.setVisibility(View.GONE);
                                tishitishi.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }


                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");

                }
            }
        });
    }


    // 照片质量
    private void link(final File file,ChaXun.ListBean listBean) {

//        if (screen_token.equals("") || url.equals("")) {
//
//            return;
//        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
                //  .cookieJar(new CookiesManager())
               // .retryOnConnectionFailure(true)
                .build();
        ;
        MultipartBody mBody;
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        builder.addFormDataPart("myfiles", file.getName(), fileBody1);
        builder.addFormDataPart("companyId","G6002");
        builder.addFormDataPart("employeeId",listBean.getId());
        builder.addFormDataPart("visitDate","2018-11-11");
        builder.addFormDataPart("rangeDate","09:00-18:00");
        builder.addFormDataPart("visitOrigin","2");
        builder.addFormDataPart("numberPeople","1");
        builder.addFormDataPart("registerSubjects[0].name","周三");
        builder.addFormDataPart("registerSubjects[0].phone","周三");

        mBody = builder.build();

        Request.Builder requestBuilder = new Request.Builder()
                .header("Content-Type", "application/json")
                .post(mBody)
                .url("http://192.168.2.189:8980" + "/front/weixin/registerVisitSave");

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
                    Log.d("AllConnects", "有意义" + ss);


                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "klklklkl");
                }
            }
        });
    }



}
