package com.tripleseven.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class latoultrabold extends androidx.appcompat.widget.AppCompatTextView {



    public latoultrabold(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
        this.setTypeface(face);
    }

    public latoultrabold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
        this.setTypeface(face);
    }

    public latoultrabold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }
}