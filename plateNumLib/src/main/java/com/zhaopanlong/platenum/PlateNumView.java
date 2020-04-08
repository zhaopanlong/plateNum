package com.zhaopanlong.platenum;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

public class PlateNumView extends LinearLayout {
    private static final int DEFAULT_TEXTSIZE = 40;
    private static final int DEFAULT_TEXTCOLOR = 0xff333333;
    private static final int DEFAULT_LINENORMALCOLOR = 0xff999999;
    private static final int DEFAULT_LINECHECKEDCOLOR = 0xff4187EE;

    private Context mContext;
    private TextViewOnClickListen mTextViewOnClickListen;
    private KeyBoardPop mKeyboardPop;

    //8位包含新能源
    private int mViewCount = 8;
    private View[] mViewArrs = new View[mViewCount];
    private int mIndicatePostion = -1;

    private int mTextSize;
    private int mTextColor;
    private int mLineWidth;
    private int mItemMargin;
    private int mLineNormalColor;
    private int mLineCheckedColor;
    private boolean mDefaultShowProvince = false;

    public PlateNumView(Context context) {
        this(context, null);
    }

    public PlateNumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(context, attrs, defStyleAttr);
        initViews();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlateNumView, defStyleAttr, 0);

        mTextColor = typedArray.getColor(R.styleable.PlateNumView_pnvTextColor, DEFAULT_TEXTCOLOR);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.PlateNumView_pnvTextSize, DEFAULT_TEXTSIZE);
        mTextSize = Util.px2sp(mContext, mTextSize);

        mLineWidth = (int) typedArray.getDimension(R.styleable.PlateNumView_pnvLineWidth, Util.dp2px(mContext, 1));
        mItemMargin = (int) typedArray.getDimension(R.styleable.PlateNumView_pnvitemMargin, Util.dp2px(mContext, 5));
        mLineNormalColor = typedArray.getColor(R.styleable.PlateNumView_pnvLineNormalColor, DEFAULT_LINENORMALCOLOR);
        mLineCheckedColor = typedArray.getColor(R.styleable.PlateNumView_pnvLineCheckedColor, DEFAULT_LINECHECKEDCOLOR);
        mDefaultShowProvince = typedArray.getBoolean(R.styleable.PlateNumView_pnvShowProvince, false);
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        setGravity(Gravity.CENTER_VERTICAL);

        mTextViewOnClickListen = new TextViewOnClickListen();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        for (int i = 0; i < mViewCount; i++) {
            DrawableTextView textView = (DrawableTextView) inflater.inflate(R.layout.textview, null);
            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            textView.setTextColor(mTextColor);
            textView.setTextSize(mTextSize);

            //设置新能源车牌
            if (i == mViewCount - 1) {
                Drawable drawableTop = AppCompatResources.getDrawable(mContext, R.mipmap.icaon_add_destribute);
                textView.setDrawable(DrawableTextView.TOP, drawableTop, drawableTop.getMinimumWidth(), drawableTop.getIntrinsicHeight());
                textViewParams.setMargins(0, 0, 0, 0);
                textView.setTextSize((float) (mTextSize / 1.5));
                textView.setCompoundDrawablePadding(5);
                textView.setHint("新能源");
                textView.setBackground(getNewEnergyNormalBg());
            } else {
                textViewParams.setMargins(0, 0, mItemMargin, 0);
                textView.setBackground(getNormalBg());
            }

            addView(textView, textViewParams);

            //设置 .
            if (i == 1) {
                View dot = new View(mContext);
                dot.setBackgroundResource(R.drawable.shape_dot);
                LayoutParams dotParams = new LayoutParams(16, 16);
                dotParams.setMargins(0, 0, mItemMargin, 0);
                addView(dot, dotParams);
            }
            mViewArrs[i] = textView;

            textView.setOnClickListener(mTextViewOnClickListen);
        }

        mKeyboardPop = new KeyBoardPop((Activity) mContext);
        mKeyboardPop.setKeyBordlisten(new LicensePlateView.KeyBordClickListen() {
            @Override
            public void text(String text) {
                DrawableTextView textView = (DrawableTextView) mViewArrs[mIndicatePostion];
                textView.setText(text);
                if (mIndicatePostion != mViewCount - 1) {
                    mIndicatePostion++;
                } else {
                    //输入类型为新能源 也就是最后一位
                    if (mIndicatePostion == mViewCount - 1) {
                        textView.setTextSize(mTextSize);
                        textView.setDrawable(DrawableTextView.TOP, null, 0, 0);
                    }

                    mKeyboardPop.dismiss();
                    return;
                }
                resetBg(mIndicatePostion);
                showKeyBoard();
            }

            @Override
            public void delete() {
                DrawableTextView textView = (DrawableTextView) mViewArrs[mIndicatePostion];
                textView.setText(null);
                if (mIndicatePostion == 0) {
                    return;
                }
                if (mIndicatePostion == mViewCount - 1) {
                    //最后一位新能源
                    Drawable drawableTop = AppCompatResources.getDrawable(mContext, R.mipmap.icaon_add_destribute);
                    textView.setDrawable(DrawableTextView.TOP, drawableTop, drawableTop.getMinimumWidth(), drawableTop.getIntrinsicHeight());
                    textView.setTextSize((float) (mTextSize / 1.5));
                }
                mIndicatePostion--;
                resetBg(mIndicatePostion);
                showKeyBoard();
            }
        });
        if (mDefaultShowProvince) {
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理默认弹出省份键盘
            if (null != ((Activity) mContext).getWindow().getDecorView().getWindowToken()) {
                mIndicatePostion = 0;
                mViewArrs[0].callOnClick();
            } else {
                handler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };

    private class TextViewOnClickListen implements OnClickListener {

        @Override
        public void onClick(View v) {
            //重置指示器
            resetBg(v);
            //显示键盘
            showKeyBoard();
        }

    }

    /**
     * 根据点击的view刷新组件
     *
     * @param view
     */
    private void resetBg(View view) {
        for (int i = 0; i < mViewArrs.length; i++) {
            View mViewArr = mViewArrs[i];
            if (mViewArr == view) {
                mIndicatePostion = i;
                break;
            }
        }

        resetBg(mIndicatePostion);

    }

    /**
     * 根据postion下标刷新组件
     *
     * @param postion
     */
    private void resetBg(int postion) {
        for (int i = 0; i < mViewArrs.length; i++) {
            View view = mViewArrs[i];

            if (postion == i){
                if (i == mViewCount-1){
                    view.setBackground(getNewEnergyCheckedBg());
                }else {
                    view.setBackground(getCheckedBg());
                }
            }else {
                if (i == mViewCount-1){
                    view.setBackground(getNewEnergyNormalBg());
                }else {
                    view.setBackground(getNormalBg());
                }
            }
        }
    }

    /**
     * 获取指示位置背景
     *
     * @return
     */
    private GradientDrawable getCheckedBg() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(0xffffffff);
        gradientDrawable.setStroke(mLineWidth, mLineCheckedColor);
        gradientDrawable.setCornerRadius(Util.dp2px(mContext, 5));
        return gradientDrawable;
    }

    /**
     * 获取新能源的选中背景
     *
     * @return
     */
    private GradientDrawable getNewEnergyCheckedBg() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(0xffffffff);
        gradientDrawable.setStroke(mLineWidth, mLineCheckedColor, (float) Util.dp2px(mContext, 2), (float) Util.dp2px(mContext, 2));
        gradientDrawable.setCornerRadius(Util.dp2px(mContext, 5));
        return gradientDrawable;
    }

    /**
     * 获取普通背景
     *
     * @return
     */
    private GradientDrawable getNormalBg() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(0xffffffff);
        gradientDrawable.setStroke(mLineWidth, mLineNormalColor);
        gradientDrawable.setCornerRadius(Util.dp2px(mContext, 5));
        return gradientDrawable;
    }

    /**
     * 获取新能源普通状态背景
     *
     * @return
     */
    private GradientDrawable getNewEnergyNormalBg() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(0xffffffff);
        gradientDrawable.setStroke(mLineWidth, mLineNormalColor, (float) Util.dp2px(mContext, 2), (float) Util.dp2px(mContext, 2));
        gradientDrawable.setCornerRadius(Util.dp2px(mContext, 5));
        return gradientDrawable;
    }

    /**
     * 展示键盘
     */
    private void showKeyBoard() {
        //判断输入的是省份还是数字和字母
        if (mIndicatePostion == 0) {
            mKeyboardPop.showProvince();
        } else {
            mKeyboardPop.showNum();
        }
        if (!mKeyboardPop.isShowing()) {
            mKeyboardPop.showAtLocation(mViewArrs[0], Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 获取车牌号
     *
     * @return
     */
    public String getPlateNum() {
        String plateNUm = "";
        for (View mViewArr : mViewArrs) {
            plateNUm += ((TextView) mViewArr).getText();
        }
        return plateNUm;
    }

    /**
     * 设置默认车牌号
     *
     * @param plateNum
     */
    public void setPlateNum(String plateNum) {
        if (TextUtils.isEmpty(plateNum)) {
            return;
        }
        char[] chars = plateNum.toCharArray();
        for (int i = 0; i < mViewArrs.length; i++) {
            DrawableTextView textView = (DrawableTextView) mViewArrs[i];
            textView.setText(String.valueOf(chars[i]));
            if (i == mViewCount - 1) {
                textView.setTextSize(mTextSize);
                textView.setDrawable(DrawableTextView.TOP, null, 0, 0);
            }
        }
    }

    /**
     * 清空车牌号 默认不隐藏键盘 并且指示到第一个展示省份
     */
    public void clearPlateNum() {
        clearPlateNum(false);
    }

    /**
     * 清空车牌号是否隐藏车牌键盘
     * 如果ture 就隐藏键盘
     *
     * @param hidleKeyBoard
     */
    public void clearPlateNum(boolean hidleKeyBoard) {
        for (int i = 0; i < mViewArrs.length; i++) {
            DrawableTextView textView = (DrawableTextView) mViewArrs[i];
            if (i == mViewCount - 1) {
                Drawable drawableTop = AppCompatResources.getDrawable(mContext, R.mipmap.icaon_add_destribute);
                textView.setDrawable(DrawableTextView.TOP, drawableTop, drawableTop.getMinimumWidth(), drawableTop.getIntrinsicHeight());
                textView.setTextSize((float) (mTextSize / 1.5));
            }
            textView.setText(null);
        }
        if (hidleKeyBoard) {
            mIndicatePostion = 0;
            resetBg(mIndicatePostion);
            mKeyboardPop.dismiss();
        } else {
            mViewArrs[0].callOnClick();
        }

    }

    /**
     * 隐藏底部键盘
     */
    public void hidePlateKeyBord() {
        mKeyboardPop.dismiss();
    }


}
