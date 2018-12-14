package megvii.testfacepass.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;


import megvii.testfacepass.R;



/**
 * Created by QZD on 2017/11/30.
 */

public class ScaleTextView extends AppCompatTextView {
    //设计尺寸
    public static int designedWidth=720;
    private int baseScreenHeight = 720;

    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray type = context.obtainStyledAttributes(attrs,R.styleable.ScaleTextView);//获得属性值
        int i = type.getInteger(R.styleable.ScaleTextView_textSizePx, 25);
        Log.d("LOGCAT","i:"+i);
        baseScreenHeight = type.getInteger(R.styleable.ScaleTextView_baseScreenHeight, designedWidth);
        Log.d("LOGCAT","baseScreenHeight:"+baseScreenHeight);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(i));
        boolean _isBold=type.getBoolean(R.styleable.ScaleTextView_textBold, false);
        getPaint().setFakeBoldText(_isBold);
    }

    /**
     * 获取当前手机屏幕分辨率，然后根据和设计图的比例对照换算实际字体大小
     * @param textSize
     * @return
     */
    private int getFontSize(int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int rate = (int) (textSize * (float) screenHeight / baseScreenHeight);
        return rate;
    }
}