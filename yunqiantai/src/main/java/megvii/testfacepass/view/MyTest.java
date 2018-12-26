package megvii.testfacepass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import megvii.testfacepass.R;

public class MyTest extends View {
    private int thisWidth,thisHight;
    private int ballX = 0;
    private int ballY = 0;
    private int radius = 200;
    private int width = 12;
    private int startAngle = 0;
    private int angleSpeed = 1;
    private Bitmap bitmapBG=null;
    private Rect rectBG=new Rect();
    private RectF rectY=new RectF();
    int ringX;
    int ringY;
    int ringRadius;
    int ringWidth;
    int headBallX;
    int headBallY;
    Paint paint;
    Paint paintBG;
    Shader sweepGradient;
    private PorterDuffXfermode porterDuffXfermode=null;

    private static final int RED = 255, GREEN = 255, BLUE = 255; //基础颜色，这里是橙红色
   // private static final int RED = 89, GREEN = 213, BLUE = 234; //基础颜色，这里是橙红色
    private static final int MIN_ALPHA =70; //最小不透明度
    private static final int MAX_ALPHA = 230; //最大不透明度
    private int mywidth,myhight;

    private static int[] changeColors = new int[]{
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(235,89,213,234),
           // Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE),
            Color.argb(230,255,255,255)

    };

    private  Matrix matrix = new Matrix();


    public MyTest(Context context) {
        super(context);

        init(ballX , ballY , radius, width, Color.WHITE);

    }

    public MyTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(ballX , ballY , radius, width, Color.WHITE);
    }

    public MyTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(ballX , ballY , radius, width, Color.WHITE);
    }


    public void setData(int width,int hight){
        thisWidth=width;
        thisHight=hight;
        ringX=(width-40)/2+20;
        Log.d("MyTest", "width:" + width);
        ringRadius=(int)((width-40)*0.35);
        ringY=hight+ringRadius;
        rectY = new RectF(ringX - ringRadius, ringY - ringRadius, ringX + ringRadius, ringY + ringRadius);
        //设置渐变
        sweepGradient = new SweepGradient(ringX, ringY, changeColors, null);
    }



    public void init(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor){
       // this.ringX = ringX;
       // this.ringY =  ringY;
       // this.ringRadius =  ringRadius;
        this.ringWidth= ringWidth;
        paint = new Paint();
        paint.reset();
        paint.setColor(ringColor);
        paint.setAntiAlias(true); //消除锯齿
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形,只显示边缘的线，不显示内部
        porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paintBG=new Paint();
        paintBG.setAntiAlias(true); //消除锯齿
        paintBG.setStyle(Paint.Style.FILL);  //绘制空心圆或 空心矩形,只显示边缘的线，不显示内部


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    startAngle += angleSpeed;
                    if (startAngle >= 360)
                        startAngle = 0;
                 //   Log.d("MyTest", "startAngle:" + startAngle);
                //        startAngle %= 360;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }).start();

        bitmapBG=BitmapFactory.decodeResource(getResources(),R.drawable.chusnebg1);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mywidth=getWidth();
        myhight=getHeight();
        rectBG.set(0,0,mywidth,myhight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

      //  Log.d("MyTest", "ringRadius:" + ringRadius);
        if (bitmapBG==null || myhight==0)
            return;

        paintBG.setXfermode(null);
        paintBG.setAntiAlias(true); //消除锯齿
        paintBG.setColor(Color.argb(255,89,213,234));
        paintBG.setStyle(Paint.Style.FILL);  //绘制空心圆或 空心矩形,只显示边缘的线，不显示内部
        canvas.drawBitmap(bitmapBG,null,rectBG,paintBG);

        paintBG.setXfermode(porterDuffXfermode);
        //实心圆
        canvas.drawCircle(ringX, ringY, ringRadius+ringWidth/2, paintBG);

      //  paintBG.setXfermode(null);
      //  paintBG.setAntiAlias(true); //消除锯齿
       // paintBG.setStrokeWidth(ringWidth);
      //  paintBG.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形,只显示边缘的线，不显示内部
       // paintBG.setColor(Color.argb(255,89,213,234));
      //  canvas.drawCircle(ringX, ringY, ringRadius+ringWidth/2, paintBG);

       // canvas.save();
       // canvas.rotate(startAngle,rectY.centerX(),rectY.centerY());

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ringWidth+4);
        paint.setStyle(Paint.Style.STROKE);
        //按照圆心旋转
        matrix.setRotate(startAngle, ringX, ringY);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);
        canvas.drawArc(rectY, 0, 360, false, paint);

        //绘制进度环开头的小圆点
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(MAX_ALPHA, RED, GREEN, BLUE));
        //使用三角函数时，需要把角度转为弧度
        headBallX = ringX + (int)(ringRadius * Math.cos((double)startAngle/180 * Math.PI));
      //  Log.e("degree", "degree: " + startAngle + "cos: " + Math.cos((double)startAngle/180 * Math.PI));
        headBallY = ringY + (int)(ringRadius * Math.sin((double)startAngle/180 * Math.PI));

        canvas.drawCircle(headBallX, headBallY, ringWidth/2+2, paint);

      //  canvas.restore();




    }
}
