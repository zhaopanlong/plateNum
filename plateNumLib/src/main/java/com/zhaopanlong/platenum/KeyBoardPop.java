package com.zhaopanlong.platenum;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class KeyBoardPop extends PopupWindow {
    private LicensePlateView licensePlateView;
    TextView tvClose;

    public KeyBoardPop(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.keyboard_layout, null);
        setContentView(view);
        tvClose = view.findViewById(R.id.tvClose);
        licensePlateView = view.findViewById(R.id.plateNum);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setKeyBordlisten(LicensePlateView.KeyBordClickListen listen) {
        licensePlateView.setOnKeyBordClicklisten(listen);
    }

    public void showProvince() {
        licensePlateView.showProvince();
    }

    public void showNum() {
        licensePlateView.showNum();
    }
}
