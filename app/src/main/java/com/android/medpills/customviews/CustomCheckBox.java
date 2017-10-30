package com.android.medpills.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.android.medpills.R;

public class CustomCheckBox extends android.support.v7.widget.AppCompatCheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked){
        if(checked) {
            // checkbox_background is blue
            this.setBackgroundResource(R.drawable.checkbox_background);
            this.setTextColor(Color.WHITE);
        } else {
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setTextColor(Color.BLACK);
        }
        super.setChecked(checked);
    }
}