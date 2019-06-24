package com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.util.EnumSoundWav;
import com.util.PlayVoice;

/**
 * Created by gyl1 on 3/30/17.
 */

public class VoiceImageButton extends android.support.v7.widget.AppCompatImageButton {
    EnumSoundWav mWav;
    public VoiceImageButton(Context context) {
        super(context);
    }
    public VoiceImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public VoiceImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setEnumSoundWav(EnumSoundWav wav){
        mWav = wav;
    }

    /**
     * 只要是使用了view.setOnClickListener()方法设置监听器，就会自动触发view.performClick()。
     * @return
     */
    @Override
    public boolean performClick() {
        //调用发声方法，暂时取消了
//        PlayVoice.playClickVoice(getContext(),mWav);
        return super.performClick();
    }



}
