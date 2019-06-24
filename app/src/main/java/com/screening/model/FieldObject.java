package com.screening.model;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by zhangbin on 2018/4/23.
 * 选择必填字段创建的对象
 */

public class FieldObject {
    private CheckBox checkBox;
    private String textView;

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getTextView() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }
}
