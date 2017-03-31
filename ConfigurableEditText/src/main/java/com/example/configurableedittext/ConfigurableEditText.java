package com.example.configurableedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by angitha.das on 14-03-2017.
 */

public class ConfigurableEditText extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener,
        TextWatcherAdapter.TextWatcherListener, View.OnTouchListener {
    private Drawable dRight;
    private Drawable icon;
    private boolean clear = false;

    public ConfigurableEditText(Context context) {
        super(context);
    }

    public ConfigurableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttr(context, attrs);
        init();
    }

    public ConfigurableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttr(context, attrs);
        init();

    }

    private void init() {
        if (clear) {
            clearFunctionality();
        }
    }

    private void processAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ConfigurableEditText, 0, 0);
        clear = ta.getBoolean(R.styleable.ConfigurableEditText_clear, false);
        icon = ta.getDrawable(R.styleable.ConfigurableEditText_clear_icon);
        ta.recycle();
    }

    void clearFunctionality() {
        checkCompoundDrawable();
        handleClearButton();
        this.setOnFocusChangeListener(this);
        this.setOnTouchListener(this);
        this.addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    void checkCompoundDrawable() {
//        Drawable[] compoundDrawables = getCompoundDrawables();
//        if (compoundDrawables[2] != null)
        if (icon != null) {
            dRight = icon; //compoundDrawables[2];
        } else {
            dRight = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_clear_black_24dp, null);
        }
        dRight.setBounds(0, 0, dRight.getIntrinsicWidth(), dRight.getIntrinsicHeight());
    }


    private void handleClearButton() {
        if (TextUtils.isEmpty(this.getText())) {
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        } else {
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], dRight, this.getCompoundDrawables()[3]);
        }
    }

    private void handleFocus(Boolean hasFocus) {
        if (!hasFocus) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        } else {
            if (TextUtils.isEmpty(this.getText())) {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
            } else {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], dRight, getCompoundDrawables()[3]);
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        ConfigurableEditText.this.handleFocus(hasFocus);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ConfigurableEditText et = ConfigurableEditText.this;
        if (et.getCompoundDrawables()[2] == null)
            return false;

        if (motionEvent.getAction() != MotionEvent.ACTION_UP)
            return false;

        if (motionEvent.getX() > et.getWidth() - et.getPaddingRight() - dRight.getIntrinsicWidth()) {
            et.setText(null);
            ConfigurableEditText.this.handleClearButton();
        }
        return false;
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        ConfigurableEditText.this.handleClearButton();
    }
}