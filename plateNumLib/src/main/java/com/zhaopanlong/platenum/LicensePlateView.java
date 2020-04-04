package com.zhaopanlong.platenum;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author xingyang.pei
 * @date 2018/1/5.
 */

public class LicensePlateView extends RelativeLayout implements View.OnClickListener {

    private Activity mActivity;
    private View mNumView;
    private View mProvinceView;

    private LayoutInflater mInflater;

    private static final int[] VIEW_PROVINCE_IDS = new int[]{
            R.id.select_province_11_tv, R.id.select_province_12_tv, R.id.select_province_13_tv,
            R.id.select_province_14_tv, R.id.select_province_15_tv, R.id.select_province_16_tv,
            R.id.select_province_17_tv, R.id.select_province_18_tv, R.id.select_province_19_tv,
            R.id.select_province_110_tv,
            R.id.select_province_21_tv, R.id.select_province_22_tv, R.id.select_province_23_tv,
            R.id.select_province_24_tv, R.id.select_province_25_tv, R.id.select_province_26_tv,
            R.id.select_province_27_tv, R.id.select_province_28_tv, R.id.select_province_29_tv,
            R.id.select_province_210_tv,
            R.id.select_province_31_tv, R.id.select_province_32_tv, R.id.select_province_33_tv,
            R.id.select_province_34_tv, R.id.select_province_35_tv, R.id.select_province_35_tv,
            R.id.select_province_36_tv, R.id.select_province_37_tv, R.id.select_province_38_tv,
            R.id.select_province_41_tv, R.id.select_province_42_tv, R.id.select_province_43_tv,
            R.id.select_province_delete_tv
    };

    private static final int[] VIEW_NUM_IDS = new int[]{
            R.id.select_num_100_tv, R.id.select_num_101_tv, R.id.select_num_102_tv,
            R.id.select_num_103_tv, R.id.select_num_104_tv, R.id.select_num_105_tv,
            R.id.select_num_106_tv, R.id.select_num_107_tv, R.id.select_num_108_tv,
            R.id.select_num_109_tv,
            R.id.select_num_200_tv, R.id.select_num_201_tv, R.id.select_num_202_tv,
            R.id.select_num_203_tv, R.id.select_num_204_tv, R.id.select_num_205_tv,
            R.id.select_num_206_tv, R.id.select_num_207_tv, R.id.select_num_208_tv,
            R.id.select_num_209_tv,
            R.id.select_num_300_tv, R.id.select_num_301_tv, R.id.select_num_302_tv,
            R.id.select_num_303_tv, R.id.select_num_304_tv, R.id.select_num_305_tv,
            R.id.select_num_306_tv, R.id.select_num_307_tv, R.id.select_num_308_tv,
            R.id.select_num_309_tv,
            R.id.select_num_400_tv, R.id.select_num_401_tv, R.id.select_num_402_tv,
            R.id.select_num_403_tv, R.id.select_num_404_tv, R.id.select_num_405_tv,
            R.id.select_num_406_tv,
            R.id.select_num_delete_tv
    };

    public LicensePlateView(Context context) {
        this(context, null);
    }

    public LicensePlateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LicensePlateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        setKeyboardContainerLayout();
    }


    private void setKeyboardContainerLayout() {
        mInflater = LayoutInflater.from(mActivity);
        mProvinceView = mInflater.inflate(R.layout.layout_keyboard_province, null);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mProvinceView.setLayoutParams(rlParams);
        mNumView = mInflater.inflate(R.layout.layout_keyboard_num, null);
        mNumView.setLayoutParams(rlParams);
        int provinceLength = VIEW_PROVINCE_IDS.length;
        View view;
        for (int i = 0; i < provinceLength; i++) {
            view = mProvinceView.findViewById(VIEW_PROVINCE_IDS[i]);
            view.setOnClickListener(this);
        }
        int numLength = VIEW_NUM_IDS.length;
        for (int i = 0; i < numLength; i++) {
            view = mNumView.findViewById(VIEW_NUM_IDS[i]);
            view.setOnClickListener(this);
        }
        addView(mProvinceView);
        addView(mNumView);
        mNumView.setVisibility(GONE);
    }


    /**
     * 键盘的点击事件
     */
    @Override
    public void onClick(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setSelected(true);
            String text = tv.getText().toString();
            if (view.getId() == R.id.select_province_delete_tv || view.getId() == R.id.select_num_delete_tv) {
                //删除按钮
                if (onKeyBordClicklisten != null) {
                    onKeyBordClicklisten.delete();
                }
            } else {
                if (onKeyBordClicklisten != null) {
                    onKeyBordClicklisten.text(text);
                }
            }
        }
    }

    private KeyBordClickListen onKeyBordClicklisten;

    public KeyBordClickListen getOnKeyBordClicklisten() {
        return onKeyBordClicklisten;
    }

    public void setOnKeyBordClicklisten(KeyBordClickListen onKeyBordClicklisten) {
        this.onKeyBordClicklisten = onKeyBordClicklisten;
    }

    public interface KeyBordClickListen {
        void text(String text);

        void delete();
    }

    public void showProvince() {
        mProvinceView.setVisibility(VISIBLE);
        mNumView.setVisibility(GONE);
    }

    public void showNum() {
        mProvinceView.setVisibility(GONE);
        mNumView.setVisibility(VISIBLE);
    }

}
