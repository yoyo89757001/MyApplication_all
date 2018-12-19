package megvii.testfacepass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FanceIdentification extends View {

    private Paint paint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint linePaint = new Paint();
    private List<Point> pointList = new ArrayList<>();
    int tag = 0;

    public FanceIdentification(Context context, AttributeSet attrs) {
        super(context);
        initPaint(paint);
        initDta();

    }

    private void initDta() {
        Point textPoint1 = new Point(110, 500);
        Point textPoint2 = new Point(110, 500);
        Point textPoint3 = new Point(110, 500);
        Point textPoint4 = new Point(110, 500);
        Point textPoint5 = new Point(110, 500);
        Point textPoint6 = new Point(110, 500);
        Point textPoint7 = new Point(110, 500);
        Point textPoint8 = new Point(110, 500);
        Point textPoint9 = new Point(110, 500);
        Point textPoint10 = new Point(110, 500);
        Point textPoint11 = new Point(110, 500);
        Point textPoint12 = new Point(110, 500);

        pointList.add(textPoint1);
        pointList.add(textPoint2);
        pointList.add(textPoint3);
        pointList.add(textPoint4);
        pointList.add(textPoint5);
        pointList.add(textPoint6);
        pointList.add(textPoint7);
        pointList.add(textPoint8);
        pointList.add(textPoint9);
        pointList.add(textPoint10);
        pointList.add(textPoint11);
        pointList.add(textPoint12);
    }

    //初始化画笔
    private void initPaint(Paint paint) {
        //设置画笔颜色
        paint.setColor(0xffffffff);
        linePaint.setColor(0xffffffff);
        circlePaint.setColor(0x55000000);
        //抗锯齿
        paint.setAntiAlias(true);
        circlePaint.setAntiAlias(true);
        linePaint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        circlePaint.setDither(true);
        linePaint.setDither(true);
        //设置画笔样式为空心
        paint.setStyle(Paint.Style.STROKE);
        circlePaint.setStyle(Paint.Style.FILL);
        //设置画笔大小
        paint.setStrokeWidth(10);
        linePaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(367, 367, 352, circlePaint);

        if(tag == 1){
            onDrawOne(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag==2){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag==3){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag==4){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag==5){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            onDrawFive(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag == 6){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            onDrawFive(canvas);
            onDrawSix(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag == 7){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            onDrawFive(canvas);
            onDrawSix(canvas);
            onDawSeven(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag == 8){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            onDrawFive(canvas);
            onDrawSix(canvas);
            onDawSeven(canvas);
            onDrawEight(canvas);
            tag++;
            postInvalidateDelayed(200);
        }else if(tag==9){
            onDrawOne(canvas);
            onDrawTwo(canvas);
            onDrawThree(canvas);
            onDrawFour(canvas);
            onDrawFive(canvas);
            onDrawSix(canvas);
            onDawSeven(canvas);
            onDrawEight(canvas);
            onDrawNight(canvas);
            tag=0;
            postInvalidateDelayed(200);
        }else {
            circlePaint.setColor(0x0000000);
            canvas.drawCircle(367, 367, 352, circlePaint);
            tag++;
            postInvalidateDelayed(200);
        }
    }

    private void onDrawNight(Canvas canvas) {
        //第九帧
        canvas.drawLine(315, 410,400,510,linePaint);
        canvas.drawLine(442, 400,400,510,linePaint);
    }

    private void onDrawEight(Canvas canvas) {
        //第八帧
        canvas.drawLine(380, 360,248, 296,linePaint);
    }

    private void onDawSeven(Canvas canvas) {
        //第七帧
        canvas.drawLine(380,360,475,282,linePaint);
    }

    private void onDrawSix(Canvas canvas) {
        //第六帧
        canvas.drawLine(475,280,404,282,linePaint);
        canvas.drawLine(343,286,404,282,linePaint);
        canvas.drawLine(343,286,248,296,linePaint);
    }

    private void onDrawFive(Canvas canvas) {
        //第五帧
        canvas.drawLine(445,250,404,282,linePaint);
        canvas.drawLine(380,360,315,410,linePaint);
        canvas.drawLine(380,360,404,282,linePaint);
        canvas.drawLine(243,392,315, 410,linePaint);
    }

    private void onDrawFour(Canvas canvas) {
        //第四帧
        canvas.drawLine(343,286,300,250,linePaint);
        canvas.drawLine(380,360,343,286,linePaint);
        canvas.drawLine(380,360,442,400,linePaint);
        canvas.drawLine(508,378,442,400,linePaint);
    }

    public void onDrawThree(Canvas canvas) {
        // 第三帧
        canvas.drawLine(300,250,445,250,linePaint);
        canvas.drawLine(445,250,475,280,linePaint);
        canvas.drawLine(300,250,248,296,linePaint);
        canvas.drawLine(243,392,248,296,linePaint);
        canvas.drawLine(508,378,475,280,linePaint);
        canvas.drawLine(400,510,243,392,linePaint);
        canvas.drawLine(400,510,508,378,linePaint);
    }

    public void onDrawTwo(Canvas canvas) {
        //第二帧
        canvas.drawPoint(248, 296, paint);//左眼左
        canvas.drawPoint(445, 250, paint);//右眼中
        canvas.drawPoint(508, 378, paint);//右脸
        canvas.drawPoint(300, 250, paint);//左眼中
        canvas.drawPoint(475, 280, paint);//右眼右
        canvas.drawPoint(243, 392, paint);//左脸
        canvas.drawPoint(400, 510, paint);//下巴
    }

    public void onDrawOne(Canvas canvas) {
        //第一帧
        canvas.drawPoint(315, 410, paint);//嘴巴左
        canvas.drawPoint(380, 360, paint);//鼻子
        canvas.drawPoint(442, 400, paint);//嘴巴右
        canvas.drawPoint(343, 286, paint);//左眼右
        canvas.drawPoint(404, 282, paint);//右眼右
    }
}
