package ru.alexey.weather.CustomView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

import ru.alexey.weather.R;

public class CustomView extends View {

    Paint paint;
    Resources resources;
    Bitmap bitmap;
    private boolean pressed = false;
    View.OnClickListener listener;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint =  new Paint();
        paint.setColor(Color.GRAY);
        resources = getResources();
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        int r = bitmap.getHeight()/2;
        super.onDraw(canvas);
        if(pressed){
            canvas.drawCircle(r, r, r, paint);
        }
        else {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int Action = event.getAction();
        if(Action == MotionEvent.ACTION_DOWN){
            pressed = true;
            invalidate();
            if (listener != null) listener.onClick(this);
        }
        else if(Action == MotionEvent.ACTION_UP){
            pressed = false;
            invalidate();
        }
        return true;
    }
}
