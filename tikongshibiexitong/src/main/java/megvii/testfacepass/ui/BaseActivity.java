package megvii.testfacepass.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.objectbox.Box;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Box<BaoCunBean> baoCunBeanDao = MyApplication.myApplication.getBoxStore().boxFor(BaoCunBean.class);
      BaoCunBean  baoCunBean = baoCunBeanDao.get(123456L);
        if (baoCunBean == null) {
            baoCunBean = new BaoCunBean();
            baoCunBean.setId(123456L);
            baoCunBean.setHoutaiDiZhi("http://39.104.180.94:1080");
            baoCunBean.setShibieFaceSize(10);
            baoCunBean.setShibieFaZhi(70);
            baoCunBean.setYudiao(5);
            baoCunBean.setYusu(5);
            baoCunBean.setBoyingren(4);
            baoCunBean.setRuKuFaceSize(60);
            baoCunBean.setRuKuMoHuDu(0.4f);
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

        //1代表横  2代表竖
        switch (baoCunBean.getMoban()){
            case 101://横屏

                startActivity(new Intent(BaseActivity.this,MianBanJiActivity.class));
                finish();
                break;
            case 102:

                break;
            case 103:

                break;

            case 201:
                //默认模版 //竖屏
                startActivity(new Intent(BaseActivity.this,MianBanJiActivity.class));
                finish();
           //  startActivity(new Intent(BaseActivity.this,MainActivity201.class));
             //   finish();

                break;
            case 202:
             //   startActivity(new Intent(BaseActivity.this,MainActivity202.class));
              //  finish();

                break;
            case 203:
                startActivity(new Intent(BaseActivity.this,MianBanJiActivity.class));
                finish();
                break;

        }



    }
}
