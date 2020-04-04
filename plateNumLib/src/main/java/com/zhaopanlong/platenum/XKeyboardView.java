package com.zhaopanlong.platenum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * 数字键盘。
 *
 * @author wuzhen
 */
public class XKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {


    private int mDeleteColor;
    private Drawable mDeleteDrawable;
    private Drawable keyBackgroundDrawable;

    private IOnKeyboardListener mOnKeyboardListener;

    public XKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public XKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XKeyboardView,
                defStyleAttr, 0);
        mDeleteDrawable = a.getDrawable(R.styleable.XKeyboardView_deleteDrawable);
        mDeleteColor = a.getColor(
                R.styleable.XKeyboardView_deleteColor, Color.parseColor("#333333"));
        keyBackgroundDrawable = a.getDrawable(R.styleable.XKeyboardView_deleteBackground);
        a.recycle();

        // 设置软键盘按键的布局
        Keyboard keyboard = new Keyboard(context, R.xml.qwerty);
        setKeyboard(keyboard);

        setEnabled(true);
        setPreviewEnabled(true); // 设置按键没有点击放大镜显示的效果
        setOnKeyboardActionListener(this);
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey) {
        return super.onLongPress(popupKey);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 遍历所有的按键
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            // 如果是左下角空白的按键，重画按键的背景
            // 如果是右下角的删除按键，重画按键的背景，并且绘制删除图标
            if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                int[] drawableState = key.getCurrentDrawableState();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < drawableState.length; i++) {
                    sb.append(String.valueOf(drawableState[i]) + "|");
                }
                Log.e("drawableState", sb.toString());
                keyBackgroundDrawable.setState(drawableState);
                final Rect bounds = keyBackgroundDrawable.getBounds();
                if (key.width != bounds.right ||
                        key.height != bounds.bottom) {
                    keyBackgroundDrawable.setBounds(0, 0, key.width, key.height);
                }
                canvas.save();
                canvas.translate(key.x + getPaddingLeft(), key.y + getPaddingTop());
                keyBackgroundDrawable.draw(canvas);
                canvas.restore();
                drawDeleteButton(key, canvas);
            }
        }
    }

    // 绘制按键的背景
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas, int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        drawable.setBounds(key.x, key.y + getPaddingTop(), key.x + key.width, key.y + getPaddingTop() + key.height);
        drawable.draw(canvas);
    }

    // 绘制删除按键
    private void drawDeleteButton(Keyboard.Key key, Canvas canvas) {
        if (mDeleteDrawable == null) {
            return;
        }

        // 计算删除图标绘制的坐标
        int drawWidth, drawHeight;
        int intrinsicWidth = mDeleteDrawable.getIntrinsicWidth();
        int intrinsicHeight = mDeleteDrawable.getIntrinsicHeight();

        drawWidth = intrinsicWidth;
        drawHeight = intrinsicHeight;

        // 限制图标的大小，防止图标超出按键
        if (drawWidth > key.width) {
            drawWidth = key.width;
            drawHeight = drawWidth * intrinsicHeight / intrinsicWidth;
        }
        if (drawHeight > key.height) {
            drawHeight = key.height;
            drawWidth = drawHeight * intrinsicWidth / intrinsicHeight;
        }

        // 获取删除图标绘制的坐标
        int left = key.x + (key.width - drawWidth) / 2;
        int top = key.y + (key.height - drawHeight) / 2 + getPaddingTop();
        Rect mDeleteDrawRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        // 绘制删除的图标
        if (mDeleteDrawRect != null && !mDeleteDrawRect.isEmpty()) {
            mDeleteDrawable.setBounds(mDeleteDrawRect.left, mDeleteDrawRect.top,
                    mDeleteDrawRect.right, mDeleteDrawRect.bottom);
            mDeleteDrawable.setColorFilter(mDeleteColor, PorterDuff.Mode.SRC_IN);
            mDeleteDrawable.draw(canvas);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        // 处理按键的点击事件
        // 点击了删除按键
        if (primaryCode == Keyboard.KEYCODE_DELETE) {
            if (mOnKeyboardListener != null)
                mOnKeyboardListener.onDeleteKeyEvent();
        }
        // 点击了
        else {
            if (mOnKeyboardListener != null) {
                mOnKeyboardListener.onInsertKeyEvent(Character.toString(
                        (char) primaryCode));
            }
        }
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * 设置键盘的监听事件。
     *
     * @param listener 监听事件
     */
    public void setIOnKeyboardListener(IOnKeyboardListener listener) {
        this.mOnKeyboardListener = listener;
    }

    /**
     * 键盘的监听事件。
     */
    public interface IOnKeyboardListener {

        /**
         * 点击数字按键。
         *
         * @param text 输入的数字
         */
        void onInsertKeyEvent(String text);

        /**
         * 点击了删除按键。
         */
        void onDeleteKeyEvent();
    }
}
