package com.example.hyperion.spacecombatsimulation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ThreadLocalRandom;

public class MenuBackground extends SurfaceView implements SurfaceHolder.Callback  {

    private DrawThread thread;
    private Paint paint = new Paint();
    private int[] starSize, starColor;
    private int[] starX, starY, starZ;
    private int starCount = 500, starBrightness = 230;
    public static int width, height;

    private int random(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max); }


    public MenuBackground(Context context) { super(context); getHolder().addCallback(this); }
    public MenuBackground(Context context, AttributeSet attrs) { super(context, attrs); getHolder().addCallback(this); }
    public MenuBackground(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); getHolder().addCallback(this); }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(8,0,0, Color.BLUE);

        width = w; height = h;
        Main.height = h;
        Log.d("Call menu","surfaceChanged");

        starSize = new int[starCount]; starColor = new int[starCount];
        starX = new int[starCount]; starY = new int[starCount]; starZ = new int[starCount];

        for (int i = 0; i < starCount; i++) {
            starX[i] = random(1, width * 10); starY[i] = random(1, height * 10);
            starZ[i] = random(1, 20);
            starSize[i] = random(1, 3);
            starColor[i] = random(1, starBrightness);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setWillNotDraw(true);
        thread = new DrawThread(holder, this);
        thread.setRunning(true);
        thread.start();
        Log.d("Thread menu", "Started");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        try {
            thread.setRunning(false);
            thread.join();
            Log.d("Thread menu", "Joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        for (int i = 0; i < starCount; i++) {
            int bright = starColor[i];
            paint.setColor(Color.argb(bright, bright, bright, bright));
            canvas.drawCircle((float) starX[i] / 10, (float) starY[i] / 10, starSize[i], paint);
        }
    }

    protected void doDraw() {

        postInvalidate();

        for (int i = 0; i < starCount; i++) {
            if (starX[i] <= 1) {
                starX[i] = width * 10 + 30;
                starY[i] = random(1, height * 10);
                starZ[i] = Math.max(1, Math.min(20, starZ[i] + random(-3, 3)));
                starSize[i] = random(1, 3);
                starColor[i] = random(1, starBrightness);
            } else {
                starX[i] -= starZ[i] * 1.5;
            }
        }
    }
}
