package com.zhaopanlong.platenum;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Method;

public class PlateNumEditText  extends androidx.appcompat.widget.AppCompatEditText implements LicensePlateView.KeyBordClickListen {

    private KeyBoardPop mKeyBoardPop;
    public PlateNumEditText(Context context) {
        super(context);
        init();
    }

    public PlateNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlateNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
           setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity)getContext()).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mKeyBoardPop = new KeyBoardPop(getContext());
        mKeyBoardPop.setKeyBordlisten(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mKeyBoardPop.isShowing()){
                    mKeyBoardPop.showAtLocation(PlateNumEditText.this,Gravity.BOTTOM,0,0);
                }
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !mKeyBoardPop.isShowing()){
                    mKeyBoardPop.showAtLocation(PlateNumEditText.this,Gravity.BOTTOM,0,0);
                }
            }
        });
    }

    @Override
    public void text(String text) {
        append(text);
        mKeyBoardPop.showNum();
    }

    @Override
    public void delete() {
        int index = getSelectionStart();
        if (index == 0){
            return;
        }
        if (index == 1){
            mKeyBoardPop.showProvince();
        }
        getText().delete(index-1,index);
    }
}
